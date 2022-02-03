package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.controllers.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class SanitationServicesController extends GenericServiceRequestsController {
  private FXMLLoader loader = new FXMLLoader();

  @FXML
  private void initialize() {
    sceneID = SceneController.SCENES.SANITATION_SERVICE_REQUEST_SCENE;
  }

  @FXML
  void submitRequest() {}
}
