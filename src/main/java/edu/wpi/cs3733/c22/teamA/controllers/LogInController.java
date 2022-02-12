package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LogInController {
  @FXML private Text welcomeBox;
  @FXML private TextField usernameBox;
  @FXML private PasswordField passwordBox;
  @FXML private JFXButton logInButton;

  private final SceneSwitcher sceneSwitcher = Aapp.sceneSwitcher;

  @FXML
  private void initialize() {
    logInButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(40), Insets.EMPTY)));
  }

  @FXML
  private void logIn() {
    Aapp.factory.setUsername(usernameBox.getText());
    Aapp.factory.setPassword(passwordBox.getText());

    try {
      Aapp.connection = Aapp.factory.newConnection("app:audit component:event-consumer");

      sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME_SCENE);
    } catch (Exception e) {
      welcomeBox.setText("Unsuccessful login!");
      welcomeBox.setFill(Color.RED);
      //      sceneController.switchScene(SceneController.SCENES.HOME_SCENE);

      usernameBox.setText("");
      passwordBox.setText("");
    }
  }

  @FXML
  private void exitApp() {
    System.exit(0);
  }
}
