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
import org.n_scientific.scientificnoon.ui.adapters.CategoriesAdapter;
import org.n_scientific.scientificnoon.ui.adapters.PostsAdapter;
import org.n_scientific.scientificnoon.ui.posts_list.PostsListActivity;
import org.n_scientific.scientificnoon.utils.AnimUtils;
import org.n_scientific.scientificnoon.utils.ResourcesUtils;
import org.n_scientific.scientificnoon.utils.Utils;
import org.n_scientific.scientificnoon.utils.ViewsUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class MainActivity extends BaseActivity implements MainContract.View, SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

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

    //    @BindView(R.id.nestedScrollView)
//    NestedScrollView nestedScrollView;
    @BindView(R.id.postsRecyclerView)
    RecyclerView postsRecyclerView;
    @BindView(R.id.categoriesRecyclerView)
    RecyclerView categoriesRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txtNoPosts)
    TextView txtNoPosts;

    @BindView(R.id.txtCategoryName)
    TextView txtCategoryName;

    @BindView(R.id.noConnectionMessage)
    ViewGroup noConnectionMessage;

    SearchView searchView;

    List<Post> posts;
    int page;
    int mode;

    private PostsAdapter postsAdapter;
    private boolean downloading;

    private Category category;
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
        init(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        init(true);
    }

    private void init(boolean animate) {

        page = 1;
        posts = null;
        downloading = false;
        swipeRefreshLayout.setRefreshing(false); // If it is already refreshing, stop it..

        txtNoPosts.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        postsRecyclerView.setVisibility(View.GONE);
        categoriesRecyclerView.setVisibility(View.GONE);
        noConnectionMessage.setVisibility(View.GONE);

        txtCategoryName.setVisibility(View.GONE);

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
                txtCategoryName.setVisibility(View.VISIBLE);

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


    private void initRecyclerView(List<Post> results) {

        posts = results;
        postsAdapter = new PostsAdapter(this, posts, catRemoteDataSource, categoriesLocalDataSource, DaggerSoundCloudComponent.builder().soundCloudModule(new SoundCloudModule()).build().getSoundCloudService(), mode != SEARCH_MODE);
        postsAdapter.setLoadMoreClickListener(this);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(layoutManager);

//        ViewCompat.setNestedScrollingEnabled(postsRecyclerView, false);
        postsRecyclerView.setAdapter(postsAdapter);

        if (mode == SEARCH_MODE) {
//            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    // Grab the last child placed in the ScrollView, we need it to determinate the bottom position.
//                    View view = v.getChildAt(0);
//                    // Calculate the scrolldiff
//                    int diff = view.getBottom() - (v.getHeight() + v.getScrollY()) - progressBar.getHeight() - 48;
//                    // if diff is zero, then the bottom has been reached
//                    if (diff < 0 && !downloading && !postsAdapter.isAllDownloaded()) {
//                        downloading = true;
//                        page++;
//                        loadPosts();
//                    }
//                }
//            });


            if (results.size() < Config.DEFAULT_POSTS_PER_CALL)
                postsAdapter.setAllDownloaded(true);
        } else {
//            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                }
//            });

            postsAdapter.setAllDownloaded(true);
        }

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
        if (posts == null) {
            progressBar.setVisibility(View.GONE);
            if (results.size() == 0) {
                txtNoPosts.setVisibility(View.VISIBLE);

            } else
                initRecyclerView(results);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            if (results.size() < Config.DEFAULT_POSTS_PER_CALL)
                postsAdapter.setAllDownloaded(true);

            posts.addAll(results);
            postsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCategoriesLoaded(List<Category> categories) {
        if (categories.size() != 0) {
            CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, categories);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            categoriesRecyclerView.setLayoutManager(linearLayoutManager);
            categoriesRecyclerView.setAdapter(categoriesAdapter);
            AnimUtils.slideDown(categoriesRecyclerView, 400);
            categoriesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        if (!Utils.isConnected(this)) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            categoriesProgressBar.setVisibility(View.GONE);
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
        txtNoPosts.setVisibility(View.GONE);
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
            init(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        categoriesRecyclerView.setVisibility(View.GONE);
        txtCategoryName.setVisibility(View.GONE);
        txtNoPosts.setVisibility(View.GONE);
        txtCategoryName.setVisibility(View.GONE);
        postsRecyclerView.setVisibility(View.GONE);

        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        init(false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoadMore:
                Intent intent = new Intent(this, PostsListActivity.class);
                intent.putExtra(PostsListActivity.MODE_KEY, mode);
                if (mode == CATEGORIES_MODE)
                    intent.putExtra(PostsListActivity.CATEGORY_KEY, category);
                startActivity(intent);
                break;
        }
    }
}
