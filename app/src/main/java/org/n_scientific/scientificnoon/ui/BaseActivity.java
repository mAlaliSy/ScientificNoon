package org.n_scientific.scientificnoon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.n_scientific.scientificnoon.MyApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.PostsRemoteDataSource;
import org.n_scientific.scientificnoon.ui.main.MainActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohammad on 29/05/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BaseActivity";
    @Inject
    protected CategoriesRemoteDataSource catRemoteDataSource;

    @Inject
    protected PostsRemoteDataSource postsRemoteDataSource;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    protected NavigationView mNavigationView;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.categoriesProgressBar)
    ProgressBar categoriesProgressBar;

    List<Category> categories;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).getRemoteDataSourceComponent().inject(this);
        injectDependencies();
        setContentView(getContentResource());

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        catRemoteDataSource.getCategories(new Callbacks.ListCallback<Category>() {

            @Override
            public void onLoaded(List<Category> results) {
                categoriesProgressBar.setVisibility(View.GONE);

                categories = results;
                Menu menu = mNavigationView.getMenu();
                for (int i = 0; i < categories.size(); i++) {
                    menu.add(R.id.categoriesItems, i, Menu.NONE, categories.get(i).getName());
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });


        mNavigationView.setNavigationItemSelectedListener(this);


    }


    public abstract int getContentResource();

    public abstract void injectDependencies();



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.settings:

                break;
            case R.id.about:

                break;
            default:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.MODE_KEY, MainActivity.CATEGORIES_MODE);
                intent.putExtra(MainActivity.CATEGORY_KEY, categories.get(menuItem.getItemId()));

                startActivity(intent);


        }


        return false;
    }
}
