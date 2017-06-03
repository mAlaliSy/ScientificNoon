package org.n_scientific.scientificnoon.ui.showarticle;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.pojo.Comment;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.pojo.User;

import java.util.List;

/**
 * Created by mohammad on 30/05/17.
 */

public interface ShowArticleContract {

    interface View {

        void showErrorMessage(String message);

        void userLoaded(User user);

        void categoriesLoaded(List<Category> categories);

        void commentsLoaded(List<Comment> comments);

        Activity getActivity();

        ShowArticleComponent getComponent();

        void showErrorMessage(int resId);

        void isFavoriteLoaded(boolean isFavorite);

    }

    interface Presenter {

        void setView(View view);

        void loadCategories(int[] catsIds);

        void loadComments(int postId, int page);

        void loadUser(int userId);

        void parseContent(String content, LinearLayout contentContainer, ImageView postImage);

        void setFontSize(int fontSize);

        int getFontSize();

        void addToFavorite(Post post);

        void removeFromFavorite(Post post);

        void isFavorite(Post post);

    }


}
