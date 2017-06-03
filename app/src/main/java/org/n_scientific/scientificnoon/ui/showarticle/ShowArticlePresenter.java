package org.n_scientific.scientificnoon.ui.showarticle;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.contentparser.ContentParser;
import org.n_scientific.scientificnoon.contentparser.Image;
import org.n_scientific.scientificnoon.contentparser.PostContent;
import org.n_scientific.scientificnoon.contentparser.SoundCloudTrack;
import org.n_scientific.scientificnoon.contentparser.Text;
import org.n_scientific.scientificnoon.contentparser.YoutubeVideo;
import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.SettingsManager;
import org.n_scientific.scientificnoon.data.local.FavoriteDataSource;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Comment;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.pojo.SoundCloudInfo;
import org.n_scientific.scientificnoon.data.pojo.User;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CommentsRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.UsersRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.services.SoundCloudService;
import org.n_scientific.scientificnoon.ui.views.NoonTextView;
import org.n_scientific.scientificnoon.utils.ApisUtils;
import org.n_scientific.scientificnoon.utils.ResourcesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mohammad on 30/05/17.
 */

public class ShowArticlePresenter implements ShowArticleContract.Presenter {

    private ShowArticleContract.View view;

    private List<Category> categories;

    private List<AudioPlayer> audioPlayers;

    private Context context;

    private SettingsManager settingsManager;

    @Inject
    CategoriesRemoteDataSource categoriesRemoteDataSource;

    @Inject
    UsersRemoteDataSource usersRemoteDataSource;

    @Inject
    CommentsRemoteDataSource commentsRemoteDataSource;

    @Inject
    SoundCloudService soundCloudService;

    @Inject
    FavoriteDataSource favoriteDataSource;

    @Override
    public void setView(ShowArticleContract.View view) {
        this.view = view;
        view.getComponent().inject(this);
        context = view.getActivity();
        settingsManager = SettingsManager.getInstance(context);
    }

    @Override
    public void loadCategories(final int[] catsIds) {

        categories = new ArrayList<>();

        for (int id : catsIds) {

            categoriesRemoteDataSource.getCategory(id, new Callbacks.Callback<Category>() {
                @Override
                public void onLoaded(Category result) {
                    categories.add(result);

                    if (categories.size() == catsIds.length) {
                        Collections.sort(categories, new Comparator<Category>() {
                            @Override
                            public int compare(Category o1, Category o2) {
                                if (o1.getParent() == 0)
                                    return -1;
                                return 1;
                            }
                        });
                        view.categoriesLoaded(categories);
                    }
                }

                @Override
                public void onError(String message) {
                    view.showErrorMessage(message);
                }
            });

        }

    }

    @Override
    public void loadComments(int postId, int page) {

        commentsRemoteDataSource.getComments(postId, Config.DEFAULT_COMMENTS_PER_CALL, page, new Callbacks.ListCallback<Comment>() {
            @Override
            public void onLoaded(List<Comment> comments) {
                view.commentsLoaded(comments);
            }

            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        });

    }

    @Override
    public void loadUser(int userId) {
        usersRemoteDataSource.getUser(userId, new Callbacks.Callback<User>() {
            @Override
            public void onLoaded(User result) {
                view.userLoaded(result);
            }

            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        });
    }


    @Override
    public void parseContent(String html, LinearLayout contentContainer, final ImageView postImage) {

        ContentParser parser = new ContentParser(html);

        List<PostContent> postContent = parser.parseContent();


        boolean firstImage = true;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = params.topMargin = 0;

        final RequestListener<String, GlideDrawable> postImageListener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                postImage.setColorFilter(ResourcesUtils.getColor(context, R.color.overlay));
                postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return false;
            }
        };

        for (final PostContent c : postContent) {
            if (c instanceof Image) {
                String src = ((Image) c).getSrc();
                if (firstImage) {
                    loadImage(postImage, ((Image) c).getSrc(), postImageListener);
                    setImageClickListener(postImage, ((Image) c).getSrc());
                    firstImage = false;
                } else {
                    ImageView imageView = new ImageView(contentContainer.getContext());
                    imageView.setLayoutParams(params);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    loadImage(imageView, ((Image) c).getSrc(), null);
                    setImageClickListener(imageView, ((Image) c).getSrc());

                    contentContainer.addView(imageView);
                }
            } else if (c instanceof Text) {
                NoonTextView textView = new NoonTextView(context);
                textView.setLayoutParams(params);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsManager.getFontSize());
                textView.setLineSpacing(0, 1.5f);
                textView.setText(Html.fromHtml(((Text) c).getContent().replaceAll("<p>&nbsp;</p>", "")));

                if (!textView.getText().toString().isEmpty()) {
                    contentContainer.addView(textView);
                }
            } else if (c instanceof YoutubeVideo) {
                FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.youtube_video_preview, contentContainer, false);
                frameLayout.setLayoutParams(params);
                contentContainer.addView(frameLayout);

                ImageView imageView = (ImageView) frameLayout.findViewById(R.id.image);
                loadImage(imageView, ApisUtils.getYoutubeThumbnail(((YoutubeVideo) c).getSrc()), null);

                frameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, YoutubePlayerActivity.class);
                        intent.putExtra(YoutubePlayerActivity.VIDEO_ID_KEY, ((YoutubeVideo) c).getSrc());
                        context.startActivity(intent);
                    }
                });
            } else if (c instanceof SoundCloudTrack) {

                String url = ApisUtils.getSoundCloudStreamingUrl(((SoundCloudTrack) c).getId());

                AudioPlayer audioPlayer = new AudioPlayer(context, url, contentContainer);

                if (audioPlayers == null)
                    audioPlayers = new ArrayList<>();

                audioPlayers.add(audioPlayer);

                contentContainer.addView(audioPlayer.getView());

            }
        }

        if (firstImage) { // No Image found in the post..
            for (final PostContent content : postContent) {
                if (content instanceof YoutubeVideo) {
                    String imgUrl = ApisUtils.getYoutubeThumbnail(((YoutubeVideo) content).getSrc());
                    loadImage(postImage, imgUrl, postImageListener);
                    setImageClickListener(postImage, imgUrl);
                    break;
                } else if (content instanceof SoundCloudTrack) {

                    soundCloudService.getSoundCloudTrack(((SoundCloudTrack) content).getId())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<SoundCloudInfo>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onNext(SoundCloudInfo soundCloudInfo) {
                                    loadImage(postImage, soundCloudInfo.getImageUrl(), postImageListener);
                                    setImageClickListener(postImage, soundCloudInfo.getImageUrl());
                                }
                            });
                }
            }
        }

    }

    @Override
    public void setFontSize(int fontSize) {
        settingsManager.setFontSize(fontSize);
    }

    @Override
    public int getFontSize() {
        return settingsManager.getFontSize();
    }

    @Override
    public void addToFavorite(Post post) {
        favoriteDataSource.addToFavorite(post);
    }

    @Override
    public void removeFromFavorite(Post post) {
        favoriteDataSource.removeFromFavorite(post.getId());
    }

    @Override
    public void isFavorite(Post post) {
        favoriteDataSource.isFavorite(post.getId(), new Callbacks.Callback<Boolean>() {
            @Override
            public void onLoaded(Boolean result) {
                view.isFavoriteLoaded(result);
            }

            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        });
    }


    private void loadImage(ImageView imageView, final String url, RequestListener<String, GlideDrawable> listener) {
        Glide.with(context)
                .load(url)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_broken_image))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_image))
                .listener(listener)
                .into(imageView);
    }

    private void setImageClickListener(ImageView imageView, final String url) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageViewerActivity.class);
                intent.putExtra(ImageViewerActivity.IMAGE_URL, url);
                context.startActivity(intent);
            }
        });
    }

}
