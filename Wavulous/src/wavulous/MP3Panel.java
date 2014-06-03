/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxswingtest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Alex
 */
class MP3Panel extends JFrame{
    private int numtracks, currenttrack;
    private Media playingnow;
    private MediaPlayer player;
    private JButton playbtn, fwdbtn, bwdbtn;
    private ImageIcon play, pause, fwd, bwd;
    private JTextArea nowplaying;
    private Playlist playlist;
    private Song song1, song2, song3, thissong;
    private URI thisURI;
    private File thisfile;
    private Box box1, box2;
    private Runnable autoplay;
    
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
        song1 = new Song("Blumenkranz.mp3");
        song2 = new Song("Dimensiontripper!!!!.mp3");
        song3 = new Song("misterioso.mp3");
        playlist = new Playlist();
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);
        numtracks = playlist.getNumTracks();
        currenttrack = 0;
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
        
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        box1 = new Box(BoxLayout.X_AXIS);
        box1.add(nowplaying);
        box2 = new Box(BoxLayout.X_AXIS);
        box2.add(bwdbtn);
        box2.add(playbtn);
        box2.add(fwdbtn);
        getContentPane().add(box1);
        getContentPane().add(box2);
        
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
        }
    }
    
}
