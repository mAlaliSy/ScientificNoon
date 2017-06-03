package org.n_scientific.scientificnoon.data;

import org.n_scientific.scientificnoon.data.pojo.Post;

/**
 * Created by mohammad on 28/05/17.
 */

public interface PostsDataSource {

    void getPosts(int postsPerPage, int page, Callbacks.ListCallback<Post> callback);

    void getPost(int postId, Callbacks.Callback<Post> callback);

    void getPostsByCategory(int catId, int postsPerPage, int page, Callbacks.ListCallback<Post> callback);

    void getPostsByUser(int userId, int postsPerPage, int page, Callbacks.ListCallback<Post>  callback);

    void search(String query, int postsPerPage, int page, Callbacks.ListCallback<Post> callback);

}
