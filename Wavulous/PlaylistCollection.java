/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxswingtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Alex
 */
public class PlaylistCollection implements Serializable
{
    private Playlist allsongs;
    private Playlist[] artistplaylists;
    private ArrayList<String> artistdatabase;
    
    public PlaylistCollection()
    {
        
    }
    
    public PlaylistCollection(List<String> a)
    {
        allsongs = new Playlist("Todas las Canciones",a);
        getArtistNames();
        
    }
    
    public int getAllSongsLength()
    {
        return allsongs.getNumTracks();
    }
    
    private void getArtistNames()
    {
        artistdatabase = new ArrayList<>(allsongs.getNumTracks());
        for(int i=0; i<allsongs.getNumTracks(); i++)
        {
            if(!artistdatabase.contains(allsongs.getSong(i).getArtist()))
            {
                artistdatabase.add(allsongs.getSong(i).getArtist());
            }
        }
        artistdatabase.trimToSize();
        generateArtistPlaylists();
    }
    
    private void generateArtistPlaylists()
    {
        String[] artistnames = new String[artistdatabase.size()];
        for(int i=0; i<artistnames.length; i++)
            artistnames[i]=(String)artistdatabase.get(i);
        Arrays.sort(artistnames);
        
        artistplaylists = new Playlist[artistnames.length];
        for(int i=0; i<artistplaylists.length; i++)
        {
            artistplaylists[i] = new Playlist(artistnames[i]);
            populatePlaylist(artistplaylists[i]);
        }
    }
    
    private void populatePlaylist(Playlist a)
    {
        for(int i=0; i<allsongs.getNumTracks(); i++)
        {
            if(allsongs.getSong(i).getArtist().equals(a.getName()))
                a.addSong(allsongs.getSong(i));
        }
    }
    
}
