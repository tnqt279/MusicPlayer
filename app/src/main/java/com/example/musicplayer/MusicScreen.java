package com.example.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MusicScreen extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layoutmusic);
        //Intent caller = getIntent();
        //String prevActivity = caller.getExtras().getString("callingActivity");

        AskPermission();
        Load();
        Play();


    }

    @Override
    protected void onStop() {
        super.onStop();

        mediaPlayer.stop();
        mediaPlayer.release();
    }

    MediaPlayer mediaPlayer = new MediaPlayer();
    int Songs[] = new int[5];
    Jukebox jukebox = new Jukebox();
    int index = 0;
    public static final int REQUEST_CODE = 1;
    boolean RepeatFlag = false;
    boolean ShuffleFlag = false;
    //ArrayList<JukeboxTrack> testArray = new ArrayList<JukeboxTrack>();

    public void OnPlayButtonClick(View view) {
        //set new image
        ImageView  playButton = (ImageView)findViewById(R.id.PlayButton);
        if (playButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.play).getConstantState()) {

            Play();
            //playButton.setImageResource(R.drawable.play2);
            playButton.setImageResource(R.drawable.pause);

        }
        else if (playButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.pause).getConstantState()){
            //playButton.setImageResource(R.drawable.pause2);
            Pause();

            playButton.setImageResource(R.drawable.play);

        }
    }

    public void OnNextButtonClick(View view) {
        try {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;

            //if (++index >= Songs.length) {
            //    index = 0;
            //}
            if (++index >=jukebox.GetPlaylist().size())
            {
                index = 0;
            }
            mediaPlayer = MediaPlayer.create(this, Uri.parse(jukebox.getTrack(index).GetFilepath()));
            Play();
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
        }

    }

    public void OnPreviousButtonClick(View view) {
        try {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;

            //if (--index < 0) {
            //   index = Songs.length - 1;
            //}
            if (--index < 0) {
                index = jukebox.GetPlaylist().size() - 1;
            }

            mediaPlayer = MediaPlayer.create(this, Uri.parse(jukebox.getTrack(index).GetFilepath()));
            Play();
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
        }
    }

    public void Load() {
        //int Songs[] = new int[2];
        Songs[0] = R.raw.uprising;
        Songs[1] = R.raw.wonderful_tonight;
        Songs[2] = R.raw.dont_speak;
        Songs[3] = R.raw.thriller;
        Songs[4] = R.raw.yeah_yeah;
        //RecordTrackInfo();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer = MediaPlayer.create(this, Songs[0]);
        index = 0;
        mediaPlayer = MediaPlayer.create(this, Uri.parse("android.resource://"+getPackageName()+"/raw/uprising.mp3"));


    }
    public void Play() {
        //String SongUri[] = new String[2];
        //SongUri[0] = "android.resource://"+getPackageName()+"/"+R.raw.uprising;
        //SongUri[1] = "android.resource://"+getPackageName()+"/"+R.raw.wonderful_tonight;
        if (RepeatFlag)
            mediaPlayer.setLooping(true);
        else
            mediaPlayer.setLooping(false);
        mediaPlayer.start();
        TrackInfoUpdate();
    }
    public void Pause() {
        mediaPlayer.pause();
    }
    public void Shuffle() {
        //add arraylist of original song here
        ArrayList<JukeboxTrack> temparray = new ArrayList<JukeboxTrack>();
        //replace above
        Random random = new Random(System.currentTimeMillis());
        Collections.shuffle(temparray, random);
    }
    public void UnShuffle() {

    }
    public void RecordTrackInfo() {
        /*String path[] = new String[5];
        path[0] = "android.resource://"+getPackageName()+"/raw/uprising.mp3";
        path[1] = "android.resource://"+getPackageName()+"/raw/wonderful_tonight.mp3";
        path[2] = "android.resource://"+getPackageName()+"/raw/dont_speak.mp3";
        path[3] = "android.resource://"+getPackageName()+"/raw/thriller.mp3";
        path[4] = "android.resource://"+getPackageName()+"/raw/thriller.mp3";*/
        /*for (String s: path) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(s);
            JukeboxTrack jbt = new JukeboxTrack();
            jbt.SetName(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            jbt.SetArtist(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            jbt.SetAlbum(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            jbt.SetDuration(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            jbt.SetFiletype("mp3");
            jbt.SetFilepath(s);
            jukebox.AddTrack(jbt);
        }*/
        ArrayList<File> temp = new ArrayList<File>();
       File temp2[] = Environment.getExternalStorageDirectory().listFiles();
        for (File f: temp2
             ) {
            if(f.isDirectory()) {
                
            }
        }
    }

    public void OnRepeatButtonClick(View view) {

        ImageView RepeatButton = findViewById(R.id.RepeatButton);
        if (RepeatButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.repeat).getConstantState()) {
            RepeatButton.setImageResource(R.drawable.repeat2);
            mediaPlayer.setLooping(true);
            RepeatFlag = true;
        }
        else if (RepeatButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.repeat2).getConstantState()) {
            RepeatButton.setImageResource(R.drawable.repeat);
            mediaPlayer.setLooping(false);
            RepeatFlag = false;
        }
    }

    public void OnShuffleButtonClick(View view) {
        ImageView ShuffleButton = findViewById(R.id.ShuffleButton);
        if (ShuffleButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.shuffle).getConstantState()) {
            ShuffleButton.setImageResource(R.drawable.shuffle2);
            ShuffleFlag = true;
            Shuffle();
        }
        else if (ShuffleButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.shuffle2).getConstantState()) {
            ShuffleButton.setImageResource(R.drawable.shuffle);
            ShuffleFlag = false;
            UnShuffle();
        }
    }

    public void AskPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }

    }

    public void TrackInfoUpdate() {
        TextView TrackName = findViewById(R.id.SongName);
        if (jukebox.getTrack(0).GetName() == null)
            TrackName.setText("NULL");
        else
            TrackName.setText(jukebox.getTrack(0).GetName());
    }

    /*@Override
    public void OnRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show();
                    }
                }
                return;
            }
        }
    }*/
}
