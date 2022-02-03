package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.controllers.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ReligiousServicesController extends GenericServiceRequestsController {
  @FXML private TextField religiousDesc = new TextField();
  @FXML private ChoiceBox religionChoice = new ChoiceBox();
  @FXML private ChoiceBox fromRoom = new ChoiceBox();
  @FXML private Button homeButton = new Button();
  @FXML private Button backButton = new Button();
  @FXML private Button clearButton;
  @FXML private Button submitRequestButton = new Button();

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() {
    sceneID = SceneController.SCENES.RELIGIOUS_SERVICE_REQUEST_SCENE;
  }

  @FXML
  void submitRequest() {
    // send request to database
  }
}
