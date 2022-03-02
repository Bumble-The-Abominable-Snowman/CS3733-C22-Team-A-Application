package edu.wpi.cs3733.c22.teamA.Adb.employee;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class EmployeeDerbyImpl implements EmployeeDAO {

  public void EmployeeDerbyImpl() {}

  public Employee getEmployee(String ID) {
    try {
      Statement get = Adb.connection.createStatement();
      String str = String.format("SELECT * FROM Employee WHERE employee_id = '%s'", ID);
      ResultSet rset = get.executeQuery(str);
      Employee emp = new Employee();

      if (rset.next()) {
        String employeeID = rset.getString("employee_id");
        String employeeType = rset.getString("employee_type");
        String firstName = rset.getString("first_name");
        String lastName = rset.getString("last_name");
        String email = rset.getString("email");
        String phoneNum = rset.getString("phone_num");
        String address = rset.getString("address");
        Date startDate = rset.getDate("start_date");

        emp =
            new Employee(
                employeeID, employeeType, firstName, lastName, email, phoneNum, address, startDate);
      }

      return emp;
    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public void updateEmployee(Employee e)
          throws SQLException {

    Statement get = Adb.connection.createStatement();

    HashMap<String, String> e_string_fields = e.getStringFields();

    String str =
            String.format(
                    "SELECT * FROM Employee WHERE employee_id = '%s'",
                     e_string_fields.get("employee_id"));

    ResultSet resultSet = get.executeQuery(str);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

    if (resultSet.next()) {
      for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
        String returnValOld = resultSet.getString(i);
        String columnName = resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT);

        Statement update = Adb.connection.createStatement();

        if (!returnValOld.equals(e_string_fields.get(columnName)))
        {
            str =
                    String.format(
                            "UPDATE Employee SET " + columnName + " = '%s' WHERE employee_id = '%s'",
                            e_string_fields.get(columnName),
                            e_string_fields.get("employee_id"));

          update.execute(str);
        }

      }
    }
  }

  public void enterEmployee(
      String employeeID,
      String employeeType,
      String firstName,
      String lastName,
      String email,
      String phoneNum,
      String address,
      Date startDate) {
    try {
      Statement insert = Adb.connection.createStatement();

      SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
      String startDateStr = originalFormat.format(startDate);

      String str =
          String.format(
              "INSERT INTO Employee(employee_id, employee_type, first_name, "
                  + "last_name, email, phone_num, "
                  + "address, start_date) "
                  + " VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
              employeeID,
              employeeType,
              firstName,
              lastName,
              email,
              phoneNum,
              address,
              startDateStr);
      insert.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void deleteEmployee(String ID) {
    try {
      Statement delete = Adb.connection.createStatement();
      String str = String.format("DELETE FROM Employee WHERE employee_id = '%s'", ID);
      delete.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public List<Employee> getEmployeeList() {
    List<Employee> empList = new ArrayList<>();
    try {
      Statement getNodeList = Adb.connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM Employee");

      while (rset.next()) {
        String employeeID = rset.getString("employee_id");
        String employeeType = rset.getString("employee_type");
        String firstName = rset.getString("first_name");
        String lastName = rset.getString("last_name");
        String email = rset.getString("email");
        String phoneNum = rset.getString("phone_num");
        String address = rset.getString("address");
        Date startDate = rset.getDate("start_date");

        Employee emp =
            new Employee(
                employeeID, employeeType, firstName, lastName, email, phoneNum, address, startDate);
        empList.add(emp);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }

    return empList;
  }

  // Read From Employees CSV
  public static List<Employee> readEmployeeCSV(String csvFilePath)
          throws IOException, ParseException, IllegalAccessException {
    // System.out.println("beginning to read csv");

    ClassLoader classLoader = EmployeeDerbyImpl.class.getClassLoader();
    InputStream is = classLoader.getResourceAsStream(csvFilePath);
    Scanner lineScanner = new Scanner(is);
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<Employee> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      Employee thisEmployee = new Employee();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        data = data.trim();
        if (dataIndex == 0) thisEmployee.setFieldByString("employee_id", data);
        else if (dataIndex == 1) thisEmployee.setFieldByString("employee_type", data);
        else if (dataIndex == 2) thisEmployee.setFieldByString("first_name", data);
        else if (dataIndex == 3) thisEmployee.setFieldByString("last_name", data);
        else if (dataIndex == 4) thisEmployee.setFieldByString("email", data);
        else if (dataIndex == 5) thisEmployee.setFieldByString("phone_num", data);
        else if (dataIndex == 6) thisEmployee.setFieldByString("address", data);
        else if (dataIndex == 7) {
          thisEmployee.setFieldByString("start_date", data);
        } else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisEmployee);
       System.out.println(thisEmployee);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  public static void writeEmployeeCSV(List<Employee> List, String csvFilePath) throws IOException {

    // create a writer
    File file = new File(csvFilePath);
    file.createNewFile();
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "employee_id, employee_type, first_name, last_name, email, phone_num, address, start_date");
    writer.newLine();

    // write location data
    for (Employee thisEmployee : List) {

      String startDate = String.valueOf(thisEmployee.getStringFields().get("start_date"));
      writer.write(
          String.join(
              ",",
              thisEmployee.getStringFields().get("employee_id"),
              thisEmployee.getStringFields().get("employee_type"),
              thisEmployee.getStringFields().get("first_name"),
              thisEmployee.getStringFields().get("last_name"),
              thisEmployee.getStringFields().get("email"),
              thisEmployee.getStringFields().get("phone_num"),
              thisEmployee.getStringFields().get("address"),
              startDate));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String csvFilePath) { // Check employee table
    try {
      Statement dropTable = Adb.connection.createStatement();

      dropTable.execute("DELETE FROM Employee");
    } catch (SQLException e) {
      System.out.println("Delete on Employee failed");
    }

    EmployeeDerbyImpl empDerby = new EmployeeDerbyImpl();
    List<Employee> employeeList = empDerby.getEmployeeList();
    for (Employee emp : employeeList) {
      empDerby.deleteEmployee(emp.getStringFields().get("employee_id"));
    }

    try {

      List<Employee> employeeList1 = EmployeeDerbyImpl.readEmployeeCSV(csvFilePath);

      for (Employee employee : employeeList1) {
        Statement addStatement = Adb.connection.createStatement();

        //        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        //        System.out.println(l.getStartDate() == null);
        //        String date = originalFormat.format(l.getStartDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");

        String str = "INSERT INTO Employee(employee_id, employee_type, first_name, last_name, email, phone_num, address, start_date) VALUES('"
                + employee.getStringFields().get("employee_id")
                + "', '"
                + employee.getStringFields().get("employee_type")
                + "', '"
                + employee.getStringFields().get("first_name")
                + "', '"
                + employee.getStringFields().get("last_name")
                + "', '"
                + employee.getStringFields().get("email")
                + "', '"
                + employee.getStringFields().get("phone_num")
                + "', '"
                + employee.getStringFields().get("address")
                + "', '"
                + employee.getStringFields().get("start_date")
                + "')";

        System.out.println(str);
        addStatement.executeUpdate(str);
      }
    } catch (SQLException | IOException | ParseException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException {
    EmployeeDAO Employee = new EmployeeDerbyImpl();
    EmployeeDerbyImpl.writeEmployeeCSV(Employee.getEmployeeList(), csvFilePath);
  }
}
