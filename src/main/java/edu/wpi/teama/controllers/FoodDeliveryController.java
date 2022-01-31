package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FoodDeliveryController {
  @FXML private Button nextButton;
  @FXML private Button backButton;
  @FXML private Button homeButton;
  @FXML private TextField roomText;
  @FXML private Button submitButton;
  @FXML private ChoiceBox<String> mainChoice;
  @FXML private ChoiceBox<String> drinkChoice;
  @FXML private ChoiceBox<String> sideChoice;
  @FXML private ChoiceBox<String> dessertChoice;
  @FXML private TextField commentsText;
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
}
