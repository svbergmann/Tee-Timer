package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.classes.TeaTypeTimeHandler;
import sample.controllers.Controller;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static Main self;

    public static URL BEEP_PATH = Thread.currentThread().getContextClassLoader().getResource("audio/out.wav");

    private Stage primaryStage;
    private Controller controller;
    private String nameOfScene;

    /**
     * Returns an instance of the main class,
     * should be executed after the application was started.
     * Otherwise the instance is null.
     *
     * @return the instance.
     */
    public static Main getInstance() {
        if (self == null) {
            System.out.println("ERROR: Main instance does not exist.");
        }
        return self;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        self = this;
        self.primaryStage = primaryStage;

        TeaTypeTimeHandler handler = TeaTypeTimeHandler.getInstance();
        if (handler.isSuccessfulRead()) {
            replaceScene("TimerPage.fxml");
        } else {
            replaceScene("ErrorPage.fxml");
        }

        primaryStage.setTitle("Tee-Timer");
        primaryStage.getIcons().add(new Image("pictures/ProgrammIcon.png"));

        primaryStage.setOnCloseRequest(event -> {
            if (!nameOfScene.equals("TimerPage.fxml")) {
                event.consume();
                replaceScene("NoPage.fxml");
            } else {
                System.exit(1);
            }
        });

        primaryStage.show();
    }

    /**
     * Method for replacing the current scene.
     *
     * @param fxml the fxml document.
     */
    public void replaceScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation((getClass().getResource("/view/" + fxml)));

            Parent root = loader.load();
            controller = loader.getController();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();

            primaryStage.sizeToScene();
            nameOfScene = fxml;
        } catch (IOException ignored) {
        }
    }

    /**
     * Method for getting the current scene.
     *
     * @return the current scene as fxml.
     */
    public String getNameOfScene() {
        return nameOfScene;
    }
}
