package org.n_scientific.scientificnoon.data;

import java.util.List;

/**
 * Created by mohammad on 29/05/17.
 */

public interface Callbacks {
    interface ListCallback<T>{
        void onLoaded(List<T> categories);
        void onError(String message);
    }

    interface Callback<T>{

        void onLoaded(T post);

        void onError(String message);

    }

}
