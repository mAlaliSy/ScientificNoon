package org.n_scientific.scientificnoon.ui.noon_members;

import org.n_scientific.scientificnoon.data.pojo.User;

import java.util.List;

/**
 * Created by mohammad on 14/07/17.
 */

public interface MembersContract {

    interface View {

        void onUsersLoaded(List<User> users);

        void showErrorMessage(String message);
    }

    interface Presenter {

        void loadUsers(int page, int perPage);

    }

}
