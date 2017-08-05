package org.n_scientific.scientificnoon.ui.posts_list;

import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceComponent;
import org.n_scientific.scientificnoon.ui.CustomScope;

import dagger.Component;

/**
 * Created by mohammad on 16/07/17.
 */

@CustomScope
@Component(modules = PostsListModule.class, dependencies = RemoteDataSourceComponent.class)
public interface PostsListComponent {
    void inject(PostsListActivity activity);

    void inject(PostsPresenter presenter);

}
