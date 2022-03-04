package edu.wpi.cs3733.c22.teamA.Adb.employee;

import edu.wpi.cs3733.c22.teamA.entities.Employee;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface EmployeeDAO {

  public Employee getEmployee(String ID) throws IOException, ParseException;

  public void updateEmployee(Employee e) throws SQLException, IOException;

  public void enterEmployee(Employee e) throws ParseException, IOException;

  public void enterEmployee(
      String employeeID,
      String employeeType,
      String firstName,
      String lastName,
      String email,
      String phoneNum,
      String address,
      Date startDate) throws IOException;

  public void deleteEmployee(Employee e) throws IOException;

  public List<Employee> getEmployeeList() throws IOException, ParseException;
}
