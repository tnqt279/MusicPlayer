package com.example.musicplayer;



import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoScreen extends AppCompatActivity {

    VideoView videoView = (VideoView) findViewById(R.id.videoView);
    MediaController mediaController = new MediaController(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutvid);

        //String path = "android.resource://"+getPackageName()+"/"+com.example.musicplayer.R.raw.video;
        //videoView.setVideoPath(path);

        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        videoView.stopPlayback();
    }


}
