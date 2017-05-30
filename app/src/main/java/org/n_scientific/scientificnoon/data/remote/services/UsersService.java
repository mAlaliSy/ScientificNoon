package org.n_scientific.scientificnoon.data.remote.services;

import org.n_scientific.scientificnoon.data.pojo.User;

import java.util.List;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mohammad on 29/05/17.
 */

public interface UsersService {

    @GET("users?page={page}&per_page={per_page}")
    Observable<List<User>> getUsers(@Path("page") int page, @Path("per_page") int perPage);

    @GET("users/{id}")
    Observable<User> getUser(@Path("id") int userId);

}
