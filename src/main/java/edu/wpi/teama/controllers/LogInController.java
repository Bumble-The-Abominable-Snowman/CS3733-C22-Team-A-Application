package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LogInController {
  @FXML private TextField usernameBox;
  @FXML private TextField passwordBox;
  @FXML private Button loginButton;
  @FXML private Button exitButton;
  private FXMLLoader loader = new FXMLLoader();

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  private void logIn(ActionEvent actionEvent) throws IOException {
    String username = usernameBox.getText();
    String password = passwordBox.getText();

    // Here would be where we implement the database access portion, but haven't met yet so
    // placeholder

    System.out.println("LOG IN");

    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
  }

  @FXML
  private void exitApp(ActionEvent actionEvent) {
    System.exit(0);
  }
}
