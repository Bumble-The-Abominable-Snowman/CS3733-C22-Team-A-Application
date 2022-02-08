package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.Aapp;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class HomeController {
  @FXML private JFXButton settingsButton;
  @FXML private JFXButton serviceRequestsButton;
  @FXML private JFXButton viewServiceRequestsButton;
  @FXML private JFXButton equipmentTrackerButton;
  @FXML private JFXButton locationDataButton;
  @FXML private JFXButton exitButton;
  private FXMLLoader loader = new FXMLLoader();

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  private void goToCreateNewServiceRequest() throws IOException {
    sceneController.switchScene(SceneController.SCENES.SELECT_SERVICE_REQUEST_SCENE);
  }

  public void goToSettings(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene(SceneController.SCENES.SETTINGS_SCENE);
  }

  @FXML
  private void goToEquipmentTracker() throws IOException {
    sceneController.switchScene(SceneController.SCENES.VIEW_MEDICAL_EQUIPMENT_SCENE);
  }

  @FXML
  private void goToLocationData() throws IOException {
    sceneController.switchScene(SceneController.SCENES.MAP_EDITOR_SCENE);
  }

  @FXML
  private void goToEditLocationData() throws IOException {
    sceneController.switchScene(SceneController.SCENES.VIEW_EDIT_LOCATION_DATA);
  }

  @FXML
  private void goToViewServiceRequest() throws IOException {
    sceneController.switchScene(SceneController.SCENES.VIEW_SERVICE_REQUEST_SCENE);
  }

  @FXML
  public void goToViewEmployees(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene(SceneController.SCENES.VIEW_EMPLOYEES_SCENE);
  }

  @FXML
  private void exitHome() throws IOException {
    sceneController.switchScene(SceneController.SCENES.LOG_IN_SCENE);
  }

  public void exitApp(ActionEvent actionEvent) {}
}
