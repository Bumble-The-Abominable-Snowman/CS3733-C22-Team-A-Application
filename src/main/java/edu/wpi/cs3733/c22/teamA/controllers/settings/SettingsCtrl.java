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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class SettingsCtrl extends MasterCtrl {

  @FXML private JFXToggleButton toggleClientServerButton;
  @FXML private JFXToggleButton toggleCloudButton;
  @FXML private JFXButton loadBackupButton;
  @FXML private JFXButton saveBackupButton;
  @FXML private ImageView bumbleHead;
  @FXML private Label bubbleText;

  double stageWidth;

  double loadBackupTextSize;
  double saveBackupTextSize;
  double clientServerTextSize;
  double cloudTextSize;

  @FXML
  private void initialize() {

    configure();

    double bubbleTextSize = bubbleText.getFont().getSize();

    if (Adb.usingEmbedded) {
      toggleClientServerButton.setSelected(false);
    } else {
      toggleClientServerButton.setSelected(true);
    }

    loadBackupTextSize = loadBackupButton.getFont().getSize();
    saveBackupTextSize = saveBackupButton.getFont().getSize();
    clientServerTextSize = toggleClientServerButton.getFont().getSize();
    cloudTextSize = toggleClientServerButton.getFont().getSize();

    updateSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateSize();
              bubbleText.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bubbleTextSize)
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

  public void toggleCloud() {}

  @FXML
  private void updateSize() {

    stageWidth = App.getStage().getWidth();

    loadBackupButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * loadBackupTextSize) + "pt;");
    saveBackupButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * saveBackupTextSize) + "pt;");
    toggleClientServerButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * loadBackupTextSize) + "pt;");
    toggleCloudButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * saveBackupTextSize) + "pt;");
  }


}
