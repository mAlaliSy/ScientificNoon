package org.n_scientific.scientificnoon.ui.posts_list;

import org.n_scientific.scientificnoon.data.pojo.Post;

import java.util.List;

/**
 * Created by mohammad on 16/07/17.
 */

public interface PostsListContract {

    interface View {
        void onPostsLoaded(List<Post> posts);

        void showErrorMessage(String message);
    }

    interface Presenter {
        void loadCategoryPosts(int catId, int page, int perPage);

        void loadRecentPosts(int page, int perPage);
    }
}
