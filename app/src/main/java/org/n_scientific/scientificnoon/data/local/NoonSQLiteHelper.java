package org.n_scientific.scientificnoon.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mohammad on 03/06/17.
 */

public class NoonSQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "noon.db";

    public static class FavoriteContract {
        public static final String TABLE_NAME = "favorites";
        public static final String ID_COLUMN_NAME = "_id";
        public static final String POST_ID_COLUMN_NAME = "post_id";
        public static final String TITLE_COLUMN_NAME = "title";
        public static final String CONTENT_COLUMN_NAME = "content";
        public static final String LINK_COLUMN_NAME = "link";
        public static final String DATE_COLUMN_NAME = "date";
        public static final String CATEGORIES_COLUMN_NAME = "categories";

        public static final int ID_COLUMN_INDEX = 0;
        public static final int POST_ID_COLUMN_INDEX = 1;
        public static final int TITLE_COLUMN_INDEX = 2;
        public static final int CONTENT_COLUMN_INDEX = 3;
        public static final int LINK_COLUMN_INDEX = 4;
        public static final int DATE_COLUMN_INDEX = 5;
        public static final int CATEGORIES_COLUMN_INDEX = 6;

    }

    public static class CategoriesContract {

        public static final String TABLE_NAME = "categories";
        public static final String ID_COLUMN_NAME = "_id";
        public static final String NAME_COLUMN = "name";
        public static final String PARENT_ID_COLUMN = "parent_id";


        public static final int ID_INDEX = 0;
        public static final int NAME_INDEX = 1;
        public static final int PARENT_ID_INDEX = 2;
    }

    public NoonSQLiteHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String favoriteSql = "CREATE TABLE IF NOT EXISTS " + FavoriteContract.TABLE_NAME
                + " ( "
                + FavoriteContract.ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + FavoriteContract.POST_ID_COLUMN_NAME + " INTEGER NOT NULL , "
                + FavoriteContract.TITLE_COLUMN_NAME + " VARCHAR(255) NOT NULL , "
                + FavoriteContract.CONTENT_COLUMN_NAME + " TEXT NOT NULL , "
                + FavoriteContract.LINK_COLUMN_NAME + " VARCHAR(255) NOT NULL , "
                + FavoriteContract.DATE_COLUMN_NAME + " VARCHAR(255) NOT NULL , "
                + FavoriteContract.CATEGORIES_COLUMN_NAME + " VARCHAR(255) NOT NULL );";

        String categoriesSql = "CREATE TABLE IF NOT EXISTS " + CategoriesContract.TABLE_NAME
                + " ( "
                + CategoriesContract.ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + CategoriesContract.NAME_COLUMN + " VARCHAR(255) NOT NULL , "
                + CategoriesContract.PARENT_ID_COLUMN + " INTEGER NOT NULL );";

        db.execSQL(favoriteSql);
        db.execSQL(categoriesSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
