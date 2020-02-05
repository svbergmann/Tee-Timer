package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
    @FXML
    ComboBox<String> typeOfTeaComboBox;
    @FXML
    TextField addTeaTextField;

    private Zeiterfassung z;
    private HashMap<String, Integer> teaMap;

    public void initialize() throws IOException {

        //Text Datei einlesen und speichern
        TeaTypeTimeHandler.readAndSave(new File("src/main/resources/TeesortenUndZiehzeit"));
        teaMap = TeaTypeTimeHandler.getTeaTypeTimeMap();

        TeaTypeTimeHandler.writeAndSave();

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
        minutes.setText(String.format("%02d", startminutes));

        //Das Label zur Sekundenanzeige
        seconds.setText(String.format("%02d", startseconds));

        //Deklarieren eines neuen Zeiterfassungsobjektes
        z = new Zeiterfassung(seconds, minutes, progressBar, finishedBackground, steamGif);

        //Buttons zum hoch und runterzaehlen
        minutesPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            minutes.setText(String.format("%02d", Integer.parseInt(minutes.getText().trim()) + 1));
            checkButtonsAndUpdateProgressBar();
        });


        minutesMinus.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            minutes.setText(String.format("%02d", Integer.parseInt(minutes.getText().trim()) - 1));
            checkButtonsAndUpdateProgressBar();
        });

        secondsPlus.setOnMouseClicked(mouseEvent -> {
            int secondsInt = Integer.parseInt(seconds.getText().trim());
            int minutesInt = Integer.parseInt(minutes.getText().trim());
            if (secondsInt == 59) {
                seconds.setText(String.format("%02d", 0));
                minutes.setText(String.format("%02d", Integer.parseInt(minutes.getText().trim()) + 1));
            } else {
                seconds.setText(String.format("%02d", Integer.parseInt(seconds.getText().trim()) + 1));
            }
            checkButtonsAndUpdateProgressBar();
        });

        secondsMinus.setOnMouseClicked(mouseEvent -> {
            int secondsInt = Integer.parseInt(seconds.getText().trim());
            int minutesInt = Integer.parseInt(minutes.getText().trim());
            if (secondsInt == 0) {
                seconds.setText(String.format("%02d", 59));
                minutes.setText(String.format("%02d", Integer.parseInt(minutes.getText().trim()) - 1));
            } else {
                seconds.setText(String.format("%02d", Integer.parseInt(seconds.getText().trim()) - 1));
            }
            checkButtonsAndUpdateProgressBar();
        });

        //Buttons start und stop
        start.setOnAction(actionEvent ->
        {
            start.setDisable(true);
            stop.setDisable(false);
            z = new Zeiterfassung(seconds, minutes, progressBar, finishedBackground, steamGif);
            z.start(Integer.parseInt(minutes.getText().trim()) * 60 + Integer.parseInt(seconds.getText().trim()));
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
                start.setDisable(false);
                stop.setDisable(true);
            }
        });

        //typeOfTeaComboBox setzen
        typeOfTeaComboBox.setItems(TeaTypeTimeHandler.getTeaTypes());
        typeOfTeaComboBox.setOnAction(actionEvent -> minutes.setText(String.format("%02d", teaMap.get(typeOfTeaComboBox.getValue()))));

        //Textfield Aktion setzen
        addTeaTextField.setOnAction(actionEvent -> {
            //TODO
        });
    }

    public void checkButtonsAndUpdateProgressBar() {
        checkButtons();
        z.setStartMin(minutes);
        z.setStartSec(seconds);
        z.setProgressBarMax(progressBar);
    }

    public void checkButtons() {
        int secondsInt = Integer.parseInt(seconds.getText().trim());
        int minutesInt = Integer.parseInt(minutes.getText().trim());
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
