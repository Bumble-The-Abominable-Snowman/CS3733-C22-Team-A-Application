package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryRequest;

import java.io.IOException;
import java.text.ParseException;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FoodDeliveryController extends GenericServiceRequestsController {
  @FXML private Button backButton;
  @FXML private Button returnButton;
  @FXML private Button clearButton;
  @FXML private ChoiceBox typeMenu;
  @FXML private Button submitButton;
  @FXML private ChoiceBox<String> mainChoice;
  @FXML private ChoiceBox<String> drinkChoice;
  @FXML private ChoiceBox<String> sideChoice;
  @FXML private ChoiceBox<String> dessertChoice;
  @FXML private ComboBox roomChoice;
  @FXML private ComboBox employeeChoice;
  @FXML private TextArea commentsText;
  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() throws ParseException {
    sceneID = SceneController.SCENES.FOOD_DELIVERY_SERVICE_REQUEST_SCENE;

    mainChoice.getItems().addAll("Turkey Sandwich", "Grilled Cheese Sandwich", "Friend Chicken");
    drinkChoice.getItems().addAll("Water", "Juice", "Milk");
    sideChoice.getItems().addAll("French Fries", "Apple", "Biscuit");
    dessertChoice.getItems().addAll("Cookie", "Brownie", "Cinnamon Roll");
    roomChoice.getItems().addAll(000, 101, 102, 103);
    mainChoice.getSelectionModel().select("Entree");
    drinkChoice.getSelectionModel().select("Beverage");
    sideChoice.getSelectionModel().select("Side");
    dessertChoice.getSelectionModel().select("Dessert");
    roomChoice.getSelectionModel().select(0);
    mainChoice.getSelectionModel().selectedItemProperty();
    drinkChoice.getSelectionModel().selectedItemProperty();
    sideChoice.getSelectionModel().selectedItemProperty();
    dessertChoice.getSelectionModel().selectedItemProperty();
    roomChoice.getSelectionModel().selectedItemProperty();

    roomChoice.getItems().removeAll(roomChoice.getItems());
    roomChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    roomChoice.setVisibleRowCount(5);
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
  private void createFoodRequest() throws IOException {
    FoodDeliveryRequest aRequest =
        new FoodDeliveryRequest(
            mainChoice.getValue(),
            sideChoice.getValue(),
            drinkChoice.getValue(),
            dessertChoice.getValue(),
            (String) roomChoice.getSelectionModel().getSelectedItem(),
            (String) employeeChoice.getSelectionModel().getSelectedItem(),
            commentsText.getText());
  }

  @FXML
  void submitRequest() {}
}
