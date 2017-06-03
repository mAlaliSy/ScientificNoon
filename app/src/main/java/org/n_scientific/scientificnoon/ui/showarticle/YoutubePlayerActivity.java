package org.n_scientific.scientificnoon.ui.showarticle;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YoutubePlayerActivity extends YouTubeBaseActivity {

    public static final String VIDEO_ID_KEY = "video_id";

    @BindView(R.id.youtube_player)
    YouTubePlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_youtube_player);

        final String videoId = getIntent().getStringExtra(VIDEO_ID_KEY);

        ButterKnife.bind(this);

        playerView.initialize(Config.API_URL, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b)
                    youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(YoutubePlayerActivity.this, getString(R.string.youtube_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
