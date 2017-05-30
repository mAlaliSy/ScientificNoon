package org.n_scientific.scientificnoon.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.n_scientific.scientificnoon.AppConstants;
import org.n_scientific.scientificnoon.data.remote.services.CategoriesService;
import org.n_scientific.scientificnoon.data.remote.services.CommentsService;
import org.n_scientific.scientificnoon.data.remote.services.PostsService;
import org.n_scientific.scientificnoon.data.remote.services.UsersService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mohammad on 29/05/17.
 */

@Module
public class NetworkModule {

    @Singleton
    @Provides
    Retrofit provideRetrofit() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(AppConstants.API_URL)
                .build();
    }

    @Singleton
    @Provides
    CategoriesService provideCategoriesService(Retrofit retrofit) {
        return retrofit.create(CategoriesService.class);
    }

    @Singleton
    @Provides
    CommentsService provideCommentsService(Retrofit retrofit) {
        return retrofit.create(CommentsService.class);
    }

    @Singleton
    @Provides
    PostsService providePostsService(Retrofit retrofit) {
        return retrofit.create(PostsService.class);
    }

    @Singleton
    @Provides
    UsersService provideUsersService(Retrofit retrofit) {
        return retrofit.create(UsersService.class);
    }

}
