package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.LaundrySR;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wpi.cs3733.c22.teamA.entities.servicerequests.AutoCompleteBox;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;

public class LaundrySRCtrl extends SRCtrl {
  @FXML private Label locationLabel;
  @FXML private JFXComboBox<String> washMode;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private ServiceRequestDerbyImpl serviceRequestDatabase = new ServiceRequestDerbyImpl(SR.SRType.LAUNDRY);

  @FXML
  protected void initialize() throws ParseException {
    super.initialize();
    sceneID = SceneSwitcher.SCENES.LAUNDRY_SR;

    // double washModeTextSize = washMode.getFont().getSize();
    // double toLocationTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    // double locationLabelTextSize = locationLabel.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              commentsBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * commentsTextSize)
                      + "pt;");
              /*locationLabel.setStyle(
              "-fx-font-size: "
                  + ((App.getStage().getWidth() / 1000) * locationLabelTextSize)
                  + "pt;");*/
            });

    commentsBox.setWrapText(true);

    washMode.getItems().removeAll(washMode.getItems());
    washMode.getItems().addAll("Colors", "Whites", "Perm. press", "Save the trees!");
    washMode.setValue("Wash Mode");
    new AutoCompleteBox(washMode);
    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.toLocationChoice);
    new AutoCompleteBox(toLocationChoice);
    new AutoCompleteBox(employeeChoice);
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {
    System.out.print("\nNew request, got some work to do bud!\n");
    System.out.printf("Selected wash mode is : %s\n", washMode.getValue());
    System.out.printf("Added this note : \n[NOTE START]\n%s\n[NOTE END]\n", commentsBox.getText());
    if (!washMode.getValue().equals("Wash Mode")) {

      int employeeIndex = this.employeeChoice.getSelectionModel().getSelectedIndex();
      Employee employeeSelected = this.employeeList.get(employeeIndex);

      int locationIndex = this.toLocationChoice.getSelectionModel().getSelectedIndex();
      Location toLocationSelected = this.locationList.get(locationIndex);

      //      //get a uniqueID
      String uniqueID = "";
      List<String> currentIDs = new ArrayList<>();
      for(SR sr: serviceRequestDatabase.getServiceRequestList()){
        currentIDs.add(sr.getFields_string().get("request_id"));
      }
      boolean foundUnique = false;
      while(!foundUnique){

        String possibleUnique = "LND" + getUniqueNumbers();

        if(currentIDs.contains(possibleUnique)) continue;
        else if(!(currentIDs.contains(possibleUnique))){
          foundUnique = true;
          uniqueID = possibleUnique;
        }
      }


      SR sr = new SR(uniqueID,
              (new LocationDerbyImpl()).getLocationNode("N/A"),
              toLocationSelected,
              (new EmployeeDerbyImpl()).getEmployee("001"),
              employeeSelected,
              new Timestamp((new Date()).getTime()),
              SR.Status.BLANK,
              SR.Priority.REGULAR,
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
              SR.SRType.LAUNDRY);

      sr.setFieldByString("wash_mode", this.washMode.getValue());

      serviceRequestDatabase.enterServiceRequest(sr);
    }
  }

  @FXML
  public void chooseFloor(ActionEvent actionEvent) {
    locationLabel.setText(((Button) actionEvent.getSource()).getText());
    locationLabel.setAlignment(Pos.CENTER);
  }
}
