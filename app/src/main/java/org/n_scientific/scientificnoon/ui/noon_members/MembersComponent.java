package org.n_scientific.scientificnoon.ui.noon_members;

import org.n_scientific.scientificnoon.data.local.LocalDataSourceComponent;
import org.n_scientific.scientificnoon.data.remote.RemoteDataSourceComponent;
import org.n_scientific.scientificnoon.ui.CustomScope;

import dagger.Component;

/**
 * Created by mohammad on 14/07/17.
 */

@CustomScope
@Component(modules = MembersModule.class, dependencies = {RemoteDataSourceComponent.class})
public interface MembersComponent {

    void inject(MembersPresenter presenter);

    void inject(NoonMembersActivity activity);

}
