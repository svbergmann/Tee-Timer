package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    @FXML
    Spinner secondsSpinner;
    @FXML
    Spinner minutesSpinner;
    @FXML
    ImageView startBackground;
    @FXML
    ProgressBar progressBar;
    @FXML
    ImageView steamGif;
    @FXML
    ImageView finishedBackground;
    @FXML
    Label minutes;
    @FXML
    Label seconds;
    @FXML
    Button start;
    @FXML
    Button stop;

    private Timer timer = new Timer();
    private Duration duration;

    public void initialize() {

        //Hintergrund setzen
        startBackground.setImage(new Image("teacup.jpg"));

        //Gif setzen
        steamGif.setImage(new Image("watersteam.gif"));

        //Fertigen Hintergrund setzen
        finishedBackground.setImage(new Image("AlarmClock.png"));

        //Startwerte fuer die Spinner initialisieren
        minutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
        minutesSpinner.getValueFactory().setValue(5);
        secondsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
        secondsSpinner.getValueFactory().setValue(0);

        //Labels und Buttons initialisieren
        minutes.setText("" + minutesSpinner.getValue());
        seconds.setText("" + secondsSpinner.getValue());
        stop.setDisable(true);

        //Logik der Spinner

        //Startet die aktuelle Zeit
        start.setOnAction(actionEvent -> {
            duration = Duration.ofSeconds(Long.parseLong("" + minutesSpinner.getValue()) * 60 + Long.parseLong("" + secondsSpinner.getValue()));
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (duration.isZero()) {
                        animationtimer();
                    } else {
                        Platform.runLater(() -> {
                            seconds.setText(getActualseconds() + "");
                            minutes.setText(getActualminutes() + "");
                            duration = duration.minusSeconds(1);
                        });
                    }
                }
            }, 1000, 1000);
            secondsSpinner.setDisable(true);
            minutesSpinner.setDisable(true);
            steamGif.setVisible(true);
            stop.setDisable(false);
            start.setDisable(true);
        });

        //Stoppt die aktuell durchlaufende Zeit
        stop.setOnAction(actionEvent -> {
            try {
                timer.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            start.setDisable(false);
        });


    }

    private void animationtimer() {
        new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long l) {
                if (l - last > 1000000000) {
                    last = l;
                    Toolkit.getDefaultToolkit().beep();
                    steamGif.setVisible(false);
                    if (finishedBackground.isVisible()) {
                        finishedBackground.setVisible(false);
                    } else {
                        finishedBackground.setVisible(true);
                    }
                }
            }
        };
    }


    public long getActualminutes() {
        return duration.toMinutes();
    }

    public long getActualseconds() {
        return duration.getSeconds() % 60;
    }
}
