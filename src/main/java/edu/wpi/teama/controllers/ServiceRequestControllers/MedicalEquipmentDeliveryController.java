package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.Adb.Employee.Employee;
import edu.wpi.teama.Adb.Employee.EmployeeDAO;
import edu.wpi.teama.Adb.Employee.EmployeeDerbyImpl;
import edu.wpi.teama.Adb.Location.Location;
import edu.wpi.teama.Adb.Location.LocationDerbyImpl;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequest;
import edu.wpi.teama.controllers.SceneController;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class MedicalEquipmentDeliveryController extends GenericServiceRequestsController {
  @FXML private TextArea specialNotes;
  @FXML private ComboBox employeeChoiceBox;
  @FXML private ComboBox toChoiceBox;

  @FXML private ChoiceBox typeChoiceBox;
  @FXML private ChoiceBox fromChoiceBox;

  private FXMLLoader loader = new FXMLLoader();
  private List<String> bedLocations = new ArrayList<>();
  private List<String> xrayLocations = new ArrayList<>();
  private List<String> infusionPumpLocations = new ArrayList<>();
  private List<String> reclinerLocations = new ArrayList<>();

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
  }

  @FXML
  void submitRequest() throws IOException {
    MedicalEquipmentServiceRequest medicalEquipmentServiceRequest =
        new MedicalEquipmentServiceRequest();
    if (!typeChoiceBox.getSelectionModel().getSelectedItem().equals("Type")
        && toChoiceBox.getSelectionModel().getSelectedItem() != null
        && employeeChoiceBox.getSelectionModel().getSelectedItem() != null) {
      // pass medical service request object
      this.returnToHomeScene();
    }
  }
}
