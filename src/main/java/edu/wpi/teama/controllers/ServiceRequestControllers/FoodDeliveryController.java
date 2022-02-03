package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.controllers.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class FoodDeliveryController extends GenericServiceRequestsController {
  @FXML private Button nextButton;
  @FXML private Button backButton;
  @FXML private Button homeButton;
  @FXML private Button clearButton;
  @FXML private TextField roomText;
  @FXML private Button submitButton;
  @FXML private ChoiceBox<String> mainChoice;
  @FXML private ChoiceBox<String> drinkChoice;
  @FXML private ChoiceBox<String> sideChoice;
  @FXML private ChoiceBox<String> dessertChoice;
  @FXML private TextField commentsText;
  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() {
    sceneID = SceneController.SCENES.FOOD_DELIVERY_SERVICE_REQUEST_SCENE;
  }
}
