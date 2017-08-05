package org.n_scientific.scientificnoon.ui.showarticle;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.ui.adapters.ImagesPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewerActivity extends AppCompatActivity {

    public static final String IMAGES_URLS = "image_url";
    public static final String IMAGE_INDEX = "image_index";


    @BindView(R.id.imagesPager)
    ViewPager imagesPager;

    ArrayList<String> imagesUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image_viewer);

        ButterKnife.bind(this);


        ArrayList<String> imgUrls = getIntent().getStringArrayListExtra(IMAGES_URLS);
        imagesUrls = new ArrayList<>(imgUrls.size());

        for (String s : imgUrls) {
            String imgUrl = s.replaceAll("-(\\d){2,}x(\\d){2,}", ""); // Remove resize part -If there is- from URL
            imagesUrls.add(imgUrl);

            Log.d("TAG", "Befor : " + s + "\t After : " + imgUrl);
        }


        ImagesPagerAdapter imagesPagerAdapter = new ImagesPagerAdapter(getSupportFragmentManager(), imagesUrls);

        imagesPager.setAdapter(imagesPagerAdapter);

        imagesPager.setCurrentItem(getIntent().getIntExtra(IMAGE_INDEX, 0));

    }

}
