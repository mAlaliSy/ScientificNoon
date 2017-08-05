package org.n_scientific.scientificnoon.ui.main;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohammad on 29/05/17.
 */

@Module
public class MainModule {

    @Provides
    MainPresenter providePresenter() {
        return new MainPresenter();
    }

}
