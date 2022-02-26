package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.ConsultationSR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.AutoCompleteBox;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ConsultationSRCtrl extends SRCtrl {
  @FXML private Label titleLabel;
  @FXML private Label mapLabel;
  @FXML private Label reasonLabel;
  @FXML private Label employeeLabel;
  @FXML private Label locationLabel;
  @FXML private JFXComboBox<String> reasonChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  @FXML
  public void initialize() throws ParseException {
    sceneID = SceneSwitcher.SCENES.CONSULTATION_SR;

    // double reasonTextSize = reasonChoice.getFont().getSize();
    // double toLocationTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();

    double commentsTextSize = commentsBox.getFont().getSize();
    double titleTextSize = titleLabel.getFont().getSize();
    double mapTextSize = mapLabel.getFont().getSize();
    double reasonTextSize = reasonLabel.getFont().getSize();
    double employeeTextSize = employeeLabel.getFont().getSize();
    double locationTextSize = locationLabel.getFont().getSize();

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
              reasonLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * reasonTextSize)
                      + "pt;");
              employeeLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * employeeTextSize)
                      + "pt;");
              locationLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * locationTextSize)
                      + "pt;");
            });

    commentsBox.setWrapText(true);

    reasonChoice.getItems().removeAll(reasonChoice.getItems());
    reasonChoice.getItems().addAll("Change In Care", "General Check-up", "Professional Advice");
    reasonChoice.getSelectionModel().select("Reason");
    new AutoCompleteBox(reasonChoice);
    reasonChoice.setVisibleRowCount(5);

    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.toLocationChoice);
    new AutoCompleteBox(toLocationChoice);
    new AutoCompleteBox(employeeChoice);
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {

    if (!reasonChoice.getSelectionModel().getSelectedItem().equals("Reason")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && !employeeChoice.getSelectionModel().getSelectedItem().equals("Employee")) {

      int employeeIndex = this.employeeChoice.getSelectionModel().getSelectedIndex();
      Employee employeeSelected = this.employeeList.get(employeeIndex);

      int locationIndex = this.toLocationChoice.getSelectionModel().getSelectedIndex();
      Location toLocationSelected = this.locationList.get(locationIndex);

      //ConsultationSR consultationSR =
        //  new ConsultationSR(
          //    "ConsultationSRID",
            //  "N/A",
              //toLocationSelected.getNodeID(),
              //"001",
              //employeeSelected.getEmployeeID(),
              //new Timestamp((new Date()).getTime()),
              //SR.Status.BLANK,
              //SR.Priority.REGULAR,
              //commentsBox.getText().equals("") ? "N/A" : commentsBox.getText());

     // ServiceRequestDerbyImpl<ConsultationSR> serviceRequestDAO =
      //    new ServiceRequestDerbyImpl<>(new ConsultationSR());
     // serviceRequestDAO.enterServiceRequest(consultationSR);
    }
  }
}
