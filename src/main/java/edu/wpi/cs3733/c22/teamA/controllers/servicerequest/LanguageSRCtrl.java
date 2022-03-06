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

public class LanguageSRCtrl extends SRCtrl {
  @FXML private Label titleLabel;
  @FXML private JFXComboBox<String> languageChoice;
  @FXML private JFXComboBox<String> locationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private double stageWidth;
  double commentsTextSize;
  double titleTextSize;
  double locationChoiceSize;
  double languageChoiceSize;
  double employeeChoiceSize;

  private ServiceRequestWrapperImpl serviceRequestDatabase = new ServiceRequestWrapperImpl(SR.SRType.LANGUAGE);

  @FXML
  protected void initialize() throws ParseException, IOException {
    super.initialize();

    sceneID = SceneSwitcher.SCENES.LANGUAGE_SR;

    configure();

    stageWidth = App.getStage().getWidth();

    commentsTextSize = commentsBox.getFont().getSize();
    titleTextSize = titleLabel.getFont().getSize();
    //locationChoiceSize = locationChoice.getWidth();
    //languageChoiceSize = reasonChoice.getWidth();
    //employeeChoiceSize = employeeChoice.getWidth();

    App.getStage()
            .widthProperty()
            .addListener((obs, oldVal, newVal) -> {updateSize();});

    commentsBox.setWrapText(true);

    languageChoice.getItems().removeAll(languageChoice.getItems());
    languageChoice.getItems().addAll("American Sign Language", "Arabic", "French", "German", "Italian", "Japanese", "Korean", "Mandarin", "Russian", "Spanish");
    new AutoCompleteBox(languageChoice);
    languageChoice.setVisibleRowCount(5);

    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.locationChoice);
    new AutoCompleteBox(locationChoice);
    new AutoCompleteBox(employeeChoice);

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

    if (languageChoice.getSelectionModel().getSelectedItem() != null
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

      // pass language service request object
      SR sr = new SR(uniqueID,
              (new LocationWrapperImpl()).getLocationNode("NA"),
              toLocationSelected,
              (new EmployeeWrapperImpl()).getEmployee("002"),
              employeeSelected,
              new Timestamp((new Date()).getTime()),
              SR.Status.BLANK,
              SR.Priority.REGULAR,
              commentsBox.getText().equals("") ? "NA" : commentsBox.getText(),
              SR.SRType.LANGUAGE);

      ServiceRequestWrapperImpl serviceRequestWrapper = new ServiceRequestWrapperImpl(SR.SRType.LANGUAGE);
      serviceRequestWrapper.enterServiceRequest(sr);
    }
  }
}