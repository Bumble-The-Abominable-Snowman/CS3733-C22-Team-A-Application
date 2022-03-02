package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
  @FXML public JFXButton aboutButton;
  @FXML public JFXButton homeButton;

  @FXML public JFXButton bumbleXButton;
  @FXML public Label bubbleText;
  @FXML public ImageView bumbleHead;

  public final static SceneSwitcher sceneSwitcher = App.sceneSwitcher;
  public static int sceneFlag = 0;
  public static List<Integer> sceneFlags = new ArrayList<Integer>();

  public enum ACCOUNT {
    ADMIN,
    STAFF,
  }

  public static ACCOUNT account = ACCOUNT.ADMIN;

  double selectSRButtonSize;
  double mapButtonSize;
  double viewSRButtonSize;
  double viewEmployeesButtonSize;
  double viewLocationsButtonSize;
  double viewEquipmentButtonSize;
  double settingsButtonSize;
  double exitButtonSize;
  double loginButtonSize;
  double homeButtonSize;
  double aboutButtonSize;
  double titleTextSize;

  double stageWidth;
  double stageHeight;

  boolean animating = false;

  public void configure() {

    if (account == ACCOUNT.STAFF) {

      viewEmployeesButton.setStyle("-fx-background-color: #808080");
      viewEmployeesButton.setDisable(true);
      settingsButton.setStyle("-fx-background-color: #808080");
      settingsButton.setDisable(true);

    }

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
    aboutButtonSize = aboutButton.getFont().getSize();
    titleTextSize = titleLabel.getFont().getSize();

    double bumbleXTextSize = bumbleXButton.getFont().getSize();
    double bubbleTextSize = bubbleText.getFont().getSize();

    drawer.setSidePane(menuBox);
    drawer.setOnDrawerClosed(e -> animating = false);
    drawer.setOnDrawerOpened(e -> animating = false);
    HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(hamburger);
    burgerTask.setRate(-1);

    drawer.toBack();
    menuBox.toBack();
    hamburger.addEventHandler(
        MouseEvent.MOUSE_PRESSED,
        (e) -> {
          if (!animating) {
            animating = true;
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
          }
        });

    updateSize();


    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateSize();
              bumbleXButton.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bumbleXTextSize)
                              + "pt;");
              bubbleText.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bubbleTextSize)
                              + "pt;");
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
    aboutButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * aboutButtonSize) + "pt;");
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
    sceneFlags.add(sceneFlag);
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);

  }

  @FXML
  private void goToLocationData() throws IOException {

    this.onSceneSwitch();
    sceneFlag = 2;
    sceneFlags.add(sceneFlag);
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToEquipmentData() throws IOException {

    this.onSceneSwitch();
    sceneFlag = 3;
    sceneFlags.add(sceneFlag);
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  public void goToEmployeeData() throws IOException {

    this.onSceneSwitch();
    sceneFlag = 4;
    sceneFlags.add(sceneFlag);
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToSettings() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SETTINGS);
  }

  @FXML
  private void goToAbout() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.ABOUT);
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
    sceneSwitcher.fxmlval.remove(sceneSwitcher.fxmlval.size() - 1);
    SceneSwitcher.SCENES lastScene = sceneSwitcher.fxmlval.get(sceneSwitcher.fxmlval.size() - 1);
    if (lastScene == SceneSwitcher.SCENES.DATA_VIEW) {
      sceneFlags.remove(sceneFlags.size() - 1);
      sceneFlag = sceneFlags.get(sceneFlags.size() - 1);
    }
    sceneSwitcher.switchScene(lastScene);
    sceneSwitcher.fxmlval.remove(sceneSwitcher.fxmlval.size() - 1);
  }

  @FXML
  private void help() throws IOException {

  }

}