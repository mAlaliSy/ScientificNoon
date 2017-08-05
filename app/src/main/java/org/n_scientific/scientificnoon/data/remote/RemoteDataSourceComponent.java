package org.n_scientific.scientificnoon.data.remote;

import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CommentsRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.PostsRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.UsersRemoteDataSource;
import org.n_scientific.scientificnoon.ui.BaseActivity;
import org.n_scientific.scientificnoon.ui.main.MainActivity;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mohammad on 29/05/17.
 */

@Singleton
@Component(modules = RemoteDataSourceModule.class)
public interface RemoteDataSourceComponent {
    CategoriesRemoteDataSource getCatDataSource();

    CommentsRemoteDataSource getComDataSource();

    PostsRemoteDataSource getPostsDataSource();

    UsersRemoteDataSource getUsersDataSource();

}
