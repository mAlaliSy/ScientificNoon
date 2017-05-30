package org.n_scientific.scientificnoon.data.remote.services;

import org.n_scientific.scientificnoon.data.pojo.Comment;

import java.util.List;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mohammad on 29/05/17.
 */

public interface CommentsService {

    @GET("comments?post={post_id}&page={page}&per_page={per_page}")
    Observable<List<Comment>> getComments(@Path("post_id") int postId, @Path("page") int page, @Path("per_page") int perPage);


}
