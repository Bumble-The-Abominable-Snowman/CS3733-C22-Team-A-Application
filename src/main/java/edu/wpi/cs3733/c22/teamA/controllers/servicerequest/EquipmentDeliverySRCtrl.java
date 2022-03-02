package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.EquipmentSR;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.AutoCompleteBox;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;

// TODO fix naming
// TODO make dependent on database for medical equipment

public class EquipmentDeliverySRCtrl extends SRCtrl {

  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> fromChoice;
  @FXML private JFXComboBox<String> statusChoice;
  @FXML private JFXComboBox<String> locationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private ServiceRequestDerbyImpl serviceRequestDatabase = new ServiceRequestDerbyImpl(SR.SRType.EQUIPMENT);
  private EquipmentDerbyImpl equipmentDatabase = new EquipmentDerbyImpl();
  private List<Equipment> equipmentList = equipmentDatabase.getMedicalEquipmentList();
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
  protected void initialize() throws ParseException {
    super.initialize();
    sceneID = SceneSwitcher.SCENES.EQUIPMENT_DELIVERY_SR;

    // double typeChoiceTextSize = typeChoice.getFont().getSize();
    // double fromTypeChoiceTextSize = fromTypeChoice.getFont().getSize();
    // double statusChoiceTextSize = statusChoice.getFont().getSize();
    // double toLocationChoiceTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();
    double titleTextSize = titleLabel.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              commentsBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * commentsTextSize)
                      + "pt;");
              titleLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * titleTextSize) + "pt;");
            });

    commentsBox.setWrapText(true);

    typeChoice.getItems().removeAll(typeChoice.getItems());
    typeChoice.getItems().addAll("Type", "Bed", "XRAY", "Infusion Pump", "Patient Recliner");
    new AutoCompleteBox(typeChoice);
    typeChoice.getSelectionModel().select(0);
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
    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.locationChoice);

    statusChoice.getItems().removeAll(statusChoice.getItems());
    statusChoice.getItems().setAll(status);
    new AutoCompleteBox(locationChoice);
    new AutoCompleteBox(employeeChoice);
    new AutoCompleteBox(fromChoice);
    new AutoCompleteBox(statusChoice);
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {

    if (!typeChoice.getSelectionModel().getSelectedItem().equals("Type")
        && locationChoice.getSelectionModel().getSelectedItem() != null
        && employeeChoice.getSelectionModel().getSelectedItem() != null) {

      int employeeIndex = this.employeeChoice.getSelectionModel().getSelectedIndex();
      Employee employeeSelected = this.employeeList.get(employeeIndex);

      int locationIndex = this.locationChoice.getSelectionModel().getSelectedIndex();
      Location toLocationSelected = this.locationList.get(locationIndex);

//      //get a uniqueID
      String uniqueID = "";
      List<String> currentIDs = new ArrayList<>();
      for(SR sr: serviceRequestDatabase.getServiceRequestList()){
        currentIDs.add(sr.getFields_string().get("request_id"));
      }
      boolean foundUnique = false;
      while(!foundUnique){

        String possibleUnique = "EQPDEL" + getUniqueNumbers();

        if(currentIDs.contains(possibleUnique)) continue;
        else if(!(currentIDs.contains(possibleUnique))){
          foundUnique = true;
          uniqueID = possibleUnique;
        }
      }


      // pass medical service request object
      SR sr = new SR(uniqueID,
              (new LocationDerbyImpl()).getLocationNode("N/A"),
              toLocationSelected,
              (new EmployeeDerbyImpl()).getEmployee("002"),
              employeeSelected,
              new Timestamp((new Date()).getTime()),
              SR.Status.BLANK,
              SR.Priority.REGULAR,
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
              SR.SRType.EQUIPMENT);

      sr.setField("equipment_id", typeChoice.getValue());

      ServiceRequestDerbyImpl serviceRequestDerby = new ServiceRequestDerbyImpl(SR.SRType.EQUIPMENT);
      serviceRequestDerby.enterServiceRequest(sr);
    }
  }
}
