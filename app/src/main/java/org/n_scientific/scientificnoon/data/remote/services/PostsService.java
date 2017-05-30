package org.n_scientific.scientificnoon.data.remote.services;

import org.n_scientific.scientificnoon.data.pojo.Post;

import java.util.List;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mohammad on 28/05/17.
 */

public interface PostsService {

    @GET("posts?page={page}&per_page={per_page}")
    Observable<List<Post>> getPosts(@Path("page") int page, @Path("per_page") int perPage);

    @GET("posts?page={page}&per_page={per_page}&author={user_id}")
    Observable<List<Post>> getPostsByUser(@Path("user_id") int userId, @Path("page") int page, @Path("per_page") int perPage);

    @GET("posts?page={page}&per_page={per_page}&categories={cat_id}")
    Observable<List<Post>> getPostsByCategory(@Path("cat_id") int catId, @Path("page") int page, @Path("per_page") int perPage);

    @GET("posts/{id}")
    Observable<Post> getPost(@Path("id") int id);

}
