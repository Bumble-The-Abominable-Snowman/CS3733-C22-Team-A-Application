package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest.FoodDeliveryServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest.FoodDeliveryServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryServiceRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class FoodDeliveryController extends GenericServiceRequestsController {
  @FXML private JFXButton backButton;
  @FXML private JFXButton returnHomeButton;
  @FXML private JFXButton clearButton;
  @FXML private JFXButton submitButton;
  @FXML private JFXComboBox<String> mainChoice;
  @FXML private JFXComboBox<String> sideChoice;
  @FXML private JFXComboBox<String> dessertChoice;
  @FXML private JFXComboBox<String> drinkChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() throws ParseException {
    sceneID = SceneController.SCENES.FOOD_DELIVERY_SERVICE_REQUEST_SCENE;

    backButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
    returnHomeButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
    clearButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
    submitButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));

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
  private FoodDeliveryServiceRequest createFoodRequest() throws IOException {
    return new FoodDeliveryServiceRequest(
        "PlaceHolderID",
        "N/A",
        toLocationChoice.getSelectionModel().getSelectedItem().toString(),
        Aapp.factory.getUsername(),
        employeeChoice.getSelectionModel().getSelectedItem().toString(),
        new Timestamp((new Date()).getTime()).toString(),
        "NEW",
        "Food Delivery",
        commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
        mainChoice.getValue(),
        sideChoice.getValue(),
        drinkChoice.getValue(),
        dessertChoice.getValue());
  }

  @FXML
  void submitRequest() throws IOException {
    FoodDeliveryServiceRequest foodDeliveryServiceRequest = createFoodRequest();
    FoodDeliveryServiceRequestDAO foodDeliveryServiceRequestDAO =
        new FoodDeliveryServiceRequestDerbyImpl();
    // foodDeliveryServiceRequestDAO.enterRequest(foodDeliveryServiceRequest);
  }
}
