package edu.wpi.cs3733.c22.teamA.Adb.Employee;

import edu.wpi.cs3733.c22.teamA.entities.Employee;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface EmployeeDAO {

  public Employee getEmployee(String ID);

  public void updateEmployee(String ID, String field, String change);

  public void enterEmployee(
      String employeeID,
      String employeeType,
      String firstName,
      String lastName,
      String email,
      String phoneNum,
      String address,
      Date startDate);

  public void deleteEmployee(String ID);

  public List<Employee> getEmployeeList();

  public void writeEmployeeCSV(List<Employee> List, String csvFilePath) throws IOException;
}
