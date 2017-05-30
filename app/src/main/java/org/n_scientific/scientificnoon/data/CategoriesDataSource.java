package org.n_scientific.scientificnoon.data;

import org.n_scientific.scientificnoon.data.pojo.Category;

import java.util.List;

/**
 * Created by mohammad on 28/05/17.
 */

public interface CategoriesDataSource {

    void getCategories(Callbacks.ListCallback<Category> callback);

    void getCategory(int catId, Callbacks.Callback<Category> callback);

    void getCategoriesByParent(int parentId, Callbacks.ListCallback<Category> callback);


}
