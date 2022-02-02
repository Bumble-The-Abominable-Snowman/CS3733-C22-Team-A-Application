package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SettingsController {
  @FXML private Button homeButton;
  @FXML private Button backButton;
  @FXML private Button loadFromBackupButton;
  private FXMLLoader loader = new FXMLLoader();

  public void returnToHomeScene(ActionEvent actionEvent) throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/home.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) homeButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Home");
    window.show();
  }

  public void returnToSettingsScene(ActionEvent actionEvent) throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/settings.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) backButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Settings");
    window.show();
  }

  public void loadFromBackup(ActionEvent actionEvent) throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/loadFromBackup.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) loadFromBackupButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Load From Backup");
    window.show();
  }
}
