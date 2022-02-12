package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class SelectServiceRequestCtrl {

  @FXML private JFXButton medicalEquipmentDeliveryButton;
  @FXML private JFXButton religiousRequestsButton;
  @FXML private JFXButton sanitationServicesButton;
  @FXML private JFXButton laundryServicesButton;
  @FXML private JFXButton foodDeliveryButton;
  @FXML private JFXButton languageServicesButton;
  @FXML private JFXButton floralServicesButton;
  @FXML private JFXButton medicineDeliveryButton;
  @FXML private JFXButton backButton;

  private final SceneSwitcher sceneSwitcher = Aapp.sceneSwitcher;

  @FXML
  private void initialize() {
    medicalEquipmentDeliveryButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(40), Insets.EMPTY)));
    religiousRequestsButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(40), Insets.EMPTY)));
    sanitationServicesButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(40), Insets.EMPTY)));
    laundryServicesButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(40), Insets.EMPTY)));
    languageServicesButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(40), Insets.EMPTY)));
    floralServicesButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(40), Insets.EMPTY)));
    medicineDeliveryButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(40), Insets.EMPTY)));
    foodDeliveryButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(40), Insets.EMPTY)));
    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
  }

  @FXML
  private void goToMedicalEquipmentDeliveryScene() throws IOException {
    sceneSwitcher.switchScene(
        SceneSwitcher.SCENES.MEDICAL_EQUIPMENT_DELIVERY_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToReligiousRequests() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.RELIGIOUS_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToSanitationServices() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SANITATION_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToLaundryServices() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LAUNDRY_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToFoodDelivery() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.FOOD_DELIVERY_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToLanguageServices() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LANGUAGE_INTERPRETER_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToFloralDelivery() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.FLORAL_DELIVERY_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void goToMedicineDelivery() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.MEDICINE_DELIVERY_SERVICE_REQUEST_SCENE);
  }

  @FXML
  void goToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME_SCENE);
  }
}
