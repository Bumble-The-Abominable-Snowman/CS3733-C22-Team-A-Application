package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
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
import javafx.scene.paint.Color;

public abstract class SRCtrl extends MasterCtrl {
  @FXML Button submitButton;
  @FXML Button clearButton;

  private double stageWidth;

  List<Employee> employeeList = new ArrayList<>();
  List<Location> locationList = new ArrayList<>();

  SceneSwitcher.SCENES sceneID;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  void initialize() throws ParseException {
    double submitTextSize = submitButton.getFont().getSize();
    double clearTextSize = clearButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              stageWidth = App.getStage().getWidth();
              submitButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * submitTextSize) + "pt;");
              clearButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * clearTextSize) + "pt;");
            });
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

  @FXML
  private void help() throws IOException {

    if (helpState != 0) {
      nextButton.setVisible(false);
      helpText.setVisible(false);
      drawer.setEffect(null);
      helpButton.setEffect(null);
      helpState = 0;
    }
    else {
      borderGlow.setColor(Color.GOLD);
      borderGlow.setOffsetX(0f);
      borderGlow.setOffsetY(0f);
      borderGlow.setHeight(45);
      nextButton.setVisible(true);
      helpText.setVisible(true);
      helpText.setText("Select a menu option to use the application.  This menu is present on every page and is the primary navigation tool you will use.");
      drawer.setEffect(borderGlow);
      helpState = 1;
    }

  }

  @FXML
  private void next() throws IOException {

    if (helpState == 1) {
      drawer.setEffect(null);
      helpText.setText("You can always click the help button to exit help at any time");
      helpButton.setEffect(borderGlow);
      helpState = 2;
    }

  }

}