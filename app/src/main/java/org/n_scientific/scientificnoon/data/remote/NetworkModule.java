package org.n_scientific.scientificnoon.data.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.data.remote.services.CategoriesService;
import org.n_scientific.scientificnoon.data.remote.services.CommentsService;
import org.n_scientific.scientificnoon.data.remote.services.PostsService;
import org.n_scientific.scientificnoon.data.remote.services.UsersService;
import org.n_scientific.scientificnoon.utils.Utils;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mohammad on 29/05/17.
 */

@Module
public class NetworkModule {

    private Context context;

    public NetworkModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.cache(new Cache(context.getCacheDir(), 10 * 1024 * 1024)); // 10 MB
        clientBuilder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (Utils.isConnected(context)) {
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + 300)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
            }
        });


        return new Retrofit.Builder()
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Config.API_URL)
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
