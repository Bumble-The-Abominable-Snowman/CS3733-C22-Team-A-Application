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
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public abstract class MasterCtrl {

  @FXML public Label titleLabel;
  @FXML public GridPane gridPane;

  @FXML public JFXHamburger hamburger;
  @FXML public JFXButton backButton;
  @FXML public JFXButton helpButton;
  @FXML public JFXButton nextButton;
  @FXML public JFXDrawer drawer;
  @FXML public VBox menuBox;
  @FXML public TextArea helpText;

  @FXML public JFXButton selectSRButton;
  @FXML public JFXButton mapButton;
  @FXML public JFXButton viewSRButton;
  @FXML public JFXButton viewEmployeesButton;
  @FXML public JFXButton viewLocationsButton;
  @FXML public JFXButton viewEquipmentButton;
  @FXML public JFXButton viewMedicineButton;
  @FXML public JFXButton settingsButton;
  @FXML public JFXButton exitButton;
  @FXML public JFXButton loginButton;
  @FXML public JFXButton aboutButton;
  @FXML public JFXButton homeButton;

  @FXML public JFXButton bumbleXButton;
  @FXML public Label bubbleText;
  @FXML public ImageView bumbleHead;

  public final static SceneSwitcher sceneSwitcher = App.sceneSwitcher;
  @FXML public ImageView newSRIcon;
  @FXML public ImageView mapIcon;
  @FXML public ImageView viewSRIcon;
  @FXML public ImageView viewEmployeesIcon;
  @FXML public ImageView viewLocationsIcon;
  @FXML public ImageView viewEquipmentIcon;
  @FXML public ImageView viewMedicineIcon;
  @FXML public ImageView settingsIcon;
  @FXML public ImageView exitIcon;
  @FXML public ImageView loginIcon;
  @FXML public ImageView aboutIcon;
  @FXML public ImageView homeIcon;

  double homeSize;
  double titleSize;
  double nextSize;
  double helpTextSize;
  public DropShadow borderGlow = new DropShadow();

  public static int sceneFlag = 0;
  public static List<Integer> sceneFlags = new ArrayList<Integer>();

  public int helpState;

  public enum ACCOUNT {
    ADMIN,
    STAFF,
  }

  public static ACCOUNT account = ACCOUNT.ADMIN;

  double stageWidth;
  double stageHeight;

  boolean animating = false;

  public void configure() {

    if (Adb.username.equals("admin")) {
      account = ACCOUNT.ADMIN;
    } else if (Adb.username.equals("staff")) {
      account = ACCOUNT.STAFF;
    }

    if (account == ACCOUNT.STAFF) {  //Remove some permissions
      if (account == ACCOUNT.STAFF) {

        menuBox.getChildren().remove(0);

      }

      nextButton.setVisible(false);
      helpText.setVisible(false);
      homeSize = homeButton.getFont().getSize();
      titleSize = titleLabel.getFont().getSize();
      nextSize = homeButton.getFont().getSize();

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
      App.getStage().widthProperty().addListener((obs, oldVal, newVal) -> {
        updateSize();
      });
      handleIconPulses();

    }

  }

  private void updateSize() {

    stageWidth = App.getStage().getWidth();
    stageHeight = App.getStage().getHeight();

    titleLabel.setStyle("-fx-font-size: " + ((stageWidth / 1000) * titleSize) + "pt;");
    setStyle(selectSRButton);
    setStyle(mapButton);
    setStyle(viewSRButton);
    setStyle(viewEmployeesButton);
    setStyle(viewEquipmentButton);
    setStyle(viewLocationsButton);
    setStyle(viewMedicineButton);
    setStyle(settingsButton);
    setStyle(exitButton);
    setStyle(loginButton);
    setStyle(aboutButton);
    setStyle(homeButton);
    setStyle(nextButton);

  }

  private void setStyle(JFXButton thisButton) {
    thisButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * homeSize) + "pt;");
  }

  protected void onSceneSwitch() {}

  @FXML
  private void goToHome() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  @FXML
  private void goToSelectServiceRequest() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SELECT_SERVICE_REQUEST);
  }

  @FXML
  private void goToMap() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.MAP);
  }

  @FXML
  private void goToServiceRequestData() throws IOException {
    sceneFlag = 1;
    sceneFlags.add(sceneFlag);
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToLocationData() throws IOException {
    sceneFlag = 2;
    sceneFlags.add(sceneFlag);
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToEquipmentData() throws IOException {
    sceneFlag = 3;
    sceneFlags.add(sceneFlag);
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  public void goToEmployeeData() throws IOException {
    sceneFlag = 4;
    sceneFlags.add(sceneFlag);
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void goToSettings() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SETTINGS);
  }

  @FXML
  private void goToAbout() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.ABOUT);
  }

  @FXML
  private void goToLogin() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
  }

  @FXML
  private void goToMedicineData() throws IOException {
    sceneFlag = 5;
    sceneFlags.add(sceneFlag);
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  private void exitApp() {
    System.exit(0);
  }

  @FXML
  private void back() throws IOException {

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
  private void handleIconPulses() {

    homeButton.setOnMouseEntered(homeEnter -> {homeIcon.setFitHeight(40);});
    homeButton.setOnMouseExited(homeExit -> {homeIcon.setFitHeight(26);});

    selectSRButton.setOnMouseEntered(selectSREnter -> {newSRIcon.setFitHeight(40);});
    selectSRButton.setOnMouseExited(selectSRExit -> {newSRIcon.setFitHeight(26);});

    mapButton.setOnMouseEntered(mapEnter -> {mapIcon.setFitHeight(40);});
    mapButton.setOnMouseExited(mapExit -> {mapIcon.setFitHeight(26);});

    viewSRButton.setOnMouseEntered(mouseEnter -> {viewSRIcon.setFitHeight(40);});
    viewSRButton.setOnMouseExited(mouseExit -> {viewSRIcon.setFitHeight(26);});

    viewEmployeesButton.setOnMouseEntered(mouseEnter -> {viewEmployeesIcon.setFitHeight(40);});
    viewEmployeesButton.setOnMouseExited(mouseExit -> {viewEmployeesIcon.setFitHeight(26);});

    viewLocationsButton.setOnMouseEntered(mouseEnter -> {viewLocationsIcon.setFitHeight(40);});
    viewLocationsButton.setOnMouseExited(mouseExit -> {viewLocationsIcon.setFitHeight(26);});

    viewEquipmentButton.setOnMouseEntered(mouseEnter -> {viewEquipmentIcon.setFitHeight(40);});
    viewEquipmentButton.setOnMouseExited(mouseExit -> {viewEquipmentIcon.setFitHeight(26);});

    viewMedicineButton.setOnMouseEntered(mouseEnter -> {viewMedicineIcon.setFitHeight(40);});
    viewMedicineButton.setOnMouseExited(mouseExit -> {viewMedicineIcon.setFitHeight(26);});

    settingsButton.setOnMouseEntered(mouseEnter -> {settingsIcon.setFitHeight(40);});
    settingsButton.setOnMouseExited(mouseExit -> {settingsIcon.setFitHeight(26);});

    aboutButton.setOnMouseEntered(mouseEnter -> {aboutIcon.setFitHeight(40);});
    aboutButton.setOnMouseExited(mouseExit -> {aboutIcon.setFitHeight(26);});

    loginButton.setOnMouseEntered(mouseEnter -> {loginIcon.setFitHeight(40);});
    loginButton.setOnMouseExited(mouseExit -> {loginIcon.setFitHeight(26);});

    exitButton.setOnMouseEntered(mouseEnter -> {exitIcon.setFitHeight(40);});
    exitButton.setOnMouseExited(mouseExit -> {exitIcon.setFitHeight(26);});

  }

}