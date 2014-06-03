/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxswingtest;

import java.io.Serializable;

/**
 *
 * @author Alex
 */
class Playlist implements Serializable
{
    private Song[] songlist;
    private String name;
    
    public Playlist()
    {
        songlist= new Song[0];
        name="";
    }
    
    public Playlist(String a)
    {
        name = a;
        songlist = new Song[0];
    }
    
    public Song[] getSongList()
    {
        return songlist;
    }
    
    public void setSongList(Song[] a)
    {
        songlist = a;
    }
    
    public int getNumTracks()
    {
        return songlist.length;
    }
    
    public Song getFirstSong()
    {
        return songlist[0];
    }
    
    public Song getSong(int a)
    {
        return songlist[a];
    }
    
    public Song getSong(String a)
    {
        for(int i=0; i<songlist.length; i++)
        {
            if(songlist[i].getFilename().equals(a))
                return songlist[i];
        }
        return null;
    }
    
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String a)
    {
        name = a;
    }
    
    public void addSong(Song a)
    {
        if(songlist.length == 0)
            songlist = new Song[] {a};
        else
        {
            Song[] addsonglist = new Song[songlist.length+1];
            for(int i=0; i<songlist.length; i++)
                addsonglist[i]=songlist[i];
            addsonglist[songlist.length]=a;
            songlist=addsonglist;
        }
    }
    
    public boolean deleteSong(String a)
    {
        if(!hasSong(a))
            return false;
        Song[] delsonglist = new Song[songlist.length-1];
        int i=0;
	for(Song x : songlist)
	{
            if(!x.getFilename().equals(a))
            {
		delsonglist[i]=x;
		i++;
            }	
	}
	songlist=delsonglist;
	return true;	
    }
    
    public Song nextSong(Song a)
    {
        if(hasSong(a.getFilename()))
        {    
            for(int i=0; i<songlist.length-1; i++)
            {
               if(songlist[i].getFilename().equals(a.getFilename()))
                   return songlist[i+1];
            }
            return null;
        }
        return null;
    }
    
    public Song lastSong(Song a)
    {
        if(songlist.length<=1)
        {
            if(hasSong(a.getFilename()))
            {
                for(int i=1; i<songlist.length; i++)
                {
                    if(songlist[i].getFilename().equals(a.getFilename()))
                        return songlist[i-1];
                }
            }
        }
      return null;
    }
    
    private boolean hasSong(String a) 
    {
        for(int i=0; i<songlist.length; i++)
            if(songlist[i].getFilename().equals(a))
                return true;
        return false;
    }


}
