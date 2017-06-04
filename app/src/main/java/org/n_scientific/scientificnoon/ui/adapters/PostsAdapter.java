package org.n_scientific.scientificnoon.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.services.SoundCloudService;
import org.n_scientific.scientificnoon.ui.showarticle.ShowArticleActivity;
import org.n_scientific.scientificnoon.utils.DateUtils;
import org.n_scientific.scientificnoon.utils.PostsUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohammad on 04/06/17.
 */

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int POST_TYPE = 0;
    private static final int LOADING_TYPE = 1;


    private Context context;
    private List<Post> data;
    private CategoriesRemoteDataSource categoriesRemoteDataSource;
    private SoundCloudService soundCloudService;
    private boolean allDownloaded = false;

    public PostsAdapter(Context context, List<Post> posts, CategoriesRemoteDataSource categoriesRemoteDataSource, SoundCloudService soundCloudService) {
        this.context = context;
        this.data = posts;
        this.categoriesRemoteDataSource = categoriesRemoteDataSource;
        this.soundCloudService = soundCloudService;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if (i == LOADING_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.loading_item, viewGroup, false);
            return new LoadingHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof PostViewHolder) {

            final PostViewHolder postViewHolder = (PostViewHolder) viewHolder;

            final Post post = data.get(i);

            postViewHolder.txtTitle.setText(Html.fromHtml(post.getTitle().getTitle()));

            final List<Category> categories = new ArrayList<>(post.getCategories().length);

            for (int catId : post.getCategories()) {
                categoriesRemoteDataSource.getCategory(catId, new Callbacks.Callback<Category>() {
                    @Override
                    public void onLoaded(Category result) {
                        categories.add(result);
                        if (categories.size() == post.getCategories().length) {

                            StringBuilder cats = new StringBuilder("");
                            for (int i = 0; i < categories.size(); i++) {
                                cats.append(categories.get(i).getName()).append(i != categories.size() - 1 ? ", " : "");
                            }
                            postViewHolder.txtCategories.setText(cats.toString());
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }


            Date date = DateUtils.parseDate(post.getDate().replace("T", "-"), Config.FETCHED_POST_COMMENT_DATE_PATTERN);

            postViewHolder.txtDate.setText(DateUtils.smartFormat(date, Config.POST_COMMENT_DATE_PATTERN_NO_YEAR, Config.POST_COMMENT_DATE_PATTERN));

            PostsUtils.getPostImage(post.getContent().getContent(), soundCloudService, new Callbacks.Callback<String>() {
                @Override
                public void onLoaded(String result) {
                    Glide.with(context).load(result)
                            .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_image))
                            .error(ContextCompat.getDrawable(context, R.drawable.ic_broken_image))
                            .into(postViewHolder.postImage);

                }

                @Override
                public void onError(String message) {
                    postViewHolder.postImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_broken_image));
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return data.size() + (allDownloaded ? 0 : 1);
    }

    @Override
    public int getItemViewType(int position) {
        return position == data.size() ? LOADING_TYPE : POST_TYPE;
    }

    public boolean isAllDownloaded() {
        return allDownloaded;
    }

    public void setAllDownloaded(boolean allDownloaded) {
        this.allDownloaded = allDownloaded;
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

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ShowArticleActivity.class);
            intent.putExtra(ShowArticleActivity.POST_INTENT_KEY, data.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }

}
