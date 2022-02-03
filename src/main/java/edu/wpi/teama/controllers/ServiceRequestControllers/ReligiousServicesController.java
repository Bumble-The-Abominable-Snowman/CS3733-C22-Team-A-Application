package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ReligiousServicesController {
  @FXML private TextArea specialNotes = new TextArea();
  @FXML private ChoiceBox religionChoiceBox = new ChoiceBox();
  @FXML private ChoiceBox toChoiceBox = new ChoiceBox();
  @FXML private ChoiceBox employeeChoiceBox = new ChoiceBox();
  @FXML private ChoiceBox fromChoiceBox = new ChoiceBox();
  @FXML private Label locationLabel = new Label();
  @FXML private Button homeButton = new Button();
  @FXML private Button backButton = new Button();
  @FXML private Button clearButton;
  @FXML private Button submitButton = new Button();

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  private void returnToHomeScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
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
  private void submitRequest() {
    // send request to database
  }

  @FXML
  private void clearSubmission() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/religiousServices.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) clearButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Religious Requests");
    window.show();
  }
}
