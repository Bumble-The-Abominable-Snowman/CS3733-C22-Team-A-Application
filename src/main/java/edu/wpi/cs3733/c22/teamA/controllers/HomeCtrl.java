package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class HomeCtrl {
  @FXML private JFXButton settingsButton;
  @FXML private JFXButton serviceRequestsButton;
  @FXML private JFXButton viewServiceRequestsButton;
  @FXML private JFXButton equipmentTrackerButton;
  @FXML private JFXButton locationDataButton;
  @FXML private JFXButton mapEditorButton;
  @FXML private JFXButton employeesButton;
  @FXML private JFXButton exitButton;

  @FXML public JFXHamburger hamburger;
  @FXML public JFXDrawer drawer;
  @FXML public JFXButton backButton;
  @FXML public VBox menuBox;

  public static int sceneFlag = 0;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  private void initialize() {

    drawer.setSidePane(menuBox);
    HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(hamburger);
    burgerTask.setRate(-1);
    hamburger.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (e) -> {
          burgerTask.setRate(burgerTask.getRate() * -1);
          burgerTask.play();
          if (drawer.isOpened()) drawer.close();
          else drawer.open();
        });

    double settingsTextSize = settingsButton.getFont().getSize();
    double serviceRequestTextSize = serviceRequestsButton.getFont().getSize();
    double viewServiceRequestsTextSize = viewServiceRequestsButton.getFont().getSize();
    double equipmentTrackerTextSize = equipmentTrackerButton.getFont().getSize();
    double locationDataTextSize = locationDataButton.getFont().getSize();
    double mapEditorTextSize = mapEditorButton.getFont().getSize();
    double employeesTextSize = employeesButton.getFont().getSize();
    double exitTextSize = exitButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              settingsButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * settingsTextSize)
                      + "pt;");
              serviceRequestsButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * serviceRequestTextSize)
                      + "pt;");
              viewServiceRequestsButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * viewServiceRequestsTextSize)
                      + "pt;");
              equipmentTrackerButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * equipmentTrackerTextSize)
                      + "pt;");
              locationDataButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * locationDataTextSize)
                      + "pt;");
              mapEditorButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * mapEditorTextSize)
                      + "pt;");
              employeesButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * employeesTextSize)
                      + "pt;");
              exitButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * exitTextSize) + "pt;");
            });
  }

  @FXML
  private void goToCreateNewServiceRequest() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SELECT_SERVICE_REQUEST);
  }

  @FXML
  private void goToSettings() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SETTINGS);
  }

  @FXML
  private void goToEquipmentTracker() throws IOException {
    sceneFlag = 3;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToLocationData() throws IOException {
    sceneFlag = 2;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToEditLocationData() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.MAP);
  }

  @FXML
  private void goToViewServiceRequest() throws IOException {
    sceneFlag = 1;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  public void goToViewEmployees() throws IOException {
    sceneFlag = 4;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void exitHome() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
  }

  @FXML
  private void exitApp() {
    System.exit(0);
  }
}
