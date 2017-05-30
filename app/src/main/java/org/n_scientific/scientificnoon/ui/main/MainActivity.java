package org.n_scientific.scientificnoon.ui.main;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.n_scientific.scientificnoon.MyApplication;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.remote.remote_data_sources.PostsRemoteDataSource;
import org.n_scientific.scientificnoon.ui.BaseActivity;

import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainContract.View {

    private static final String TAG = "MainActivity";
    @Inject
    PostsRemoteDataSource postsRemoteDataSource;

    @Inject
    MainContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
