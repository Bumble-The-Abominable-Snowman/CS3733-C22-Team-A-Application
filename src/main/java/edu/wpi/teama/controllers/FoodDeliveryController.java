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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class FoodDeliveryController {
  @FXML private Button nextButton;
  @FXML private Button backButton;
  @FXML private Button homeButton;
  @FXML private Button clearButton;
  @FXML private Button submitButton;
  @FXML private ChoiceBox<String> mainChoice;
  @FXML private ChoiceBox<String> drinkChoice;
  @FXML private ChoiceBox<String> sideChoice;
  @FXML private ChoiceBox<String> dessertChoice;
  @FXML private TextArea commentsBox;
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
  private void createFoodRequest() throws IOException {
    String theMain = mainChoice.getValue();
    String theSide = sideChoice.getValue();
    String theDrink = drinkChoice.getValue();
    String theDessert = dessertChoice.getValue();
    String theInstructions = commentsBox.getText();
    commentsBox.setText(theMain + theSide + theDrink + theDessert + theInstructions);
  }

  @FXML
  private void initialize() {
    mainChoice.getItems().removeAll(mainChoice.getItems());
    mainChoice
        .getItems()
        .addAll("Ham Sandwich", "Turkey Sandwich", "Peanut Butter KJelly Sandwich");
    mainChoice.getSelectionModel().select("Entree");
    mainChoice.getSelectionModel().selectedItemProperty();

    sideChoice.getItems().removeAll(sideChoice.getItems());
    sideChoice.getItems().addAll("Chicken Noodle Soup", "Mac & Cheese", "Bread");
    sideChoice.getSelectionModel().select("Side");
    sideChoice.getSelectionModel().selectedItemProperty();

    drinkChoice.getItems().removeAll(drinkChoice.getItems());
    drinkChoice.getItems().addAll("Water", "Juice", "Milk");
    drinkChoice.getSelectionModel().select("Beverage");
    drinkChoice.getSelectionModel().selectedItemProperty();

    dessertChoice.getItems().removeAll(dessertChoice.getItems());
    dessertChoice.getItems().addAll("Chocolate Chip Cookie", "Brownie", "Jello");
    dessertChoice.getSelectionModel().select("Dessert");
    dessertChoice.getSelectionModel().selectedItemProperty();
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
}
