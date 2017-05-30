package org.n_scientific.scientificnoon.data.remote.remote_data_sources;

import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.CommentsDataSource;
import org.n_scientific.scientificnoon.data.pojo.Comment;
import org.n_scientific.scientificnoon.data.remote.services.CommentsService;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mohammad on 29/05/17.
 */

public class CommentsRemoteDataSource implements CommentsDataSource {

    private CommentsService commentsService;

    @Inject
    public CommentsRemoteDataSource(CommentsService commentsService){
        this.commentsService = commentsService;
    }

    @Override
    public void getComments(int postId, int commentsPerPage, int page, final Callbacks.ListCallback<Comment> callback) {
        commentsService.getComments(postId, page, commentsPerPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        callback.onLoaded(comments);
                    }
                });
    }
}
