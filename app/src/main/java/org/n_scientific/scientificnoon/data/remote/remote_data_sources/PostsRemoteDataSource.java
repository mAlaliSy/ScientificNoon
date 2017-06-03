package org.n_scientific.scientificnoon.data.remote.remote_data_sources;

import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.PostsDataSource;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.remote.services.PostsService;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mohammad on 29/05/17.
 */

public class PostsRemoteDataSource implements PostsDataSource {

    private PostsService postsService;

    @Inject
    public PostsRemoteDataSource(PostsService postsService) {
        this.postsService = postsService;
    }

    @Override
    public void getPosts(int postsPerPage, int page, final Callbacks.ListCallback<Post> callback) {
        postsService.getPosts(page, postsPerPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Post>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        callback.onLoaded(posts);
                    }
                });
    }

    @Override
    public void getPost(int postId, final Callbacks.Callback<Post> callback) {
        postsService.getPost(postId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Post>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Post post) {
                        callback.onLoaded(post);
                    }
                });
    }

    @Override
    public void getPostsByCategory(int catId, int postsPerPage, int page, final Callbacks.ListCallback<Post> callback) {
        postsService.getPostsByCategory(catId, page, postsPerPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Post>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        callback.onLoaded(posts);
                    }
                });
    }

    @Override
    public void getPostsByUser(int userId, int postsPerPage, int page, final Callbacks.ListCallback<Post> callback) {
        postsService.getPostsByUser(userId, page, postsPerPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Post>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        callback.onLoaded(posts);
                    }
                });
    }

    @Override
    public void search(String query, int postsPerPage, int page, final Callbacks.ListCallback<Post> callback) {
        postsService.search(query, postsPerPage, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Post>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        callback.onLoaded(posts);
                    }
                });
    }


}
