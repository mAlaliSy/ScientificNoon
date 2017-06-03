package org.n_scientific.scientificnoon.ui.showarticle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;

import org.n_scientific.scientificnoon.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class ImageViewerActivity extends AppCompatActivity {

    public static final String IMAGE_URL = "image_url";

    @BindView(R.id.photoView)
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_image_viewer);

        ButterKnife.bind(this);

        String imgUrl = getIntent().getStringExtra(IMAGE_URL);

        Glide.with(this).load(imgUrl)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .fitCenter()
                .into(photoView);

    }
}
