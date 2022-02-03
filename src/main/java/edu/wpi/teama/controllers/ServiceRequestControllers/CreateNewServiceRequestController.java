package edu.wpi.teama.controllers.ServiceRequestControllers;

// DEPRECATED FOR NOW

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CreateNewServiceRequestController {

  @FXML private Button newRequestButton;
  private FXMLLoader loader = new FXMLLoader();

  @FXML
  private void goToServicesPage() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/selectServiceRequest.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) newRequestButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Select Service Request");
    window.show();
  }
}
