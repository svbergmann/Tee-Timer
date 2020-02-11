package controllers;

import classes.TeaTimeCount;
import classes.TeaTypeTimeHandler;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class Controller for connecting the logic with the GUI.
 *
 * @author Prof Schmergmann
 */
public class MainPage implements Controller {

    @FXML
    AnchorPane stage;
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

    private TeaTimeCount z;
    private Timeline colonAnimationTimer;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Sets the GridPane
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

        //Sets the background.
        startBackground.setImage(new Image("pictures/teacup.jpg"));

        //Sets the GIF.
        steamGif.setImage(new Image("pictures/watersteam.gif"));

        //Sets finished background.
        finishedBackground.setImage(new Image("pictures/AlarmClock.png"));

        //Initial values for minutes and seconds.
        int startminutes = 5;
        int startseconds = 0;

        //Label for minutes.
        minutes.setText(String.format("%02d", startminutes));

        //Label for seconds.
        seconds.setText(String.format("%02d", startseconds));

        //Declare a new TeaTimeCount object.
        z = new TeaTimeCount(seconds, minutes, progressBar, finishedBackground, steamGif);

        //Buttons for plus and minus
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
            if (secondsInt == 0) {
                seconds.setText(String.format("%02d", 59));
                minutes.setText(String.format("%02d", Integer.parseInt(minutes.getText().trim()) - 1));
            } else {
                seconds.setText(String.format("%02d", Integer.parseInt(seconds.getText().trim()) - 1));
            }
            checkButtonsAndUpdateProgressBar();
        });

        //Buttons start and stop.
        start.setOnAction(actionEvent ->
        {
            start.setDisable(true);
            stop.setDisable(false);
            z = new TeaTimeCount(seconds, minutes, progressBar, finishedBackground, steamGif);
            z.start(Integer.parseInt(minutes.getText().trim()) * 60 + Integer.parseInt(seconds.getText().trim()));
            secondsPlus.setDisable(true);
            secondsMinus.setDisable(true);
            minutesPlus.setDisable(true);
            minutesMinus.setDisable(true);
            steamGif.setVisible(true);
            colonAnimationTimer.play();
            start.setVisible(false);
            stop.setVisible(true);
        });

        //Stops the running time.
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
                colonAnimationTimer.stop();
                colonLabel.setVisible(true);
                start.setVisible(true);
                stop.setVisible(false);
            }
            stop.setVisible(false);
        });

        //Set typeOfTeaComboBox.
        typeOfTeaComboBox.setItems(TeaTypeTimeHandler.getInstance().getTeaTypesAsObservableList());
        typeOfTeaComboBox.setVisibleRowCount(30);
        typeOfTeaComboBox.getSelectionModel().selectFirst();
        typeOfTeaComboBox.setOnMouseClicked(actionEvent -> updateContents());

        //Action for adding a new type of tea.
        buttonAddTea.setOnMouseClicked(mouseEvent -> checkForValidInputAndSave());

        addTeaTextField.setOnKeyTyped(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                checkForValidInputAndSave();
            }
        });

        //ErrorLabel disappears when there is a new attempt.
        addTeaTextField.setOnMouseClicked(mouseEvent -> wrongFormatError.setVisible(false));

        //Action for colon.
        colonAnimationTimer = new Timeline(new KeyFrame(Duration.ZERO,
                new KeyValue(colonLabel.visibleProperty(), true)),
                new KeyFrame(new Duration(500), new KeyValue(colonLabel.visibleProperty(), false)),
                new KeyFrame(new Duration(1000), new KeyValue(colonLabel.visibleProperty(), true)));
        colonAnimationTimer.setOnFinished((event) -> colonAnimationTimer.play());
    }

    /**
     * Updates the Hashmap and all other corresponding files.
     */
    public void update() {
        ObservableList<String> list = TeaTypeTimeHandler.getInstance().getTeaTypesAsObservableList();
        typeOfTeaComboBox.setItems(list);
        typeOfTeaComboBox.getSelectionModel().selectFirst();
        TeaTypeTimeHandler.getInstance().writeFile();
    }

    /**
     * Checks the buttons for legal numbers and updates the progressBar.
     */
    public void checkButtonsAndUpdateProgressBar() {
        checkButtons();
        z.setStartMin(minutes);
        z.setStartSec(seconds);
        z.setProgressBarMax(progressBar);
    }

    /**
     * Checks the buttons.
     */
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

    /**
     * Updates the minutes and seconds label if there is a new type of tea.
     */
    private void updateContents() {
        double temp = TeaTypeTimeHandler.getInstance().getTeaTimeInSeconds(typeOfTeaComboBox.getValue());
        minutes.setText(String.format("%02d", (int) temp / 60));
        seconds.setText(String.format("%02d", (int) temp % 60));
    }

    /**
     * Checks for a valid input and saves it.
     * If the input isn't valid, the ErrorBox appears.
     */
    private void checkForValidInputAndSave() {
        try {
            String[] temp = addTeaTextField.getText().split("\\s+:\\s|:\\s+|\\s:|:"); //Splits the input string at ":" or " :" or " :" or " : "
            TeaTypeTimeHandler.getInstance().addTeaType(temp[0], Double.parseDouble(temp[1]));
            update();
            typeOfTeaComboBox.getSelectionModel().select(temp[0]);
            updateContents();
            wrongFormatError.setVisible(false);
            addTeaTextField.clear();
        } catch (Throwable e) {
            wrongFormatError.setVisible(true);
        }
    }
}