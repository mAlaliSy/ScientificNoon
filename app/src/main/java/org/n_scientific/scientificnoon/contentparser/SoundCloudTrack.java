package org.n_scientific.scientificnoon.contentparser;

/**
 * Created by mohammad on 31/05/17.
 */

public class SoundCloudTrack extends PostContent {

    private String id;

    public SoundCloudTrack(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
