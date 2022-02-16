package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class SRCtrl {
  @FXML Button homeButton;
  @FXML Button submitButton;
  @FXML Button backButton;
  @FXML Button clearButton;

  List<Employee> employeeList = new ArrayList<>();
  List<Location> locationList = new ArrayList<>();

  SceneSwitcher.SCENES sceneID;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  void goToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  @FXML
  private void goToSelectServiceScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SELECT_SERVICE_REQUEST);
  }

  @FXML
  private void clearSubmission() throws IOException {
    sceneSwitcher.switchScene(sceneID);
  }

  @FXML
  abstract void submitRequest()
      throws IOException, TimeoutException, SQLException, InvocationTargetException,
          IllegalAccessException;

  @FXML
  protected void populateEmployeeAndLocationList() {
    this.employeeList = new EmployeeDerbyImpl().getEmployeeList();
    this.locationList = new LocationDerbyImpl().getNodeList();
  }

  @FXML
  protected void populateEmployeeComboBox(JFXComboBox<String> employeeChoice) {
    employeeChoice.getItems().removeAll(employeeChoice.getItems());
    employeeChoice
        .getItems()
        .addAll(this.employeeList.stream().map(Employee::getFullName).collect(Collectors.toList()));
    employeeChoice.setVisibleRowCount(5);
  }

  @FXML
  protected void populateLocationComboBox(JFXComboBox<String> locationChoice) {
    locationChoice.getItems().removeAll(locationChoice.getItems());
    locationChoice
        .getItems()
        .addAll(
            this.locationList.stream().map(Location::getShortName).collect(Collectors.toList()));
    locationChoice.setVisibleRowCount(5);
  }
}
