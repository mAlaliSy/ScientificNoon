package org.n_scientific.scientificnoon.data.local;

import dagger.Component;

/**
 * Created by mohammad on 03/06/17.
 */

@Component(modules = LocalDataSourceModule.class)
public interface LocalDataSourceComponent {

    FavoriteDataSource getFavoriteDataSource();

    CategoriesLocalDataSource getCategoriesLocalDataSource();

}
