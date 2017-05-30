package org.n_scientific.scientificnoon.data;

import org.n_scientific.scientificnoon.data.pojo.Comment;

import java.util.List;

/**
 * Created by mohammad on 28/05/17.
 */

public interface CommentsDataSource {
    void getComments(int postId, int commentsPerPage, int page, Callbacks.ListCallback<Comment> callback);



}
