package org.n_scientific.scientificnoon;

import android.support.multidex.MultiDexApplication;

import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.local.DaggerLocalDataSourceComponent;
import org.n_scientific.scientificnoon.data.local.LocalDataSourceComponent;
import org.n_scientific.scientificnoon.data.local.LocalDataSourceModule;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.remote.DaggerNetworkComponent;
import org.n_scientific.scientificnoon.data.remote.DaggerRemoteDataSourceComponent;
import org.n_scientific.scientificnoon.data.remote.NetworkComponent;
import org.n_scientific.scientificnoon.data.remote.NetworkModule;
import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceComponent;
import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceModule;
import org.n_scientific.scientificnoon.utils.Utils;

import java.util.List;

/**
 * Created by mohammad on 29/05/17.
 */

public class NoonApplication extends MultiDexApplication {

    NetworkComponent networkComponent;
    RemoteDataSourceComponent remoteDataSourceComponent;
    LocalDataSourceComponent localDataSourceComponent;

    @Override
    public void onCreate() {
        super.onCreate();


        NetworkModule networkModule = new NetworkModule(this);

        networkComponent = DaggerNetworkComponent
                .builder()
                .networkModule(networkModule)
                .build();

        remoteDataSourceComponent = DaggerRemoteDataSourceComponent
                .builder()
                .networkModule(networkModule)
                .remoteDataSourceModule(new RemoteDataSourceModule())
                .build();
        localDataSourceComponent = DaggerLocalDataSourceComponent
                .builder()
                .localDataSourceModule(new LocalDataSourceModule(this))
                .build();

        if (Utils.isConnected(this)) {
            remoteDataSourceComponent.getCatDataSource().getCategories(new Callbacks.ListCallback<Category>() {
                @Override
                public void onLoaded(List<Category> results) {
                    localDataSourceComponent.getCategoriesLocalDataSource().replaceCategories(results);
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    }


    public NetworkComponent getNetworkComponent() {
        return networkComponent;
    }

    public RemoteDataSourceComponent getRemoteDataSourceComponent() {
        return remoteDataSourceComponent;
    }

    public LocalDataSourceComponent getLocalDataSourceComponent() {
        return localDataSourceComponent;
    }
}
