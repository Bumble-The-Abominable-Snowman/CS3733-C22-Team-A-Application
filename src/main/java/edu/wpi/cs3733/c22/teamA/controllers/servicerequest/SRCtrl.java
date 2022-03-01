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

public abstract class SRCtrl extends MasterCtrl {
  @FXML Button submitButton;
  @FXML Button clearButton;

  double stageWidth;
  double stageHeight;

  List<Employee> employeeList = new ArrayList<>();
  List<Location> locationList = new ArrayList<>();

  SceneSwitcher.SCENES sceneID;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  private void initialize() {
    double submitTextSize = submitButton.getFont().getSize();
    double clearTextSize = clearButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              submitButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * submitTextSize)
                      + "pt;");
              clearButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * clearTextSize) + "pt;");
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

  public String getUniqueNumbers(){
    int round = 0;
    double rand = Math.random()*1000;
    if(rand < 1.0) round = (int) Math.ceil(rand);
    else if(rand > 999.0) round = (int) Math.floor(rand);
    else round = (int) Math.floor(rand);

    String str ="";
    if(round >= 100) str = str + round;
    else if(round >= 10 && round <= 100) str = str + "0" + round;
    else if(round >= 1 && round <= 9 ) str = str + "00" + round;
    else if(round == 0) str = "000";
    System.out.println("rand = " + rand +" round = " + round + " str = " + str);

    return str;
  }
}
