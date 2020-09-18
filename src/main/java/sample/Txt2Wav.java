package sample;

import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.MaryAudioUtils;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Txt2Wav {

    public static void main(String[] args) throws MaryConfigurationException {
        // get output option
        String outputFileName = "src/main/resources/out.wav";
        String inputFileName = "in.txt";
        // get input
        String inputText = null;
        String file = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(inputFileName))
                .getPath();
        //Remove leading / for windows, gets put there wrongly
        file = file.substring(1);
        try {
            inputText = new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            System.err.println("Could not read from file " + inputFileName + ": " + e.getMessage());
            System.exit(1);
        }

        // init mary
        LocalMaryInterface mary = null;
        try {
            mary = new LocalMaryInterface();
        } catch (
                MaryConfigurationException e) {
            System.err.println("Could not initialize MaryTTS interface: " + e.getMessage());
            throw e;
        }
        mary.setVoice("bits1-hsmm");

        // synthesize
        AudioInputStream audio = null;
        try {
            audio = mary.generateAudio(inputText);
        } catch (
                SynthesisException e) {
            System.err.println("Synthesis failed: " + e.getMessage());
            System.exit(1);
        }

        // write to output
        double[] samples = MaryAudioUtils.getSamplesAsDoubleArray(audio);
        try {
            MaryAudioUtils.writeWavFile(samples, outputFileName, audio.getFormat());
            System.out.println("Output written to " + outputFileName);
        } catch (
                IOException e) {
            System.err.println("Could not write to file: " + outputFileName + "\n" + e.getMessage());
            System.exit(1);
        }
    }
}