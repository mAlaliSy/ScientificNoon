package org.n_scientific.scientificnoon.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.NoonApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.Callbacks;
import org.n_scientific.scientificnoon.data.local.CategoriesLocalDataSource;
import org.n_scientific.scientificnoon.data.pojo.Category;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.CategoriesRemoteDataSource;
import org.n_scientific.scientificnoon.ui.about.AboutActivity;
import org.n_scientific.scientificnoon.ui.favorites.FavoritesActivity;
import org.n_scientific.scientificnoon.ui.main.MainActivity;
import org.n_scientific.scientificnoon.utils.ResourcesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohammad on 29/05/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Callbacks.ListCallback<Category> {

    private static final String TAG = "BaseActivity";

    protected CategoriesRemoteDataSource catRemoteDataSource;


    protected CategoriesLocalDataSource categoriesLocalDataSource;


    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    protected NavigationView mNavigationView;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.categoriesProgressBar)
    protected ProgressBar categoriesProgressBar;

    List<Category> categories;

    private ActionBarDrawerToggle mDrawerToggle;

    protected static int lastCategorySelectedId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        catRemoteDataSource = ((NoonApplication) getApplication()).getRemoteDataSourceComponent().getCatDataSource();
        categoriesLocalDataSource = ((NoonApplication) getApplication()).getLocalDataSourceComponent().getCategoriesLocalDataSource();
        injectDependencies();
        setContentView(getContentResource());

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        mNavigationView.setNavigationItemSelectedListener(this);

        categoriesLocalDataSource.categoriesByParent(0, this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        ResourcesUtils.changeLanguageToArabic(this);
    }

    public abstract int getContentResource();

    public abstract void injectDependencies();



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        mDrawerLayout.closeDrawer(Gravity.RIGHT);
        
        switch (menuItem.getItemId()) {

            case R.id.about:
                if (!(this instanceof AboutActivity))
                    startActivity(new Intent(this, AboutActivity.class));

                break;
            case R.id.favorites:
                if (!(this instanceof FavoritesActivity)) {
                    startActivity(new Intent(this, FavoritesActivity.class));
                }
                break;
            case R.id.twitter:
                Intent twitter = new Intent(Intent.ACTION_VIEW);
                twitter.setData(Uri.parse(Config.TWITTER_URL));
                startActivity(twitter);

                break;
            case R.id.youtube:
                Intent youtube = new Intent(Intent.ACTION_VIEW);
                youtube.setData(Uri.parse(Config.YOUTUBE_URL));
                startActivity(youtube);

                break;
            case R.id.soundcloud:
                Intent soundcloud = new Intent(Intent.ACTION_VIEW);
                soundcloud.setData(Uri.parse(Config.SOUNDCLOUD_URL));
                startActivity(soundcloud);

                break;
            default:
                Category category = categories.get(menuItem.getItemId());
                if (!(this instanceof MainActivity && lastCategorySelectedId == category.getId())) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(MainActivity.MODE_KEY, MainActivity.CATEGORIES_MODE);
                    intent.putExtra(MainActivity.CATEGORY_KEY, category);
                    startActivity(intent);

                    if (this instanceof MainActivity && lastCategorySelectedId != -1 || this instanceof FavoritesActivity)
                        finish();
                }
        }


        return false;
    }

    @Override
    public void onLoaded(List<Category> results) {
        if (results.size() == 0)
            catRemoteDataSource.getCategoriesByParent(0, this);
        else {
            this.categories = results;
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId() == 1 || categories.get(i).getId() == 28)
                    categories.remove(i);
            }
            fetchCategories();
        }
    }

    private void fetchCategories() {
        categoriesProgressBar.setVisibility(View.GONE);
        Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < categories.size(); i++) {
            menu.add(R.id.categoriesItems, i, Menu.NONE, categories.get(i).getName()).setCheckable(true).setChecked(false);
        }

    }


    @Override
    public void onError(String message) {

    }
}
