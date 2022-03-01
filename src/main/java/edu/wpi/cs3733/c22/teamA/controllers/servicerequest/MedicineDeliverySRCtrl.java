package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.MedicineDeliverySR;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.AutoCompleteBox;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MedicineDeliverySRCtrl extends SRCtrl {

  private ServiceRequestDerbyImpl serviceRequestDatabase = new ServiceRequestDerbyImpl(SR.SRType.MEDICINE_DELIVERY);
  private MedicineDAO medicineDatabase = new MedicineDerbyImpl();
  private List<Medicine> medicineList = medicineDatabase.getMedicineList();

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
    for(Medicine med : medicineList) {
      medicineChoice
              .getItems()
              .add(med.getGenericName());

    }
    new AutoCompleteBox(medicineChoice);
    medicineChoice.setVisibleRowCount(5);

    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.toLocationChoice);
    new AutoCompleteBox(toLocationChoice);
    new AutoCompleteBox(employeeChoice);
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

//      //get a uniqueID
//      String uniqueID = "";
//      List<String> currentIDs = new ArrayList<>();
//      for(SR sr: serviceRequestDatabase.getServiceRequestList()){
//        currentIDs.add(sr.get());
//      }
//      boolean foundUnique = false;
//      while(!foundUnique){
//
//        String possibleUnique = "MEDDEL" + getUniqueNumbers();
//
//        if(currentIDs.contains(possibleUnique)) continue;
//        else if(!(currentIDs.contains(possibleUnique))){
//          foundUnique = true;
//          uniqueID = possibleUnique;
//        }
//      }

      SR sr = new SR("MedDel",
              (new LocationDerbyImpl()).getLocationNode("N/A"),
              toLocationSelected,
              (new EmployeeDerbyImpl()).getEmployee("001"),
              employeeSelected,
              new Timestamp((new Date()).getTime()),
              SR.Status.BLANK,
              SR.Priority.REGULAR,
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
              SR.SRType.MEDICINE_DELIVERY);

      serviceRequestDatabase.enterServiceRequest(sr);
    }
  }


}
