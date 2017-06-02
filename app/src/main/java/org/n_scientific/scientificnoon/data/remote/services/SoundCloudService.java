package org.n_scientific.scientificnoon.data.remote.services;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.data.pojo.SoundCloudInfo;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mohammad on 01/06/17.
 */

public interface SoundCloudService {

    @GET("tracks/{id}?client_id=" + Config.SOUNDCLOUD_API_KEY)
    Observable<SoundCloudInfo> getSoundCloudTrack(@Path("id") String trackId);

}
