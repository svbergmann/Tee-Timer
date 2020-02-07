package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.IOException;

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
    @FXML
    Button buttonAddTea;
    @FXML
    Label wrongFormatError;
    @FXML
    Label colonLabel;

    private Zeiterfassung z;
    private TeaTypeTimeHandler teaTypeTimeHandler;
    private Timeline colonAnimationtimer;

    public void initialize() throws IOException {

        //Text Datei einlesen und speichern
        teaTypeTimeHandler = new TeaTypeTimeHandler();

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
            colonAnimationtimer.play();
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
                colonAnimationtimer.stop();
                colonLabel.setVisible(true);
            }
        });

        //typeOfTeaComboBox setzen
        typeOfTeaComboBox.setItems(teaTypeTimeHandler.getTeaTypesAsObservableList());
        typeOfTeaComboBox.setVisibleRowCount(30);
        typeOfTeaComboBox.getSelectionModel().selectFirst();
        typeOfTeaComboBox.setOnMouseClicked(actionEvent -> updateContents());

        //Aktion für das Hinzufuegen des neuen Tees setzen
        buttonAddTea.setOnMouseClicked(mouseEvent -> {
            try {
                String[] temp = addTeaTextField.getText().split(" : ");
                teaTypeTimeHandler.addTeaType(temp[0], Double.parseDouble(temp[1]));
                update();
                typeOfTeaComboBox.getSelectionModel().select(temp[0]);
                updateContents();
                wrongFormatError.setVisible(false);
                addTeaTextField.clear();
            } catch (Throwable e) {
                wrongFormatError.setVisible(true);
            }
        });

        //Aktion für das Doppelpunkt Label setzen
        colonAnimationtimer = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(colonLabel.visibleProperty(), true)), new KeyFrame(new Duration(500), new KeyValue(colonLabel.visibleProperty(), false)), new KeyFrame(new Duration(1000), new KeyValue(colonLabel.visibleProperty(), true)));
        colonAnimationtimer.setOnFinished((event) -> colonAnimationtimer.play());

    }

    public void update() throws IOException {
        ObservableList<String> list = teaTypeTimeHandler.getTeaTypesAsObservableList();
        typeOfTeaComboBox.setItems(list);
        typeOfTeaComboBox.getSelectionModel().selectFirst();
        teaTypeTimeHandler.writeFile();
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

    private void updateContents() {
        double temp = teaTypeTimeHandler.getTeaTimeInSeconds(typeOfTeaComboBox.getValue());
        minutes.setText(String.format("%02d", (int) temp / 60));
        seconds.setText(String.format("%02d", (int) temp % 60));
    }
}
