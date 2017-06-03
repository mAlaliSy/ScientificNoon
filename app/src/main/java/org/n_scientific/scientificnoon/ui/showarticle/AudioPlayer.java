package org.n_scientific.scientificnoon.ui.showarticle;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.n_scientific.scientificnoon.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohammad on 01/06/17.
 */

public class AudioPlayer implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener {

    private Context context;
    private String url;

    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.playButton)
    AppCompatImageView btnPlay;

    @BindView(R.id.duration)
    TextView duration;

    @BindView(R.id.currentPosition)
    TextView currentPosition;

    Handler handler;

    private LinearLayout linearLayout;

    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;
    private boolean prepared = false;

    public AudioPlayer(Context context, String url, ViewGroup container) {
        this.context = context;
        this.url = url;

        linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.audio_player, container, false);

        ButterKnife.bind(this, linearLayout);

        handler = new Handler();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getText(R.string.sound_error), Toast.LENGTH_SHORT).show();
        }


        btnPlay.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    public View getView() {
        return linearLayout;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.playButton:
                if (prepared) {
                    if (isPlaying) {
                        btnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_auto_mirror));
                        mediaPlayer.pause();
                    } else {
                        btnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));
                        mediaPlayer.start();
                        handler.postDelayed(timeUpdater, 1000);
                    }

                    isPlaying = !isPlaying;
                }


                break;

        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (prepared) {
            mediaPlayer.seekTo(seekBar.getProgress());
            currentPosition.setText(parseTime(seekBar.getProgress()));
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        seekBar.setMax(mp.getDuration());
        duration.setText(parseTime(mediaPlayer.getDuration()));
        prepared = true;
    }

    private Runnable timeUpdater = new Runnable() {
        @Override
        public void run() {
            updateTime();
        }
    };


    private void updateTime() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        currentPosition.setText(parseTime(mediaPlayer.getCurrentPosition()));
        if (isPlaying)
            handler.postDelayed(timeUpdater, 1000);
    }

    private String parseTime(int time) {

        if (time / 1000 % 60 >= 10)
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        else
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
    }

}
