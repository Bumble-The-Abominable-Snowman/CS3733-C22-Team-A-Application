package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.medicalequipmentservicerequest.EquipmentServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.medicalequipmentservicerequest.EquipmentServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

// TODO fix naming
// TODO make dependent on database for medical equipment
public class EquipmentDeliverySRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> fromChoice;
  @FXML private JFXComboBox<String> statusChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private List<String> bedLocations = new ArrayList<>();
  private List<String> xrayLocations = new ArrayList<>();
  private List<String> infusionPumpLocations = new ArrayList<>();
  private List<String> reclinerLocations = new ArrayList<>();
  private List<String> status = new ArrayList<>();

  public EquipmentDeliverySRCtrl() {
    super();

    bedLocations.add("Nearest Location");
    bedLocations.add("OR Bed Park");
    bedLocations.add("Nearest Hallway");

    xrayLocations.add("Nearest Location");
    xrayLocations.add("Near In-patient Unit");

    infusionPumpLocations.add("Clean Storage Area");

    reclinerLocations.add("Nearest from Hallways");
    reclinerLocations.add("West Plaza 1st Floor");

    status.add("NEW/BLANK");
    status.add("IN-PROGRESS");
    status.add("WAITING FOR EQUIPMENT");
    status.add("CANCELED");
    status.add("DONE");
  }

  @FXML
  private void initialize() {
    sceneID = SceneSwitcher.SCENES.MEDICAL_EQUIPMENT_DELIVERY_SERVICE_REQUEST_SCENE;

    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    homeButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    clearButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    submitButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    commentsBox.setWrapText(true);

    typeChoice.getItems().removeAll(typeChoice.getItems());
    typeChoice.getItems().addAll("Type", "Bed", "XRAY", "Infusion Pump", "Patient Recliner");
    typeChoice
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue.equals("Type")) {
                fromChoice.getItems().clear();
                fromChoice.setDisable(true);
              } else if (newValue.equals("Bed")) {
                fromChoice.getItems().clear();
                fromChoice.getItems().setAll(bedLocations);
                fromChoice.getSelectionModel().select(bedLocations.get(0));
                fromChoice.setDisable(false);
              } else if (newValue.equals("XRAY")) {
                fromChoice.getItems().clear();
                fromChoice.getItems().setAll(xrayLocations);
                fromChoice.getSelectionModel().select(xrayLocations.get(0));
                fromChoice.setDisable(false);
              } else if (newValue.equals("Infusion Pump")) {
                fromChoice.getItems().clear();
                fromChoice.getItems().setAll(infusionPumpLocations);
                fromChoice.getSelectionModel().select(infusionPumpLocations.get(0));
                fromChoice.setDisable(false);
              } else if (newValue.equals("Patient Recliner")) {
                fromChoice.getItems().clear();
                fromChoice.getItems().setAll(reclinerLocations);
                fromChoice.getSelectionModel().select(reclinerLocations.get(0));
                fromChoice.setDisable(false);
              }
            });

    fromChoice.getItems().removeAll(fromChoice.getItems());
    toLocationChoice.getItems().removeAll(toLocationChoice.getItems());
    toLocationChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toLocationChoice.setVisibleRowCount(5);
    employeeChoice.setVisibleRowCount(5);

    EmployeeDAO EmployeeDAO = new EmployeeDerbyImpl();

    employeeChoice
        .getItems()
        .addAll(
            EmployeeDAO.getEmployeeList().stream()
                .map(Employee::getFirstName)
                .collect(Collectors.toList()));

    statusChoice.getItems().removeAll(statusChoice.getItems());
    statusChoice.getItems().setAll(status);
  }

  @FXML
  void submitRequest() throws IOException {

    if (!typeChoice.getSelectionModel().getSelectedItem().equals("Type")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && employeeChoice.getSelectionModel().getSelectedItem() != null) {

      // pass medical service request object
      EquipmentServiceRequestDAO equipmentServiceRequestDAO =
          new EquipmentServiceRequestDerbyImpl();
      MedicalEquipmentServiceRequest medicalEquipmentServiceRequest =
          new MedicalEquipmentServiceRequest(
              "PlaceHolderID",
              fromChoice.getSelectionModel().getSelectedItem().toString(),
              toLocationChoice.getSelectionModel().getSelectedItem().toString(),
              Aapp.factory.getUsername(),
              employeeChoice.getSelectionModel().getSelectedItem().toString(),
              new Timestamp((new Date()).getTime()).toString(),
              "NEW",
              "Medical Equipment",
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
              typeChoice.getSelectionModel().getSelectedItem().toString());

      equipmentServiceRequestDAO.enterMedicalEquipmentServiceRequest(
          medicalEquipmentServiceRequest);

      this.goToHomeScene();
    }
  }
}