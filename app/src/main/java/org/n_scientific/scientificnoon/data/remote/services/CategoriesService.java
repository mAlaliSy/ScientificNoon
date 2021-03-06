package org.n_scientific.scientificnoon.data.remote.services;

import org.n_scientific.scientificnoon.data.pojo.Category;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mohammad on 29/05/17.
 */

public interface CategoriesService {

    @GET("categories?per_page=100&orderby=id&exclude=1,28")
    Observable<List<Category>> getCategories();

    @GET("categories/{id}")
    Observable<Category> getCategory(@Path("id") int id);

    @GET("categories")
    Observable<List<Category>> getCategoriesByParent(@Query("parent") int parentId);


}
