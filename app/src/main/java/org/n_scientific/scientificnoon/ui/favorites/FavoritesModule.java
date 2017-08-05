package org.n_scientific.scientificnoon.ui.favorites;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohammad on 13/07/17.
 */

@Module
public class FavoritesModule {

    private FavoritesContract.View view;

    public FavoritesModule(FavoritesContract.View view) {
        this.view = view;
    }

    @Provides
    FavoritesContract.Presenter providePresenter() {
        return new FavoritesPresenter(view);
    }

}
