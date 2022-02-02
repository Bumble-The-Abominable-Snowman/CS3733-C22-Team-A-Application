package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController {
  @FXML private Button settingsButton;
  @FXML private Button serviceRequestsButton;
  @FXML private Button equipmentTrackerButton;
  @FXML private Button viewRequestsButton;
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

  public void goTosettings(ActionEvent actionEvent) throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/settings.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) settingsButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Settings");
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
  private void goToServiceRequests() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/viewServiceRequest.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) viewRequestsButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Service Requests");
    window.show();
  }

  @FXML
  private void exitApp() {
    System.exit(0);
  }
}
