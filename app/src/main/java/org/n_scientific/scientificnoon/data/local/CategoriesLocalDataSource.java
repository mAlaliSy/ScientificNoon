package org.n_scientific.scientificnoon.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.pojo.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mohammad on 16/07/17.
 */

public class CategoriesLocalDataSource {

    private NoonSQLiteHelper sqLiteHelper;

    public CategoriesLocalDataSource(Context context) {
        sqLiteHelper = new NoonSQLiteHelper(context);
    }

    public void replaceCategories(final List<Category> categories) {
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

                db.delete(NoonSQLiteHelper.CategoriesContract.TABLE_NAME, null, null);

                for (Category category : categories) {
                    db.insert(NoonSQLiteHelper.CategoriesContract.TABLE_NAME, null, category.toContentValues());
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void categoriesByParent(final int parentId, final Callbacks.ListCallback<Category> callback) {
        Observable.fromCallable(new Callable<List<Category>>() {
            @Override
            public List<Category> call() throws Exception {
                Cursor cursor = sqLiteHelper.getReadableDatabase().query(NoonSQLiteHelper.CategoriesContract.TABLE_NAME, null, NoonSQLiteHelper.CategoriesContract.PARENT_ID_COLUMN + " =? ", new String[]{String.valueOf(parentId)}, null, null, null);

                List<Category> categories = new ArrayList<>(cursor.getCount());

                if (cursor.moveToFirst()) {
                    do {
                        categories.add(Category.fromCursor(cursor));
                    } while (cursor.moveToNext());
                }

                cursor.close();

                return categories;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Category>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e.getMessage());

            }

            @Override
            public void onNext(List<Category> Categorys) {
                callback.onLoaded(Categorys);
            }
        });
    }

    public void getCategories(final Callbacks.ListCallback<Category> callback) {
        Observable.fromCallable(new Callable<List<Category>>() {
            @Override
            public List<Category> call() throws Exception {
                Cursor cursor = sqLiteHelper.getReadableDatabase().query(NoonSQLiteHelper.CategoriesContract.TABLE_NAME, null, null, null, null, null, null);

                List<Category> categories = new ArrayList<>(cursor.getCount());

                if (cursor.moveToFirst()) {
                    do {
                        categories.add(Category.fromCursor(cursor));
                    } while (cursor.moveToNext());
                }

                cursor.close();

                return categories;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Category>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e.getMessage());

            }

            @Override
            public void onNext(List<Category> Categorys) {
                callback.onLoaded(Categorys);
            }
        });
    }

    public void getCategory(final int id, final Callbacks.Callback<Category> callback) {
        Observable.fromCallable(new Callable<Category>() {
            @Override
            public Category call() throws Exception {
                Cursor cursor = sqLiteHelper.getReadableDatabase().query(NoonSQLiteHelper.CategoriesContract.TABLE_NAME, null, NoonSQLiteHelper.CategoriesContract.ID_COLUMN_NAME + " =? ", new String[]{String.valueOf(id)}, null, null, null);


                Category category = null;
                if (cursor.moveToFirst()) {
                    category = Category.fromCursor(cursor);
                }

                cursor.close();

                return category;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Category>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e.getMessage());

            }

            @Override
            public void onNext(Category category) {
                callback.onLoaded(category);
            }
        });
    }


}
