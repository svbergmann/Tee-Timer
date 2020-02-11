package sample.classes;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sample.Main;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class TeaTimeCount for the animation of a time running down.
 *
 * @author ProfSchmergmann
 */
public class TeaTimeCount {

    private static AtomicInteger progressBarMax = new AtomicInteger(300);
    private Duration duration;
    private boolean isRunning;
    private int startMin;
    private int startSec;
    private AnimationTimer animationTimer;
    private AtomicBoolean beeped = new AtomicBoolean(false);

    /**
     * Constructor for an Object, that counts down to zero, stops and does an animation.
     *
     * @param secondsLabel   the label for the seconds.
     * @param minutesLabel   the label for the minutes.
     * @param progressBar    the progressBar.
     * @param imageView      the imageView for the finished timer.
     * @param imageViewSteam the imageView for the running timer.
     */
    public TeaTimeCount(Label secondsLabel, Label minutesLabel, ProgressBar progressBar, ImageView imageView, ImageView imageViewSteam) {
        startSec = Integer.parseInt(secondsLabel.getText().trim());
        startMin = Integer.parseInt(minutesLabel.getText().trim());
        setProgressBarMax(progressBar);
        this.start(this.startMin * 60 + this.startSec);
        isRunning = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isRunning && !duration.isZero()) {
                    duration = duration.minusSeconds(1);
                    int max = progressBarMax.get();
                    Platform.runLater(() -> {
                        progressBar.setProgress(1. - 1. * duration.getSeconds() / max);
                        secondsLabel.setText(String.format("%02d", getActualSeconds()));
                        minutesLabel.setText(String.format("%02d", getActualMinutes()));
                    });
                } else if (duration.isZero()) {
                    animationTimer.start();
                    isRunning = false;
                    duration = Duration.ofSeconds(1);
                }
            }
        }, 0, 1000);
        animationTimer = new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long l) {
                if (l - last > 1000000000) {
                    last = l;
                    if (!beeped.get()) {
                        beeped.set(true);
                        String file = "";
                        try {
                            file = Main.BEEP_PATH.toURI().toString();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        Media test = new Media(file);
                        MediaPlayer x = new MediaPlayer(test);
                        x.play();
                    }
                    imageViewSteam.setVisible(false);
                    if (imageView.isVisible()) {
                        imageView.setVisible(false);
                    } else {
                        imageView.setVisible(true);
                    }
                }
            }
        };
    }

    /**
     * Stops the animationTimer which starts if the count is zero.
     */
    public void stopAnimationTimer() {
        animationTimer.stop();
    }

    /**
     * Sets the initial value for the minutes read from the minutes label.
     *
     * @param minutes the label.
     */
    public void setStartMin(Label minutes) {
        this.startMin = Integer.parseInt(minutes.getText().trim());
    }

    /**
     * Sets the initial value for the seconds read from the seconds label.
     *
     * @param seconds the label.
     */
    public void setStartSec(Label seconds) {
        this.startSec = Integer.parseInt(seconds.getText().trim());
    }

    /**
     * Returns the actual minutes read from the duration object.
     *
     * @return the minutes in a long.
     */
    public long getActualMinutes() {
        return duration.toMinutes();
    }

    /**
     * Returns the actual seconds read from the duration object.
     *
     * @return the seconds in a long.
     */
    public long getActualSeconds() {
        return duration.getSeconds() % 60;
    }

    /**
     * Starts the actual timer.
     *
     * @param time the time at witch the timer should start.
     */
    public void start(long time) {
        duration = Duration.ofSeconds(time);
        isRunning = true;
        beeped.set(false);
    }

    /**
     * Stops the timer.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Sets the maximum of the progressBar.
     *
     * @param progressBar the progressBar.
     */
    public void setProgressBarMax(ProgressBar progressBar) {
        progressBarMax.set(startMin * 60 + startSec);
        progressBar.setProgress(0);
    }

}