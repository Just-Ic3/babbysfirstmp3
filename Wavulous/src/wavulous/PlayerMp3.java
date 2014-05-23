package order;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;

/**
 *
 * @author Luis Padilla, Jorge Imperial, Alex Ramirez y Luis Torres.
 */

public class PlayerMp3 
{
	public static void main(String [] arg)
	{
		PlayerX paragonX = new PlayerX();
		paragonX.setSize(416,160);
		paragonX.setVisible(true);
		paragonX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		paragonX.setLocationRelativeTo(null);
		paragonX.setResizable(false);
	}//main
}//PlayerMp3

class PlayerX extends JFrame implements ChangeListener
{
	private JButton 
                botonPlay, botonNext, 
                botonPrev, botonPause;
//	private JButton botonHide;
    private JLabel timeRunning;
	private JSlider slider1;
	private ImageIcon
                play, pause,
                prev, next;
//	private ImageIcon sound;
	private JProgressBar barra1;	
	private AudioFile1 filename = new AudioFile1("El Bombito.wav");
	private FloatControl control;
	
    private  final int  BUFFER_SIZE = 128000;
    private  File soundFile;
    private  AudioInputStream audioStream;
    private  AudioFormat audioFormat;
    private  SourceDataLine sourceLine;
    private volatile boolean running=true;
//	AudioFormat audioFormat = audioInputStream.getFormat();
    
	public void stateChanged (ChangeEvent event) {
  //      setControlFromSlider();
    }
	
	public PlayerX()
	{
		super("Wavulous Audio Player");
		setLayout(null);
		ManejadorCampos manejador = new ManejadorCampos();
		play = new ImageIcon("play.png");
		next = new ImageIcon("next.png");
		prev = new ImageIcon("prev.png");
        pause = new ImageIcon("pause.png");
//		hide = new ImageIcon("hide.png");
//		sound = new ImageIcon("sound.png");
		botonPlay = new JButton(play);
        botonPause = new JButton(pause);
		botonNext = new JButton(next);
		botonPrev = new JButton(prev);
		slider1 = new JSlider(JSlider.VERTICAL, 0,1000,500);
		slider1.setBounds(375, 8, 30, 100);
		slider1.setEnabled(true);
		timeRunning = new JLabel();
        timeRunning.setText("0:00");
		barra1 = new JProgressBar();
                
        timeRunning.setBounds(178, 115, 340, 10);
		botonPrev.setMargin(new Insets(0, 0, 0, 0));
		botonPrev.setBounds(10, 20, 60, 60);
		botonPrev.setFocusable(false);
		botonPlay.setMargin(new Insets(0, 0, 0, 0));
		botonPlay.setBounds(160, 20, 60, 60);
		botonPlay.setFocusable(false);
        botonPause.setMargin(new Insets(0, 0, 0, 0));
		botonPause.setBounds(160, 20, 60, 60);
		botonPause.setFocusable(false);
        botonPause.setVisible(false);
		botonNext.setMargin(new Insets(0, 0, 0, 0));
		botonNext.setBounds(310, 20, 60, 60);
		botonNext.setFocusable(false);
		barra1.setBounds(20, 100, 340, 10);
		
		botonPlay.addActionListener(manejador);
		botonNext.addActionListener(manejador);
		botonPrev.addActionListener(manejador);
        botonPause.addActionListener(manejador);
                
	/*	slider1.addChangeListener(new ChangeListener() 
		{
	        public void stateChanged(ChangeEvent event) //valores del slider, deberan cambiar el volumen
	        {
	        	JSlider slider1 = (JSlider)event.getSource();
	       	}//stateChanged
		});//slider1.addChangeListener */
                
        add(timeRunning);
		add(botonPlay);
        add(botonPause);
		add(botonNext);
		add(botonPrev);
		add(barra1);
		add(slider1);
	}//public PlayerX
        
	class ManejadorCampos implements ActionListener
	{
		public void actionPerformed(ActionEvent evento)
		{
			if(evento.getSource() == botonPlay)
			{
				running=true;
				playSound(filename.getNombre());
				new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						try {
							long operationTime;
							int m=0, t=0;
							barra1.setMaximum((int)getWavLength(filename.getNombre()));
							for(int s=0;t<=getWavLength(filename.getNombre());s++, t++) 
							{
								while (!running)
									Thread.yield();
								botonPlay.setVisible(false);
								botonPause.setVisible(true);
								operationTime = (long)(1000);
								if (s>60)
									m++;
								if (s<10)
									timeRunning.setText(m+":0"+s);
								else
									timeRunning.setText(m+":"+s);
								barra1.setValue(s);
								Thread.sleep(operationTime);
							}
							timeRunning.setText("0:00");
							botonPlay.setVisible(true);
							botonPause.setVisible(false);
							barra1.setValue(0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (UnsupportedAudioFileException ex) {
							Logger.getLogger(PlayerMp3.class.getName()).log(Level.SEVERE, null, ex);
						} catch (IOException ex) {
							Logger.getLogger(PlayerMp3.class.getName()).log(Level.SEVERE, null, ex);
						}
					}//public void run()
				}).start();//new Thread
			}//botonPlay
			if(evento.getSource() == botonPause)
			{
				running=false;
				botonPlay.setVisible(true);
				botonPause.setVisible(false);
				System.out.println(filename.getNombre());
			}//botonPause
			if(evento.getSource() == botonNext)
			{
				
			}//botonNext
			if(evento.getSource() == botonPrev)
			{
				
			}//botonPrev
		}//actionPerformed
	}//class ManejadorCampos
        
        
        
        public void playSound(final String filename)
        {
            new Thread(new Runnable() 
            {
            	public void run() 
            	{
            		String strFilename = filename;
            		try {
            			soundFile = new File(strFilename);
            		} catch (Exception e) {
            			e.printStackTrace();
            			System.exit(1);
            		}
            		try {
            			audioStream = AudioSystem.getAudioInputStream(soundFile);
            		} catch (Exception e){
            			e.printStackTrace();
            			System.exit(1);
            		}
            		audioFormat = audioStream.getFormat();
            		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            		try {
            			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            			sourceLine.open(audioFormat);
            		} catch (LineUnavailableException e) {
            			e.printStackTrace();
            			System.exit(1);
            		} catch (Exception e) {
            			e.printStackTrace();
            			System.exit(1);
            		}
            		sourceLine.start();
            		int nBytesRead = 0;
            		byte[] abData = new byte[BUFFER_SIZE];
            		while (nBytesRead != -1) 
            		{
            			try {
            				nBytesRead = audioStream.read(abData, 0, abData.length);
            			} catch (IOException e) {
            				e.printStackTrace();
            			}
            			if (nBytesRead >= 0) 
            			{
            				@SuppressWarnings("unused")
            				int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            			}//if
            		}//while
            		sourceLine.drain();
            		sourceLine.close();
            	}//public void run()
            }).start();//Thread (kek)
        }//playSound
        
        public void pauseThread() throws InterruptedException
        {
            running = false;
        }//pauseThread
        
        public double getWavLength(String filename) throws UnsupportedAudioFileException, IOException
        {
            File file = new File(filename);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames+0.0) / format.getFrameRate(); 
            return durationInSeconds;
        }//getWavLength
}//class PlayerX