import java.io.*;
import javax.sound.sampled.*;

public class Live{


    public static void main(String[] args) {
        AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, true);

        TargetDataLine m_targetLine;
	    SourceDataLine m_sourceLine;
	    int	m_nExternalBufferSize;

        try{
            m_targetLine = (TargetDataLine) AudioSystem.getLine(
                new DataLine.Info(TargetDataLine.class, audioFormat));
            m_sourceLine = (SourceDataLine) AudioSystem.getLine(
                new DataLine.Info(SourceDataLine.class, audioFormat));
            m_targetLine.open(audioFormat);
            m_sourceLine.open(audioFormat);

            byte[] buffer = new byte[m_targetLine.getBufferSize()/5];

            m_targetLine.start();
            m_sourceLine.start();

            while(true){
               // int read = ;
                m_sourceLine.write(buffer, 0, m_targetLine.read(buffer, 0, buffer.length));
            }
        }
        catch (LineUnavailableException e){
            System.out.println("an error has occured opening a line");
        }

    }
}