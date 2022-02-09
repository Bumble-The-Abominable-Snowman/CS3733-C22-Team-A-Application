package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.Aapp;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LogInController {
  @FXML private Text welcomeBox;
  @FXML private TextField usernameBox;
  @FXML private PasswordField passwordBox;
  @FXML private JFXButton logInButton;
  @FXML private JFXButton exitButton;

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  private void logIn(ActionEvent actionEvent) throws IOException, TimeoutException {
    Aapp.factory.setUsername(usernameBox.getText());
    Aapp.factory.setPassword(passwordBox.getText());

    try {
      Aapp.connection = Aapp.factory.newConnection("app:audit component:event-consumer");

      welcomeBox.setText("Success!");
      welcomeBox.setFill(Color.GREEN);

      sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
    } catch (Exception e) {

      welcomeBox.setText("Unsuccesful login!");
      welcomeBox.setFill(Color.RED);
      sceneController.switchScene(SceneController.SCENES.HOME_SCENE);

      usernameBox.setText("");
      passwordBox.setText("");
    }
  }

  @FXML
  private void exitApp(ActionEvent actionEvent) {
    System.exit(0);
  }
}
