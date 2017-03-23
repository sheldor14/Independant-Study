import java.io.*;
import javax.sound.sampled.*;
public class First {
    public static void main(String[] args) throws IOException, LineUnavailableException{
        AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, true);


        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info))
			throw new IOException("Line type not supported: " + info);
		TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
		microphone.open(format, microphone.getBufferSize());
        File soundFile = new File("audio.wav");
        Thread session = new Thread() {
			public void run() {
				AudioInputStream sound = new AudioInputStream(microphone);
				microphone.start();
				try {
					AudioSystem.write(sound, fileType, soundFile);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(e);
				}
			}
		};
        session.start();
        int i=0;
        while ((i=System.in.read())!=13);
        microphone.stop();
        microphone.close();
        try {
            session.join();
        } catch (Exception e) {
        }
    }
}