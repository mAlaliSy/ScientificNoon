package org.n_scientific.scientificnoon.ui.noon_members;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.NoonApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.User;
import org.n_scientific.scientificnoon.ui.BaseActivity;
import org.n_scientific.scientificnoon.ui.adapters.UsersAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

public class NoonMembersActivity extends BaseActivity implements MembersContract.View {

    @Inject
    MembersContract.Presenter presenter;


    List<User> users;

    UsersAdapter usersAdapter;

    @BindView(R.id.users)
    RecyclerView usersRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private boolean downloading;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        downloading = false;
        page = 1;
        presenter.loadUsers(page, Config.NUMBER_OF_USERS_PER_CALL);
    }

    @Override
    public int getContentResource() {
        return R.layout.activity_noon_members;
    }

    @Override
    public void injectDependencies() {

        MembersComponent component = DaggerMembersComponent.builder()
                .remoteDataSourceComponent(((NoonApplication) getApplication()).getRemoteDataSourceComponent())
                .membersModule(new MembersModule(this)).build();

        component.inject(this);
        component.inject((MembersPresenter) presenter);
    }

    @Override
    public void onUsersLoaded(List<User> data) {
        downloading = false;
        if (users == null) {
            initRecyclerView(data);
        } else {
            users.addAll(data);
            usersAdapter.notifyDataSetChanged();

            if (data.size() < Config.NUMBER_OF_USERS_PER_CALL)
                usersAdapter.setAllDownloaded(true);
        }

    }

    private void initRecyclerView(final List<User> data) {
        users = data;
        usersAdapter = new UsersAdapter(data.size() < Config.NUMBER_OF_USERS_PER_CALL, users, this);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        usersRecyclerView.setLayoutManager(layoutManager);
        SlideInBottomAnimationAdapter adapter = new SlideInBottomAnimationAdapter(usersAdapter);
        adapter.setFirstOnly(true);
        adapter.setDuration(250);
        usersRecyclerView.setAdapter(adapter);

        usersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (layoutManager.findLastVisibleItemPosition() == users.size() && !downloading && !usersAdapter.isAllDownloaded()) {
                    page++;
                    presenter.loadUsers(page, Config.NUMBER_OF_USERS_PER_CALL);
                    downloading = true;
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        progressBar.setVisibility(View.GONE);
        usersRecyclerView.setVisibility(View.VISIBLE);


        usersAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String message) {

    }
}
