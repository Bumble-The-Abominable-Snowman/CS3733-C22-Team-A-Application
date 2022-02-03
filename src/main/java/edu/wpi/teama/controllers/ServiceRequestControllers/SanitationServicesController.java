package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import edu.wpi.teama.entities.foodDeliveryRequest;
import edu.wpi.teama.controllers.SceneController;
import java.io.IOException;
import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SanitationServicesController extends GenericServiceRequestsController {
  @FXML private Button backButton;
  @FXML private Button returnButton;
  @FXML private Button clearButton;
  @FXML private ChoiceBox typeMenu;
  @FXML private Button submitButton;
  @FXML private ChoiceBox<String> mainChoice;
  @FXML private ChoiceBox<String> drinkChoice;
  @FXML private ChoiceBox<String> sideChoice;
  @FXML private ChoiceBox<String> dessertChoice;
  @FXML private ChoiceBox<Integer> roomChoice;
  @FXML private TextField commentsText;
  private FXMLLoader loader = new FXMLLoader();

  @FXML
  private void initialize() {
    mainChoice.getItems().addAll("Turkey Sandwich", "Grilled Cheese Sandwich", "Friend Chicken");
    drinkChoice.getItems().addAll("Water", "Juice", "Milk");
    sideChoice.getItems().addAll("French Fries", "Apple", "Biscuit");
    dessertChoice.getItems().addAll("Cookie", "Brownie", "Cinnamon Roll");
    roomChoice.getItems().addAll(000, 101, 102, 103);
    mainChoice.getSelectionModel().select("Entree");
    drinkChoice.getSelectionModel().select("Beverage");
    sideChoice.getSelectionModel().select("Side");
    dessertChoice.getSelectionModel().select("Dessert");
    roomChoice.getSelectionModel().select(0);
    mainChoice.getSelectionModel().selectedItemProperty();
    drinkChoice.getSelectionModel().selectedItemProperty();
    sideChoice.getSelectionModel().selectedItemProperty();
    dessertChoice.getSelectionModel().selectedItemProperty();
    roomChoice.getSelectionModel().selectedItemProperty();
  }

  @FXML
  private void initialize() {
    sceneID = SceneController.SCENES.SANITATION_SERVICE_REQUEST_SCENE;

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
  private void clearSubmission() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/foodDelivery.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) clearButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Food Delivery");
    window.show();
  }

  @FXML
  private void createFoodRequest() throws IOException {
    foodDeliveryRequest aRequest =
        new foodDeliveryRequest(
            mainChoice.getValue(),
            sideChoice.getValue(),
            drinkChoice.getValue(),
            dessertChoice.getValue(),
            roomChoice.getValue(),
            commentsText.getText());
  }
}
