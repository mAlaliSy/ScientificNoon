package org.n_scientific.scientificnoon.ui.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import org.n_scientific.scientificnoon.MyApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.PostsRemoteDataSource;
import org.n_scientific.scientificnoon.ui.BaseActivity;

import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainContract.View {

    private static final String TAG = "MainActivity";

    // Mode of display: Recent Posts, By User, By Category..
    public static final String MODE_KEY = "mode";

    public static final int RECENT_POSTS_MODE = 0;
    public static final int CATEGORIES_MODE = 1;
    public static final int USER_MODE = 2;

    // Category to be displayed..
    public static final String CATEGORY_KEY = "category";
    // User to display his posts..
    public static final String USER_KEY = "user";

    @Inject
    PostsRemoteDataSource postsRemoteDataSource;

    @Inject
    MainContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Change the language to Arabic => Use RTL instead of LTR..
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, null);

    }

    @Override
    public int getContentResource() {
        return R.layout.activity_main;
    }

    @Override
    public void injectDependencies() {
        DaggerMainComponent.builder()
                .mainModule(new MainModule())
                .remoteDataSourceComponent(((MyApplication) getApplication())
                        .getRemoteDataSourceComponent())
                .build()
                .inject(this);
    }
}
