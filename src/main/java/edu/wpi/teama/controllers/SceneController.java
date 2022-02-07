package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneController {
  public enum SCENES {
    LOG_IN_SCENE,
    HOME_SCENE,
    SELECT_SERVICE_REQUEST_SCENE,
    LAUNDRY_SERVICE_REQUEST_SCENE,
    FOOD_DELIVERY_SERVICE_REQUEST_SCENE,
    LANGUAGE_INTERPRETER_SERVICE_REQUEST_SCENE,
    MEDICAL_EQUIPMENT_DELIVERY_SERVICE_REQUEST_SCENE,
    RELIGIOUS_SERVICE_REQUEST_SCENE,
    SANITATION_SERVICE_REQUEST_SCENE,
    VIEW_MEDICAL_EQUIPMENT_SCENE,
    VIEW_LOCATION_DATA_SCENE,
    VIEW_SERVICE_REQUEST_SCENE,
    VIEW_EMPLOYEES_SCENE,
    SETTINGS_SCENE,
    LOAD_FROM_BACKUP_SCENE,
    EXPORT_TO_BACKUP_SCENE,
  }

  private final HashMap<SCENES, String> screenMap = new HashMap<>();

  public SceneController() {

    addScene(SceneController.SCENES.LOG_IN_SCENE, "views/logIn.fxml");

    addScene(SceneController.SCENES.HOME_SCENE, "views/home.fxml");

    addScene(
        SCENES.SELECT_SERVICE_REQUEST_SCENE, "views/ServiceRequestViews/selectServiceRequest.fxml");
    addScene(
        SCENES.LAUNDRY_SERVICE_REQUEST_SCENE, "views/ServiceRequestViews/laundryServices.fxml");
    addScene(
        SCENES.FOOD_DELIVERY_SERVICE_REQUEST_SCENE, "views/ServiceRequestViews/foodDelivery.fxml");
    addScene(
        SCENES.LANGUAGE_INTERPRETER_SERVICE_REQUEST_SCENE,
        "views/ServiceRequestViews/languageInterpreter.fxml");
    addScene(
        SCENES.MEDICAL_EQUIPMENT_DELIVERY_SERVICE_REQUEST_SCENE,
        "views/ServiceRequestViews/medicalEquipmentDelivery.fxml");
    addScene(
        SCENES.RELIGIOUS_SERVICE_REQUEST_SCENE, "views/ServiceRequestViews/religiousServices.fxml");
    addScene(
        SCENES.SANITATION_SERVICE_REQUEST_SCENE,
        "views/ServiceRequestViews/sanitationServices.fxml");

    addScene(SCENES.VIEW_MEDICAL_EQUIPMENT_SCENE, "views/DataViewViews/medicalEquipmentData.fxml");
    addScene(SCENES.VIEW_LOCATION_DATA_SCENE, "views/DataViewViews/locationData.fxml");
    addScene(SCENES.VIEW_SERVICE_REQUEST_SCENE, "views/DataViewViews/viewServiceRequest.fxml");
    addScene(SCENES.VIEW_EMPLOYEES_SCENE, "views/DataViewViews/employeeData.fxml");

    addScene(SCENES.SETTINGS_SCENE, "views/SettingsViews/settings.fxml");
    addScene(SCENES.LOAD_FROM_BACKUP_SCENE, "views/SettingsViews/loadFromBackup.fxml");
    addScene(SCENES.EXPORT_TO_BACKUP_SCENE, "views/SettingsViews/exportToBackup.fxml");
  }

  public void addScene(SCENES name, String pathToFXML) {
    screenMap.put(name, pathToFXML);
  }

  public void removeScreen(String name) {
    screenMap.remove(name);
  }

  public void switchScene(SCENES name) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource(screenMap.get(name));
    loader.setLocation(xmlUrl);

    Parent root = loader.load();
    Aapp.getStage().setScene(new Scene(root));
    Aapp.getStage().show();
  }
}
