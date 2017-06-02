package org.n_scientific.scientificnoon.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.n_scientific.scientificnoon.data.remote.services.SoundCloudService;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mohammad on 01/06/17.
 */

@Module
public class SoundCloudModule {

    @Provides
    Retrofit provideRetrofit() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("http://api.soundcloud.com/")
                .build();
    }

    @Inject
    @Provides
    SoundCloudService provideService(Retrofit retrofit){
        return retrofit.create(SoundCloudService.class);

    }


}
