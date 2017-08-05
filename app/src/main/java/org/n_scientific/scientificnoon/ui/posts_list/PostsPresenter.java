package org.n_scientific.scientificnoon.ui.posts_list;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.PostsRemoteDataSource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by mohammad on 16/07/17.
 */

public class PostsPresenter implements PostsListContract.Presenter {

    PostsListContract.View view;

    @Inject
    PostsRemoteDataSource postsRemoteDataSource;

    public PostsPresenter(PostsListContract.View view) {
        this.view = view;
    }

    @Override
    public void loadCategoryPosts(int catId, int page, int perPage) {
        postsRemoteDataSource.getPostsByCategory(catId, perPage, page, new Callbacks.ListCallback<Post>() {
            @Override
            public void onLoaded(List<Post> results) {
                view.onPostsLoaded(results);
            }

            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        });
    }

    @Override
    public void loadRecentPosts(int page, int perPage) {
        postsRemoteDataSource.getPosts(Config.DEFAULT_POSTS_PER_CALL, page, new Callbacks.ListCallback<Post>() {
            @Override
            public void onLoaded(List<Post> results) {
                view.onPostsLoaded(results);
            }

            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        });
    }
}
