package org.n_scientific.scientificnoon.ui.noon_members;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohammad on 14/07/17.
 */

@Module
public class MembersModule {


    MembersContract.View view;

    public MembersModule(MembersContract.View view) {
        this.view = view;
    }

    @Provides
    MembersContract.Presenter providePresenter() {
        return new MembersPresenter(view);
    }

}
