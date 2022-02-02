package edu.wpi.teama.Adb.Employee;

import java.util.Date;
import java.util.List;

public interface EmployeeDAO {

  public static Employee getEmployee(String ID) {
    return null;
  }

  public static void updateEmployee(String ID, String field, String change) {}

  public static void enterEmployee(
      String employeeID,
      String employeeType,
      String firstName,
      String lastName,
      String email,
      String phoneNum,
      String address,
      Date startDate) {}

  public static void deleteEmployee(String ID) {}

  public static List<Employee> getEmployeeList() {
    return null;
  }
}
