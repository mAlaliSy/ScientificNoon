package org.n_scientific.scientificnoon.ui.main;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.local.CategoriesLocalDataSource;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.PostsRemoteDataSource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by mohammad on 29/05/17.
 */

public class MainPresenter implements MainContract.Presenter {


    MainActivity view;

    @Inject
    PostsRemoteDataSource postsRemoteDataSource;


    @Inject
    CategoriesLocalDataSource categoriesLocalDataSource;

    public void setView(MainActivity view) {
        this.view = view;
    }

    @Override
    public void loadCategories(int parentId) {

        categoriesLocalDataSource.categoriesByParent(parentId, new Callbacks.ListCallback<Category>() {
            @Override
            public void onLoaded(List<Category> results) {
                view.onCategoriesLoaded(results);
            }

            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        });
    }

    @Override
    public void loadPosts(int page) {
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

    @Override
    public void loadCategoryPosts(int catId, int page) {
        postsRemoteDataSource.getPostsByCategory(catId, Config.DEFAULT_POSTS_PER_CALL, page, new Callbacks.ListCallback<Post>() {
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
    public void loadSearchPosts(String query, int page) {
        postsRemoteDataSource.search(query, Config.DEFAULT_POSTS_PER_CALL, page, new Callbacks.ListCallback<Post>() {
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
