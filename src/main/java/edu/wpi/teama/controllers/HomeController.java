package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class HomeController {
  @FXML private Button settingsButton;
  @FXML private Button serviceRequestsButton;
  @FXML private Button equipmentTrackerButton;
  @FXML private Button locationDataButton;
  @FXML private Button exitButton;
  private FXMLLoader loader = new FXMLLoader();

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  private void goToCreateNewServiceRequest() throws IOException {
    sceneController.switchScene(SceneController.SCENES.SELECT_SERVICE_REQUEST_SCENE);
  }

  public void goTosettings(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene(SceneController.SCENES.SETTINGS_SCENE);
  }

  @FXML
  private void goToEquipmentTracker() throws IOException {
    sceneController.switchScene(SceneController.SCENES.VIEW_MEDICAL_EQUIPMENT_SCENE);
  }

  @FXML
  private void goToLocationData() throws IOException {
    sceneController.switchScene(SceneController.SCENES.VIEW_LOCATION_DATA_SCENE);
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
  private void exitApp() {
    System.exit(0);
  }
}
