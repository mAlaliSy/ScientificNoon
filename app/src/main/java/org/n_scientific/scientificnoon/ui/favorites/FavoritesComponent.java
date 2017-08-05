package org.n_scientific.scientificnoon.ui.favorites;

import org.n_scientific.scientificnoon.data.local.LocalDataSourceComponent;
import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceComponent;
import org.n_scientific.scientificnoon.ui.CustomScope;

import dagger.Component;

/**
 * Created by mohammad on 13/07/17.
 */
@CustomScope
@Component(modules = FavoritesModule.class, dependencies = {LocalDataSourceComponent.class, RemoteDataSourceComponent.class})
public interface FavoritesComponent {

    void inject(FavoritesPresenter presenter);

    void inject(FavoritesActivity favoritesActivity);
}
