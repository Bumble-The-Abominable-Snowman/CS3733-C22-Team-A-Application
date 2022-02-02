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
}
