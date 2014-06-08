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
    private String[] artistnames;
    
    public PlaylistCollection()
    {
        
    }
    
    public PlaylistCollection(String[] a)
    {
        allsongs = new Playlist("Todas las Canciones", a);
        System.out.println(allsongs.getSong(0));
        System.out.println(allsongs.getSong(0).printInfo());
        if(allsongs.getNumTracks() == a.length)
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
    
    public void deletePlaylist(Playlist a)
    {
        for(int i=0; i<customplaylists.length; i++)
            if(a.getName().equals(customplaylists[i].getName()))
                if(Arrays.equals(a.getSongList(),customplaylists[i].getSongList()))
                    deletePlaylist(i);        
    }
    
    public void addSongs(String[] a)
    {
        int c=0;
        for(int i=0; i<a.length; i++)
        {
            if(allsongs.addSong(a[i]))
                c++;
        }
        System.out.println("Se agregaron: " + c + " canciones.");
        getArtistNames();
    }
    
    public Playlist getAllSongs()
    {
        return allsongs;
    }
    
    public int getAllSongsLength()
    {
        return allsongs.getNumTracks();
    }
    
    public Playlist[] getCustomPlaylists()
    {
        return customplaylists;
    }
    
    public Playlist[] getAutomatedPlaylists()
    {
        Playlist[] allandauto = new Playlist[artistplaylists.length+1];
        allandauto[0] = allsongs;
        for(int i=1; i<allandauto.length; i++)
            allandauto[i] = artistplaylists[i-1];
        return allandauto;
    }
    
    private void getArtistNames()
    {
        artistnames = new String[0];
        for(int i=0; i<allsongs.getNumTracks(); i++)
        {
            if(alreadyHaveArtist(allsongs.getSong(i).getArtist()))
            {
            } 
            else 
            {
                addArtist(allsongs.getSong(i).getArtist());
            }
        }
        Arrays.sort(artistnames);
        generateArtistPlaylists();
    }
    
    
    
    private void generateArtistPlaylists()
    {
        /*for(int i=0; i<artistnames.length; i++)
        {    
            artistnames[i]= (String)artistdatabase.get(i);
            System.out.println(artistnames[i]);
        }
        System.out.println(artistnames[0]); */
        artistplaylists = new Playlist[artistnames.length];
        for(int i=0; i<artistplaylists.length; i++)
        {
            artistplaylists[i] = populatePlaylist(artistnames[i]);
        }
    }
    
    private Playlist populatePlaylist(String b)
    {
        Playlist a = new Playlist(b);
        System.out.println("Number of songs in allsongs: " + allsongs.getNumTracks());
        for(int i=0; i<allsongs.getNumTracks(); i++)
        {
            if(b.equals(allsongs.getSong(i).getArtist()))
                a.addSong(allsongs.getSong(i));
        }
        return a;
    }
    
    private boolean alreadyHaveArtist(String a)
    {
        if(artistnames.length==0)
            return false;
        for(int i=0; i<artistnames.length; i++)
        {
            if(a.equals(artistnames[i]))
                return true;
        }
        return false;
    }
    
    private void addArtist(String a)
    {
        String[] adder = new String[artistnames.length+1];
        for(int i=0; i<artistnames.length; i++)
        {
            adder[i]=artistnames[i];
        }
        adder[artistnames.length]=a;
        artistnames=adder;
    }
    
}
