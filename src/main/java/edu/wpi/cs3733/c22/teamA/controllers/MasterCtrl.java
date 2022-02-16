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

public abstract class MasterCtrl {

  @FXML public JFXHamburger hamburger;
  @FXML public JFXDrawer drawer;
  @FXML public JFXButton backButton;
  @FXML public VBox menuBox;

  @FXML public JFXButton selectSRButton;
  @FXML public JFXButton mapButton;
  @FXML public JFXButton viewSRButton;
  @FXML public JFXButton viewEmployeesButton;
  @FXML public JFXButton viewLocationsButton;
  @FXML public JFXButton viewEquipmentButton;
  @FXML public JFXButton settingsButton;
  @FXML public JFXButton exitButton;
  @FXML public JFXButton loginButton;
  @FXML public JFXButton homeButton;

  public final SceneSwitcher sceneSwitcher = App.sceneSwitcher;
  public static int sceneFlag = 0;

  double selectSRButtonSize;
  double mapButtonSize;
  double viewSRButtonSize;
  double viewEmployeesButtonSize;
  double viewLocationsButtonSize;
  double viewEquipmentButtonSize;
  double settingsButtonSize;
  double exitButtonSize;
  double loginButtonSize;
  double backButtonSize;
  double homeButtonSize;

  double stageWidth;
  double stageHeight;

  public void configure() {

    selectSRButtonSize = selectSRButton.getFont().getSize();
    mapButtonSize = mapButton.getFont().getSize();
    viewSRButtonSize = viewSRButton.getFont().getSize();
    viewEmployeesButtonSize = viewEmployeesButton.getFont().getSize();
    viewLocationsButtonSize = viewLocationsButton.getFont().getSize();
    viewEquipmentButtonSize = viewEquipmentButton.getFont().getSize();
    settingsButtonSize = settingsButton.getFont().getSize();
    exitButtonSize = exitButton.getFont().getSize();
    loginButtonSize = loginButton.getFont().getSize();
    backButtonSize = backButton.getFont().getSize();
    homeButtonSize = homeButton.getFont().getSize();

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

    stageWidth = App.getStage().getWidth();
    stageHeight = App.getStage().getHeight();
    updateSize();
    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              stageWidth = App.getStage().getWidth();
              stageHeight = App.getStage().getHeight();
              updateSize();
            });
  }

  private void updateSize() {

    selectSRButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * selectSRButtonSize) + "pt;");
    mapButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * mapButtonSize) + "pt;");
    viewSRButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * viewSRButtonSize) + "pt;");
    viewEmployeesButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * viewEmployeesButtonSize) + "pt;");
    viewEquipmentButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * viewEquipmentButtonSize) + "pt;");
    settingsButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * settingsButtonSize) + "pt;");
    viewLocationsButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * viewLocationsButtonSize) + "pt;");
    exitButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * exitButtonSize) + "pt;");
    loginButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * loginButtonSize) + "pt;");
    backButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * backButtonSize) + "pt;");
    homeButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * homeButtonSize) + "pt;");
  }

  @FXML
  private void goToSelectServiceRequest() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SELECT_SERVICE_REQUEST);
  }

  @FXML
  private void goToSettings() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SETTINGS);
  }

  @FXML
  private void goToServiceRequestData() throws IOException {
    sceneFlag = 1;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToLocationData() throws IOException {
    sceneFlag = 2;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToEquipmentData() throws IOException {
    sceneFlag = 3;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  public void goToEmployeeData() throws IOException {
    sceneFlag = 4;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToMap() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.MAP);
  }

  @FXML
  private void goToLogin() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
  }

  @FXML
  private void exitApp() {
    System.exit(0);
  }

  @FXML
  private void back() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  @FXML
  private void goToHome() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }
}
