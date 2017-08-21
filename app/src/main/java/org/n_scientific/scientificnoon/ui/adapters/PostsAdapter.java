package org.n_scientific.scientificnoon.ui.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.local.CategoriesLocalDataSource;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.services.SoundCloudService;
import org.n_scientific.scientificnoon.ui.showarticle.ShowArticleActivity;
import org.n_scientific.scientificnoon.utils.DateUtils;
import org.n_scientific.scientificnoon.utils.PostsUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohammad on 04/06/17.
 */

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Callbacks.ListCallback<Category> {

    private static final int POST_TYPE = 0;
    private static final int LOADING_TYPE = 1;
    private static final int LOAD_MORE_TYPE = 2;
    private static final int CATEGORIES_TYPE = 3;
    private static final int NO_POSTS_TYPE = 4;


    private Context context;
    private List<Post> data;
    private CategoriesRemoteDataSource categoriesRemoteDataSource;
    private CategoriesLocalDataSource categoriesLocalDataSource;
    private SoundCloudService soundCloudService;
    private boolean allDownloaded = false;
    private View.OnClickListener loadMoreClickListener;
    private boolean loadMoreEnabled;

    List<Category> categories;

    private boolean showSubCategories;

    private List<Category> subcategories;

    public PostsAdapter(Context context, List<Post> posts, CategoriesRemoteDataSource categoriesRemoteDataSource, CategoriesLocalDataSource categoriesLocalDataSource, SoundCloudService soundCloudService, boolean loadMoreEnabled, boolean showSubCategories, List<Category> subcategories) {
        this.context = context;
        this.data = posts;
        this.categoriesRemoteDataSource = categoriesRemoteDataSource;
        this.categoriesLocalDataSource = categoriesLocalDataSource;
        this.soundCloudService = soundCloudService;
        this.loadMoreEnabled = loadMoreEnabled;

        categoriesLocalDataSource.getCategories(this);

        this.subcategories = subcategories;
        this.showSubCategories = showSubCategories;

    }

    public View.OnClickListener getLoadMoreClickListener() {
        return loadMoreClickListener;
    }

    public void setLoadMoreClickListener(View.OnClickListener loadMoreClickListener) {
        this.loadMoreClickListener = loadMoreClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        switch (i) {
            case LOADING_TYPE: {
                View view = LayoutInflater.from(context).inflate(R.layout.loading_item, viewGroup, false);
                return new LoadingHolder(view);
            }
            case LOAD_MORE_TYPE: {
                View view = LayoutInflater.from(context).inflate(R.layout.load_more_item, viewGroup, false);
                return new LoadMoreViewHolder(view);
            }
            case CATEGORIES_TYPE: {
                View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, viewGroup, false);
                return new CategoriesViewHolder(view);
            }
            case NO_POSTS_TYPE: {
                View view = LayoutInflater.from(context).inflate(R.layout.no_posts_item, viewGroup, false);
                return new NoPostsViewHolder(view);
            }
            default: {
                View view = LayoutInflater.from(context).inflate(R.layout.post_item, viewGroup, false);
                return new PostViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {


        if (viewHolder instanceof PostViewHolder) {

            final PostViewHolder postViewHolder = (PostViewHolder) viewHolder;

            int pos = position - (showSubCategories && subcategories != null && subcategories.size() != 0 ? 1 : 0);

            final Post post = data.get(pos);

            postViewHolder.txtTitle.setText(Html.fromHtml(post.getTitle().getTitle()));

            postViewHolder.txtCategories.setText("");

            StringBuilder cats = new StringBuilder("");

            if (categories != null) {
                for (int j = 0; j < post.getCategories().length; j++) {
                    cats.append(categoryById(post.getCategories()[j]).getName());
                    if (j != post.getCategories().length - 1)
                        cats.append("، ");
                }
            }

            postViewHolder.txtCategories.setText(cats.toString());

            Date date = DateUtils.parseDate(post.getDate().replace("T", "-"), Config.FETCHED_POST_COMMENT_DATE_PATTERN);

            postViewHolder.txtDate.setText(DateUtils.smartFormat(date, Config.POST_COMMENT_DATE_PATTERN_NO_YEAR, Config.POST_COMMENT_DATE_PATTERN));


            postViewHolder.image = null;

            postViewHolder.postImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            postViewHolder.postImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_white));
            PostsUtils.getPostImage(post.getContent().getContent(), soundCloudService, new Callbacks.Callback<String>() {
                @Override
                public void onLoaded(String result) {
                    postViewHolder.image = null;

                    Glide.with(context).load(result)
                            .asBitmap()
                            .placeholder(R.drawable.logo_white)
                            .listener(new RequestListener<String, Bitmap>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                    postViewHolder.image = resource;
                                    postViewHolder.postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    return false;
                                }
                            })
                            .listener(new RequestListener<String, Bitmap>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    postViewHolder.image = resource;
                                    postViewHolder.postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    return false;
                                }
                            })
                            .into(postViewHolder.postImage);

                }

                @Override
                public void onError(String message) {
                    postViewHolder.postImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_broken_image));
                }
            });

        } else if (viewHolder instanceof CategoriesViewHolder) {

            CategoriesViewHolder categoriesViewHolder = (CategoriesViewHolder) viewHolder;

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            categoriesViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
            categoriesViewHolder.recyclerView.setAdapter(new CategoriesAdapter(context, subcategories));

        }

    }

    private Category categoryById(int id) {
        for (Category category : categories) {
            if (category.getId() == id)
                return category;
        }
        return new Category();
    }

    @Override
    public int getItemCount() {
        int count = data.size();
        if (showSubCategories && subcategories != null && subcategories.size() != 0)
            count++;
        if (!allDownloaded || loadMoreEnabled)
            count++;
        if (data.size() == 0)
            count++;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && loadMoreEnabled && data.size() != 0) {
                return LOAD_MORE_TYPE;
        } else if (position == getItemCount() - 1 && !allDownloaded && data.size() != 0) {
            return LOADING_TYPE;
        } else if (position == 0 && showSubCategories && subcategories != null && subcategories.size() != 0)
            return CATEGORIES_TYPE;
        else if (data.size() == 0)
            return NO_POSTS_TYPE;
        else {
            return POST_TYPE;
        }
    }

    public boolean isAllDownloaded() {
        return allDownloaded;
    }

    public void setAllDownloaded(boolean allDownloaded) {
        this.allDownloaded = allDownloaded;
    }

    @Override
    public void onLoaded(List<Category> results) {
        categories = results;
        notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {

    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imgPostImage)
        ImageView postImage;

        @BindView(R.id.txtTitle)
        TextView txtTitle;

        @BindView(R.id.txtCategories)
        TextView txtCategories;

        @BindView(R.id.txtDate)
        TextView txtDate;

        Bitmap image;
        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition() - (showSubCategories && subcategories != null && subcategories.size() != 0 ? 1 : 0);
            Intent intent = new Intent(context, ShowArticleActivity.class);
            intent.putExtra(ShowArticleActivity.POST_INTENT_KEY, data.get(position));
            if (image != null) {
                ShowArticleActivity.bitmap = image;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            ((Activity) context),
                            postImage, String.valueOf(position));
                    intent.putExtra(ShowArticleActivity.TRANSITION_NAME, String.valueOf(position));

                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            } else {
            ShowArticleActivity.bitmap = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(((Activity) context)).toBundle());
            } else
                context.startActivity(intent);
            }
        }
    }

    class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btnLoadMore)
        Button btnLoadMore;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            btnLoadMore.setOnClickListener(loadMoreClickListener);
        }
    }


    private class CategoriesViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        public CategoriesViewHolder(View view) {
            super(view);
            recyclerView = (RecyclerView) view;
        }

    }


    private class NoPostsViewHolder extends RecyclerView.ViewHolder {

        public NoPostsViewHolder(View itemView) {
            super(itemView);
        }
    }

}
