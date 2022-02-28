package edu.wpi.cs3733.c22.teamA;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wpi.cs3733.c22.teamA.auth0.rest.login.Auth0Login;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneSwitcher {

  public static List<SCENES> fxmlval = new ArrayList<SCENES>();

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
    GIFT_DELIVERY_SR,
    SECURITY_SR,
    CONSULTATION_SR,
    DATA_VIEW,
    SETTINGS,
    LOAD_BACKUP,
    SAVE_BACKUP,
    MAP,
    ABOUT;
  }

  private final HashMap<SCENES, String> screenMap = new HashMap<>();
  public static Scene currentScene;

  public SceneSwitcher() {

    addScene(SceneSwitcher.SCENES.LOGIN, "views/Login.fxml");
    addScene(SceneSwitcher.SCENES.HOME, "views/Home.fxml");
    addScene(SCENES.SETTINGS, "views/settings/Settings.fxml");
    addScene(SCENES.LOAD_BACKUP, "views/settings/LoadBackup.fxml");
    addScene(SCENES.SAVE_BACKUP, "views/settings/SaveBackup.fxml");
    addScene(SCENES.ABOUT, "views/About.fxml");
    addScene(SCENES.SELECT_SERVICE_REQUEST, "views/servicerequest/SelectServiceRequest.fxml");
    addScene(SCENES.LAUNDRY_SR, "views/servicerequest/LaundrySR.fxml");
    addScene(SCENES.FOOD_DELIVERY_SR, "views/servicerequest/FoodDeliverySR.fxml");
    addScene(SCENES.LANGUAGE_SR, "views/servicerequest/LanguageSR.fxml");
    addScene(SCENES.EQUIPMENT_DELIVERY_SR, "views/servicerequest/EquipmentDeliverySR.fxml");
    addScene(SCENES.RELIGIOUS_SR, "views/servicerequest/ReligiousSR.fxml");
    addScene(SCENES.SANITATION_SR, "views/servicerequest/SanitationSR.fxml");
    addScene(SCENES.FLORAL_DELIVERY_SR, "views/servicerequest/FloralDeliverySR.fxml");
    addScene(SCENES.MEDICINE_DELIVERY_SR, "views/servicerequest/MedicineDeliverySR.fxml");
    addScene(SCENES.CONSULTATION_SR, "views/servicerequest/ConsultationSR.fxml");
    addScene(SCENES.GIFT_DELIVERY_SR, "views/servicerequest/GiftDeliverySR.fxml");
    addScene(SCENES.MAINTENANCE_SR, "views/servicerequest/MaintenanceSR.fxml");
    addScene(SCENES.SECURITY_SR, "views/servicerequest/SecuritySR.fxml");
    addScene(SCENES.DATA_VIEW, "views/DataView.fxml");
    addScene(SCENES.MAP, "views/Map.fxml");
  }

  public void addScene(SCENES name, String pathToFXML) {
    screenMap.put(name, pathToFXML);
  }

  public void switchScene(SCENES name) throws IOException {

    if (name == SCENES.LOGIN)
    {
      Auth0Login.login();
      return;
    }

    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = App.class.getResource(screenMap.get(name));
    loader.setLocation(xmlUrl);

    Parent root = loader.load();
    if (currentScene == null) currentScene = new Scene(root);
    else currentScene.setRoot(root);
    App.getStage().setScene(currentScene);
    App.getStage().show();
    fxmlval.add(name);

  }
}
