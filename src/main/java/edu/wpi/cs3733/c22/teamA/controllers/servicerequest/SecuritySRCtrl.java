package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SecuritySR;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class SecuritySRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  @FXML
  private void initialize() {
    sceneID = SceneSwitcher.SCENES.SECURITY_SR;

    commentsBox.setWrapText(true);

    // Put sanitation types in temporary type menu
    typeChoice.getItems().addAll("Decontaminate Area", "Floor Spill", "Other");
    typeChoice.getSelectionModel().select("Select Type");

    // Put locations in temporary location menu
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

    SecuritySR securitySR =
        new SecuritySR(
            "SecuritySRID",
            "N/A",
            toLocationSelected.getNodeID(),
            "001",
            employeeSelected.getEmployeeID(),
            new Timestamp((new Date()).getTime()),
            SR.Status.BLANK,
            SR.Priority.REGULAR,
            commentsBox.getText().equals("") ? "N/A" : commentsBox.getText());

    ServiceRequestDerbyImpl<SecuritySR> serviceRequestDAO =
        new ServiceRequestDerbyImpl<>(new SecuritySR());
    serviceRequestDAO.enterServiceRequest(securitySR);
  }
}
