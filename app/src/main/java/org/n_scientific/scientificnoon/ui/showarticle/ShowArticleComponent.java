package org.n_scientific.scientificnoon.ui.showarticle;

import org.n_scientific.scientificnoon.data.local.LocalDataSourceComponent;
import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceComponent;
import org.n_scientific.scientificnoon.data.remote.SoundCloudComponent;
import org.n_scientific.scientificnoon.ui.CustomScope;

import dagger.Component;

/**
 * Created by mohammad on 30/05/17.
 */

@CustomScope
@Component(dependencies = {RemoteDataSourceComponent.class, SoundCloudComponent.class, LocalDataSourceComponent.class}, modules = ShowArticleModule.class)
public interface ShowArticleComponent {

    void inject(ShowArticleActivity showArticleActivity);

    void inject(ShowArticlePresenter presenter);
}
