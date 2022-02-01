package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import edu.wpi.teama.entities.MedicalEquipmentServiceRequest;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MedicalEquipmentDeliveryController {
  @FXML private Button homeButton;
  @FXML private ChoiceBox typeChoiceBox;
  @FXML private ChoiceBox fromChoiceBox;
  @FXML private Button submitButton;
  @FXML private Label locationLabel;
  @FXML private TextField specialNotes;
  @FXML private Button backButton;
  private FXMLLoader loader = new FXMLLoader();
  private MedicalEquipmentServiceRequest medicalEquipmentServiceRequest;

  public MedicalEquipmentDeliveryController() {
    medicalEquipmentServiceRequest = new MedicalEquipmentServiceRequest();
  }

  @FXML
  private void initialize() {
    typeChoiceBox.getItems().removeAll(typeChoiceBox.getItems());
    typeChoiceBox.getItems().addAll("Type", "Bed", "XRAY", "Infusion Pump", "Patient Recliner");
    typeChoiceBox.getSelectionModel().select("Type");
    typeChoiceBox.getSelectionModel().selectedItemProperty();
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/home.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) homeButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Home");
    window.show();
  }

  @FXML
  private void returnToSelectServiceScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource("views/selectServiceRequest.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) backButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Select Service Request");
    window.show();
  }

  @FXML
  public void chooseFloor(ActionEvent actionEvent) {
    locationLabel.setText(((Button) actionEvent.getSource()).getText());
    locationLabel.setAlignment(Pos.CENTER);
  }
}
