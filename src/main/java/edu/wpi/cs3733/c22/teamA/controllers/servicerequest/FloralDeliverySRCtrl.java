package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.FloralDeliverySR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FloralDeliverySRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> flowerChoice;
  @FXML private JFXComboBox<String> bouquetTypeChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private Label titleLabel;
  @FXML private Label mapLabel;
  @FXML private Label locationLabel;
  @FXML private Label flowerLabel;
  @FXML private Label bouquetLabel;
  @FXML private Label employeeLabel;

  @FXML
  public void initialize() throws ParseException {
    sceneID = SceneSwitcher.SCENES.FLORAL_DELIVERY_SR;

    // double flowerChoiceTextSize = flowerChoice.getFont().getSize();
    // double bouquetTypeChoiceTextSize = bouquetTypeChoice.getFont().getSize();
    // double toLocationChoiceTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();
    double titleTextSize = titleLabel.getFont().getSize();
    double mapTextSize = mapLabel.getFont().getSize();
    double locationTextSize = locationLabel.getFont().getSize();
    double flowerTextSize = flowerLabel.getFont().getSize();
    double bouquetTextSize = bouquetLabel.getFont().getSize();
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
              flowerLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * flowerTextSize)
                      + "pt;");
              bouquetLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * bouquetTextSize)
                      + "pt;");
              mapLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * mapTextSize) + "pt;");
              employeeLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * employeeTextSize)
                      + "pt;");
            });

    commentsBox.setWrapText(true);

    flowerChoice.getItems().removeAll(flowerChoice.getItems());
    flowerChoice
        .getItems()
        .addAll("Carnation", "Daisy", "Forget-Me-Not", "Lily", "Orchid", "Rose", "Tulip");
    flowerChoice.getSelectionModel().select("Flower");
    flowerChoice.setVisibleRowCount(5);

    bouquetTypeChoice.getItems().removeAll(bouquetTypeChoice.getItems());
    bouquetTypeChoice.getItems().addAll("Full Bouquet", "Single Flower");
    bouquetTypeChoice.getSelectionModel().select("Bouquet Type");
    bouquetTypeChoice.setVisibleRowCount(5);

    toLocationChoice.getItems().removeAll(toLocationChoice.getItems());
    toLocationChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toLocationChoice.setVisibleRowCount(5);

    employeeChoice.getItems().removeAll(employeeChoice.getItems());
    EmployeeDAO EmployeeDAO = new EmployeeDerbyImpl();
    String input = "2022-02-03";
    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = originalFormat.parse(input);
    EmployeeDAO.enterEmployee(
        "323",
        "Admin",
        "Lucy",
        "Bernard",
        "lcbernard@wpi.edu",
        "0000000000",
        "100 institute Rd",
        date);
    employeeChoice
        .getItems()
        .addAll(
            EmployeeDAO.getEmployeeList().stream()
                .map(Employee::getFirstName)
                .collect(Collectors.toList()));
    employeeChoice.getSelectionModel().select("Employee");
    employeeChoice.setVisibleRowCount(5);
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {
    if (!flowerChoice.getSelectionModel().getSelectedItem().equals("Flower")
        && !bouquetTypeChoice.getSelectionModel().getSelectedItem().equals("Bouquet Type")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && !employeeChoice.getSelectionModel().getSelectedItem().equals("Employee")) {

      FloralDeliverySR floralDeliverySR =
          new FloralDeliverySR(
              "FloralDeliverySRID",
              "N/A",
              "N/A",
              "001",
              "002",
              new Timestamp((new Date()).getTime()),
              SR.Status.BLANK,
              SR.Priority.REGULAR,
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText());

      ServiceRequestDerbyImpl<FloralDeliverySR> serviceRequestDAO =
          new ServiceRequestDerbyImpl<>(new FloralDeliverySR());
      serviceRequestDAO.enterServiceRequest(floralDeliverySR);
    }
  }
}
