package org.n_scientific.scientificnoon.ui.main;

import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Post;

import java.util.List;

/**
 * Created by mohammad on 29/05/17.
 */

public interface MainContract {
    interface View {

        void onPostsLoaded(List<Post> posts);

        void onCategoriesLoaded(List<Category> categories);

        void showErrorMessage(String message);
    }

    interface Presenter {
        void loadCategories(int parentId);

        void loadPosts(int page);

        void loadCategoryPosts(int catId, int page);

        void loadSearchPosts(String query, int page);
    }

}
