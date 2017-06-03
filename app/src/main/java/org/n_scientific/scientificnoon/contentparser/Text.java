package org.n_scientific.scientificnoon.contentparser;

/**
 * Created by mohammad on 31/05/17.
 */

public class Text extends PostContent {
    private String content;

    public Text(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
