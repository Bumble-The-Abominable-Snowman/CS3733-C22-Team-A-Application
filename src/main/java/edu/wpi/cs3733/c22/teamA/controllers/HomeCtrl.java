package edu.wpi.cs3733.c22.teamA.controllers;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeCtrl extends MasterCtrl {

  @FXML public Label homeTitle;

  @FXML
  private void initialize() {

    configure();

    EmployeeDAO employeeBase = new EmployeeDerbyImpl();
    List<Employee> empList = employeeBase.getEmployeeList();

    for (Employee emp : empList) {
      if (emp.getEmail().equals(App.factory.getUsername())) {
        homeTitle.setText("Welcome, " + emp.getFullName());
      }
    }
  }
}
