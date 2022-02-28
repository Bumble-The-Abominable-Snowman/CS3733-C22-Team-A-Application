package edu.wpi.cs3733.c22.teamA.Adb.employee;

import edu.wpi.cs3733.c22.teamA.entities.Employee;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface EmployeeDAO {

  public Employee getEmployee(String ID);

  public void updateEmployee(Employee e) throws SQLException;

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
}
