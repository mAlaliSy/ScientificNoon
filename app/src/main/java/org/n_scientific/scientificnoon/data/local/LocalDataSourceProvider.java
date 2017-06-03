package org.n_scientific.scientificnoon.data.local;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohammad on 03/06/17.
 */

@Module
public class LocalDataSourceProvider {

    private Context context;

    public LocalDataSourceProvider(Context context) {
        this.context = context;
    }



    @Provides
    public FavoriteDataSource provideFavoriteDataSource(){
        return new FavoriteDataSource(context);
    }
}
