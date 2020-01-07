package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.VideoView;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    ArrayList<String> arrayList;
    Jukebox jukebox;
    ListView listview;
    //hello
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.Tool_bar);
        setSupportActionBar(toolbar);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

            }
        }
        else
        {
            FileListed();
        }

    }

    public void FileListed() {
        listview = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        GetMusic();
        GetVideo();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Cursor cursor = listview.getItemAtPosition(position);
                int i = getActualPosition(position, listview);

                OpenMusic(i);

            }
        });
    }
    public void GetMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if(songCursor != null && songCursor.moveToFirst()){
            int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.RELATIVE_PATH);
            int songPath2 = songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int songPath3 = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{
                String currentTitle = songCursor.getString(songName);
                String currentArtist = songCursor.getString(songArtist);
                int temp = songCursor.getInt(Duration);
                String currentDuration = ConvertDurationMusic(temp);
                String currentAlbum = songCursor.getString(songAlbum);
                arrayList.add(currentTitle + "\nArtist: " + currentArtist + "\nDuration: " + currentDuration + "\nAlbum: "+ currentAlbum);
                String currentPath = songCursor.getString(songPath);
                String currentPath2 = songCursor.getString(songPath2);
                String currentPath3 = songCursor.getString(songPath3);
                //arrayList.add(currentTitle + "\n" + currentArtist);
                JukeboxTrack jbt = new JukeboxTrack(currentTitle, currentArtist, currentAlbum,   currentPath2, currentDuration);
                jukebox.AddTrack(jbt);
                arrayList.add(jbt.GetName() + "\n" + jbt.GetFilepath());
            }while(songCursor.moveToNext());
        }
    }

    public void GetVideo(){
        ContentResolver contentResolver = getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor videoCursor = contentResolver.query(videoUri, null, null, null, null);
        if(videoCursor != null && videoCursor.moveToFirst()){
            int videoName = videoCursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            int videoDirector = videoCursor.getColumnIndex(MediaStore.Video.Media.ARTIST);
            int Duration = videoCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do{
                String currentTitle = videoCursor.getString(videoName);
                String currentDirector = videoCursor.getString(videoDirector);
                int temp = videoCursor.getInt(Duration);
                String currentDuration = ConvertDurationVideo(temp);
                arrayList.add(currentTitle + "\nDirector: " + currentDirector + "\nDuration: "+ currentDuration);
            }while(videoCursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted!!", Toast.LENGTH_SHORT).show();
                        FileListed();
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_music,menu);
        MenuItem item = menu.findItem(R.id.search_music);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public int getActualPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return position;
        } else {
            final int childIndex = position - firstListItemPosition;
            return childIndex;

        }
    }

    public void OpenMusic(int index) {
        Intent intent = new Intent(this, MusicScreen.class);
        Bundle JukeboxBundle = new Bundle();
        JukeboxBundle.putSerializable("JUKEBOX_BUNDLE", jukebox);
        intent.putExtra("SONG_INDEX", index);
        intent.putExtra("JUKEBOX", JukeboxBundle);

        startActivity(intent);
    }


    public String ConvertDurationMusic(int duration)
    {
        String result = "";
        int mns = (duration / 60000) % 60000;
        int scs = duration % 60000 / 1000;
        if(mns > 0 && scs > 0 && scs < 10)
            result =  mns +":0"+scs;
        else if(mns > 0 && mns < 10)
            result = mns +":"+scs;
        return result;
    }
    public String ConvertDurationVideo(int duration)
    {
        String result = "";
        int hrs = duration / 360000;
        int mns = (duration / 60000) % 60000;
        int scs = duration % 60000 / 1000;
        if(hrs > 0 && mns > 0 && mns < 10 && scs > 0 && scs < 10)
            result=hrs+":0"+mns+":0"+scs;
        else if(hrs > 0 && mns >= 10 && scs > 0 && scs < 10)
            result =  hrs + ":" + mns +":0" + scs;
        else if(hrs > 0 && mns >= 10 && scs >= 10)
            result =  hrs + ":" + mns +":" + scs;
        else if(hrs == 0 && mns > 0 && mns < 10 && scs > 0 && scs < 10)
            result = "00:0" + mns +":0" + scs;
        else if(hrs == 0 && mns >= 10 && scs > 0 && scs < 10)
            result = "00:" + mns + ":0" + scs;
        else if(hrs == 0 && mns >= 10 && scs >= 10)
            result = "00:" + mns + ":" + scs;
        return result;
    }
}

