package org.n_scientific.scientificnoon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import org.n_scientific.scientificnoon.MyApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohammad on 29/05/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    @Inject
    protected CategoriesRemoteDataSource catRemoteDataSource;


    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    protected NavigationView mNavigationView;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
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

        loadMenuItems();


    }

    private void loadMenuItems() {
        catRemoteDataSource.getCategories(new Callbacks.ListCallback<Category>() {
            @Override
            public void onLoaded(List<Category> categories) {

                Menu menu = mNavigationView.getMenu();

                for (Category category : categories) {
                    menu.add(category.getName());
                }

            }

            @Override
            public void onError(String message) {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }


    public abstract int getContentResource();

    public abstract void injectDependencies();


}
