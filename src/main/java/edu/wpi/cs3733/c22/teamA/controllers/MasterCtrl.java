package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public abstract class MasterCtrl {

  @FXML public Label titleLabel;

  @FXML public JFXHamburger hamburger;
  @FXML public JFXButton backButton;
  @FXML public JFXButton helpButton;
  @FXML public JFXDrawer drawer;
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
  double titleTextSize;

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
    homeButtonSize = homeButton.getFont().getSize();
    titleTextSize = titleLabel.getFont().getSize();

    drawer.setSidePane(menuBox);
    HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(hamburger);
    burgerTask.setRate(-1);

    drawer.toBack();
    menuBox.toBack();

    hamburger.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (e) -> {
          burgerTask.setRate(burgerTask.getRate() * -1);
          burgerTask.play();
          if (drawer.isOpened()) {
            drawer.close();
            drawer.toBack();
            menuBox.toBack();
          } else {
            drawer.open();
            drawer.toFront();
            menuBox.toFront();
          }
        });

    updateSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateSize();
            });
  }

  private void updateSize() {

    stageWidth = App.getStage().getWidth();
    stageHeight = App.getStage().getHeight();

    titleLabel.setStyle("-fx-font-size: " + ((stageWidth / 1000) * titleTextSize) + "pt;");
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

  protected void onSceneSwitch() {}

  @FXML
  private void goToHome() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  @FXML
  private void goToSelectServiceRequest() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SELECT_SERVICE_REQUEST);
  }

  @FXML
  private void goToMap() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.MAP);
  }

  @FXML
  private void goToServiceRequestData() throws IOException {

    this.onSceneSwitch();
    sceneFlag = 1;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToLocationData() throws IOException {

    this.onSceneSwitch();
    sceneFlag = 2;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToEquipmentData() throws IOException {

    this.onSceneSwitch();
    sceneFlag = 3;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  public void goToEmployeeData() throws IOException {

    this.onSceneSwitch();
    sceneFlag = 4;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToSettings() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SETTINGS);
  }

  @FXML
  private void goToLogin() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
  }

  @FXML
  private void exitApp() {

    System.exit(0);
  }

  @FXML
  private void back() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  @FXML
  private void help() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }
}
