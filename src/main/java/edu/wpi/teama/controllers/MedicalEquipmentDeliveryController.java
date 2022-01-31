package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MedicalEquipmentDeliveryController {
  @FXML private Button homeButton;
  @FXML private MenuButton typeMenu;
  @FXML private MenuButton fromMenu;
  @FXML private Button submitButton;
  @FXML private Label locationLabel;
  @FXML private TextField specialNotes;
  @FXML private Button backButton;
  private FXMLLoader loader = new FXMLLoader();

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
  private void selectTypeBed() {
    typeMenu.setText("Bed");
  }

  @FXML
  private void selectTypeXRAY() {
    typeMenu.setText("XRAY");
  }

  @FXML
  private void selectTypeInfusionPump() {
    typeMenu.setText("Infusion Pump");
  }

  @FXML
  private void selectTypePatientRecliner() {
    typeMenu.setText("Patient Recliner");
  }

  @FXML
  private void selectFromBedPark() {
    fromMenu.setText("Bed Park");
  }

  @FXML
  private void selectFromStorageUnit() {
    fromMenu.setText("Storage Unit");
  }

  @FXML
  public void chooseFloor(ActionEvent actionEvent) {
    locationLabel.setText(((Button) actionEvent.getSource()).getText());
    locationLabel.setAlignment(Pos.CENTER);
  }
}
