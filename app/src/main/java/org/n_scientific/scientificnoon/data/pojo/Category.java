package org.n_scientific.scientificnoon.data.pojo;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.n_scientific.scientificnoon.data.local.NoonSQLiteHelper;

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

    public static Category fromCursor(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(NoonSQLiteHelper.CategoriesContract.ID_INDEX));
        category.setName(cursor.getString(NoonSQLiteHelper.CategoriesContract.NAME_INDEX));
        category.setParent(cursor.getInt(NoonSQLiteHelper.CategoriesContract.PARENT_ID_INDEX));
        return category;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoonSQLiteHelper.CategoriesContract.ID_COLUMN_NAME, id);
        contentValues.put(NoonSQLiteHelper.CategoriesContract.NAME_COLUMN, name);
        contentValues.put(NoonSQLiteHelper.CategoriesContract.PARENT_ID_COLUMN, parent);

        return contentValues;
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
