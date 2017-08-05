package org.n_scientific.scientificnoon.ui.favorites;

import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.local.FavoriteDataSource;
import org.n_scientific.scientificnoon.data.pojo.Post;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by mohammad on 13/07/17.
 */

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private FavoritesContract.View view;

    @Inject
    FavoriteDataSource favoriteDataSource;

    public FavoritesPresenter(FavoritesContract.View view) {
        this.view = view;
    }


    @Override
    public void loadFavorites() {
        favoriteDataSource.getFavorites(new Callbacks.ListCallback<Post>() {
            @Override
            public void onLoaded(List<Post> results) {
                view.onFavoritesLoaded(results);
            }

            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        });
    }
}
