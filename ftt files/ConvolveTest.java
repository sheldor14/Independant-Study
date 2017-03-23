import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import sun.audio.AudioData;
import java.util.Arrays;

import sun.audio.AudioStream;

public class ConvolveTest{
    public static Complex[] convertByte(byte[] arr){
        Complex[] c = new Complex[arr.length];
        for(int i=0; i<arr.length; i++)
            c[i] = new Complex(arr[i], 0);
        return c;
    }
    public static byte[] convertComplex(Complex[] arr){
        byte[] b = new byte[arr.length];
        for(int i=0; i<arr.length; i++)
            b[i] = (byte)arr[i].re();
        return b;
    }
    public static void main(String[] args) {
        try {
            int totalFramesRead = 0;
            File audio = new File(args[0]);
            AudioInputStream ais = AudioSystem.getAudioInputStream(audio);
            FileOutputStream output = new FileOutputStream("output.wav");
            int perFrame = ais.getFormat().getFrameSize();
            perFrame = (perFrame == AudioSystem.NOT_SPECIFIED) ? 1:perFrame;
            int numBytes = 256*perFrame;
            byte[] in= new byte[numBytes], out= new byte[numBytes], filter = new byte[numBytes];
            //ArrayList<byte> ret = new ArrayList<byte>();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //AudioSystem.write(ulaw, AudioFileFormat.Type.WAVE, tempFile);
            for(int i =0; i<numBytes; i++){
                filter[i] = (Math.random()<.5)?(byte)0:(byte)1;
            }
            try {
                int bread = 0, fread = 0, inc = 0;
                while((bread = ais.read(in))!= -1){
                    fread = bread/perFrame;
                    inc = Math.max(inc, bread);
                    totalFramesRead+=fread;
                    baos.write(in, 0, bread);
                    //ret.addAll(convertComplex(FFT.fft(convertByte(in))));
                }
                int cur = 0;
                byte [] convert = new byte[baos.toByteArray().length], hold = baos.toByteArray();
                for(int i =0; i< hold.length; i+=inc){
                    byte[] h = Arrays.copyOfRange(hold,i,inc);
                    h = convertComplex(FFT.fft(convertByte(h)));
                    for(int j = 0; j<h.length; j++)
                        convert[cur++] = h[j];
                }
                ByteArrayInputStream bais = new ByteArrayInputStream(convert);
                AudioInputStream stream = AudioSystem.getAudioInputStream(bais);
                AudioSystem.write(stream, AudioFileFormat.Type.WAVE, new File("please.wav"));
                output.close();
            } catch (Exception e) {
                System.err.println(e);
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

}