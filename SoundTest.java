import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SoundTest {

    private static int sliderValue = 500;

    public static void main(String[] args) throws Exception {
        final JFrame frame = new JFrame();
        final JSlider slider = new JSlider(500, 1000);
        frame.add(slider);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sliderValue = slider.getValue();
            }
        });
        frame.pack();
        frame.setVisible(true);

        final AudioFormat audioFormat = new AudioFormat(44100, 8, 1, true, true);
        final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, 1);
        final SourceDataLine soundLine = (SourceDataLine) AudioSystem.getLine(info);
        final int bufferSize = 2200; // in Bytes
        soundLine.open(audioFormat, bufferSize);
        soundLine.start();
        byte counter = 0;
        final byte[] buffer = new byte[bufferSize];
        byte sign = 1;
        while (frame.isVisible()) {
            int threshold = audioFormat.getFrameRate() / sliderValue;
            for (int i = 0; i < bufferSize; i++) {
                if (counter > threshold) {
                    sign = (byte) -sign;
                    counter = 0;
                }
                buffer[i] = (byte) (sign * 30);
                counter++;
            }
            // the next call is blocking until the entire buffer is 
            // sent to the SourceDataLine
            soundLine.write(buffer, 0, bufferSize);
        }
    }
}