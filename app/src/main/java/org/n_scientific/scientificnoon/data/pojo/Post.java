package org.n_scientific.scientificnoon.data.pojo;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.n_scientific.scientificnoon.data.local.NoonSQLiteHelper;

import java.io.Serializable;

/**
 * Created by mohammad on 28/05/17.
 */

public class Post implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("modified")
    @Expose
    private String modified;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("content")
    @Expose
    private Content content;

    @SerializedName("author")
    @Expose
    private int authorId;

    @SerializedName("categories")
    @Expose
    private int[] categories;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("title")
    @Expose
    private Title title;


    public Post() {
    }

    public static Post fromCursor(Cursor cursor) {
        Post post = new Post();

        post.id = cursor.getInt(NoonSQLiteHelper.FavoriteContract.POST_ID_COLUMN_INDEX);
        post.title = new Title(cursor.getString(NoonSQLiteHelper.FavoriteContract.TITLE_COLUMN_INDEX));
        post.content = new Content(cursor.getString(NoonSQLiteHelper.FavoriteContract.CONTENT_COLUMN_INDEX));
        post.link = cursor.getString(NoonSQLiteHelper.FavoriteContract.LINK_COLUMN_INDEX);
        post.date = cursor.getString(NoonSQLiteHelper.FavoriteContract.DATE_COLUMN_INDEX);

        String cats = cursor.getString(NoonSQLiteHelper.FavoriteContract.CATEGORIES_COLUMN_INDEX);
        post.categories = new Gson().fromJson(cats, int[].class);
//
//        String[] categoriesS = cats.split(",");
//
//        int[] categories = new int[categoriesS.length];
//
//        for (int i = 0; i < categories.length; i++) {
//            categories[i] = Integer.valueOf(categoriesS[i]);
//        }

//        post.categories = categories;

        return post;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoonSQLiteHelper.FavoriteContract.POST_ID_COLUMN_NAME, id);
        contentValues.put(NoonSQLiteHelper.FavoriteContract.TITLE_COLUMN_NAME, title.getTitle());
        contentValues.put(NoonSQLiteHelper.FavoriteContract.CONTENT_COLUMN_NAME, content.getContent());
        contentValues.put(NoonSQLiteHelper.FavoriteContract.LINK_COLUMN_NAME, link);
        contentValues.put(NoonSQLiteHelper.FavoriteContract.DATE_COLUMN_NAME, date);

//        StringBuilder builder = new StringBuilder("");
//        for (int i = 0; i < categories.length; i++) {
//            builder.append(categories[i]);
//            if (i != categories.length - 1)
//                builder.append(",");
//        }
//
        contentValues.put(NoonSQLiteHelper.FavoriteContract.CATEGORIES_COLUMN_NAME, new Gson().toJson(categories));

        return contentValues;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int[] getCategories() {
        return categories;
    }

    public void setCategories(int[] categories) {
        this.categories = categories;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }



    // Represent Title JSON Object
    public static class Title implements Serializable {
        @SerializedName("rendered")
        @Expose
        private String title;

        public Title() {
        }

        public Title(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    // Represent Content JSON Object
    public static class Content implements Serializable {
        @SerializedName("rendered")
        @Expose
        private String content;

        @SerializedName("protected")
        @Expose
        private boolean isProtected;

        public Content() {
        }

        public Content(String content) {
            this.content = content;
        }

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
