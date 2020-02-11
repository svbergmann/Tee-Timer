package sample.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sample.Main;
import sample.classes.TeaTypeTimeHandler;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Class ErrorPage that appears if the file contains an error.
 *
 * @author ProfSchmergmann
 */
public class ErrorPage implements Controller {

    @FXML
    AnchorPane anchorPane;
    @FXML
    Button buttonCreateANewFile;
    @FXML
    Button button1;
    @FXML
    Button button2;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttonCreateANewFile.setOnMouseClicked(mouseEvent -> {
            TeaTypeTimeHandler.getInstance().wasWrongFormat();
            Main.getInstance().replaceScene("TimerPage.fxml");
        });

        button1.setOnMouseEntered(mouseEvent(button1, button2));
        button2.setOnMouseEntered(mouseEvent(button2, button1));
    }

    /**
     * MouseEvent for hovering over the Buttons.
     *
     * @param button1 the button to hover over.
     * @param button2 the other button.
     * @return a mouseEvent.
     */
    private EventHandler<MouseEvent> mouseEvent(Button button1, Button button2) {
        return mouseEvent -> {
            button2.setVisible(true);
            button2.setLayoutX(generateRandomIntIntRange(0, 600));
            button2.setLayoutY(generateRandomIntIntRange(0, 650));
            button1.setVisible(false);
        };
    }

    /**
     * Method for generating a random int number in the given range.
     *
     * @param min the min value.
     * @param max the max value.
     * @return a random int number in the given range.
     */
    private int generateRandomIntIntRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
