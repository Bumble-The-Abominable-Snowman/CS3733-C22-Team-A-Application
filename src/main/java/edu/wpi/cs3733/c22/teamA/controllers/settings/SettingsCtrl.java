package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;

public class SettingsCtrl extends MasterCtrl {

  @FXML private JFXToggleButton toggleClientServerButton;
  @FXML private JFXButton loadBackupButton;
  @FXML private JFXButton saveBackupButton;

  @FXML
  private void initialize() {

    configure();

    if (Adb.usingEmbedded) {
      toggleClientServerButton.setSelected(false);
    } else {
      toggleClientServerButton.setSelected(true);
    }

    double loadBackupTextSize = loadBackupButton.getFont().getSize();
    double saveBackupTextSize = saveBackupButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              loadBackupButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * loadBackupTextSize)
                      + "pt;");
              saveBackupButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * saveBackupTextSize)
                      + "pt;");
            });
  }

  public void goToHome() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  public void loadBackup() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOAD_BACKUP);
  }

  public void saveBackup() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SAVE_BACKUP);
  }

  public void toggleClientServer() {
    try{
      if (toggleClientServerButton.isSelected()) {
        Adb.initialConnection("ClientDriver");
        System.out.println("Client Driver");
      } else {
        Adb.initialConnection("EmbeddedDriver");
        System.out.println("Embedded Driver");
      }
    }catch (SQLException e){
      System.out.println(e);
    }
    // add client server toggle code here
  }
}
