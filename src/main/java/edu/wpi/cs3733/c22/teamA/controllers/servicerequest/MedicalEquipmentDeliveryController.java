package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipmentservicerequest.MedicalEquipmentServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipmentservicerequest.MedicalEquipmentServiceRequestImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;
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
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class MedicalEquipmentDeliveryController extends GenericServiceRequestsController {
  @FXML private JFXButton backButton;
  @FXML private JFXButton returnHomeButton;
  @FXML private JFXButton clearButton;
  @FXML private JFXButton submitButton;
  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> fromChoice;
  @FXML private JFXComboBox<String> statusChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

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

    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    returnHomeButton.setBackground(
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
    MedicalEquipmentServiceRequest medicalEquipmentServiceRequest =
        new MedicalEquipmentServiceRequest();
    if (!typeChoice.getSelectionModel().getSelectedItem().equals("Type")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && employeeChoice.getSelectionModel().getSelectedItem() != null) {
      // pass medical service request object
      MedicalEquipmentServiceRequestDAO medicalEquipmentServiceRequestDAO =
          new MedicalEquipmentServiceRequestImpl();

      medicalEquipmentServiceRequestDAO.enterMedicalEquipmentServiceRequest(
          Integer.toString(rand.nextInt(10000)),
          fromChoice.getValue().toString(),
          toLocationChoice.getSelectionModel().getSelectedItem().toString(),
          "Alex Sun",
          employeeChoice.getSelectionModel().getSelectedItem().toString(),
          new Timestamp((new Date()).getTime()),
          statusChoice.getSelectionModel().getSelectedItem().toString(),
          typeChoice.getSelectionModel().getSelectedItem().toString(),
          "MedicalEquipmentRequest");

      this.returnToHomeScene();
    }
  }
}
