package org.n_scientific.scientificnoon.data.remote.remote_data_sources;

import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.UsersDataSource;
import org.n_scientific.scientificnoon.data.pojo.User;
import org.n_scientific.scientificnoon.data.remote.services.UsersService;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mohammad on 29/05/17.
 */

public class UsersRemoteDataSource implements UsersDataSource {

    private UsersService usersService;

    @Inject
    public UsersRemoteDataSource(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void getUsers(int usersPerPage, int page, final Callbacks.ListCallback<User> callback) {
        usersService.getUsers(page, usersPerPage, "id", "1, 214")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<User> users) {
                        callback.onLoaded(users);
                    }
                });
    }

    @Override
    public void getUser(int userId, final Callbacks.Callback<User> callback) {

        usersService.getUser(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        callback.onLoaded(user);
                    }
                });

    }
}
