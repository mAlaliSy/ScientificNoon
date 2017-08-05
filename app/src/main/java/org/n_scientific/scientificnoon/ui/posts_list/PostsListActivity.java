package org.n_scientific.scientificnoon.ui.posts_list;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.NoonApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.remote.DaggerSoundCloudComponent;
import org.n_scientific.scientificnoon.data.remote.SoundCloudModule;
import org.n_scientific.scientificnoon.ui.BaseActivity;
import org.n_scientific.scientificnoon.ui.adapters.PostsAdapter;
import org.n_scientific.scientificnoon.utils.Utils;
import org.n_scientific.scientificnoon.utils.ViewsUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class PostsListActivity extends BaseActivity implements PostsListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    public static final String MODE_KEY = "mode";

    public static final int RECENT_POSTS_MODE = 0;
    public static final int CATEGORIES_MODE = 1;

    int mode;
    // Category to be displayed..
    public static final String CATEGORY_KEY = "category";

    @Inject
    PostsListContract.Presenter presenter;

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;


    @BindView(R.id.postsRecyclerView)
    RecyclerView postsRecyclerView;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txtNoPosts)
    TextView txtNoPosts;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    List<Post> posts;
    int page;


    private PostsAdapter postsAdapter;
    private boolean downloading;

    private Category category;

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

        txtNoPosts.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        postsRecyclerView.setVisibility(View.GONE);


        Bundle options = getIntent().getExtras();

        mode = options.getInt(MODE_KEY);
        if (mode == CATEGORIES_MODE) {
            category = (Category) options.getSerializable(CATEGORY_KEY);
            setTitle(category.getName());
        } else
            setTitle(R.string.recent_articles);
        loadPosts();

    }

    @Override
    public int getContentResource() {
        return R.layout.activity_posts_list;
    }

    @Override
    public void injectDependencies() {

        PostsListComponent component = DaggerPostsListComponent.builder()
                .postsListModule(new PostsListModule(this))
                .remoteDataSourceComponent(((NoonApplication) getApplication())
                        .getRemoteDataSourceComponent())
                .build();
        component.inject(this);
        component.inject((PostsPresenter) presenter);
    }


    private void initRecyclerView(List<Post> results) {

        posts = results;
        postsAdapter = new PostsAdapter(this, posts, catRemoteDataSource, categoriesLocalDataSource, DaggerSoundCloudComponent.builder().soundCloudModule(new SoundCloudModule()).build().getSoundCloudService(), false);
        SlideInBottomAnimationAdapter adapter = new SlideInBottomAnimationAdapter(postsAdapter);
        adapter.setFirstOnly(true);
        adapter.setDuration(500);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        postsRecyclerView.setLayoutManager(layoutManager);

        postsRecyclerView.setAdapter(adapter);


        postsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findLastVisibleItemPosition() == posts.size() && !downloading && !postsAdapter.isAllDownloaded()) {
                    downloading = true;
                    page++;
                    loadPosts();
                }
            }
        });

        postsRecyclerView.setVisibility(View.VISIBLE);

        if (results.size() < Config.DEFAULT_POSTS_PER_CALL)
            postsAdapter.setAllDownloaded(true);
    }

    private void loadPosts() {
        if (mode == CATEGORIES_MODE)
            presenter.loadCategoryPosts(category.getId(), page, Config.DEFAULT_POSTS_PER_CALL);
        else
            presenter.loadRecentPosts(page, Config.DEFAULT_POSTS_PER_CALL);
    }


    @Override
    public void onPostsLoaded(List<Post> results) {
        downloading = false;
        if (posts == null) {
            progressBar.setVisibility(View.GONE);
            if (results.size() == 0) {
                txtNoPosts.setVisibility(View.VISIBLE);
            } else {
                initRecyclerView(results);
            }
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
            else
                postsRecyclerView.scrollToPosition(postsAdapter.getItemCount() - 1);

        } else {
            if (results.size() < Config.DEFAULT_POSTS_PER_CALL)
                postsAdapter.setAllDownloaded(true);

            posts.addAll(results);
            postsAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void showErrorMessage(String message) {
        if (!Utils.isConnected(this)) {
            swipeRefreshLayout.setRefreshing(false);
            ViewsUtils.getInfoSnackBar(coordinatorLayout, getString(R.string.no_internet_connection_message)).show();

            downloading = false;
        } else
            ViewsUtils.getErrorSnackBar(coordinatorLayout, message).show();
    }

    @Override
    public void onRefresh() {
        if (downloading) {
            swipeRefreshLayout.setRefreshing(false);
        } else {
            page = 1;
            posts = null;

            loadPosts();
        }
    }
}
