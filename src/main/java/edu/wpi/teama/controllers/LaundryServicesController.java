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
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LaundryServicesController {
  @FXML private Button floor4Button;
  @FXML private Button floor3Button;
  @FXML private Button floor2Button;
  @FXML private Button floor1Button;

  @FXML private Label locationLabel;
  @FXML private TextField specialNotes;
  @FXML private ChoiceBox washMode = new ChoiceBox();
  @FXML private Button homeButton = new Button();
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private Button clearButton;

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() {
    washMode.getItems().removeAll(washMode.getItems());
    washMode.getItems().addAll("Colors", "Whites", "Perm. press", "Save the trees!");
    washMode.setValue("Wash Mode");
  }

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
    System.out.print("\nNew request, got some work to do bud!\n");
    System.out.printf("Selected wash mode is : %s\n", washMode.getValue());
    System.out.printf(
        "Added this note : \n[NOTE START]\n%s\n[NOTE END]\n", specialNotes.getCharacters());
    // send request to database
  }

  @FXML
  public void chooseFloor(ActionEvent actionEvent) {
    locationLabel.setText(((Button) actionEvent.getSource()).getText());
    locationLabel.setAlignment(Pos.CENTER);
  }

  @FXML
  private void clearSubmission() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/laundryServices.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) clearButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Laundry Services");
    window.show();
  }
}
