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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Alex
 */
class MP3Panel extends JFrame{
    private JPanel playerpanel, functionalpanel, listpanel, artistpanel, custompanel, optionpanel;
    private JDialog createplaylistdialog, editplaylistdialog, deleteplaylistdialog;
    private DefaultListModel playlistlistmodel, customplaylistlistmodel, songlistmodel, tobeaddedmodel, addedmodel;
    private JList playlistlist, customplaylistlist, songlist, tobeadded, added;
    private List passer;
    private JComboBox playlistbox;
    private DefaultComboBoxModel playlistboxmodel;
    private JTabbedPane functionalpane;
    private int numtracks, currenttrack, toedit;
    private Media playingnow;
    private MediaPlayer player;
    private JButton playbtn, fwdbtn, bwdbtn, createplaylistbtn, editplaylistbtn, deleteplaylistbtn, addmusicbtn;
    private JButton passbtn, returnbtn, okbtn, cancelbtn;
    private ImageIcon play, pause, fwd, bwd;
    private JTextArea nowplaying;
    private JTextField playlistnameinput;
    private JLabel playlistnamelabel;
    private PlaylistCollection playlistcollection;
    private Playlist playlist;
    private Song song1, song2, song3, thissong;
    private URI thisURI;
    private File thisfile;
    private Box box1, box2, box3, box4;
    private Box newplaybox, editbox, passbox, passbtnbox;
    private Runnable autoplay;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private String getpath;
    private JOptionPane pathgiver;
    private String[] songsinfolder;
    
    public MP3Panel()
    {
        super("BlueMP3");
        windowManager winMan = new windowManager();
        addWindowListener(winMan);
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
        passbtn = new JButton(">");
        returnbtn = new JButton("<");
        okbtn = new JButton("Ok");
        cancelbtn = new JButton("Cancelar");
        playlistnamelabel = new JLabel("Nombre:");
        
        
        if(new File("songs.dat").exists())
        {
            deserializeExistingData();
            numtracks = playlistcollection.getAllSongsLength();
        }
        else
        {
        getpath = JOptionPane.showInputDialog("Porfavor, ingrese el path de donde desea agregar su musica.\n Ej: C:/Usuarios/Yo/Musica", "Bienvenido a BlueMP3!");
        songsinfolder = reapSongs(getpath);
        playlistcollection = new PlaylistCollection(songsinfolder);
        playlist = playlistcollection.getAllSongs();
        numtracks = playlist.getNumTracks();
        }
        tobeaddedmodel = populateWithSongs(playlistcollection.getAllSongs());
        tobeadded.setModel(tobeaddedmodel);
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
        passbtn.addActionListener(actman);
        returnbtn.addActionListener(actman);
        okbtn.addActionListener(actman);
        cancelbtn.addActionListener(actman);
        
        ListManager listman = new ListManager();
        
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
        playlistlistmodel = populate(0);
        playlistlist.setModel(playlistlistmodel);
        artistpanel.add(playlistlist);
        functionalpane.add("Artistas",artistpanel);
        custompanel = new JPanel(new BorderLayout());
        customplaylistlist = new JList();
        customplaylistlistmodel = populate(1);
        customplaylistlist.setModel(customplaylistlistmodel);
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
        box4 = new Box(BoxLayout.Y_AXIS);
        songlist = new JList();
        songlist.addListSelectionListener(listman);
        songlistmodel = populateWithSongs(playlistcollection.getAllSongs());
        songlist.setModel(songlistmodel);
        box4.add(songlist);
        add(box3);
        add(box4);
        newplaybox=new Box(BoxLayout.X_AXIS);
        editbox=new Box(BoxLayout.X_AXIS);
        passbtnbox = new Box(BoxLayout.Y_AXIS);
        passbtnbox.add(passbtn);
        passbtnbox.add(returnbtn);
        passbox = new Box(BoxLayout.X_AXIS);
        passbox.add(tobeadded);
        passbox.add(passbtnbox);
        passbox.add(added);
    }
    
    private String[] reapSongs(String dir)
    {
        int i=0; //For debugging plz take out later.
        File directory = new File(dir);
        String[] mp3s = new String[directory.list().length];
        for(File file : directory.listFiles())
        {
            System.out.println(file.getName());
            if(file.getName().toLowerCase().endsWith((".mp3")))
            {
                mp3s[i]=(dir + "/" + file.getName());
                System.out.println("Esto es lo que genera reapSongs: " + mp3s[i]);
                i++;
            }    
        }
        String[] cutter = new String[i];
        for(int j=0; j<cutter.length; j++)
            cutter[j]=mp3s[j];
        return cutter;
    }
    
    private void deserializeExistingData()
    {
        try
        {
            reader = new ObjectInputStream(new FileInputStream("songs.dat"));
            playlistcollection = (PlaylistCollection) reader.readObject();
            reader.close();
        }
        catch(FileNotFoundException e)
        {
            
        }
        catch(IOException | ClassNotFoundException ex)
        {
            
        }
    }
    
    private DefaultListModel populate(int b)
    {
        Playlist[] adder;
        DefaultListModel populated = new DefaultListModel();
        if(b==1)
            adder = playlistcollection.getCustomPlaylists();
        else
            adder = playlistcollection.getAutomatedPlaylists();
        for(int i=0; i<adder.length; i++)
            populated.addElement(adder[i]);
        return populated;
    }
    private DefaultListModel populateWithSongs(Playlist a)
    {
        DefaultListModel populated = new DefaultListModel();
        for(int i=0; i<a.getNumTracks(); i++)
        {
            populated.addElement(a.getSong(i));
        }
        return populated;
    }
    
    private void saveCollection()
    {
        try //Abrir el archivo
        {
            writer = new ObjectOutputStream(new FileOutputStream("songs.dat"));
        }
        catch(FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "File Not Found Exception");
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "IO Exception");
        }
        try //Escribir/Grabar al arcihvo
        {
            writer.writeObject(playlistcollection);
        }
        catch(IOException horror)
        {
            JOptionPane.showMessageDialog(null, "No pude escribir!");
        }
        try //Cerrar el archivo
        {
            writer.close();
        }    
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "No pude cerrar el archivo!");
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
                addedmodel.clear();
                added.setModel(addedmodel);
                //add buttons
                createplaylistdialog.setLayout(new BoxLayout(createplaylistdialog, BoxLayout.Y_AXIS));
                createplaylistdialog.add(passbox);
                playlistnameinput.setText("");
                newplaybox.add(playlistnamelabel);
                newplaybox.add(playlistnameinput);
                newplaybox.add(okbtn);
                newplaybox.add(cancelbtn);
                createplaylistdialog.add(newplaybox);
                createplaylistdialog.pack();
                createplaylistdialog.setVisible(true);
                
            }
            
            if(e.getSource() == editplaylistbtn)
            {
                toedit = JOptionPane.showOptionDialog(null, "Escoga la playlist a editar:", "Editar Playlist", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, playlistcollection.getCustomPlaylists(), playlistcollection.getCustomPlaylists()[0]);
                addedmodel = populateWithSongs(playlistcollection.getCustomPlaylists()[toedit]);
                added.setModel(addedmodel);
                //add buttons
                editplaylistdialog.setLayout(new BoxLayout(editplaylistdialog, BoxLayout.Y_AXIS));
                editplaylistdialog.add(passbox);
                playlistnameinput.setText(playlistcollection.getCustomPlaylists()[toedit].getName());
                editbox.add(playlistnamelabel);
                editbox.add(playlistnameinput);
                editbox.add(okbtn);
                editbox.add(cancelbtn);
                editplaylistdialog.add(editbox);
                editplaylistdialog.pack();
                editplaylistdialog.setVisible(true);
                
            }
            
            if(e.getSource() == passbtn)
            {
                passer = tobeadded.getSelectedValuesList();
                for(int i=0; i<passer.size(); i++)
                {
                    tobeaddedmodel.removeElement(passer.get(i));
                    addedmodel.addElement(passer.get(i));
                }
                added.setModel(addedmodel);
                tobeadded.setModel(tobeaddedmodel);
            }
            
            if(e.getSource() == returnbtn)
            {
                passer = added.getSelectedValuesList();
                for(int i=0; i<passer.size(); i++)
                {
                    addedmodel.removeElement(passer.get(i));
                    tobeaddedmodel.removeElement(passer.get(i));
                }
                tobeadded.setModel(tobeaddedmodel);
                added.setModel(addedmodel);
            }
            
            if(e.getSource() == okbtn)
            {
                
                if(okbtn.getParent() == newplaybox)
                {
                    playlistcollection.addPlaylist(new Playlist(playlistnameinput.getText(),(Song[]) addedmodel.toArray()));
                    createplaylistdialog.dispose();
                }
                else
                {
                    playlistcollection.getCustomPlaylists()[toedit].setName(playlistnameinput.getText());
                    playlistcollection.getCustomPlaylists()[toedit].setSongList((Song[])addedmodel.toArray());
                    editplaylistdialog.dispose();
                }
                tobeaddedmodel = populateWithSongs(playlistcollection.getAllSongs());
                tobeadded.setModel(tobeaddedmodel);
                addedmodel.clear();
                added.setModel(addedmodel);
                //More JLists to be updated
                playlistnameinput.setText("");
            }
            
            if(e.getSource() == cancelbtn)
            {
                tobeaddedmodel = populateWithSongs(playlistcollection.getAllSongs());
                tobeadded.setModel(tobeaddedmodel);
                addedmodel.clear();
                added.setModel(addedmodel);
                playlistnameinput.setText("");
                if(cancelbtn.getParent() == editbox)
                    editplaylistdialog.dispose();
                else
                    createplaylistdialog.dispose();
            }
            
            if(e.getSource() == deleteplaylistbtn)
            {
                playlistcollection.deletePlaylist((Playlist)JOptionPane.showInputDialog(null,"¿Cual playlist desea borrar?", "Borrar Playlist", JOptionPane.QUESTION_MESSAGE, null, playlistcollection.getCustomPlaylists(), playlistcollection.getCustomPlaylists()[0]));
            }
            
            if(e.getSource() == addmusicbtn)
            {
                getpath = JOptionPane.showInputDialog("Porfavor, ingrese el path de donde desea agregar su musica.\nEj: C:\\Usuarios\\Yo\\Musica");
                songsinfolder = reapSongs(getpath);
                playlistcollection.addSongs(songsinfolder);
                playlistlistmodel = populate(0);
                customplaylistlistmodel = populate(1);
                playlistlist.setModel(playlistlistmodel);
                customplaylistlist.setModel(customplaylistlistmodel);
                //The third list/panel must be reset and the player stopped.
            }
        }
    }
    
    private class ListManager implements ListSelectionListener
    {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(e.getSource() == songlist)
            {
                Platform.runLater(new Runnable()
                {
                    public void run()
                    {
                        if((Song[])songlistmodel.toArray() == playlist.getSongList())
                        {
                            player.dispose();
                            player = new MediaPlayer(playlist.getSong(songlist.getSelectedIndex()).getMedia());
                            player.setOnEndOfMedia(autoplay);
                            currenttrack=songlist.getSelectedIndex();
                        }
                        else
                        {
                            player.dispose();
                            playlist.setSongList((Song[])songlistmodel.toArray());
                            player = new MediaPlayer(playlist.getSong(songlist.getSelectedIndex()).getMedia());
                            player.setOnEndOfMedia(autoplay);
                            numtracks = playlist.getNumTracks();
                            currenttrack = songlist.getSelectedIndex();
                        }
                        nowplaying.setText(playlist.getSong(currenttrack).printInfo());
                        playbtn.setIcon(pause);
                    }
                });
            }
            
            if(e.getSource() == playlistlist)
            {
                songlistmodel = populateWithSongs(playlistcollection.getAutomatedPlaylists()[playlistlist.getSelectedIndex()]);
                songlist.setModel(songlistmodel);
            }
            
            if(e.getSource() == customplaylistlist)
            {
                songlistmodel = populateWithSongs(playlistcollection.getCustomPlaylists()[customplaylistlist.getSelectedIndex()]);
                songlist.setModel(songlistmodel);
            }
        }
        
    }
    
    private class windowManager implements WindowListener
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            saveCollection();
        }
        @Override
        public void windowOpened(WindowEvent e)
        {
            
        }
        @Override
        public void windowClosed(WindowEvent e)
        {
            
        }
        @Override
        public void windowIconified(WindowEvent e)
        {
            
        }
        @Override
        public void windowDeiconified(WindowEvent e)
        {
            
        }
        @Override
        public void windowActivated(WindowEvent e)
        {
            
        }
        @Override
        public void windowDeactivated(WindowEvent e)
        {
            
        }
    }
    
}
