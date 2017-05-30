package org.n_scientific.scientificnoon.data.remote;

import org.n_scientific.scientificnoon.data.remote.NetworkModule;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CommentsRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.PostsRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.UsersRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.services.CategoriesService;
import org.n_scientific.scientificnoon.data.remote.services.CommentsService;
import org.n_scientific.scientificnoon.data.remote.services.PostsService;
import org.n_scientific.scientificnoon.data.remote.services.UsersService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohammad on 29/05/17.
 */

@Module(includes = NetworkModule.class)
public class RemoteDataSourceModule {


    @Singleton
    @Provides
    CategoriesRemoteDataSource provideCategoriesRemoteDataSource(CategoriesService categoriesService) {
        return new CategoriesRemoteDataSource(categoriesService);
    }

    @Singleton
    @Provides
    CommentsRemoteDataSource provideCommentsRemoteDataSource(CommentsService commentsService) {
        return new CommentsRemoteDataSource(commentsService);
    }

    @Singleton
    @Provides
    PostsRemoteDataSource providePostsRemoteDataSource(PostsService postsService) {
        return new PostsRemoteDataSource(postsService);
    }

    @Singleton
    @Provides
    UsersRemoteDataSource provideUsersRemoteDataSource(UsersService usersService) {
        return new UsersRemoteDataSource(usersService);
    }


}
