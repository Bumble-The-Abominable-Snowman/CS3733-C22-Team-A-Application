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

  @FXML public JFXButton newSRButton;
  @FXML public JFXButton mapButton;
  @FXML public JFXButton viewSRButton;
  @FXML public JFXButton viewEmployeesButton;
  @FXML public JFXButton viewLocationsButton;
  @FXML public JFXButton viewEquipmentButton;
  @FXML public JFXButton settingsButton;
  @FXML public JFXButton exitButton;
  @FXML public JFXButton logOutButton;

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

    double newSRButtonTextSize = newSRButton.getFont().getSize();
    double mapButtonTextSize = mapButton.getFont().getSize();
    double viewSRButtonTextSize = viewSRButton.getFont().getSize();
    double viewEmployeesButtonTextSize = viewEmployeesButton.getFont().getSize();
    double viewLocationsButtonTextSize = viewLocationsButton.getFont().getSize();
    double viewEquipmentButtonTextSize = viewEquipmentButton.getFont().getSize();
    double settingsButtonTextSize = settingsButton.getFont().getSize();
    double exitButtonTextSize = exitButton.getFont().getSize();
    double logOutButtonTextSize = logOutButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              newSRButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * newSRButtonTextSize)
                      + "pt;");
              mapButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * mapButtonTextSize)
                      + "pt;");
              viewSRButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * viewSRButtonTextSize)
                      + "pt;");
              viewEmployeesButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * viewEmployeesButtonTextSize)
                      + "pt;");
              viewEquipmentButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * viewEquipmentButtonTextSize)
                      + "pt;");
              settingsButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * settingsButtonTextSize)
                      + "pt;");
              viewLocationsButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * viewLocationsButtonTextSize)
                      + "pt;");
              exitButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * exitButtonTextSize)
                      + "pt;");
              logOutButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * logOutButtonTextSize)
                      + "pt;");
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
