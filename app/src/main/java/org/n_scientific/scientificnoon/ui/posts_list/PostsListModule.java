package org.n_scientific.scientificnoon.ui.posts_list;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohammad on 16/07/17.
 */

@Module
public class PostsListModule {

    PostsListContract.View view;

    public PostsListModule(PostsListContract.View view) {
        this.view = view;
    }

    @Provides
    PostsListContract.Presenter providePresenter() {
        return new PostsPresenter(view);
    }
}
