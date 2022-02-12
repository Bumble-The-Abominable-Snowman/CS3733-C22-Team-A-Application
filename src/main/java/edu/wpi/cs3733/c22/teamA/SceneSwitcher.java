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
    LOAD_BACKUP,
    SAVE_BACKUP,
    MAP
  }

  private final HashMap<SCENES, String> screenMap = new HashMap<>();

  public SceneSwitcher() {

    // Home + Settings
    addScene(SceneSwitcher.SCENES.LOGIN, "views/Login.fxml");
    addScene(SceneSwitcher.SCENES.HOME, "views/Home.fxml");
    addScene(SCENES.SETTINGS_SCENE, "views/settings/Settings.fxml");
    addScene(SCENES.LOAD_BACKUP, "views/Settings/LoadBackup.fxml");
    addScene(SCENES.SAVE_BACKUP, "views/Settings/SaveBackup.fxml");

    // Service Requests
    addScene(SCENES.SELECT_SERVICE_REQUEST, "views/servicerequest/SelectServiceRequest.fxml");
    addScene(SCENES.LAUNDRY_SR, "views/servicerequest/LaundrySR.fxml");
    addScene(SCENES.FOOD_DELIVERY_SR, "views/servicerequest/FoodDeliverySR.fxml");
    addScene(SCENES.LANGUAGE_SR, "views/servicerequest/LanguageSR.fxml");
    addScene(SCENES.EQUIPMENT_DELIVERY_SR, "views/servicerequest/EquipmentDeliverySR.fxml");
    addScene(SCENES.RELIGIOUS_SR, "views/servicerequest/ReligiousSR.fxml");
    addScene(SCENES.SANITATION_SR, "views/servicerequest/SanitationSR.fxml");
    addScene(SCENES.FLORAL_DELIVERY_SR, "views/servicerequest/FloralDeliverySR.fxml");
    addScene(SCENES.MEDICINE_DELIVERY_SR, "views/servicerequest/MedicineDeliverySR.fxml");

    // Data View + Map Editor
    addScene(SCENES.VIEW_EQUIPMENT, "views/dataview/medicalEquipmentData.fxml");
    addScene(SCENES.VIEW_LOCATIONS, "views/dataview/locationData.fxml");
    addScene(SCENES.VIEW_SERVICE_REQUESTS, "views/dataview/viewServiceRequest.fxml");
    addScene(SCENES.VIEW_EMPLOYEES, "views/dataview/employeeData.fxml");
    addScene(SCENES.MAP, "views/map/mapEditor.fxml");
  }

  public void addScene(SCENES name, String pathToFXML) {
    screenMap.put(name, pathToFXML);
  }

  public void switchScene(SCENES name) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = App.class.getResource(screenMap.get(name));
    loader.setLocation(xmlUrl);

    Parent root = loader.load();
    App.getStage().setScene(new Scene(root));
    App.getStage().show();
  }
}
