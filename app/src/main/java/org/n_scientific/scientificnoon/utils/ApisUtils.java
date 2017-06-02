package org.n_scientific.scientificnoon.utils;

import org.n_scientific.scientificnoon.Config;

/**
 * Created by mohammad on 01/06/17.
 */

public final class ApisUtils {

    private ApisUtils() {
    }

    public static String getYoutubeThumbnail(String videoId) {
        return String.format("http://img.youtube.com/vi/%s/0.jpg", videoId);
    }

    public static String getSoundCloudId(String url) {
        String id = url.split("tracks")[1];
        id = id.substring(3, id.indexOf('&'));
        return id;
    }

    public static String getSoundCloudStreamingUrl(String id) {
        return String.format("https://api.soundcloud.com/tracks/%s/stream?client_id=%s",
                id, Config.SOUNDCLOUD_API_KEY);
    }


}
