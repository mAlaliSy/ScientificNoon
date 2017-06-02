package org.n_scientific.scientificnoon.data.remote;

import org.n_scientific.scientificnoon.data.remote.services.SoundCloudService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mohammad on 01/06/17.
 */

@Component(modules = SoundCloudModule.class)
public interface SoundCloudComponent {

    SoundCloudService getSoundCloudService();

}
