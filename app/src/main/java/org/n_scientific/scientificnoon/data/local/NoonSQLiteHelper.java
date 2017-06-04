package org.n_scientific.scientificnoon.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mohammad on 03/06/17.
 */

public class NoonSQLiteHelper extends SQLiteOpenHelper {

    public static class SQLiteContract {
        public static final String DB_NAME = "noon.db";
        public static final String FAVORITES_TABLE_NAME = "favorites";
        public static final String ID_COLUMN_NAME = "_id";
        public static final String POST_ID_COLUMN_NAME = "post_id";
        public static final String TITLE_COLUMN_NAME = "title";
        public static final String CONTENT_COLUMN_NAME = "content";
        public static final String LINK_COLUMN_NAME = "link";
        public static final String DATE_COLUMN_NAME = "date";
        public static final String CATEGORIES_COLUMN_NAME = "categories";

        public static final int POST_ID_COLUMN_INDEX = 1;
        public static final int TITLE_COLUMN_INDEX = 2;
        public static final int CONTENT_COLUMN_INDEX = 3;
        public static final int LINK_COLUMN_INDEX = 4;
        public static final int DATE_COLUMN_INDEX = 5;
        public static final int CATEGORIES_COLUMN_INDEX = 6;

    }

    public NoonSQLiteHelper(Context context) {
        super(context, SQLiteContract.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + SQLiteContract.FAVORITES_TABLE_NAME
                + " ( "
                + SQLiteContract.ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + SQLiteContract.POST_ID_COLUMN_NAME + " INTEGER NOT NULL , "
                + SQLiteContract.TITLE_COLUMN_NAME + " VARCHAR(255) NOT NULL , "
                + SQLiteContract.CONTENT_COLUMN_NAME + " TEXT NOT NULL , "
                + SQLiteContract.LINK_COLUMN_NAME + " VARCHAR(255) NOT NULL , "
                + SQLiteContract.DATE_COLUMN_NAME + " VARCHAR(255) NOT NULL , "
                + SQLiteContract.CATEGORIES_COLUMN_NAME + " VARCHAR(255) NOT NULL );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
