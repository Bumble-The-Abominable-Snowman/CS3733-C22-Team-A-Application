package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestWrapperImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.ConsultationSR;
import edu.wpi.cs3733.c22.teamA.entities.map.LocationMarker;
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

public class ReligiousSRCtrl extends SRCtrl {
  @FXML private Label titleLabel;
  @FXML private JFXComboBox<String> religionChoice;
  @FXML private JFXComboBox<String> denominationChoice;
  @FXML private JFXComboBox<String> locationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private double stageWidth;
  double commentsTextSize;
  double titleTextSize;
  double locationChoiceSize;
  double religionChoiceSize;
  double denominationChoiceSize;
  double employeeChoiceSize;

  private List<String> christianDenom = new ArrayList<>();
  private List<String> nonDenom = new ArrayList<>();
  private List<String> otherDenom = new ArrayList<>();
  private List<String> initChoices = new ArrayList<>();

  private ServiceRequestWrapperImpl serviceRequestDatabase = new ServiceRequestWrapperImpl(SR.SRType.RELIGIOUS);

  @FXML
  protected void initialize() throws ParseException {
    super.initialize();

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

    sceneID = SceneSwitcher.SCENES.RELIGIOUS_SR;

    configure();

    stageWidth = App.getStage().getWidth();

    commentsTextSize = commentsBox.getFont().getSize();
    titleTextSize = titleLabel.getFont().getSize();
    //locationChoiceSize = locationChoice.getWidth();
    //religionChoiceSize = religionChoice.getWidth();
    //employeeChoiceSize = employeeChoice.getWidth();

    App.getStage()
            .widthProperty()
            .addListener((obs, oldVal, newVal) -> {updateSize();});

    commentsBox.setWrapText(true);

    religionChoice.getItems().removeAll(religionChoice.getItems());
    religionChoice.getItems().addAll("Christian", "Non-Religious", "Other");
    new AutoCompleteBox(religionChoice);

    religionChoice
            .getSelectionModel()
            .selectedItemProperty()
            .addListener(
                    (obs, oldValue, newValue) -> {
                      if (newValue.equals("Christian")) {
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
                      } else {
                        denominationChoice.getItems().clear();
                        denominationChoice.setDisable(true);
                      }
                    });
    denominationChoice.getItems().removeAll(denominationChoice.getItems());
    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.locationChoice);
    new AutoCompleteBox(locationChoice);
    new AutoCompleteBox(employeeChoice);
    new AutoCompleteBox(denominationChoice);

    for (LocationMarker lm : getMarkerManager().getLocationMarkers()){
      lm.getButton().setOnAction(e -> locationChoice.getSelectionModel().select(lm.getLocation().getShortName()));
    }
  }

  @FXML
  private void updateSize() {

    stageWidth = App.getStage().getWidth();
    commentsBox.setStyle("-fx-font-size: " + ((stageWidth / 1000) * commentsTextSize) + "pt;");
    titleLabel.setStyle("-fx-font-size: " + ((stageWidth / 1000) * titleTextSize) + "pt;");

  }

  @FXML
  void submitRequest()
          throws IOException, SQLException, InvocationTargetException, IllegalAccessException, ParseException {

    if (religionChoice.getSelectionModel().getSelectedItem() != null
            && locationChoice.getSelectionModel().getSelectedItem() != null
            && !employeeChoice.getSelectionModel().getSelectedItem().equals("Employee")) {

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

        String possibleUnique = "CNS" + getUniqueNumbers();

        if(currentIDs.contains(possibleUnique)) continue;
        else if(!(currentIDs.contains(possibleUnique))){
          foundUnique = true;
          uniqueID = possibleUnique;
        }
      }

      // pass religious service request object
      SR sr = new SR(uniqueID,
              (new LocationWrapperImpl()).getLocationNode("N/A"),
              toLocationSelected,
              (new EmployeeWrapperImpl()).getEmployee("002"),
              employeeSelected,
              new Timestamp((new Date()).getTime()),
              SR.Status.BLANK,
              SR.Priority.REGULAR,
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
              SR.SRType.RELIGIOUS);

      ServiceRequestWrapperImpl serviceRequestWrapper = new ServiceRequestWrapperImpl(SR.SRType.RELIGIOUS);
      serviceRequestWrapper.enterServiceRequest(sr);
    }
  }
}