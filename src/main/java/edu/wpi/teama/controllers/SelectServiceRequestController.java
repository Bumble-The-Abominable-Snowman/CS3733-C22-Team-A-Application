package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SelectServiceRequestController {

  @FXML private Button medicalEquipmentDeliveryButton;
  @FXML private Button religiousRequestsButton;
  @FXML private Button sanitationServicesButton;
  @FXML private Button laundryServicesButton;
  @FXML private Button foodDeliveryButton;
  @FXML private Button languageServicesButton;
  private FXMLLoader loader = new FXMLLoader();

  @FXML
  private void goToMedicalEquipmentDeliveryScene() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/medicalEquipmentDelivery.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) medicalEquipmentDeliveryButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Medical Equipment Delivery");
    window.show();
  }

  @FXML
  private void goToReligiousRequests() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/religiousServices.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) religiousRequestsButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Religious Requests");
    window.show();
  }

  @FXML
  private void goToSanitationServices() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/sanitationServices.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) sanitationServicesButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Sanitation Services");
    window.show();
  }

  @FXML
  private void goToLaundryServices() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/laundryServices.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) laundryServicesButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Laundry Services");
    window.show();
  }

  @FXML
  private void goToFoodDelivery() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/foodDelivery.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) foodDeliveryButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Food Delivery");
    window.show();
  }

  @FXML
  private void goToLanguageServices() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/languageInterpreter.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) languageServicesButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Language Services");
    window.show();
  }
}
