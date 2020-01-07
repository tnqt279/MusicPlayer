package com.example.musicplayer;

import java.io.Serializable;

public class JukeboxTrack implements Serializable {
    private String name;
    private String artist;
    private String filetype;
    private String duration;
    private String album;
    private String filepath;
    private int number;

    public  JukeboxTrack (String Name, String Artist, String Album,String Filepath, String Duration) {
        name = Name;
        artist = Artist;
        album = Album;
        //filetype = Filetype;
        filepath = Filepath;
        duration = Duration;
        //number = Number;

    }

    public JukeboxTrack () {
        name = "";
        artist = "" ;
        album = "";
        filetype = "";
        filepath = "";
        duration = "";
        number = 0;
    }

    //Name
    public String GetName() {
        return name;
    }
    public void SetName(String Name) {
        name = Name;
    }

    //Artist
    public String GetArtist () {
        return artist;
    }
    public void SetArtist(String Artist) {
        artist = Artist;
    }

    //Album
    public String GetAlbum() {
        return  album;
    }
    public void SetAlbum(String Album) {
        album = Album;
    }

    //Filetype
    public String GetFiletype() {
        return filetype;
    }
    public void SetFiletype(String Filetype) {
        filetype = Filetype;
    }

    //Filepath
    public String GetFilepath () {
        return  filepath;
    }
    public void SetFilepath(String Filepath) {
        filepath = Filepath;
    }

    //Duration
    public String GetDuration() {
        return duration;
    }
    public void SetDuration(String d) {
        duration = d;
    }

    //Track number
    public int GetNumber() {return number;}
    public void SetNumber(int n) {number = n;}

}
