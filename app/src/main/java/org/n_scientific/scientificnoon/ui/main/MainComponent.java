package org.n_scientific.scientificnoon.ui.main;

import org.n_scientific.scientificnoon.ui.CustomScope;
import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceComponent;

import dagger.Component;

/**
 * Created by mohammad on 29/05/17.
 */
@CustomScope
@Component(dependencies = RemoteDataSourceComponent.class, modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
