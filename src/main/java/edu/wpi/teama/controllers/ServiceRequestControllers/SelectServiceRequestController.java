package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.Aapp;
import edu.wpi.teama.controllers.SceneController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class SelectServiceRequestController {

  @FXML private Button medicalEquipmentDeliveryButton;
  @FXML private Button religiousRequestsButton;
  @FXML private Button sanitationServicesButton;
  @FXML private Button laundryServicesButton;
  @FXML private Button foodDeliveryButton;
  @FXML private Button languageServicesButton;
  private FXMLLoader loader = new FXMLLoader();

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  private void goToMedicalEquipmentDeliveryScene() throws IOException {
    sceneController.switchScene(
        SceneController.SCENES.MEDICAL_EQUIPMENT_DELIVERY_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToReligiousRequests() throws IOException {
    sceneController.switchScene(SceneController.SCENES.RELIGIOUS_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToSanitationServices() throws IOException {
    sceneController.switchScene(SceneController.SCENES.SANITATION_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToLaundryServices() throws IOException {
    sceneController.switchScene(SceneController.SCENES.LAUNDRY_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToFoodDelivery() throws IOException {
    sceneController.switchScene(SceneController.SCENES.FOOD_DELIVERY_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToLanguageServices() throws IOException {
    sceneController.switchScene(SceneController.SCENES.LANGUAGE_INTERPRETER_SERVICE_REQUEST_SCENE);
  }
}
