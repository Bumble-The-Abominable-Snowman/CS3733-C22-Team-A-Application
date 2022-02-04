package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.Adb.Employee.EmployeeDAO;
import edu.wpi.teama.Adb.Employee.EmployeeDerbyImpl;
import edu.wpi.teama.Adb.Location.LocationDerbyImpl;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequestDAO;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequestImpl;
import edu.wpi.teama.controllers.SceneController;
import edu.wpi.teama.entities.Employee;
import edu.wpi.teama.entities.Location;
import edu.wpi.teama.entities.requests.MedicalEquipmentServiceRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javax.swing.*;

public class MedicalEquipmentDeliveryController extends GenericServiceRequestsController {
  @FXML private ComboBox statusChoiceBox;
  @FXML private TextArea specialNotes;
  @FXML private ComboBox employeeChoiceBox;
  @FXML private ComboBox toChoiceBox;
  @FXML private ComboBox typeChoiceBox;
  @FXML private ComboBox fromChoiceBox;

  private FXMLLoader loader = new FXMLLoader();
  private List<String> bedLocations = new ArrayList<>();
  private List<String> xrayLocations = new ArrayList<>();
  private List<String> infusionPumpLocations = new ArrayList<>();
  private List<String> reclinerLocations = new ArrayList<>();
  private List<String> status = new ArrayList<>();
  private Random rand = new Random();

  public MedicalEquipmentDeliveryController() {
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
  private void initialize() throws ParseException {
    sceneID = SceneController.SCENES.MEDICAL_EQUIPMENT_DELIVERY_SERVICE_REQUEST_SCENE;

    specialNotes.setWrapText(true);

    typeChoiceBox.getItems().removeAll(typeChoiceBox.getItems());
    typeChoiceBox.getItems().addAll("Type", "Bed", "XRAY", "Infusion Pump", "Patient Recliner");
    typeChoiceBox.getSelectionModel().select("Type");
    typeChoiceBox
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue.equals("Type")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.setDisable(true);
              } else if (newValue.equals("Bed")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.getItems().setAll(bedLocations);
                fromChoiceBox.getSelectionModel().select(bedLocations.get(0));
                fromChoiceBox.setDisable(false);
              } else if (newValue.equals("XRAY")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.getItems().setAll(xrayLocations);
                fromChoiceBox.getSelectionModel().select(xrayLocations.get(0));
                fromChoiceBox.setDisable(false);
              } else if (newValue.equals("Infusion Pump")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.getItems().setAll(infusionPumpLocations);
                fromChoiceBox.getSelectionModel().select(infusionPumpLocations.get(0));
                fromChoiceBox.setDisable(false);
              } else if (newValue.equals("Patient Recliner")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.getItems().setAll(reclinerLocations);
                fromChoiceBox.getSelectionModel().select(reclinerLocations.get(0));
                fromChoiceBox.setDisable(false);
              }
            });

    fromChoiceBox.getItems().removeAll(fromChoiceBox.getItems());
    toChoiceBox.getItems().removeAll(toChoiceBox.getItems());
    toChoiceBox
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toChoiceBox.setVisibleRowCount(5);
    employeeChoiceBox.setVisibleRowCount(5);

    EmployeeDAO EmployeeDAO = new EmployeeDerbyImpl();

    employeeChoiceBox
        .getItems()
        .addAll(
            EmployeeDAO.getEmployeeList().stream()
                .map(Employee::getFirstName)
                .collect(Collectors.toList()));

    statusChoiceBox.getItems().removeAll(statusChoiceBox.getItems());
    statusChoiceBox.getItems().setAll(status);
  }

  @FXML
  void submitRequest() throws IOException {
    MedicalEquipmentServiceRequest medicalEquipmentServiceRequest =
        new MedicalEquipmentServiceRequest();
    if (!typeChoiceBox.getSelectionModel().getSelectedItem().equals("Type")
        && toChoiceBox.getSelectionModel().getSelectedItem() != null
        && employeeChoiceBox.getSelectionModel().getSelectedItem() != null) {
      // pass medical service request object
      MedicalEquipmentServiceRequestDAO medicalEquipmentServiceRequestDAO =
          new MedicalEquipmentServiceRequestImpl();

      medicalEquipmentServiceRequestDAO.enterMedicalEquipmentServiceRequest(
          Integer.toString(rand.nextInt(10000)),
          fromChoiceBox.getValue().toString(),
          toChoiceBox.getSelectionModel().getSelectedItem().toString(),
          "Alex Sun",
          employeeChoiceBox.getSelectionModel().getSelectedItem().toString(),
          new Timestamp((new Date()).getTime()),
          statusChoiceBox.getSelectionModel().getSelectedItem().toString(),
          typeChoiceBox.getSelectionModel().getSelectedItem().toString(),
          "MedicalEquipmentRequest");

      this.returnToHomeScene();
    }
  }
}
