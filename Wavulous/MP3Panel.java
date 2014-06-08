/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxswingtest;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 *
 * @author Alex
 */
class MP3Panel extends JFrame{
    private JPanel playerpanel, functionalpanel, listpanel, artistpanel, custompanel, optionpanel;
    private JDialog createplaylistdialog, editplaylistdialog, deleteplaylistdialog;
    private JList playlistlist, customplaylistlist, songslist;
    private JTabbedPane functionalpane;
    private int numtracks, currenttrack;
    private Media playingnow;
    private MediaPlayer player;
    private JButton playbtn, fwdbtn, bwdbtn, createplaylistbtn, editplaylistbtn, deleteplaylistbtn, addmusicbtn;
    private JButton a;
    private ImageIcon play, pause, fwd, bwd;
    private JTextArea nowplaying;
    private PlaylistCollection automatedplaylists, customplaylists;
    private Playlist playlist;
    private Song song1, song2, song3, thissong;
    private URI thisURI;
    private File thisfile;
    private Box box1, box2, box3, box4, box5;
    private Runnable autoplay;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private String getpath;
    private JOptionPane pathgiver;
    private List<String> songsinfolder;
    
    public MP3Panel()
    {
        super("BlueMP3");
        play = new ImageIcon("play.png");
        pause = new ImageIcon("pause.png");
        fwd = new ImageIcon("next.png");
        bwd = new ImageIcon("prev.png");
        bwdbtn = new JButton(bwd);
        fwdbtn = new JButton(fwd);
        playbtn = new JButton(play);
        createplaylistbtn = new JButton("Crear Playlist");
        editplaylistbtn = new JButton("Editar Playlist");
        deleteplaylistbtn = new JButton("Borrar Playlist");
        addmusicbtn = new JButton("Agregar Musica");
        
        
        /* To be deprecated for file calling, etc.
        song1 = new Song("Blumenkranz.mp3");
        song2 = new Song("Dimensiontripper!!!!.mp3");
        song3 = new Song("misterioso.mp3");
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);
        */
        if(new File("songs.dat").exists())
        {
            deserializeExistingData();
            numtracks = automatedplaylists.getAllSongsLength();
            
            /*Agarrar canciones de songs.dat
            A implementarse luego ya que existan varias playlists o playlist collections.
            try
            {
            writer = new ObjectOutputStream(new FileOutputStream("songs.dat"));
            }
            catch(FileNotFoundException e)
            {
                System.out.println("No se encontro el archivo.");
            } 
            catch (IOException ex){ 
                System.out.println("No se pudo escribir el archivo.");
            }   
            */
        }
        else
        {
        getpath = JOptionPane.showInputDialog("Porfavor, ingrese el path de donde desea agregar su musica.\n Ej: C:/Usuarios/Yo/Musica");
        songsinfolder = reapSongs(getpath);
        playlist = new Playlist("My Songs",songsinfolder);
        numtracks = playlist.getNumTracks();
        }
        System.out.println("Se consiguieron: " + numtracks + " canciones.");
        currenttrack=0;
        
        autoplay = new Runnable(){
            public void run()
            {
                  if(currenttrack==numtracks-1)
                        {
                            nowplaying.setText("");
                            currenttrack=0;
                            playbtn.setIcon(play);
                        }    
                        else 
                        {
                            player.dispose();
                            player = new MediaPlayer(playlist.getSong(++currenttrack).getMedia());
                            nowplaying.setText(playlist.getSong(currenttrack).printInfo());
                            player.play();
                            player.setOnEndOfMedia(autoplay);
                        }  
            }
        };
        
        Platform.runLater(new Runnable(){

            @Override
            public void run() 
            {
                player = new MediaPlayer(playlist.getFirstSong().getMedia());
                player.setOnEndOfMedia(autoplay);
            }
        });
        
        
        nowplaying = new JTextArea(3, 50);
        nowplaying.setText("");
        nowplaying.setEditable(false);
        
        
        ActionManager actman = new ActionManager();
        playbtn.addActionListener(actman);
        fwdbtn.addActionListener(actman);
        bwdbtn.addActionListener(actman);
        createplaylistbtn.addActionListener(actman);
        editplaylistbtn.addActionListener(actman);
        deleteplaylistbtn.addActionListener(actman);
        addmusicbtn.addActionListener(actman);
        
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        playerpanel = new JPanel();
        playerpanel.setLayout(new BoxLayout(playerpanel, BoxLayout.Y_AXIS));
        box1 = new Box(BoxLayout.X_AXIS);
        box1.add(nowplaying);
        box2 = new Box(BoxLayout.X_AXIS);
        box2.add(bwdbtn);
        box2.add(playbtn);
        box2.add(fwdbtn);
        playerpanel.add(box1);
        playerpanel.add(box2);
        functionalpanel = new JPanel(new BorderLayout());
        functionalpane = new JTabbedPane();
        artistpanel = new JPanel(new BorderLayout());
        playlistlist = new JList(); //Aqui iran las playlists generadas.
        artistpanel.add(playlistlist);
        functionalpane.add("Artistas",artistpanel);
        custompanel = new JPanel(new BorderLayout());
        customplaylistlist = new JList();
        custompanel.add(customplaylistlist);
        functionalpane.add("Playlists Personalizadas",custompanel);
        optionpanel = new JPanel();
        optionpanel.setLayout(new GridLayout(0,4));
        optionpanel.add(createplaylistbtn);
        optionpanel.add(editplaylistbtn);
        optionpanel.add(deleteplaylistbtn);
        optionpanel.add(addmusicbtn);
        functionalpane.add("Opciones",optionpanel);
        functionalpanel.add(functionalpane);
        box3 = new Box(BoxLayout.Y_AXIS);
        box3.add(playerpanel);
        box3.add(functionalpanel);
        add(box3);
    }
    
    private List<String> reapSongs(String dir)
    {
        int i=0; //For debugging plz take out later.
        List<String> mp3s = new ArrayList<String>();
        File directory = new File(dir);
        for(File file : directory.listFiles())
        {
            System.out.println(file.getName());
            if(file.getName().toLowerCase().endsWith((".mp3")))
            {
                mp3s.add(dir + "/" + file.getName());
                System.out.println("Esto es lo que genera reapSongs: " + mp3s.get(i));
                i++;
            }    
        }
        System.out.println("Si entre, nomas no sabes que pedo :b");
        return mp3s;
    }
    
    private void deserializeExistingData()
    {
        try
        {
            reader = new ObjectInputStream(new FileInputStream("songs.dat"));
            automatedplaylists = (PlaylistCollection) reader.readObject();
            customplaylists = (PlaylistCollection) reader.readObject();
            reader.close();
        }
        catch(FileNotFoundException e)
        {
            
        }
        catch(IOException | ClassNotFoundException ex)
        {
            
        }
    }
    
    /*private Media intoMedia(Song a)
    {
        thissong = a; 
        thisfile = new File(thissong.getFilename());
        thisURI = thisfile.toURI();
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
        playingnow = new Media(thisURI.toString());
            }
        });    
        return playingnow;
    }
*/
    private class ActionManager implements ActionListener
    {    
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == playbtn)
            {
                Platform.runLater(new Runnable() {
                    
                    @Override
                    public void run()
                    {
                        if(playbtn.getIcon() == play)
                        {
                            if(player.getStatus()==MediaPlayer.Status.STOPPED)
                            {
                                player.dispose();
                                player = new MediaPlayer(playlist.getFirstSong().getMedia());
                                player.setOnEndOfMedia(autoplay);
                                currenttrack=0;
                            }
                            player.play(); 
                            playbtn.setIcon(pause);
                        }
                        else
                        {
                            player.pause();
                            playbtn.setIcon(play);
                        }
                        nowplaying.setText(playlist.getSong(currenttrack).printInfo());
                    }
                });    
            }
            
            if(e.getSource() == fwdbtn)
            {
                Platform.runLater(new Runnable() {
                    
                    @Override
                    public void run()
                    {
                        if(currenttrack == numtracks-1)
                        {    
                            player.stop();
                            playbtn.setIcon(play);
                            nowplaying.setText("");
                        }
                        else
                        {
                            player.dispose();
                            player = new MediaPlayer(playlist.getSong(++currenttrack).getMedia());
                            player.play();
                            player.setOnEndOfMedia(autoplay);
                            nowplaying.setText(playlist.getSong(currenttrack).printInfo());
                            playbtn.setIcon(pause);
                        }
                    }
                });
            }
            
            if(e.getSource() == bwdbtn)
            {
                Platform.runLater(new Runnable(){
                    @Override
                    public void run()
                    {
                        player.dispose();
                        if(currenttrack == 0)
                            player = new MediaPlayer(playlist.getSong(currenttrack).getMedia()); 
                        else
                            player = new MediaPlayer(playlist.getSong(--currenttrack).getMedia());  
                        player.setOnEndOfMedia(autoplay);
                        player.play();
                        nowplaying.setText(playlist.getSong(currenttrack).printInfo());
                        playbtn.setIcon(pause);
                    }
                });
            }
            
            if(e.getSource() == createplaylistbtn)
            {
                
            }
            
            if(e.getSource() == editplaylistbtn)
            {
                
            }
            
            if(e.getSource() == deleteplaylistbtn)
            {
                
            }
            
            if(e.getSource() == addmusicbtn)
            {
                getpath = JOptionPane.showInputDialog("Porfavor, ingrese el path de donde desea agregar su musica.\nEj: C:\\Usuarios\\Yo\\Musica");
                songsinfolder = reapSongs(getpath);
                automatedplaylists.addSongs(songsinfolder);
            }
        }
    }
    
}
