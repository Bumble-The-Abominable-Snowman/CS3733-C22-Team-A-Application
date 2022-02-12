package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class SettingsController {
  @FXML private JFXButton backButton;
  @FXML private JFXButton loadFromBackupButton;
  @FXML private JFXButton exportToBackupButton;
  private FXMLLoader loader = new FXMLLoader();
  private final SceneSwitcher sceneSwitcher = Aapp.sceneSwitcher;

  @FXML
  public void initialize() {
    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    loadFromBackupButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(20), Insets.EMPTY)));
    exportToBackupButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(20), Insets.EMPTY)));
  }

  public void returnToHomeScene(ActionEvent actionEvent) throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME_SCENE);
  }

  public void loadFromBackup(ActionEvent actionEvent) throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOAD_FROM_BACKUP_SCENE);
  }

  public void exportToBackup(ActionEvent actionEvent) throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.EXPORT_TO_BACKUP_SCENE);
  }
}
