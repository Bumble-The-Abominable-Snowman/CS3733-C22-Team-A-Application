package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SanitationServicesController {
  @FXML private Button backButton;
  @FXML private Button returnButton;
  @FXML private Button clearButton;
  @FXML private ChoiceBox typeMenu;
  @FXML private Button submitButton;
  @FXML private Label locationLabel;
  @FXML private TextField specialNotes;
  @FXML private TextField typeOtherBox;

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  private void initialize() {
    typeMenu.getItems().removeAll(typeMenu.getItems());
    typeMenu.getItems().addAll("Type", "Decontaminate Room", "Floor Spill", "Other");
    typeMenu.getSelectionModel().select("Type");
    typeMenu
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (ChangeListener<String>)
                (observable, oldValue, newValue) -> {
                  if (newValue.equals("Other")) typeOtherBox.setVisible(true);
                  else typeOtherBox.setVisible(false);
                });
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource("views/home.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) returnButton.getScene().getWindow();
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
  private void clearSubmission() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/sanitationServices.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) clearButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Sanitation Services");
    window.show();
  }
}
