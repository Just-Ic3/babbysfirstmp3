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
    private Playlist[] customplaylists;
    private ArrayList<String> artistdatabase;
    
    public PlaylistCollection()
    {
        
    }
    
    public PlaylistCollection(List<String> a)
    {
        allsongs = new Playlist("Todas las Canciones",a);
        getArtistNames();
        
    }
    
    public void addPlaylist(Playlist a)
    {
        Playlist[] customplaylistadd = new Playlist[customplaylists.length+1];
        for(int i=0; i<customplaylists.length; i++)
            customplaylistadd[i] = customplaylists[i];
        customplaylistadd[customplaylists.length] = a;
        customplaylists = customplaylistadd;
    }
    
    public void deletePlaylist(int a)
    {
        Playlist[] deleter = new Playlist[customplaylists.length-1];
        int b=0;
        for(int i=0; i<deleter.length; i++)
        {
            if(i!=a)
                deleter[i]=customplaylists[b];
            else
                deleter[i]=customplaylists[++b];
            b++;
        }
        customplaylists = deleter;
    }
    
    public void addSongs(List<String> a)
    {
        int c=0;
        for(int i=0; i<a.size(); i++)
        {
            if(allsongs.addSong(a.get(i)))
                c++;
        }
        System.out.println("Se agregaron: " + c + " canciones.");
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
