package edu.wpi.teama.Adb.Employee;

import edu.wpi.teama.entities.Employee;
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
}
