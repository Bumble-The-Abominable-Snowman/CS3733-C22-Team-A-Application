package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.auth0.Auth0Login;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginCtrl {

  @FXML private Text welcomeBox;
  @FXML private TextField usernameBox;
  @FXML private PasswordField passwordBox;
  @FXML private JFXButton logInButton;
  @FXML private JFXButton exitButton;
  @FXML private Text warningText;

  double stageWidth;
  double welcomeTextSize;
  double usernameTextSize;
  double passwordTextSize;
  double logInButtonTextSize;
  double exitButtonTextSize;
  double warningTextSize;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  private void initialize() {

    welcomeTextSize = welcomeBox.getFont().getSize();
    usernameTextSize = usernameBox.getFont().getSize();
    passwordTextSize = passwordBox.getFont().getSize();
    logInButtonTextSize = logInButton.getFont().getSize();
    exitButtonTextSize = exitButton.getFont().getSize();
    warningTextSize = warningText.getFont().getSize();

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

    updateSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateSize();
            });
  }

  @FXML
  private void logIn() {
      //Auth0Login.login();
      Adb.username = usernameBox.getText();
      Adb.password = passwordBox.getText();

    try {
        Adb.initialConnection("EmbeddedDriver");
        if(!Adb.isInitialized) {
            try {
                DriverManager.getConnection(String.format("jdbc:derby:%s;user=%s;shutdown=true", Adb.pathToDBA, Adb.username));
            } catch (SQLException e) {
                System.out.println(e);
            }
            Adb.initialConnection("EmbeddedDriver");
        }
        sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
    } catch (Exception e) {
        warningText.setFill(Color.RED);

        passwordBox.setText("");
    }
  }

  @FXML
  private void exitApp() {
    System.exit(0);
  }

  @FXML
  private void updateSize() {

    stageWidth = App.getStage().getWidth();

    welcomeBox.setStyle("-fx-font-size: " + ((stageWidth / 1000) * welcomeTextSize) + "pt;");
    usernameBox.setStyle("-fx-font-size: " + ((stageWidth / 1000) * usernameTextSize) + "pt;");
    passwordBox.setStyle("-fx-font-size: " + ((stageWidth / 1000) * passwordTextSize) + "pt;");
    logInButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * logInButtonTextSize) + "pt;");
    exitButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * exitButtonTextSize) + "pt;");
    warningText.setStyle("-fx-font-size: " + ((stageWidth / 1000) * warningTextSize) + "pt;");
  }

}
