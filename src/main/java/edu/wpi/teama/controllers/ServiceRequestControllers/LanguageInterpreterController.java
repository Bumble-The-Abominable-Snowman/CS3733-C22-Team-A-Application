package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.controllers.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class LanguageInterpreterController extends GenericServiceRequestsController {
  @FXML private Button floor1Button;
  @FXML private Button floor2Button;
  @FXML private Button floor3Button;
  @FXML private Button floor4Button;
  @FXML private Button returnHomeButton;
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private Button clearButton;
  @FXML private ChoiceBox<String> displayLang;
  @FXML private ChoiceBox<String> langInterpreter;
  @FXML private ChoiceBox<String> langInterpreter1;
  @FXML private TextField roomNum;
  @FXML private TextField commentsBox;

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() {
    sceneID = SceneController.SCENES.LANGUAGE_INTERPRETER_SERVICE_REQUEST_SCENE;
  }

  @FXML
  void submitRequest() {}
}
