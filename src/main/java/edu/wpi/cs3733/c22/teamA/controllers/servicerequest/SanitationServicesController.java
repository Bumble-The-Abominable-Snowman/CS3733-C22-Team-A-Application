package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.sanitationservicerequest.SanitationServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.sanitationservicerequest.SanitationServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.requests.SanitationServiceRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SanitationServicesController extends GenericServiceRequestsController {
  @FXML private TextArea specialNotes;
  @FXML private TextArea typeOtherBox;
  @FXML private ChoiceBox<String> typeMenu;
  @FXML private ComboBox<String> locationMenu;

  @FXML
  private void initialize() {
    sceneID = SceneController.SCENES.SANITATION_SERVICE_REQUEST_SCENE;

    // Put sanitation types in temporary type menu
    typeMenu.getItems().addAll("Decontaminate Area", "Floor Spill", "Other");
    typeMenu.getSelectionModel().select("Select Type");
    typeMenu
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
    locationMenu.getSelectionModel().select("Select Location");
    locationMenu
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    locationMenu.setVisibleRowCount(5);
  }

  @FXML
  void submitRequest() {
    // Create request object
    SanitationServiceRequest sanitationServiceRequest =
        new SanitationServiceRequest(
            "PlaceHolderID",
            "N/A",
            locationMenu.getSelectionModel().getSelectedItem(),
            "Alex",
            "employee",
            new Timestamp((new Date()).getTime()).toString(),
            "NEW",
            "Language Interpreter",
            "N/A",
            typeMenu.getValue());
    SanitationServiceRequestDAO sanitationServiceRequestDAO =
        new SanitationServiceRequestDerbyImpl();
    sanitationServiceRequestDAO.enterSanitationServiceRequest(sanitationServiceRequest);
    // Submit to database
  }
}
