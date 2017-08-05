package org.n_scientific.scientificnoon.data.remote.services;

import org.n_scientific.scientificnoon.data.pojo.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mohammad on 29/05/17.
 */

public interface UsersService {

    @GET("users")
    Observable<List<User>> getUsers(@Query("page") int page, @Query("per_page") int perPage, @Query("orderby") String orderBy, @Query("exclude") String exclude);

    @GET("users/{id}")
    Observable<User> getUser(@Path("id") int userId);

}
