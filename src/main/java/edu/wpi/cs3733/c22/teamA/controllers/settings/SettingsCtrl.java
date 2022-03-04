package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import java.io.IOException;
import java.sql.SQLException;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.paint.Color;

public class SettingsCtrl extends MasterCtrl {

  @FXML private JFXToggleButton toggleClientServerButton;
  @FXML private JFXToggleButton toggleCloudButton;
  @FXML private JFXButton loadBackupButton;
  @FXML private JFXButton saveBackupButton;

/*
  @FXML private JFXButton nextButton;
  @FXML private ImageView bumbleBlinkHead;
  @FXML private JFXButton previousButton;
  @FXML private JFXButton previous1Button;
  @FXML private JFXButton next1Button;
  @FXML private JFXButton previous2Button;
  @FXML private JFXButton next2Button;
  @FXML private JFXButton previous3Button;
  @FXML private JFXButton next3Button;
  @FXML private Label bubbleText;
  @FXML private Label bubble1Text;
  @FXML private Label bubble2Text;
  @FXML private Label bubble3Text;
  @FXML private Label bubble4Text; */

  double loadBackupTextSize;
  double saveBackupTextSize;
  double clientServerTextSize;
  double cloudTextSize;

  @FXML
  private void initialize() {

    configure();

    if (Adb.usingEmbedded) {
      toggleClientServerButton.setSelected(false);
    } else {
      toggleClientServerButton.setSelected(true);
    }

    loadBackupTextSize = loadBackupButton.getFont().getSize();
    saveBackupTextSize = saveBackupButton.getFont().getSize();
    clientServerTextSize = toggleClientServerButton.getFont().getSize();
    cloudTextSize = toggleClientServerButton.getFont().getSize();
/*
    double previousTextSize = previousButton.getFont().getSize();
    double nextTextSize = nextButton.getFont().getSize();
    double previous1TextSize = previous1Button.getFont().getSize();
    double next1TextSize = next1Button.getFont().getSize();
    double previous2TextSize = previous2Button.getFont().getSize();
    double next2TextSize = next2Button.getFont().getSize();
    double previous3TextSize = previous3Button.getFont().getSize();
    double next3TextSize = next3Button.getFont().getSize();
    double bubble1TextSize = bubble1Text.getFont().getSize();
    double bubble2TextSize = bubble2Text.getFont().getSize();
    double bubble3TextSize = bubble3Text.getFont().getSize();
    double bubble4TextSize = bubble4Text.getFont().getSize(); */

    updateSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateSize(); /*
              nextButton.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * nextTextSize)
                              + "pt;");
              previousButton.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * previousTextSize)
                              + "pt;");
              previous1Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * previous1TextSize)
                              + "pt;");
              next1Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * next1TextSize)
                              + "pt;");
              previous2Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * previous2TextSize)
                              + "pt;");
              next2Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * next2TextSize)
                              + "pt;");
              previous3Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * previous3TextSize)
                              + "pt;");
              next3Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * next3TextSize)
                              + "pt;");
              bubble1Text.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bubble1TextSize)
                              + "pt;");
              bubble2Text.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bubble2TextSize)
                              + "pt;");
              bubble3Text.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bubble3TextSize)
                              + "pt;");
              bubble4Text.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bubble4TextSize)
                              + "pt;"); */
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
/*
  public void activateBumble(){
    helpButton.setVisible(false);
    bumbleXButton.setVisible(true);
    bumbleHead.setVisible(true);
    bubbleText.setVisible(true);
    nextButton.setVisible(true);
  }

  public void terminateBumble(){
    helpButton.setVisible(true);
    bumbleXButton.setVisible(false);
    bumbleHead.setVisible(false);
    bubbleText.setVisible(false);
    bubble1Text.setVisible(false);
    bubble2Text.setVisible(false);
    bubble3Text.setVisible(false);
    bubble4Text.setVisible(false);
    previousButton.setVisible(false);
    nextButton.setVisible(false);
    previous1Button.setVisible(false);
    next1Button.setVisible(false);
    previous2Button.setVisible(false);
    next2Button.setVisible(false);
    previous3Button.setVisible(false);
    next3Button.setVisible(false);
  }

  public void next(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previousButton.setVisible(true);
    nextButton.setVisible(false);
    next1Button.setVisible(true);
    bubbleText.setVisible(false);
    bubble1Text.setVisible(true);
  }

  public void previous(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previousButton.setVisible(false);
    nextButton.setVisible(true);
    next1Button.setVisible(false);
    bubbleText.setVisible(true);
    bubble1Text.setVisible(false);
  }

  public void next1(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(true);
    next1Button.setVisible(false);
    next2Button.setVisible(true);
    bubble1Text.setVisible(false);
    bubble2Text.setVisible(true);
  }

  public void previous1() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(false);
    next1Button.setVisible(true);
    next2Button.setVisible(false);
    bubble1Text.setVisible(true);
    bubble2Text.setVisible(false);
  }

  public void next2(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(false);
    previous2Button.setVisible(true);
    next2Button.setVisible(false);
    next3Button.setVisible(true);
    bubble2Text.setVisible(false);
    bubble3Text.setVisible(true);
  }

  public void previous2() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(true);
    previous2Button.setVisible(false);
    next2Button.setVisible(true);
    next3Button.setVisible(false);
    bubble2Text.setVisible(true);
    bubble3Text.setVisible(false);
  }

  public void next3(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous2Button.setVisible(false);
    previous3Button.setVisible(true);
    next3Button.setVisible(false);
    bubble3Text.setVisible(false);
    bubble4Text.setVisible(true);
  }

  public void previous3() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous2Button.setVisible(true);
    previous3Button.setVisible(false);
    next3Button.setVisible(true);
    bubble3Text.setVisible(true);
    bubble4Text.setVisible(false);
  }
*/


}
