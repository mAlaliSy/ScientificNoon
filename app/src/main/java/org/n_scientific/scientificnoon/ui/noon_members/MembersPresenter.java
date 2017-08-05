package org.n_scientific.scientificnoon.ui.noon_members;

import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.pojo.User;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.UsersRemoteDataSource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by mohammad on 14/07/17.
 */

public class MembersPresenter implements MembersContract.Presenter {

    private MembersContract.View view;

    @Inject
    UsersRemoteDataSource usersRemoteDataSource;

    public MembersPresenter(MembersContract.View view) {
        this.view = view;
    }


    @Override
    public void loadUsers(int page, int perPage) {

        usersRemoteDataSource.getUsers(perPage, page, new Callbacks.ListCallback<User>() {
            @Override
            public void onLoaded(List<User> results) {
                view.onUsersLoaded(results);
            }

            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        });
    }
}
