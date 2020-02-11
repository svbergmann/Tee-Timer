package sample.classes;

import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

import javax.sound.sampled.AudioInputStream;

public class TextToSpeech {

    private static TextToSpeech tts = null;
    private LocalMaryInterface mary;

    private TextToSpeech() {
        // init mary
        try {
            mary = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            System.err.println("Could not initialize MaryTTS interface: " + e.getMessage());
        }


    }

    public static TextToSpeech getInstance() {
        if (tts == null) {
            tts = new TextToSpeech();
        }
        return tts;
    }

    public void play(String text) {
        // synthesize
        AudioInputStream audio = null;
        try {
            audio = mary.generateAudio(text);
        } catch (SynthesisException e) {
            System.err.println("Synthesis failed: " + e.getMessage());
        }
        // Media media = new Media();
    }
}