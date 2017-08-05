package org.n_scientific.scientificnoon.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.n_scientific.scientificnoon.ui.main.MainActivity;

import java.util.Locale;

/**
 * Created by mohammad on 04/06/17.
 */

public class LaunchActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change the language to Arabic => Use RTL instead of LTR..
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, null);

        startActivity(new Intent(this, MainActivity.class));

        finish();
    }
}
