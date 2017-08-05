package org.n_scientific.scientificnoon.ui.favorites;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.n_scientific.scientificnoon.NoonApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.Post;
import org.n_scientific.scientificnoon.data.remote.DaggerSoundCloudComponent;
import org.n_scientific.scientificnoon.data.remote.SoundCloudModule;
import org.n_scientific.scientificnoon.ui.BaseActivity;
import org.n_scientific.scientificnoon.ui.adapters.PostsAdapter;
import org.n_scientific.scientificnoon.utils.AnimUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class FavoritesActivity extends BaseActivity implements FavoritesContract.View {


    @BindView(R.id.postsRecyclerView)
    RecyclerView postsRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    @BindView(R.id.noFavoritesMessage)
    ViewGroup noFavoritesMessage;


    @Inject
    FavoritesContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();


        postsRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        presenter.loadFavorites();
    }

    @Override
    public int getContentResource() {
        return R.layout.activity_favorites;
    }

    @Override
    public void injectDependencies() {

        FavoritesComponent component = DaggerFavoritesComponent.builder()
                .favoritesModule(new FavoritesModule(this))
                .localDataSourceComponent(((NoonApplication) getApplication()).getLocalDataSourceComponent())
                .remoteDataSourceComponent(((NoonApplication) getApplication()).getRemoteDataSourceComponent())
                .build();

        component.inject(this);
        component.inject((FavoritesPresenter) presenter);
    }

    @Override
    public void onFavoritesLoaded(List<Post> favorites) {

        progressBar.setVisibility(View.GONE);

        if (favorites.size() == 0) {
            AnimUtils.slideDown(noFavoritesMessage, 500);
            noFavoritesMessage.setVisibility(View.VISIBLE);
        } else {
            PostsAdapter postsAdapter = new PostsAdapter(this, favorites
                    , ((NoonApplication) getApplication()).getRemoteDataSourceComponent().getCatDataSource()
                    , ((NoonApplication) getApplication()).getLocalDataSourceComponent().getCategoriesLocalDataSource()
                    , DaggerSoundCloudComponent.builder().soundCloudModule(new SoundCloudModule()).build().getSoundCloudService(), false);
            postsAdapter.setAllDownloaded(true);

            postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            postsRecyclerView.setAdapter(postsAdapter);
            postsRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showErrorMessage(String message) {

    }
}
