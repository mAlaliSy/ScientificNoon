package org.n_scientific.scientificnoon.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mohammad on 28/05/17.
 */

public class Category implements Serializable{
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("parent")
    @Expose
    private int parent;

    public Category() {
    }

    public Category(int id, int count, String description, String name, int parent) {
        this.id = id;
        this.count = count;
        this.description = description;
        this.name = name;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }
}
