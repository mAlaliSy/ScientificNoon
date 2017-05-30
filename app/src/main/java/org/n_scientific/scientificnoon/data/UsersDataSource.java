package org.n_scientific.scientificnoon.data;

import org.n_scientific.scientificnoon.data.pojo.User;

import java.util.List;

/**
 * Created by mohammad on 28/05/17.
 */

public interface UsersDataSource {
    void getUsers(int usersPerPage, int page, Callbacks.ListCallback<User> callback);

    void getUser(int userId, Callbacks.Callback<User> callback);

}
