package org.n_scientific.scientificnoon.ui.showarticle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.MyApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Comment;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.pojo.User;
import org.n_scientific.scientificnoon.data.remote.DaggerSoundCloudComponent;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.UsersRemoteDataSource;
import org.n_scientific.scientificnoon.ui.adapters.CategoriesAdapter;
import org.n_scientific.scientificnoon.ui.adapters.CommentsAdapter;
import org.n_scientific.scientificnoon.ui.views.NoonTextView;
import org.n_scientific.scientificnoon.utils.DateUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowArticleActivity extends AppCompatActivity implements ShowArticleContract.View, View.OnClickListener {


    public static final String POST_INTENT_KEY = "post";
    private static final String TAG = "ShowArticleActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.postTitle)
    TextView postTitle;

    @BindView(R.id.increaseFontSize)
    Button btnIncreaseFont;

    @BindView(R.id.decreaseFontSize)
    Button btnDecreaseFont;

    @BindView(R.id.content)
    LinearLayout content;

    @BindView(R.id.postImage)
    ImageView postImage;


    @BindView(R.id.categoriesProgressBar)
    ProgressBar categoriesProgressBar;

    @BindView(R.id.categoriesRecyclerView)
    RecyclerView categoriesRecyclerView;

    @BindView(R.id.date)
    TextView txtDate;

    @BindView(R.id.commentsRecyclerView)
    RecyclerView commentsRecyclerView;

    @BindView(R.id.commentsProgress)
    ProgressBar commentsProgress;

    @BindView(R.id.noComments)
    NoonTextView noComments;


    @BindView(R.id.share)
    FloatingActionButton share;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private ShowArticleComponent showArticleComponent;


    boolean commentsFirstLoading = true;

    @Inject
    ShowArticleContract.Presenter presenter;

    @Inject
    CategoriesRemoteDataSource categoriesRemoteDataSource;

    @Inject
    UsersRemoteDataSource usersRemoteDataSource;


    Post post;
    User user;

    private List<Category> categories;
    private List<Comment> comments;
    private int page = 1;

    private int fontSize;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        showArticleComponent = DaggerShowArticleComponent
                .builder()
                .remoteDataSourceComponent(((MyApplication) getApplication())
                        .getRemoteDataSourceComponent())
                .soundCloudComponent(DaggerSoundCloudComponent.builder().build())
                .localDataSourceComponent(((MyApplication) getApplication())
                        .getLocalDataSourceComponent())
                .build();

        showArticleComponent.inject(this);

        presenter.setView(this);


        collapsingToolbarLayout.setExpandedTitleColor(0x000000);

        post = (Post) getIntent().getSerializableExtra(POST_INTENT_KEY);

        postTitle.setText(Html.fromHtml(post.getTitle().getTitle()));

        presenter.loadCategories(post.getCategories());

        presenter.parseContent(post.getContent().getContent(), content, postImage);

        presenter.loadComments(post.getId(), page);

        String date = (DateUtils.formatDate(post.getDate().replace('T', '-')
                , Config.FETCHED_POST_COMMENT_DATE_PATTERN
                , Config.POST_COMMENT_DATE_PATTERN));

        txtDate.setText(date);

        btnDecreaseFont.setOnClickListener(this);
        btnIncreaseFont.setOnClickListener(this);
        fontSize = presenter.getFontSize();

        share.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.show_article_menu, menu);
        this.menu = menu;
        presenter.isFavorite(post);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showErrorMessage(int resId) {
        showErrorMessage(getString(resId));
    }

    @Override
    public void isFavoriteLoaded(boolean isFavorite) {
        MenuItem item = menu.findItem(R.id.addToFavorite);
        item.setChecked(isFavorite);
        item.setIcon(isFavorite ? R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_white_24dp);
    }

    @Override
    public void userLoaded(User user) {
        this.user = user;

    }

    @Override
    public void categoriesLoaded(List<Category> categories) {
        this.categories = categories;

        categoriesProgressBar.setVisibility(View.GONE);


        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, categories);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        categoriesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void commentsLoaded(List<Comment> comments) {
        if (commentsFirstLoading) {
            if (comments.size() == 0) {
                noComments.setVisibility(View.VISIBLE);
                commentsProgress.setVisibility(View.GONE);
                return;
            }
            CommentsAdapter commentsAdapter = new CommentsAdapter(this, comments);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

            commentsRecyclerView.setLayoutManager(layoutManager);
            commentsRecyclerView.setAdapter(commentsAdapter);

            commentsRecyclerView.setVisibility(View.VISIBLE);
            commentsProgress.setVisibility(View.GONE);

            this.comments = comments;

            commentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (layoutManager.findLastVisibleItemPosition() == ShowArticleActivity.this.comments.size()) {
                        page++;
                        presenter.loadComments(post.getId(), page);
                    }


                }
            });

            commentsFirstLoading = false;
        } else {
            this.comments.addAll(comments);
            commentsRecyclerView.getAdapter().notifyDataSetChanged();
            if (comments.size() == 0)
                ((CommentsAdapter) commentsRecyclerView.getAdapter()).setAllLoaded(true);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ShowArticleComponent getComponent() {
        return showArticleComponent;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.openInBrowser:

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(post.getLink()));
                startActivity(Intent.createChooser(intent, "فتح باستخدام"));

                break;

            case R.id.addToFavorite:

                if (item.isChecked()) {
                    presenter.removeFromFavorite(post);
                    item.setChecked(false);
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                } else {
                    presenter.addToFavorite(post);
                    item.setChecked(true);
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                }

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.increaseFontSize:
                if (fontSize == Config.MAX_FONT_SIZE) {
                    showErrorMessage(getString(R.string.maxFontSizeError));
                    return;
                }
                fontSize++;
                presenter.setFontSize(fontSize);
                updateFontSize();
                break;

            case R.id.decreaseFontSize:
                if (fontSize == Config.MIN_FONT_SIZE) {
                    showErrorMessage(getString(R.string.minFontSizeError));
                    return;
                }
                fontSize--;
                presenter.setFontSize(fontSize);
                updateFontSize();
                break;

            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.site_name) + " | " + post.getTitle().getTitle() + "\n" + post.getLink());
                share.setType("text/plain");

                startActivity(Intent.createChooser(share, "مشاركة باستخدام"));

                break;

        }
    }


    private void updateFontSize() {
        View view;
        for (int i = 0; i < content.getChildCount(); i++) {
            view = content.getChildAt(i);
            if (view instanceof TextView)
                ((TextView) view).setTextSize(fontSize);
        }
    }

}
