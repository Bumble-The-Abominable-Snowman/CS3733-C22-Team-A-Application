package edu.wpi.cs3733.c22.teamA;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneSwitcher {
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
    FLORAL_DELIVERY_SERVICE_REQUEST_SCENE,
    MEDICINE_DELIVERY_SERVICE_REQUEST_SCENE,
    VIEW_MEDICAL_EQUIPMENT_SCENE,
    VIEW_LOCATION_DATA_SCENE,
    VIEW_SERVICE_REQUEST_SCENE,
    VIEW_EMPLOYEES_SCENE,
    SETTINGS_SCENE,
    LOAD_FROM_BACKUP_SCENE,
    EXPORT_TO_BACKUP_SCENE,
    MAP_EDITOR_SCENE
  }

  private final HashMap<SCENES, String> screenMap = new HashMap<>();

  public SceneSwitcher() {

    // Home + Settings
    addScene(SceneSwitcher.SCENES.LOG_IN_SCENE, "views/logIn.fxml");
    addScene(SceneSwitcher.SCENES.HOME_SCENE, "views/home.fxml");
    addScene(SCENES.SETTINGS_SCENE, "views/settings/settings.fxml");
    addScene(SCENES.LOAD_FROM_BACKUP_SCENE, "views/Settings/loadFromBackup.fxml");
    addScene(SCENES.EXPORT_TO_BACKUP_SCENE, "views/Settings/exportToBackup.fxml");

    // Service Requests
    addScene(SCENES.SELECT_SERVICE_REQUEST_SCENE, "views/servicerequest/selectServiceRequest.fxml");
    addScene(SCENES.LAUNDRY_SERVICE_REQUEST_SCENE, "views/servicerequest/laundryServices.fxml");
    addScene(SCENES.FOOD_DELIVERY_SERVICE_REQUEST_SCENE, "views/servicerequest/foodDelivery.fxml");
    addScene(
        SCENES.LANGUAGE_INTERPRETER_SERVICE_REQUEST_SCENE,
        "views/servicerequest/languageInterpreter.fxml");
    addScene(
        SCENES.MEDICAL_EQUIPMENT_DELIVERY_SERVICE_REQUEST_SCENE,
        "views/servicerequest/medicalEquipmentDelivery.fxml");
    addScene(SCENES.RELIGIOUS_SERVICE_REQUEST_SCENE, "views/servicerequest/religiousServices.fxml");
    addScene(
        SCENES.SANITATION_SERVICE_REQUEST_SCENE, "views/servicerequest/sanitationServices.fxml");
    addScene(
        SCENES.FLORAL_DELIVERY_SERVICE_REQUEST_SCENE, "views/servicerequest/floralDelivery.fxml");
    addScene(
        SCENES.MEDICINE_DELIVERY_SERVICE_REQUEST_SCENE,
        "views/servicerequest/medicineDelivery.fxml");

    // Data View + Map Editor
    addScene(SCENES.VIEW_MEDICAL_EQUIPMENT_SCENE, "views/dataview/medicalEquipmentData.fxml");
    addScene(SCENES.VIEW_LOCATION_DATA_SCENE, "views/dataview/locationData.fxml");
    addScene(SCENES.VIEW_SERVICE_REQUEST_SCENE, "views/dataview/viewServiceRequest.fxml");
    addScene(SCENES.VIEW_EMPLOYEES_SCENE, "views/dataview/employeeData.fxml");
    addScene(SCENES.MAP_EDITOR_SCENE, "views/map/mapEditor.fxml");
  }

  public void addScene(SCENES name, String pathToFXML) {
    screenMap.put(name, pathToFXML);
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
