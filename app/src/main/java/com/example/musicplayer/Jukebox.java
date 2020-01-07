package com.example.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Jukebox implements Serializable {
    private ArrayList<JukeboxTrack> playlist;
    private int index = 0;
    private String MessageError;

    //handle in mp3 and extract info
    public Jukebox(ArrayList<JukeboxTrack> arrayList) {
        playlist = arrayList;
    }
    public Jukebox() {
        playlist = new ArrayList<JukeboxTrack>();
    }

    //extractinfo pass in arraylist
    public void ConstructPlaylist(Context context) {
        Uri sourceUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ArrayList<JukeboxTrack> tempArray = new ArrayList<JukeboxTrack>();
        Cursor cursor = context.getContentResolver().query(sourceUri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                JukeboxTrack tempTrack = new JukeboxTrack();
                tempTrack.SetName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                tempTrack.SetAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                tempTrack.SetArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                //tempTrack.SetDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                tempTrack.SetFiletype(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)));
                tempTrack.SetFilepath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.RELATIVE_PATH)));
                tempArray.add(tempTrack);
                cursor.moveToNext();
            }
        }
        playlist = tempArray;
    }

    public ArrayList<JukeboxTrack> GetPlaylist() {

        return playlist;
    }
    public void SetPlaylist(ArrayList<JukeboxTrack> arr) {
        playlist = arr;
    }


    //save and load database
    public void Load(InputStream inputStream) {
        JukeboxTrack temptrack = new JukeboxTrack();
        ArrayList<JukeboxTrack> templist = new ArrayList<JukeboxTrack>();
        String text = "";
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(true);
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(inputStream, null);

            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("TrackInfo")) {
                            temptrack = new JukeboxTrack();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xmlPullParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("TrackInfo")) {
                            templist.add(temptrack);
                        }
                        else if (tagname.equalsIgnoreCase("Name")) {
                            temptrack.SetName(text);
                        }
                        else if (tagname.equalsIgnoreCase("Artist")) {
                            temptrack.SetArtist(text);
                        }
                        else if (tagname.equalsIgnoreCase("Album")) {
                            temptrack.SetAlbum(text);
                        }
                        else if (tagname.equalsIgnoreCase("Duration")) {
                            //temptrack.SetDuration(Integer.parseInt(text));
                        }
                        else if (tagname.equalsIgnoreCase("Filetype")) {
                            temptrack.SetFiletype(text);
                        }
                        else if (tagname.equalsIgnoreCase("Filepath")) {
                            temptrack.SetFilepath(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        }
        catch (XmlPullParserException ex) {
            SetMessageError(ex.getMessage());
        }
        catch (IOException ex) {
            SetMessageError(ex.getMessage());
        }
        playlist = templist;
    }
    public void Save(){
        try {
            //Build XML
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("TrackList");
            document.appendChild(root);
            Element info = document.createElement("TrackInfo");
            document.appendChild(info);
            //Create nodes
            for (JukeboxTrack jbt: playlist) {
                Element name = document.createElement("Name");
                name.appendChild(document.createTextNode(String.valueOf(jbt.GetName())));
                info.appendChild(name);

                Element artist = document.createElement("Artist");
                name.appendChild(document.createTextNode(String.valueOf(jbt.GetArtist())));
                info.appendChild(artist);

                Element album = document.createElement("Album");
                name.appendChild(document.createTextNode(String.valueOf(jbt.GetAlbum())));
                info.appendChild(album);

                Element duration = document.createElement("Duration");
                name.appendChild(document.createTextNode(String.valueOf(jbt.GetDuration())));
                info.appendChild(duration);

                Element filetype = document.createElement("Filetype");
                name.appendChild(document.createTextNode(String.valueOf(jbt.GetFiletype())));
                info.appendChild(filetype);

                Element filepath = document.createElement("Filepath");
                name.appendChild(document.createTextNode(String.valueOf(jbt.GetFilepath())));
                info.appendChild(filepath);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);

            try {
                FileWriter fileWriter = new FileWriter("./TrackList.xml");
                StreamResult streamResult = new StreamResult(fileWriter);
                transformer.transform(source, streamResult);
            }
            catch (IOException ex) {
                SetMessageError(ex.getMessage());
            }

        }
        catch (TransformerException ex) {
            SetMessageError(ex.getMessage());
        }
        catch (ParserConfigurationException ex) {
            SetMessageError(ex.getMessage());
        }
    }



    public void AddTrack (JukeboxTrack jbt) {
        playlist.add(jbt);
    }

    public void RemoveTrack(JukeboxTrack jbt) {
        playlist.remove(jbt);
    }

    public JukeboxTrack getTrack(int i) {
        return playlist.get(i);
    }

    public JukeboxTrack getCurrentTrack() {
        return playlist.get(index);
    }

    //search function...strings
    public ArrayList<JukeboxTrack> SearchTrack(String input) {
        ArrayList<JukeboxTrack>  temp = new ArrayList<JukeboxTrack>();
        for (JukeboxTrack track: playlist) {
            if ((SearchName(track, input) == true) || (SearchAlbum(track, input) == true) || (SearchArtist(track, input) == true)) {
                temp.add(track);
            }
        }
        return temp;

    }
    public boolean SearchName(JukeboxTrack track, String input) {
        String SearchItem = track.GetName();
        return SearchItem.contains(input);

    }
    public boolean SearchAlbum (JukeboxTrack track, String input) {
        String SearchItem = track.GetAlbum();
        return SearchItem.contains(input);
    }
    public boolean SearchArtist (JukeboxTrack track, String input) {
        String SearchItem = track.GetArtist();
        return SearchItem.contains(input);
    }

    public JukeboxTrack NextTrack() {
        int max = playlist.size();
        if (++index >= max) {
            index = 0;
        }
        return playlist.get(index);
    }

    public JukeboxTrack PreviousTrack() {
        int max = playlist.size();
        if (--index < 0) {
            index = max - 1;
        }
        return playlist.get(index);
    }

    public String GetMessageError() {
        return MessageError;
    }
    public void SetMessageError(String m) {
        MessageError = m;
    }

}
