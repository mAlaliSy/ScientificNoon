package org.n_scientific.scientificnoon.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.n_scientific.scientificnoon.ui.main.MainActivity;
import org.n_scientific.scientificnoon.utils.ResourcesUtils;

import java.util.Locale;

/**
 * Created by mohammad on 04/06/17.
 */

public class LaunchActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ResourcesUtils.changeLanguageToArabic(this);

        startActivity(new Intent(this, MainActivity.class));

        finish();
    }
}
