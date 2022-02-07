package edu.wpi.c22.teamA.controllers.settings;

import edu.wpi.c22.teamA.Aapp;
import edu.wpi.c22.teamA.controllers.SceneController;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class SettingsController {
  @FXML private Button homeButton;
  @FXML private Button backButton;
  @FXML private Button loadFromBackupButton;
  private FXMLLoader loader = new FXMLLoader();
  private final SceneController sceneController = Aapp.sceneController;

  public void returnToHomeScene(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
  }

  public void loadFromBackup(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene(SceneController.SCENES.LOAD_FROM_BACKUP_SCENE);
  }

  public void exportToBackup(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene(SceneController.SCENES.EXPORT_TO_BACKUP_SCENE);
  }
}
