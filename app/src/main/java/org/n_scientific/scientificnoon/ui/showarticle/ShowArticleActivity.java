package org.n_scientific.scientificnoon.ui.showarticle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.NoonApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Comment;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.pojo.User;
import org.n_scientific.scientificnoon.data.remote.DaggerSoundCloudComponent;
import org.n_scientific.scientificnoon.ui.adapters.CategoriesAdapter;
import org.n_scientific.scientificnoon.ui.adapters.CommentsAdapter;
import org.n_scientific.scientificnoon.ui.views.NoonTextView;
import org.n_scientific.scientificnoon.utils.DateUtils;
import org.n_scientific.scientificnoon.utils.ResourcesUtils;
import org.n_scientific.scientificnoon.utils.Utils;
import org.n_scientific.scientificnoon.utils.ViewsUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

public class ShowArticleActivity extends AppCompatActivity implements ShowArticleContract.View, View.OnClickListener {


    public static final String POST_INTENT_KEY = "post";


    private static final String TAG = "ShowArticleActivity";
    public static final String TRANSITION_NAME = "transition_name";


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

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


    public static Bitmap bitmap; // Avoid passing it by intents

    Post post;
    User user;

    private List<Comment> comments;
    private int page = 1;

    private int fontSize;

    Menu menu;
    private boolean commentsDownloading;
    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Transition explode = new Explode();
            explode.excludeTarget(R.id.appBar, true);

            getWindow().setEnterTransition(explode);

            Fade fade = new Fade();
            fade.excludeTarget(R.id.cardView, true);

            getWindow().setReturnTransition(fade);
            getWindow().setExitTransition(fade);

        }
        setContentView(R.layout.activity_show_article);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);


        showArticleComponent = DaggerShowArticleComponent
                .builder()
                .remoteDataSourceComponent(((NoonApplication) getApplication())
                        .getRemoteDataSourceComponent())
                .soundCloudComponent(DaggerSoundCloudComponent.builder().build())
                .localDataSourceComponent(((NoonApplication) getApplication())
                        .getLocalDataSourceComponent())
                .build();

        showArticleComponent.inject(this);

        presenter.setView(this);


        ViewCompat.setNestedScrollingEnabled(content, false);

        collapsingToolbarLayout.setExpandedTitleColor(0x000000);

        post = (Post) getIntent().getSerializableExtra(POST_INTENT_KEY);

        postTitle.setText(Html.fromHtml(post.getTitle().getTitle()));

        presenter.loadCategories(post.getCategories());


        if (bitmap != null) {
            presenter.parseContent(post.getContent().getContent(), content, postImage, false);

            postImage.setImageBitmap(bitmap);
//            postImage.setOnClickListener(this);
            postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            postImage.setColorFilter(ResourcesUtils.getColor(this, R.color.overlay_40));
            ViewCompat.setTransitionName(postImage, getIntent().getStringExtra(TRANSITION_NAME));


            supportStartPostponedEnterTransition();
        } else
            presenter.parseContent(post.getContent().getContent(), content, postImage, true);

        presenter.loadComments(post.getId(), page);

        String date = (DateUtils.formatDate(post.getDate().replace('T', '-')
                , Config.FETCHED_POST_COMMENT_DATE_PATTERN
                , Config.POST_COMMENT_DATE_PATTERN));

        txtDate.setText(date);

        btnDecreaseFont.setOnClickListener(this);
        btnIncreaseFont.setOnClickListener(this);
        fontSize = presenter.getFontSize();

        share.setOnClickListener(this);

        commentsDownloading = false;

    }

    @Override
    protected void onStart() {
        super.onStart();
        ResourcesUtils.changeLanguageToArabic(this);
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
        if (!Utils.isConnected(this)) {
            ViewsUtils.getInfoSnackBar(coordinatorLayout, getString(R.string.no_internet_connection_message)).show();

            categoriesProgressBar.setVisibility(View.GONE);
            commentsProgress.setVisibility(View.GONE);
        } else
            ViewsUtils.getErrorSnackBar(coordinatorLayout, message).show();
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
        categoriesProgressBar.setVisibility(View.GONE);


        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, categories);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        categoriesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void commentsLoaded(List<Comment> comments) {
        commentsDownloading = false;
        if (commentsFirstLoading) {
            if (comments.size() == 0) {
                noComments.setVisibility(View.VISIBLE);
                commentsProgress.setVisibility(View.GONE);
                return;
            }
            commentsAdapter = new CommentsAdapter(this, comments);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);


            commentsRecyclerView.setLayoutManager(layoutManager);
            commentsRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(commentsAdapter));

            commentsRecyclerView.setVisibility(View.VISIBLE);
            commentsProgress.setVisibility(View.GONE);

            this.comments = comments;

            commentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (layoutManager.findLastVisibleItemPosition() == ShowArticleActivity.this.comments.size() && !commentsAdapter.isAllLoaded()) {
                        page++;
                        presenter.loadComments(post.getId(), page);
                        commentsDownloading = true;
                    }


                }
            });


            commentsFirstLoading = false;
        } else {
            this.comments.addAll(comments);
            commentsRecyclerView.getAdapter().notifyDataSetChanged();
            if (comments.size() < Config.DEFAULT_COMMENTS_PER_CALL)
                commentsAdapter.setAllLoaded(true);
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

//            case R.id.postImage:
//                ImageViewerActivity.bitmap = bitmap;
//
//                Intent intent = new Intent(this, ImageViewerActivity.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, postImage, "postImage");
//                    intent.putExtra(ImageViewerActivity.TRANSITION_NAME, "postImage");
//
//                    startActivity(intent, options.toBundle());
//                } else {
//                    startActivity(intent);
//                }
//
//                break;
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
