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
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.FoodDeliverySR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FoodDeliverySRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> mainChoice;
  @FXML private JFXComboBox<String> sideChoice;
  @FXML private JFXComboBox<String> dessertChoice;
  @FXML private JFXComboBox<String> drinkChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private Label foodLabel;
  @FXML private Label mapLabel;
  @FXML private Label locationLabel;
  @FXML private Label orderLabel;
  @FXML private Label employeeLabel;

  @FXML
  public void initialize() throws ParseException {
    sceneID = SceneSwitcher.SCENES.FOOD_DELIVERY_SR;

    // double mainChoiceTextSize = mainChoice.getFont().getSize();
    // double sideChoiceTextSize = sideChoice.getFont().getSize();
    // double dessertChoiceTextSize = dessertChoice.getFont().getSize();
    // double drinkChoiceTextSize = drinkChoice.getFont().getSize();
    // double toLocationChoiceTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();
    double foodTextSize = foodLabel.getFont().getSize();
    double mapTextSize = mapLabel.getFont().getSize();
    double locationTextSize = locationLabel.getFont().getSize();
    double orderTextSize = orderLabel.getFont().getSize();
    double employeeTextSize = employeeLabel.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              commentsBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * commentsTextSize)
                      + "pt;");
              foodLabel.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * foodTextSize)
                              + "pt;");
              mapLabel.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * mapTextSize)
                              + "pt;");
              locationLabel.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * locationTextSize)
                              + "pt;");
              orderLabel.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * orderTextSize)
                              + "pt;");
              employeeLabel.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * employeeTextSize)
                              + "pt;");
            });

    commentsBox.setWrapText(true);

    mainChoice.getItems().addAll("Turkey Sandwich", "Grilled Cheese Sandwich", "Friend Chicken");
    drinkChoice.getItems().addAll("Water", "Juice", "Milk");
    sideChoice.getItems().addAll("French Fries", "Apple", "Biscuit");
    dessertChoice.getItems().addAll("Cookie", "Brownie", "Cinnamon Roll");
    mainChoice.getSelectionModel().select("Entree");
    drinkChoice.getSelectionModel().select("Beverage");
    sideChoice.getSelectionModel().select("Side");
    dessertChoice.getSelectionModel().select("Dessert");
    toLocationChoice.getSelectionModel().select(0);
    mainChoice.getSelectionModel().selectedItemProperty();
    drinkChoice.getSelectionModel().selectedItemProperty();
    sideChoice.getSelectionModel().selectedItemProperty();
    dessertChoice.getSelectionModel().selectedItemProperty();
    toLocationChoice.getSelectionModel().selectedItemProperty();

    toLocationChoice.getItems().removeAll(toLocationChoice.getItems());
    toLocationChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toLocationChoice.setVisibleRowCount(5);
    employeeChoice.setVisibleRowCount(5);

    EmployeeDAO EmployeeDAO = new EmployeeDerbyImpl();

    employeeChoice
        .getItems()
        .addAll(
            EmployeeDAO.getEmployeeList().stream()
                .map(Employee::getFirstName)
                .collect(Collectors.toList()));
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {
    FoodDeliverySR foodDeliverySR =
        new FoodDeliverySR(
            "FoodDeliverySRID",
            "N/A",
            "N/A",
            "001",
            "002",
            new Timestamp((new Date()).getTime()),
            SR.Status.BLANK,
            SR.Priority.REGULAR,
            commentsBox.getText().equals("") ? "N/A" : commentsBox.getText());

    ServiceRequestDerbyImpl<FoodDeliverySR> serviceRequestDAO =
        new ServiceRequestDerbyImpl<>(new FoodDeliverySR());
    serviceRequestDAO.enterServiceRequest(foodDeliverySR);
  }
}
