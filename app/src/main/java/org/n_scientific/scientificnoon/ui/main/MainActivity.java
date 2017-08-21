package org.n_scientific.scientificnoon.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.NoonApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.pojo.User;
import org.n_scientific.scientificnoon.data.remote.DaggerSoundCloudComponent;
import org.n_scientific.scientificnoon.data.remote.SoundCloudModule;
import org.n_scientific.scientificnoon.ui.BaseActivity;
import org.n_scientific.scientificnoon.ui.adapters.PostsAdapter;
import org.n_scientific.scientificnoon.utils.AnimUtils;
import org.n_scientific.scientificnoon.utils.ResourcesUtils;
import org.n_scientific.scientificnoon.utils.Utils;
import org.n_scientific.scientificnoon.utils.ViewsUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class MainActivity extends BaseActivity implements MainContract.View, SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    // Mode of display: Recent Posts, By User, By Category..
    public static final String MODE_KEY = "mode";

    public static final int RECENT_POSTS_MODE = 0;
    public static final int CATEGORIES_MODE = 1;
    public static final int USER_MODE = 2;
    public static final int SEARCH_MODE = 3;

    // Category to be displayed..
    public static final String CATEGORY_KEY = "category";
    // User to display his posts..
    public static final String USER_KEY = "user";


    @Inject
    MainPresenter presenter;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.postsRecyclerView)
    RecyclerView postsRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;





    @BindView(R.id.noConnectionMessage)
    ViewGroup noConnectionMessage;

    SearchView searchView;

    List<Post> posts;
    int page;
    int mode;

    private PostsAdapter postsAdapter;
    private boolean downloading;

    private Category category;
    private List<Category> subcategories;

    private boolean subcategoriesLoaded = false;
    private boolean postsLoaded = false;

    private User user;
    private String searchQuery;
    private MenuItem searchViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
            getWindow().setReturnTransition(new Explode());
        }


        postsRecyclerView.setItemAnimator(new SlideInUpAnimator());

        swipeRefreshLayout.setOnRefreshListener(this);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        init();
    }

    private void init() {

        page = 1;
        posts = null;
        downloading = false;
        swipeRefreshLayout.setRefreshing(false); // If it is already refreshing, stop it..

        postsLoaded = false;
        subcategoriesLoaded = false;

        progressBar.setVisibility(View.VISIBLE);
        postsRecyclerView.setVisibility(View.GONE);
        noConnectionMessage.setVisibility(View.GONE);


        Bundle options = getIntent().getExtras();

        if (options != null)
            mode = options.getInt(MODE_KEY, RECENT_POSTS_MODE);
        else
            mode = RECENT_POSTS_MODE;

        switch (mode) {
            case CATEGORIES_MODE:
                category = (Category) options.getSerializable(CATEGORY_KEY);
                presenter.loadCategories(category.getId());
                setTitle(category.getName());

                lastCategorySelectedId = category.getId();

                break;
            default:
                lastCategorySelectedId = -1;
        }
        loadPosts();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode != CATEGORIES_MODE)
            lastCategorySelectedId = -1;
    }

    @Override
    public int getContentResource() {
        return R.layout.activity_main;
    }

    @Override
    public void injectDependencies() {

        MainComponent mainComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule())
                .remoteDataSourceComponent(((NoonApplication) getApplication())
                        .getRemoteDataSourceComponent())
                .localDataSourceComponent(((NoonApplication) getApplication()).getLocalDataSourceComponent())
                .build();
        mainComponent.inject(this);
        mainComponent.inject(presenter);
        presenter.setView(this);
    }


    private void initRecyclerView() {


        postsAdapter = new PostsAdapter(this, posts, catRemoteDataSource, categoriesLocalDataSource, DaggerSoundCloudComponent.builder().soundCloudModule(new SoundCloudModule()).build().getSoundCloudService(), false, mode == CATEGORIES_MODE, subcategories);



        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(layoutManager);

        if (posts.size() < Config.DEFAULT_POSTS_PER_CALL)
            postsAdapter.setAllDownloaded(true);

        postsRecyclerView.setAdapter(postsAdapter);

        postsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findLastVisibleItemPosition() == posts.size() - 1 && !downloading && !postsAdapter.isAllDownloaded()) {
                    downloading = true;
                    page++;
                    loadPosts();
                }
            }
        });


        progressBar.setVisibility(View.GONE);
        postsRecyclerView.setVisibility(View.VISIBLE);

    }

    private void loadPosts() {
        switch (mode) {
            case RECENT_POSTS_MODE:
                presenter.loadPosts(page);
                break;
            case CATEGORIES_MODE:
                presenter.loadCategoryPosts(category.getId(), page);
                break;

            case SEARCH_MODE:
                presenter.loadSearchPosts(searchQuery, page);
                break;
        }
    }


    @Override
    public void onPostsLoaded(List<Post> results) {
        downloading = false;

        postsLoaded = true;

        swipeRefreshLayout.setRefreshing(false);

        if (posts == null) {

            posts = results;

            if (subcategoriesLoaded || mode != CATEGORIES_MODE) {
                initRecyclerView();
            }
        } else {
            posts.addAll(results);
            postsAdapter.notifyDataSetChanged();
            if (results.size() < Config.DEFAULT_POSTS_PER_CALL)
                postsAdapter.setAllDownloaded(true);
        }

    }


    @Override
    public void onCategoriesLoaded(List<Category> categories) {
        subcategories = categories;

        subcategoriesLoaded = true;

        if (postsLoaded)
            initRecyclerView();
    }

    @Override
    public void showErrorMessage(String message) {
        if (!Utils.isConnected(this)) {
            swipeRefreshLayout.setRefreshing(false);
            postsRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            if (posts == null) { //No Posts, No internet connection => Show no connection error message
                noConnectionMessage.animate().alpha(1).setListener(null); // Remove Old Listeners
                noConnectionMessage.setVisibility(View.VISIBLE);
                AnimUtils.bounce(noConnectionMessage, 1000, ResourcesUtils.dpToPx(16, this));
            } else {
                ViewsUtils.getInfoSnackBar(coordinatorLayout, getString(R.string.no_internet_connection_message)).show();
            }
        } else
            ViewsUtils.getErrorSnackBar(coordinatorLayout, message).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchViewItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchViewItem, this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchQuery = query;
        mode = SEARCH_MODE;
        page = 1;
        posts.clear();
        posts = null;

        postsRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        searchView.clearFocus();
        loadPosts();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (searchViewItem.isActionViewExpanded()) {
            searchViewItem.collapseActionView();
            init();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        postsRecyclerView.setVisibility(View.GONE);

        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        init();
        return true;
    }

    @Override
    public void onRefresh() {
        if (mode == SEARCH_MODE || downloading) {
            swipeRefreshLayout.setRefreshing(false);
        } else {
            page = 1;
            posts = null;
            noConnectionMessage.setVisibility(View.GONE);
            loadPosts();
        }
    }
}
