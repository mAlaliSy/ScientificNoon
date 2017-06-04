package org.n_scientific.scientificnoon.data.remote.remote_data_sources;

import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.CategoriesDataSource;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.remote.services.CategoriesService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mohammad on 29/05/17.
 */

public class CategoriesRemoteDataSource implements CategoriesDataSource {

    private CategoriesService categoriesService;

    private List<Category> categories;

    @Inject
    public CategoriesRemoteDataSource(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
        categories = new ArrayList<>();
    }


    @Override
    public void getCategories(final Callbacks.ListCallback<Category> callback) {
        categoriesService.getCategories().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Category>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        callback.onLoaded(categories);
                    }
                });
    }

    @Override
    public void getCategory(int catId, final Callbacks.Callback<Category> callback) {

        boolean found = false;

        for (Category category : categories) {

            if (category.getId() == catId) {
                found = true;
                callback.onLoaded(category);
                break;
            }

        }

        if (!found)
            categoriesService.getCategory(catId).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Category>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            callback.onError(e.getMessage());
                        }

                        @Override
                        public void onNext(Category category) {
                            callback.onLoaded(category);
                            categories.add(category);
                        }
                    });
    }

    @Override
    public void getCategoriesByParent(int parentId, final Callbacks.ListCallback<Category> callback) {
        categoriesService.getCategoriesByParent(parentId).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Category>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Category> categories) {
                        callback.onLoaded(categories);
                    }
                });

    }
}
