package org.n_scientific.scientificnoon.data.remote.services;

import org.n_scientific.scientificnoon.data.pojo.Post;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mohammad on 28/05/17.
 */

public interface PostsService {

    @GET("posts")
    Observable<List<Post>> getPosts(@Query("page") int page, @Query("per_page") int perPage);

    @GET("posts")
    Observable<List<Post>> getPostsByUser(@Query("author") int userId, @Query("page") int page, @Query("per_page") int perPage);

    @GET("posts")
    Observable<List<Post>> getPostsByCategory(@Query("categories") int catId, @Query("page") int page, @Query("per_page") int perPage);

    @GET("posts/{id}")
    Observable<Post> getPost(@Path("id") int id);

}
