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

public class HomeController {
  @FXML private Button serviceRequestsButton;
  @FXML private Button equipmentTrackerButton;
  @FXML private Button exitButton;
  private FXMLLoader loader = new FXMLLoader();

  @FXML
  private void goToCreateNewServiceRequest() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/createNewServiceRequest.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) serviceRequestsButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Create New Service Request");
    window.show();
  }

  @FXML
  private void goToEquipmentTracker() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/medicalEquipmentData.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) equipmentTrackerButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Equipment Tracker");
    window.show();
  }

  @FXML
  private void exitApp() {
    System.exit(0);
  }
}
