package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class SanitationServicesController extends GenericServiceRequestsController {
  @FXML private JFXButton backButton;
  @FXML private JFXButton returnHomeButton;
  @FXML private JFXButton clearButton;
  @FXML private JFXButton submitButton;
  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private TextArea typeOtherBox;

  @FXML
  private void initialize() {
    sceneID = SceneController.SCENES.SANITATION_SERVICE_REQUEST_SCENE;

    backButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
    returnHomeButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
    clearButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
    submitButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));

    commentsBox.setWrapText(true);
    typeOtherBox.setWrapText(true);

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
