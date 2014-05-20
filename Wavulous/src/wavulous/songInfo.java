package wavulous;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Luis Padilla, Jorge Imperial, Alex Ramirez y Luis Torres.
 */
public class songInfo {
    
    public double getLength(String filename) throws UnsupportedAudioFileException, IOException
    {
        File file = new File(filename);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        double durationInSeconds = (frames+0.0) / format.getFrameRate(); 
        return durationInSeconds;
    }
    
    public void timeRan(final String filename)
    {
        new Thread(new Runnable() {
        
        @Override
        public void run() {
            try {
            long operationTime;
            int m=0, t=0;
            for(int s=0;t<=getLength(filename);s++, t++) {
                operationTime = (long)(1000);
                if (s>60)
                {
                    m++;
                    s=0;
                }
                System.out.println((m)+"m"+(s)+"s");
                Thread.sleep(operationTime);
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(Wavulous.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Wavulous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }).start();
    }
    
    
    
    private  final int  BUFFER_SIZE = 128000;
    private  File soundFile;
    private  AudioInputStream audioStream;
    private  AudioFormat audioFormat;
    private  SourceDataLine sourceLine;
    public void playSound(String filename){

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
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }
        sourceLine.drain();
        sourceLine.close();
    }

}
