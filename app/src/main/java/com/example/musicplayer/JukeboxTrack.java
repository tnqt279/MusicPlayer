package com.example.musicplayer;

public class JukeboxTrack {
    private String name;
    private String artist;
    private String filetype;
    private String duration;
    private String album;
    private String filepath;

    public  JukeboxTrack (String Name, String Artist, String Album, String Filetype,String Filepath, String Duration) {
        name = Name;
        artist = Artist;
        album = Album;
        filetype = Filetype;
        filepath = Filepath;
        duration = Duration;

    }

    public JukeboxTrack () {
        name = "";
        artist = "" ;
        album = "";
        filetype = "";
        filepath = "";
        duration = "";
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



}
