package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class GenericServiceRequestsController {
  @FXML private Button homeButton = new Button();
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private Button clearButton;

  SceneController.SCENES sceneID;

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  void returnToHomeScene() throws IOException {
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

  @FXML
  abstract void submitRequest() throws IOException;
}
