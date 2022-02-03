package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.Aapp;
import edu.wpi.teama.controllers.SceneController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class GenericServiceRequestsController {
  @FXML private Button homeButton = new Button();
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private Button clearButton;

  SceneController.SCENES sceneID;

  private final SceneController sceneController = Aapp.sceneController;

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  private void returnToHomeScene() throws IOException {
    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
  }

  @FXML
  private void returnToSelectServiceScene() throws IOException {
    sceneController.switchScene(SceneController.SCENES.SELECT_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void clearSubmission() throws IOException {
    sceneController.switchScene(sceneID);
  }
}