package org.n_scientific.scientificnoon.utils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.pojo.SoundCloudInfo;
import org.n_scientific.scientificnoon.data.remote.services.SoundCloudService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mohammad on 03/06/17.
 */

public final class PostsUtils {

    private PostsUtils() {
    }


    public static void getPostImage(String postContent, SoundCloudService soundCloudService, final Callbacks.Callback<String> callback) {

        Element element = Jsoup.parse(postContent).body();

        Elements images = element.getElementsByTag("img");

        if (images.size() != 0) {
            callback.onLoaded(images.first().attr("src"));
        } else {
            Elements iframes = element.getElementsByTag("iframe");

            if (iframes.size() != 0) {

                String src = iframes.first().attr("src");
                if (src.contains("youtube")) {
                    String key = ApisUtils.getYoutubeVideoKey(src);
                    callback.onLoaded(ApisUtils.getYoutubeThumbnail(key));
                } else if (src.contains("soundcloud")) {
                    soundCloudService
                            .getSoundCloudTrack(ApisUtils.getSoundCloudId(src))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<SoundCloudInfo>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            callback.onError(e.getMessage());
                        }

                        @Override
                        public void onNext(SoundCloudInfo soundCloudInfo) {
                            callback.onLoaded(soundCloudInfo.getImageUrl());
                        }
                    });
                }else
                    callback.onLoaded(null);

            }

        }

    }


}
