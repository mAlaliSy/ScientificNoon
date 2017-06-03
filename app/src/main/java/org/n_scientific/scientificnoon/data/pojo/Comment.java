package org.n_scientific.scientificnoon.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mohammad on 28/05/17.
 */

public class Comment implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("author_name")
    @Expose
    private String authorName;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("content")
    @Expose
    private Content content;

    public Comment() {
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    // Represent Content JSON Object
    public class Content implements Serializable{
        @SerializedName("rendered")
        @Expose
        private String content;

        @SerializedName("protected")
        @Expose
        private boolean isProtected;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isProtected() {
            return isProtected;
        }

        public void setProtected(boolean aProtected) {
            isProtected = aProtected;
        }
    }



}
