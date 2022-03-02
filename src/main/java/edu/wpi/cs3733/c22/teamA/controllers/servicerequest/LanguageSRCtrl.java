package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.LanguageSR;
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

public class LanguageSRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> languageChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private Label titleLabel;
  @FXML private Label mapLabel;
  @FXML private Label locationLabel;
  @FXML private Label languageLabel;
  @FXML private Label employeeLabel;

  private ServiceRequestDerbyImpl serviceRequestDatabase = new ServiceRequestDerbyImpl(SR.SRType.LANGUAGE);

  @FXML
  protected void initialize() throws ParseException {
    super.initialize();
    sceneID = SceneSwitcher.SCENES.LANGUAGE_SR;

    // double languageTextSize = languageChoice.getFont().getSize();
    // double toLocationTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();
    double titleTextSize = titleLabel.getFont().getSize();
    double mapTextSize = mapLabel.getFont().getSize();
    double locationTextSize = locationLabel.getFont().getSize();
    double languageTextSize = languageLabel.getFont().getSize();
    double employeeTextSize = employeeLabel.getFont().getSize();

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
              mapLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * mapTextSize) + "pt;");
              locationLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * locationTextSize)
                      + "pt;");
              languageLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * languageTextSize)
                      + "pt;");
              employeeLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * employeeTextSize)
                      + "pt;");
            });

    commentsBox.setWrapText(true);

    languageChoice.getItems().removeAll(languageChoice.getItems());
    languageChoice
        .getItems()
        .addAll(
            "American Sign Language",
            "Arabic",
            "French",
            "German",
            "Italian",
            "Japanese",
            "Korean",
            "Mandarin",
            "Russian",
            "Spanish");
    new AutoCompleteBox(languageChoice);
    languageChoice.setVisibleRowCount(5);
    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.toLocationChoice);
    new AutoCompleteBox(toLocationChoice);
    new AutoCompleteBox(employeeChoice);
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {

    if (!languageChoice.getSelectionModel().getSelectedItem().equals("Language")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && !employeeChoice.getSelectionModel().getSelectedItem().equals("Employee")) {

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

        String possibleUnique = "LAN" + getUniqueNumbers();

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
              SR.SRType.LANGUAGE);

      sr.setField("sanitation_type", languageChoice.getValue());

      serviceRequestDatabase.enterServiceRequest(sr);
    }
  }
}
