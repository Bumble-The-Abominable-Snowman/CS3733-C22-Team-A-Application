package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.ReligiousSR;
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
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ReligiousSRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> religionChoice;
  @FXML private JFXComboBox<String> denominationChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private ServiceRequestDerbyImpl serviceRequestDatabase = new ServiceRequestDerbyImpl(SR.SRType.RELIGIOUS);

  private List<String> christianDenom = new ArrayList<>();
  private List<String> nonDenom = new ArrayList<>();
  private List<String> otherDenom = new ArrayList<>();
  private List<String> initChoices = new ArrayList<>();

  public ReligiousSRCtrl() {
    super();

    initChoices.add("Christian");
    initChoices.add("Non-Religious");
    initChoices.add("Other");

    christianDenom.add("Catholic");
    christianDenom.add("Baptist");
    christianDenom.add("Unspec. Protestant");
    christianDenom.add("Episcopal");
    christianDenom.add("UCOC");
    christianDenom.add("Methodist");
    christianDenom.add("Other");

    nonDenom.add("Atheist");
    nonDenom.add("Agnostic");
    nonDenom.add("Other");

    otherDenom.add("Jewish");
    otherDenom.add("Buddhist");
    otherDenom.add("Muslim");
    otherDenom.add("Hindu");
    otherDenom.add("Other");
  }

  @FXML
  private void initialize() throws ParseException {
    sceneID = SceneSwitcher.SCENES.RELIGIOUS_SR;

    // double religionChoiceTextSize = religionChoice.getFont().getSize();
    // double denominationChoiceTextSize = denominationChoice.getFont().getSize();
    // double toLocationChoiceTextSize = toLocationChoice.getFont().getSize();
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

    religionChoice.getItems().removeAll(religionChoice.getItems());
    religionChoice.getItems().addAll("Christian", "Non-Religious", "Other");
    new AutoCompleteBox(religionChoice);

    religionChoice
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {        //TODO this is throwing NullPointers
              if (newValue.equals("Type")) {
                denominationChoice.getItems().clear();
                denominationChoice.setDisable(true);
              } else if (newValue.equals("Christian")) {
                denominationChoice.getItems().clear();
                denominationChoice.getItems().setAll(christianDenom);
                denominationChoice.getSelectionModel().select(christianDenom.get(0));
                denominationChoice.setDisable(false);
              } else if (newValue.equals("Non-Religious")) {
                denominationChoice.getItems().clear();
                denominationChoice.getItems().setAll(nonDenom);
                denominationChoice.getSelectionModel().select(nonDenom.get(0));
                denominationChoice.setDisable(false);
              } else if (newValue.equals("Other")) {
                denominationChoice.getItems().clear();
                denominationChoice.getItems().setAll(otherDenom);
                denominationChoice.getSelectionModel().select(otherDenom.get(0));
                denominationChoice.setDisable(false);
              }
            });
    denominationChoice.getItems().removeAll(denominationChoice.getItems());
    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.toLocationChoice);
    new AutoCompleteBox(toLocationChoice);
    new AutoCompleteBox(employeeChoice);
    new AutoCompleteBox(denominationChoice);
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {
    if (!religionChoice.getSelectionModel().getSelectedItem().equals("Type")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && employeeChoice.getSelectionModel().getSelectedItem() != null) {

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

        String possibleUnique = "REL" + getUniqueNumbers();

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
              SR.SRType.RELIGIOUS);

      sr.setField("religion", religionChoice.getValue());

      serviceRequestDatabase.enterServiceRequest(sr);
    }
  }
}
