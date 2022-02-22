package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.GiftDeliverySR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class GiftDeliverySRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private Label titleLabel;
  @FXML private Label mapLabel;
  @FXML private Label locationLabel;
  @FXML private Label typeLabel;
  @FXML private Label employeeLabel;

  @FXML
  private void initialize() {
    sceneID = SceneSwitcher.SCENES.GIFT_DELIVERY_SR;

    // double typeChoiceTextSize = typeChoice.getFont().getSize();
    // double toLocationTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();
    double titleTextSize = titleLabel.getFont().getSize();
    double mapTextSize = mapLabel.getFont().getSize();
    double locationTextSize = locationLabel.getFont().getSize();
    double typeTextSize = typeLabel.getFont().getSize();
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
              typeLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * typeTextSize) + "pt;");
              employeeLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * employeeTextSize)
                      + "pt;");
            });

    commentsBox.setWrapText(true);

    // Put sanitation types in temporary type menu
    typeChoice.getItems().addAll("Balloons", "Card", "Stuffed Animal");
    typeChoice.getSelectionModel().select("Type");

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

    // Create request object
    GiftDeliverySR giftDeliverySR =
        new GiftDeliverySR(
            "GiftDeliverySRID",
            "N/A",
            toLocationSelected.getNodeID(),
            "001",
            employeeSelected.getEmployeeID(),
            new Timestamp((new Date()).getTime()),
            SR.Status.BLANK,
            SR.Priority.REGULAR,
            commentsBox.getText().equals("") ? "N/A" : commentsBox.getText());

    ServiceRequestDerbyImpl<GiftDeliverySR> serviceRequestDAO =
        new ServiceRequestDerbyImpl<>(new GiftDeliverySR());
    serviceRequestDAO.enterServiceRequest(giftDeliverySR);
  }
}
