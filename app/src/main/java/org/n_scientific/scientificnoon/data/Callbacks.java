package org.n_scientific.scientificnoon.data;

import java.util.List;

/**
 * Created by mohammad on 29/05/17.
 */

public interface Callbacks {
    interface ListCallback<T>{
        void onLoaded(List<T> results);
        void onError(String message);
    }

    interface Callback<T>{

        void onLoaded(T result);

        void onError(String message);

    }


}
