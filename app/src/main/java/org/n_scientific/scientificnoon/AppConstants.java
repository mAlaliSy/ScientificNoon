package org.n_scientific.scientificnoon;

/**
 * Created by mohammad on 29/05/17.
 */

public final class AppConstants { // Avoid extending container class..

    private AppConstants(){ // Avoid creating instances from container class..

    }

    public static final String API_URL = "http://n-scientific.org/wp-json/wp/v2/";
    public static final int DEFAULT_POSTS_PER_CALL = 10;
    public static final int NUMBER_OF_USERS_PER_CALL = 10;


}
