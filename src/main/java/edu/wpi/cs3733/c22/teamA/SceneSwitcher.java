package edu.wpi.cs3733.c22.teamA;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneSwitcher {
  public enum SCENES {
    LOGIN,
    HOME,
    SELECT_SERVICE_REQUEST,
    LAUNDRY_SR,
    FOOD_DELIVERY_SR,
    LANGUAGE_SR,
    EQUIPMENT_DELIVERY_SR,
    RELIGIOUS_SR,
    SANITATION_SR,
    FLORAL_DELIVERY_SR,
    MEDICINE_DELIVERY_SR,
    MAINTENANCE_SR,
    GIFT_SR,
    SECURITY_SR,
    DATA_VIEW,
    SETTINGS,
    LOAD_BACKUP,
    SAVE_BACKUP,
    MAP
  }

  private final HashMap<SCENES, String> screenMap = new HashMap<>();
  private Scene currentScene;

  public SceneSwitcher() {

    // Home + Settings
    addScene(SceneSwitcher.SCENES.LOGIN, "views/Login.fxml");
    addScene(SceneSwitcher.SCENES.HOME, "views/Home.fxml");
    addScene(SCENES.SETTINGS, "views/settings/Settings.fxml");
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
    addScene(SCENES.DATA_VIEW, "views/dataview/dataView.fxml");
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
    if (currentScene == null) currentScene = new Scene(root);
    else currentScene.setRoot(root);
    App.getStage().setScene(currentScene);
    App.getStage().show();
  }
}
