package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.requests.SanitationServiceRequest;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SanitationServicesController extends GenericServiceRequestsController {
  @FXML private TextArea commentsBox;
  @FXML private TextArea typeOtherBox;
  @FXML private JFXButton returnHomeButton;
  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private JFXComboBox<String> toLocationChoice;

  @FXML
  private void initialize() {
    sceneID = SceneController.SCENES.SANITATION_SERVICE_REQUEST_SCENE;

    // Put sanitation types in temporary type menu
    typeChoice.getItems().addAll("Decontaminate Area", "Floor Spill", "Other");
    typeChoice.getSelectionModel().select("Select Type");
    typeChoice
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue.equals("Other")) {
                typeOtherBox.setVisible(true);
              } else {
                typeOtherBox.setVisible(false);
              }
            });

    // Put locations in temporary location menu
    toLocationChoice.getSelectionModel().select("Select Location");
    toLocationChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toLocationChoice.setVisibleRowCount(5);
  }

  @FXML
  void submitRequest() {
    // Create request object
    SanitationServiceRequest request = new SanitationServiceRequest();
    if (typeChoice.getSelectionModel().getSelectedItem().equals("Other"))
      request.setSanitationType(typeOtherBox.getText());
    else request.setSanitationType(typeChoice.getSelectionModel().getSelectedItem());
    request.setLocation(toLocationChoice.getSelectionModel().getSelectedItem());
    request.setNotes(commentsBox.getText());
    // Submit to database
  }
}
