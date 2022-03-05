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

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LaunchCtrl {

  @FXML private JFXButton embeddedDB;
  @FXML private JFXButton clientServerDB;
  @FXML private JFXButton firebaseDB;

  double stageWidth;
  double embeddedDBSize;
  double clientServerDBSize;
  double firebaseDBSize;
  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  private void initialize() {

    embeddedDBSize = embeddedDB.getFont().getSize();
    clientServerDBSize = clientServerDB.getFont().getSize();
    firebaseDBSize = firebaseDB.getFont().getSize();

    updateSize();
    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateSize();
            });
  }

  @FXML
  private void launchEmbedded() throws IOException {
      App.DB_CHOICE = "embedded";
        sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
    }
@FXML
    private void launchClientServer() throws IOException {
    App.DB_CHOICE = "remote-derby";
        sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
    }
@FXML
    private void launchFirebase() throws IOException {
      //handleLogin()??? needs to be static but it can't.
    App.DB_CHOICE = "nosql";
    Auth0Login.login().thenRun(() -> {
        try {
            sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    });
    }

  @FXML
  private void updateSize() {
    stageWidth = App.getStage().getWidth();
    embeddedDB.setStyle("-fx-font-size: " + ((stageWidth / 1000) * embeddedDBSize) + "pt;");
    clientServerDB.setStyle("-fx-font-size: " + ((stageWidth / 1000) * clientServerDBSize) + "pt;");
    firebaseDB.setStyle("-fx-font-size: " + ((stageWidth / 1000) * firebaseDBSize) + "pt;");
  }

}
