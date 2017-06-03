package org.n_scientific.scientificnoon.contentparser;

/**
 * Created by mohammad on 31/05/17.
 */

public class Image extends PostContent {

    private String src;

    public Image(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
