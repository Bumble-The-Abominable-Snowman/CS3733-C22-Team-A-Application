package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginCtrl {
  @FXML private Text welcomeBox;
  @FXML private TextField usernameBox;
  @FXML private PasswordField passwordBox;
  @FXML private JFXButton logInButton;
  @FXML private JFXButton exitButton;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  private void initialize() {

    usernameBox.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            this.logIn();
          }
        });

    passwordBox.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            this.logIn();
          }
        });

    double welcomeTextSize = welcomeBox.getFont().getSize();
    double usernameTextSize = usernameBox.getFont().getSize();
    double passwordTextSize = passwordBox.getFont().getSize();
    double logInButtonTextSize = logInButton.getFont().getSize();
    double exitButtonTextSize = exitButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              welcomeBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * welcomeTextSize)
                      + "pt;");
              usernameBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * usernameTextSize)
                      + "pt;");
              passwordBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * passwordTextSize)
                      + "pt;");
              logInButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * logInButtonTextSize)
                      + "pt;");
              exitButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * exitButtonTextSize)
                      + "pt;");
            });
  }

  @FXML
  private void logIn() {
    App.factory.setUsername(usernameBox.getText());
    App.factory.setPassword(passwordBox.getText());

    try {
      App.connection = App.factory.newConnection("app:audit component:event-consumer");

      sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
    } catch (Exception e) {
      welcomeBox.setText("Unsuccessful login!");
      welcomeBox.setFill(Color.RED);

      passwordBox.setText("");
    }
  }

  @FXML
  private void exitApp() {
    System.exit(0);
  }
}
