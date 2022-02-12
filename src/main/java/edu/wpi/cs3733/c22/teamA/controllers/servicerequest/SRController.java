package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class SRController {
  @FXML Button homeButton;
  @FXML Button submitButton;
  @FXML Button backButton;
  @FXML Button clearButton;

  // TODO: request employee, comments, location, start end location, priority, etc.

  SceneSwitcher.SCENES sceneID;

  private final SceneSwitcher sceneSwitcher = Aapp.sceneSwitcher;

  @FXML
  void returnToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME_SCENE);
  }

  @FXML
  private void returnToSelectServiceScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SELECT_SERVICE_REQUEST_SCENE);
  }

  @FXML
  private void clearSubmission() throws IOException {
    sceneSwitcher.switchScene(sceneID);
  }

  @FXML
  abstract void submitRequest() throws IOException, TimeoutException;
}
