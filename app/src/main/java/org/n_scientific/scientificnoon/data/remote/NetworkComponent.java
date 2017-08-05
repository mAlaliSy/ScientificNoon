package org.n_scientific.scientificnoon.data.remote;

import org.n_scientific.scientificnoon.ui.BaseActivity;

import javax.inject.Inject;
import javax.inject.Singleton;


import dagger.Component;

/**
 * Created by mohammad on 29/05/17.
 */

@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {

}
