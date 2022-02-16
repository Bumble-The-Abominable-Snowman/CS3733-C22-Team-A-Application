package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import java.io.IOException;
import javafx.fxml.FXML;

public class SettingsCtrl extends MasterCtrl {
  @FXML private JFXButton loadFromBackupButton;
  @FXML private JFXButton exportToBackupButton;

  @FXML
  private void initialize() {

    configure();

    double backTextSize = backButton.getFont().getSize();
    double loadFromBackupTextSize = loadFromBackupButton.getFont().getSize();
    double exportToBackupTextSize = exportToBackupButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              backButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * backTextSize) + "pt;");
              loadFromBackupButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * loadFromBackupTextSize)
                      + "pt;");
              exportToBackupButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * exportToBackupTextSize)
                      + "pt;");
            });
  }

  public void goToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  public void loadFromBackup() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOAD_BACKUP);
  }

  public void exportToBackup() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SAVE_BACKUP);
  }
}
