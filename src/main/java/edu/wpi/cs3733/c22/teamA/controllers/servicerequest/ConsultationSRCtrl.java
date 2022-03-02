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
  @FXML private JFXComboBox<String> reasonChoice;
  @FXML private JFXComboBox<String> locationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private double stageWidth;
  double commentsTextSize;
  double titleTextSize;
  double locationChoiceSize;
  double reasonChoiceSize;
  double employeeChoiceSize;

  @FXML
  protected void initialize() throws ParseException {
    super.initialize();
  public void initialize() throws ParseException {

    sceneID = SceneSwitcher.SCENES.CONSULTATION_SR;

    configure();

    stageWidth = App.getStage().getWidth();

    commentsTextSize = commentsBox.getFont().getSize();
    titleTextSize = titleLabel.getFont().getSize();
    //locationChoiceSize = locationChoice.getWidth();
    //reasonChoiceSize = reasonChoice.getWidth();
    //employeeChoiceSize = employeeChoice.getWidth();

    App.getStage()
        .widthProperty()
        .addListener((obs, oldVal, newVal) -> {updateSize();});

    commentsBox.setWrapText(true);

    reasonChoice.getItems().removeAll(reasonChoice.getItems());
    reasonChoice.getItems().addAll("Change In Care", "General Check-up", "Professional Advice");
    reasonChoice.getSelectionModel().select("Reason");
    new AutoCompleteBox(reasonChoice);
    reasonChoice.setVisibleRowCount(5);

    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.locationChoice);
    new AutoCompleteBox(locationChoice);
    new AutoCompleteBox(employeeChoice);

  }

  @FXML
  private void updateSize() {

    stageWidth = App.getStage().getWidth();
    commentsBox.setStyle("-fx-font-size: " + ((stageWidth / 1000) * commentsTextSize) + "pt;");
    titleLabel.setStyle("-fx-font-size: " + ((stageWidth / 1000) * titleTextSize) + "pt;");

  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {

    if (!reasonChoice.getSelectionModel().getSelectedItem().equals("Reason")
        && locationChoice.getSelectionModel().getSelectedItem() != null
        && !employeeChoice.getSelectionModel().getSelectedItem().equals("Employee")) {

      int employeeIndex = this.employeeChoice.getSelectionModel().getSelectedIndex();
      Employee employeeSelected = this.employeeList.get(employeeIndex);

      int locationIndex = this.locationChoice.getSelectionModel().getSelectedIndex();
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
