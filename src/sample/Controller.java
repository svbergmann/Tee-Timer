package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML
    Button secondsMinus;
    @FXML
    Button secondsPlus;
    @FXML
    Button minutesMinus;
    @FXML
    Button minutesPlus;
    @FXML
    Button start;
    @FXML
    Button stop;
    @FXML
    Label minutes;
    @FXML
    Label seconds;
    @FXML
    ImageView startBackground;
    @FXML
    ProgressBar progressBar;
    @FXML
    ImageView steamGif;
    @FXML
    ImageView finishedBackground;
    @FXML
    Button stopBlinking;
    @FXML
    GridPane buttons;

    private Zeiterfassung z;

    public void initialize() {

        //GridPane setzen
        GridPane.setFillHeight(minutesPlus, true);
        GridPane.setFillHeight(minutesMinus, true);

        GridPane.setFillHeight(secondsPlus, true);
        GridPane.setFillHeight(secondsMinus, true);

        GridPane.setFillHeight(seconds, true);
        GridPane.setFillHeight(minutes, true);

        GridPane.setFillWidth(minutesPlus, true);
        GridPane.setFillWidth(minutesMinus, true);

        GridPane.setFillWidth(secondsPlus, true);
        GridPane.setFillWidth(secondsMinus, true);

        GridPane.setFillWidth(seconds, true);
        GridPane.setFillWidth(minutes, true);

        //Hintergrund setzen
        startBackground.setImage(new Image("teacup.jpg"));

        //Gif setzen
        steamGif.setImage(new Image("watersteam.gif"));

        //Fertigen Hintergrund setzen
        finishedBackground.setImage(new Image("AlarmClock.png"));

        //Startwerte fuer Minuten und Sekundenanzeige
        int startminutes = 5;
        int startseconds = 0;

        //Das Label zur Minutenanzeige
        minutes.setText(startminutes + "");

        //Das Label zur Sekundenanzeige
        seconds.setText(startseconds + "");

        //Deklarieren eines neuen Zeiterfassungsobjektes
        z = new Zeiterfassung(seconds, minutes, progressBar, finishedBackground, steamGif);

        //Buttons zum hoch und runterzaehlen
        minutesPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            minutes.setText(Integer.parseInt(minutes.getText()) + 1 + "");
            checkButtonsAndUpdateProgressBar();
        });


        minutesMinus.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            minutes.setText(Integer.parseInt(minutes.getText()) - 1 + "");
            checkButtonsAndUpdateProgressBar();
        });

        secondsPlus.setOnMouseClicked(mouseEvent -> {
            int secondsInt = Integer.parseInt(seconds.getText());
            int minutesInt = Integer.parseInt(minutes.getText());
            if (secondsInt == 59) {
                seconds.setText(0 + "");
                minutes.setText(minutesInt + 1 + "");
            } else {
                seconds.setText(secondsInt + 1 + "");
            }
            checkButtonsAndUpdateProgressBar();
        });

        secondsMinus.setOnMouseClicked(mouseEvent -> {
            int secondsInt = Integer.parseInt(seconds.getText());
            int minutesInt = Integer.parseInt(minutes.getText());
            if (secondsInt == 0) {
                seconds.setText(59 + "");
                minutes.setText(minutesInt - 1 + "");
            } else {
                seconds.setText(secondsInt - 1 + "");
            }
            checkButtonsAndUpdateProgressBar();
        });

        //Buttons start und stop
        start.setOnAction(actionEvent ->
        {
            z = new Zeiterfassung(seconds, minutes, progressBar, finishedBackground, steamGif);
            z.start(Integer.parseInt(minutes.getText()) * 60 + Integer.parseInt(seconds.getText()));
            secondsPlus.setDisable(true);
            secondsMinus.setDisable(true);
            minutesPlus.setDisable(true);
            minutesMinus.setDisable(true);
            steamGif.setVisible(true);
        });

        //Stoppt die aktuell durchlaufende Zeit
        stop.setOnAction(actionEvent -> {
            if (z != null) {
                z.stop();
                steamGif.setVisible(false);
                z.stopAnimationTimer();
                finishedBackground.setVisible(false);
                checkButtons();
                minutesPlus.setDisable(false);
                secondsPlus.setDisable(false);
            }
        });
    }

    public void checkButtonsAndUpdateProgressBar() {
        checkButtons();
        z.setStartMin(minutes);
        z.setStartSec(seconds);
        z.setProgressBarMax(progressBar);
    }

    public void checkButtons() {
        int secondsInt = Integer.parseInt(seconds.getText());
        int minutesInt = Integer.parseInt(minutes.getText());
        if (minutesInt == 0 && secondsInt == 0) {
            minutesMinus.setDisable(true);
            secondsMinus.setDisable(true);
        } else if (minutesInt == 0 && secondsInt > 0) {
            minutesMinus.setDisable(true);
            secondsMinus.setDisable(false);
        } else {
            minutesMinus.setDisable(false);
            secondsMinus.setDisable(false);
        }
    }
}
