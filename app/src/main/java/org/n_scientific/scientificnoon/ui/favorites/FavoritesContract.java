package org.n_scientific.scientificnoon.ui.favorites;

import org.n_scientific.scientificnoon.data.pojo.Post;

import java.util.List;

/**
 * Created by mohammad on 13/07/17.
 */

public interface FavoritesContract {

    interface View {

        void onFavoritesLoaded(List<Post> favorites);

        void showErrorMessage(String message);

    }

    interface Presenter {
        void loadFavorites();
    }

}
