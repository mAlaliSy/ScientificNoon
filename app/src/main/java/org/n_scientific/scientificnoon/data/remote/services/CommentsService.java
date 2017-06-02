package org.n_scientific.scientificnoon.data.remote.services;

import org.n_scientific.scientificnoon.data.pojo.Comment;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mohammad on 29/05/17.
 */

public interface CommentsService {

    @GET("comments")
    Observable<List<Comment>> getComments(@Query("post") int postId, @Query("page") int page, @Query("per_page") int perPage);


}
