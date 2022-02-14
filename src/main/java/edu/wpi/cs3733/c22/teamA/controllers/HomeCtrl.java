package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import javafx.fxml.FXML;

// TODO Add exit button to quit
public class HomeCtrl {
  @FXML private JFXButton settingsButton;
  @FXML private JFXButton serviceRequestsButton;
  @FXML private JFXButton viewServiceRequestsButton;
  @FXML private JFXButton equipmentTrackerButton;
  @FXML private JFXButton locationDataButton;
  @FXML private JFXButton mapEditorButton;
  @FXML private JFXButton employeesButton;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  private void goToCreateNewServiceRequest() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SELECT_SERVICE_REQUEST);
  }

  @FXML
  private void goToSettings() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SETTINGS_SCENE);
  }

  @FXML
  private void goToEquipmentTracker() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.VIEW_EQUIPMENT);
  }

  @FXML
  private void goToLocationData() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.VIEW_LOCATIONS);
  }

  @FXML
  private void goToEditLocationData() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.MAP);
  }

  @FXML
  private void goToViewServiceRequest() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.VIEW_SERVICE_REQUESTS);
  }

  @FXML
  public void goToViewEmployees() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.VIEW_EMPLOYEES);
  }

  @FXML
  private void exitHome() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
  }
}
