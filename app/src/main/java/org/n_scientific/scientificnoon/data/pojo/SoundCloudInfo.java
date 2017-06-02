package org.n_scientific.scientificnoon.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mohammad on 01/06/17.
 */

public class SoundCloudInfo implements Serializable {


    @SerializedName("artwork_url")
    @Expose
    private String imageUrl;

    public SoundCloudInfo() {
    }

    public SoundCloudInfo(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
