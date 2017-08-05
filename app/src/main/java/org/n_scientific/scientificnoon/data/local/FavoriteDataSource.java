package org.n_scientific.scientificnoon.data.local;

import android.content.Context;
import android.database.Cursor;

import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.pojo.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mohammad on 02/06/17.
 */

public class FavoriteDataSource {


    private NoonSQLiteHelper sqLiteHelper;

    public FavoriteDataSource(Context context) {
        sqLiteHelper = new NoonSQLiteHelper(context);
    }


    public void addToFavorite(final Post post) {
        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                sqLiteHelper.getWritableDatabase().insert(NoonSQLiteHelper.FavoriteContract.TABLE_NAME, null, post.toContentValues());
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

    }


    public void getFavorites(final Callbacks.ListCallback<Post> callback) {
        Observable.fromCallable(new Callable<List<Post>>() {
            @Override
            public List<Post> call() throws Exception {


                Cursor cursor = sqLiteHelper.getWritableDatabase().query(NoonSQLiteHelper.FavoriteContract.TABLE_NAME, null, null, null, null, null, null);


                List<Post> posts = new ArrayList<>(cursor.getCount());

                if (cursor.moveToFirst()) {
                    do {
                        posts.add(Post.fromCursor(cursor));
                    } while (cursor.moveToNext());
                }

                cursor.close();

                return posts;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Post>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e.getMessage());

            }

            @Override
            public void onNext(List<Post> posts) {
                callback.onLoaded(posts);
            }
        });
    }

    public void removeFromFavorite(final int postId) {
        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                sqLiteHelper.getWritableDatabase().delete(NoonSQLiteHelper.FavoriteContract.TABLE_NAME, NoonSQLiteHelper.FavoriteContract.POST_ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(postId)});
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

    }

    public void isFavorite(final int postId, final Callbacks.Callback<Boolean> callback) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {


                Cursor cursor = sqLiteHelper.getWritableDatabase()
                        .query(NoonSQLiteHelper.FavoriteContract.TABLE_NAME, null,
                                NoonSQLiteHelper.FavoriteContract.POST_ID_COLUMN_NAME + "=?",
                                new String[]{String.valueOf(postId)}, null, null, null);

                int count = cursor.getCount();
                cursor.close();


                return count != 0;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());

                    }

                    @Override
                    public void onNext(Boolean isFavorite) {
                        callback.onLoaded(isFavorite);
                    }
                });
    }


}
