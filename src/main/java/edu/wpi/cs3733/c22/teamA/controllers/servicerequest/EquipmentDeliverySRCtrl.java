package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.EquipmentSR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
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
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private Label equipmentLabel;
  @FXML private Label fromLabel;
  @FXML private Label employeeLabel;
  @FXML private Label statusLabel;
  @FXML private Label titleLabel;
  @FXML private Label mapLabel;
  @FXML private Label locationLabel;

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
    sceneID = SceneSwitcher.SCENES.EQUIPMENT_DELIVERY_SR;

    // double typeChoiceTextSize = typeChoice.getFont().getSize();
    // double fromTypeChoiceTextSize = fromTypeChoice.getFont().getSize();
    // double statusChoiceTextSize = statusChoice.getFont().getSize();
    // double toLocationChoiceTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();
    double equipmentTextSize = equipmentLabel.getFont().getSize();
    double fromTextSize = fromLabel.getFont().getSize();
    double employeeTextSize = employeeLabel.getFont().getSize();
    double statusTextSize = statusLabel.getFont().getSize();
    double titleTextSize = titleLabel.getFont().getSize();
    double mapTextSize = mapLabel.getFont().getSize();
    double locationTextSize = locationLabel.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              commentsBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * commentsTextSize)
                      + "pt;");
              equipmentLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * equipmentTextSize)
                      + "pt;");
              fromLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * fromTextSize) + "pt;");
              employeeLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * employeeTextSize)
                      + "pt;");
              statusLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * statusTextSize)
                      + "pt;");
              titleLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * titleTextSize) + "pt;");
              mapLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * mapTextSize) + "pt;");
              locationLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * locationTextSize)
                      + "pt;");
            });

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
    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.toLocationChoice);

    statusChoice.getItems().removeAll(statusChoice.getItems());
    statusChoice.getItems().setAll(status);
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {

    if (!typeChoice.getSelectionModel().getSelectedItem().equals("Type")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && employeeChoice.getSelectionModel().getSelectedItem() != null) {

      int employeeIndex = this.employeeChoice.getSelectionModel().getSelectedIndex();
      Employee employeeSelected = this.employeeList.get(employeeIndex);

      int locationIndex = this.toLocationChoice.getSelectionModel().getSelectedIndex();
      Location toLocationSelected = this.locationList.get(locationIndex);

      // pass medical service request object
      SR sr = new SR("EquipmentSRID",
              (new LocationDerbyImpl()).getLocationNode("N/A"),
              toLocationSelected,
              (new EmployeeDerbyImpl()).getEmployee("001"),
              employeeSelected,
              new Timestamp((new Date()).getTime()),
              SR.Status.BLANK,
              SR.Priority.REGULAR,
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
              SR.SRType.EQUIPMENT);

      ServiceRequestDerbyImpl serviceRequestDerby = new ServiceRequestDerbyImpl(SR.SRType.EQUIPMENT);
      serviceRequestDerby.enterServiceRequest(sr);
    }
  }
}
