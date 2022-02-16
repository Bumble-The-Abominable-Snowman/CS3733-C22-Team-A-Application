package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import javafx.fxml.FXML;

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

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  private void initialize() {
    double medicalEquipmentDeliveryTextSize = medicalEquipmentDeliveryButton.getFont().getSize();
    double religiousRequestTextSize = religiousRequestsButton.getFont().getSize();
    double sanitationServicesTextSize = sanitationServicesButton.getFont().getSize();
    double laundryServicesTextSize = laundryServicesButton.getFont().getSize();
    double foodDeliveryTextSize = foodDeliveryButton.getFont().getSize();
    double languageServicesTextSize = languageServicesButton.getFont().getSize();
    double floralServicesTextSize = floralServicesButton.getFont().getSize();
    double medicineDeliveryTextSize = medicineDeliveryButton.getFont().getSize();
    double backTextSize = backButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              medicalEquipmentDeliveryButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * medicalEquipmentDeliveryTextSize)
                      + "pt;");
              religiousRequestsButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * religiousRequestTextSize)
                      + "pt;");
              sanitationServicesButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * sanitationServicesTextSize)
                      + "pt;");
              laundryServicesButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * laundryServicesTextSize)
                      + "pt;");
              foodDeliveryButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * foodDeliveryTextSize)
                      + "pt;");
              languageServicesButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * languageServicesTextSize)
                      + "pt;");
              floralServicesButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * floralServicesTextSize)
                      + "pt;");
              medicineDeliveryButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * medicineDeliveryTextSize)
                      + "pt;");
              backButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * backTextSize) + "pt;");
            });
  }

  @FXML
  private void goToMedicalEquipmentDeliveryScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.EQUIPMENT_DELIVERY_SR);
  }

  @FXML
  private void goToReligiousRequests() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.RELIGIOUS_SR);
  }

  @FXML
  private void goToSanitationServices() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SANITATION_SR);
  }

  @FXML
  private void goToLaundryServices() throws IOException {
    System.out.println("Before");
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LAUNDRY_SR);
    System.out.println("After");
  }

  @FXML
  private void goToFoodDelivery() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.FOOD_DELIVERY_SR);
  }

  @FXML
  private void goToLanguageServices() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LANGUAGE_SR);
  }

  @FXML
  private void goToFloralDelivery() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.FLORAL_DELIVERY_SR);
  }

  @FXML
  private void goToMedicineDelivery() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.MEDICINE_DELIVERY_SR);
  }

  @FXML
  void goToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }
}
