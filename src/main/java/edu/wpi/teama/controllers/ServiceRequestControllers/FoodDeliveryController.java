package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.controllers.SceneController;
import edu.wpi.teama.entities.requests.foodDeliveryRequest;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FoodDeliveryController extends GenericServiceRequestsController {
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
  @FXML private TextArea commentsText;
  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() {
    sceneID = SceneController.SCENES.FOOD_DELIVERY_SERVICE_REQUEST_SCENE;

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

  @FXML
  void submitRequest() {}
}
