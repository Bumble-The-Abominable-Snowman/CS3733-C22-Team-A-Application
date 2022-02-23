package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SanitationSR;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SanitationSRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private TextArea typeOtherBox;
  @FXML private Label titleLabel;
  @FXML private Label mapLabel;
  @FXML private Label locationLabel;
  @FXML private Label typeLabel;
  @FXML private Label employeeLabel;

  @FXML
  private void initialize() {
    sceneID = SceneSwitcher.SCENES.SANITATION_SR;

    // double typeChoiceTextSize = typeChoice.getFont().getSize();
    // double toLocationTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();
    double typeOtherTextSize = typeOtherBox.getFont().getSize();
    double titleTextSize = titleLabel.getFont().getSize();
    double mapTextSize = mapLabel.getFont().getSize();
    double locationTextSize = locationLabel.getFont().getSize();
    double typeTextSize = typeLabel.getFont().getSize();
    double employeeTextSize = employeeLabel.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              commentsBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * commentsTextSize)
                      + "pt;");
              typeOtherBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * typeOtherTextSize)
                      + "pt;");
              titleLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * titleTextSize) + "pt;");
              mapLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * mapTextSize) + "pt;");
              locationLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * locationTextSize)
                      + "pt;");
              typeLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * typeTextSize) + "pt;");
              employeeLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * employeeTextSize)
                      + "pt;");
            });

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

    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.toLocationChoice);
  }

  @FXML
  void submitRequest() throws SQLException, InvocationTargetException, IllegalAccessException {

    int employeeIndex = this.employeeChoice.getSelectionModel().getSelectedIndex();
    Employee employeeSelected = this.employeeList.get(employeeIndex);

    int locationIndex = this.toLocationChoice.getSelectionModel().getSelectedIndex();
    Location toLocationSelected = this.locationList.get(locationIndex);

    // Create request object
    SR sr = new SR("SanitationSRID",
            (new LocationDerbyImpl()).getLocationNode("N/A"),
            toLocationSelected,
            (new EmployeeDerbyImpl()).getEmployee("001"),
            employeeSelected,
            new Timestamp((new Date()).getTime()),
            SR.Status.BLANK,
            SR.Priority.REGULAR,
            commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
            SR.SRType.SANITATION);

    ServiceRequestDerbyImpl serviceRequestDerby = new ServiceRequestDerbyImpl(SR.SRType.SANITATION);
    serviceRequestDerby.enterServiceRequest(sr);
  }
}
