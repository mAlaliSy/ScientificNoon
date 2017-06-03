package org.n_scientific.scientificnoon;

import android.app.Application;

import org.n_scientific.scientificnoon.data.local.DaggerLocalDataSourceComponent;
import org.n_scientific.scientificnoon.data.local.LocalDataSourceComponent;
import org.n_scientific.scientificnoon.data.local.LocalDataSourceProvider;
import org.n_scientific.scientificnoon.data.remote.DaggerNetworkComponent;
import org.n_scientific.scientificnoon.data.remote.DaggerRemoteDataSourceComponent;
import org.n_scientific.scientificnoon.data.remote.NetworkComponent;
import org.n_scientific.scientificnoon.data.remote.NetworkModule;
import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceComponent;
import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceModule;

/**
 * Created by mohammad on 29/05/17.
 */

public class MyApplication extends Application {

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
                .localDataSourceProvider(new LocalDataSourceProvider(this))
                .build();
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
