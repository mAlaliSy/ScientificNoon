package org.n_scientific.scientificnoon.ui.main;

import org.n_scientific.scientificnoon.data.local.LocalDataSourceComponent;
import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceComponent;
import org.n_scientific.scientificnoon.ui.CustomScope;

import dagger.Component;

/**
 * Created by mohammad on 29/05/17.
 */
@CustomScope
@Component(dependencies = {RemoteDataSourceComponent.class, LocalDataSourceComponent.class}, modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);

    void inject(MainPresenter presenter);
}
