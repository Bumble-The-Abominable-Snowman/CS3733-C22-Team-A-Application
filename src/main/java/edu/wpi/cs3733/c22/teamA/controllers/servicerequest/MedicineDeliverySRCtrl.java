package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.MedicineDeliverySR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MedicineDeliverySRCtrl extends SRCtrl {

  @FXML private JFXComboBox<String> medicineChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  @FXML
  public void initialize() throws ParseException {
    sceneID = SceneSwitcher.SCENES.MEDICINE_DELIVERY_SR;

    // double medicineChoiceTextSize = medicineChoice.getFont().getSize();
    // double toLocationTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              commentsBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * commentsTextSize)
                      + "pt;");
            });

    commentsBox.setWrapText(true);

    medicineChoice.getItems().removeAll(medicineChoice.getItems());
    medicineChoice
        .getItems()
        .addAll(
            "Abacavir",
            "Acyclovir",
            "Baclofen",
            "Bleomycin",
            "Calcium",
            "Captopril",
            "Dacarbazine",
            "Dactinomycin",
            "Eltrombopag",
            "Emicizumab",
            "Famciclovir",
            "Famotide");
    medicineChoice.getSelectionModel().select("Medicine");
    medicineChoice.setVisibleRowCount(5);

    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.toLocationChoice);
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {
    if (!medicineChoice.getSelectionModel().getSelectedItem().equals("Medicine")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && !employeeChoice.getSelectionModel().getSelectedItem().equals("Employee")) {

      int employeeIndex = this.employeeChoice.getSelectionModel().getSelectedIndex();
      Employee employeeSelected = this.employeeList.get(employeeIndex);

      int locationIndex = this.toLocationChoice.getSelectionModel().getSelectedIndex();
      Location toLocationSelected = this.locationList.get(locationIndex);

      MedicineDeliverySR medicineDeliverySR =
          new MedicineDeliverySR(
              "MedicineDeliverySRID",
              "N/A",
              toLocationSelected.getNodeID(),
              "001",
              employeeSelected.getEmployeeID(),
              new Timestamp((new Date()).getTime()),
              SR.Status.BLANK,
              SR.Priority.REGULAR,
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText());
      ServiceRequestDerbyImpl<MedicineDeliverySR> serviceRequestDAO =
          new ServiceRequestDerbyImpl<>(new MedicineDeliverySR());
      serviceRequestDAO.enterServiceRequest(medicineDeliverySR);
    }
  }
}
