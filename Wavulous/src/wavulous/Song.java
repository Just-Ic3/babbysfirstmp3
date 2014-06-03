/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxswingtest;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javax.swing.ImageIcon;

/**
 *
 * @author Alex
 */
class Song {
       private String filename, name, artist, album;
       private Media song;
       private URI aURI;
       private ImageIcon albumart;
    public Song()
    {
        filename="";
        name="";
        artist="";
        album="";
    }
    
    public Song(String a)
    {
        filename = a;
        makeMedia();
    }
    
    public Media getMedia()
    {
        return song;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getArtist()
    {
        return artist;
    }
    
    public String getAlbum()
    {
        return album;
    }
    
    public ImageIcon getAlbumArt()
    {
        return albumart;
    }
    
    public String getFilename()
    {
        return filename;
    }
    
    private void makeMedia()
    {
        File afile = new File(filename);
        aURI = afile.toURI();
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
        song = new Media(aURI.toString());
        readID3Tag();
            }
        }); 
    }
    
    private void readID3Tag()
    {
        song.getMetadata().addListener(new MapChangeListener<String, Object>()
        {

            @Override
            public void onChanged(Change<? extends String, ? extends Object> change) {
                if(change.wasAdded())
                    detectMetadata(change.getKey(), change.getValueAdded());
            }
            
        });
    }
    
    private void detectMetadata(String key, Object value)
    {
           switch (key) 
           {
               case "title":
                   name = value.toString();
                   break;
               case "artist":
                   artist = value.toString();
                   break;
               case "album":
                   album = value.toString();
                   break;
               case "image":
                   albumart = new ImageIcon((Image)value);
                   break;
           }
    }
    
    public void readID3Tag(String a)
    {
          try { 
            URI anURI = new URI(a);
            String astring = anURI.toString();
            File song = new File(astring);
            
            File [] list = song.listFiles();
            for(int i=0;i<list.length;i++)
            {
               FileInputStream file = new FileInputStream(list[i]); 
               int size = (int)list[i].length(); 
               file.skip(size - 128); 
               byte[] last128 = new byte[128]; 
               file.read(last128); 
               String id3 = new String(last128); 
               String tag = id3.substring(0, 3); 
               if (tag.equals("TAG")) { 
                  name = "Title: " + id3.substring(3, 32); 
                  artist = "Artist: " + id3.substring(33, 62); 
                  album = "Album: " + id3.substring(63, 91);  
               } 
               else 
                  System.out.println(" does not contain ID3 info.");  
               file.close();
            } 
         } 
             catch (Exception e) { 
               System.out.println("Error ? " + e.toString()); 
            }
    }
    
    public String printInfo()
    {
        if(name == null)
            name = "Untitled";
        if(artist == null)
            artist = "Unknown Artist";
        if(album == null)
            album = "Unknown Album";
        String info = name + "\n" + artist + "\n" + album;
        return info;
    }
}
