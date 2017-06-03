package org.n_scientific.scientificnoon.contentparser;

/**
 * Created by mohammad on 31/05/17.
 */

public class YoutubeVideo extends PostContent {

    private String src;

    public YoutubeVideo(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
