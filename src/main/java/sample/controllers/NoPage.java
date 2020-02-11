package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sample.Main;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class NoPage that appears if someone is trying to close the application.
 *
 * @author ProfSchmergmann
 */
public class NoPage implements Controller {

    @FXML
    Button buttonOK;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonOK.setOnMouseClicked(mouseEvent -> Main.getInstance().replaceScene("ErrorPage.fxml"));
    }
}
