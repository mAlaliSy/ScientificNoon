package org.n_scientific.scientificnoon.utils;

import android.util.Log;

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
        String[] split = url.split("tracks");
        if (split.length > 1) {
            String id = split[1];
            id = id.substring(3, id.indexOf('&'));
            return id;
        } else
            return null;
    }

    public static String getYoutubeVideoKey(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.indexOf('?'));
    }

    public static String getSoundCloudStreamingUrl(String id) {
        return String.format("https://api.soundcloud.com/tracks/%s/stream?client_id=%s",
                id, Config.SOUNDCLOUD_API_KEY);
    }


}
