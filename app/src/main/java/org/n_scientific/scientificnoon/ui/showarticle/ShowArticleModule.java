package org.n_scientific.scientificnoon.ui.showarticle;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohammad on 30/05/17.
 */

@Module
public class ShowArticleModule {

    @Provides
    ShowArticleContract.Presenter providePresenter() {
        return new ShowArticlePresenter();
    }

}
