package org.n_scientific.scientificnoon.ui.main;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohammad on 29/05/17.
 */

@Module
public class MainModule {

    @Provides
    MainContract.Presenter providePresenter() {
        return new MainPresenter();
    }

}
