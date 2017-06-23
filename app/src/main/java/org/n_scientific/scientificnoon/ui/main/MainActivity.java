package org.n_scientific.scientificnoon.ui.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;

import org.n_scientific.scientificnoon.MyApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.PostsRemoteDataSource;
import org.n_scientific.scientificnoon.ui.BaseActivity;
import org.n_scientific.scientificnoon.ui.adapters.PostsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.inject.Inject;

import static android.R.attr.onClick;
import static android.R.attr.visibility;
import static org.n_scientific.scientificnoon.R.menu.*;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.search_view_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView =(SearchView)item.getActionView();


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSearch:
                EditText txt = (EditText)findViewById(R.id.txtSearch);
                txt.setVisibility(View.VISIBLE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
