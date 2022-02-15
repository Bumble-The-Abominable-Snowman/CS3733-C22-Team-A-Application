package edu.wpi.cs3733.c22.teamA.Adb.employee;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class EmployeeDerbyImpl implements EmployeeDAO {

  public void EmployeeDerbyImpl() {}

  public Employee getEmployee(String ID) {
    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement get = connection.createStatement();
      String str = String.format("SELECT * FROM Employee WHERE employeeID = '%s'", ID);
      ResultSet rset = get.executeQuery(str);
      Employee emp = new Employee();

      if (rset.next()) {
        String employeeID = rset.getString("employeeID");
        String employeeType = rset.getString("employeeType");
        String firstName = rset.getString("firstName");
        String lastName = rset.getString("lastName");
        String email = rset.getString("email");
        String phoneNum = rset.getString("phoneNum");
        String address = rset.getString("address");
        Date startDate = rset.getDate("startDate");

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

  public void updateEmployee(String ID, String field, Object change) {
    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement update = connection.createStatement();

      String str = "";
      if (change instanceof String) {
        str =
            String.format(
                "UPDATE Employee SET " + field + " = '%s' WHERE employeeID = '%s'", change, ID);
      } else {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = originalFormat.format(change);

        str =
            String.format(
                "UPDATE Employee SET "
                    + field
                    + " = '"
                    + startDateStr
                    + "' WHERE employeeID = '%s'",
                ID);
      }

      update.execute(str);
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
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
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement insert = connection.createStatement();

      SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
      String startDateStr = originalFormat.format(startDate);

      String str =
          String.format(
              "INSERT INTO Employee(employeeID, employeeType, firstName, "
                  + "lastName, email, phoneNum, "
                  + "address, startDate) "
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
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement delete = connection.createStatement();
      String str = String.format("DELETE FROM Employee WHERE employeeID = '%s'", ID);
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
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement getNodeList = connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM Employee");

      while (rset.next()) {
        String employeeID = rset.getString("employeeID");
        String employeeType = rset.getString("employeeType");
        String firstName = rset.getString("firstName");
        String lastName = rset.getString("lastName");
        String email = rset.getString("email");
        String phoneNum = rset.getString("phoneNum");
        String address = rset.getString("address");
        Date startDate = rset.getDate("startDate");

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
      throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    File file = new File(csvFilePath);
    Scanner lineScanner = new Scanner(file);
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
        if (dataIndex == 0) thisEmployee.setEmployeeID(data);
        else if (dataIndex == 1) thisEmployee.setEmployeeType(data);
        else if (dataIndex == 2) thisEmployee.setFirstName(data);
        else if (dataIndex == 3) thisEmployee.setLastName(data);
        else if (dataIndex == 4) thisEmployee.setEmail(data);
        else if (dataIndex == 5) thisEmployee.setPhoneNum(data);
        else if (dataIndex == 6) thisEmployee.setAddress(data);
        else if (dataIndex == 7) {
          SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
          Date date = originalFormat.parse(data);
          thisEmployee.setStartDate(date);
        } else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisEmployee);
      // System.out.println(thisLocation);

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
        "getEmployeeID, getEmployeeType, getFirstName, getLastName, getEmail, getPhoneNum, getAddress, startDate");
    writer.newLine();

    // write location data
    for (Employee thisEmployee : List) {

      String startDate = String.valueOf(thisEmployee.getStartDate());
      writer.write(
          String.join(
              ",",
              thisEmployee.getEmployeeID(),
              thisEmployee.getEmployeeType(),
              thisEmployee.getFirstName(),
              thisEmployee.getLastName(),
              thisEmployee.getEmail(),
              thisEmployee.getPhoneNum(),
              thisEmployee.getAddress(),
              startDate));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath) { // Check employee table
    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement dropTable = connection.createStatement();

      dropTable.execute("DELETE FROM Employee");
    } catch (SQLException e) {
      System.out.println("Delete on Employee failed");
    }

    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));

      List<Employee> List = EmployeeDerbyImpl.readEmployeeCSV(csvFilePath);
      for (Employee l : List) {
        Statement addStatement = connection.createStatement();

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = originalFormat.format(l.getStartDate());

        addStatement.executeUpdate(
            "INSERT INTO Employee(employeeID, employeeType, firstName, lastName, email, phoneNum, address, startDate) VALUES('"
                + l.getEmployeeID()
                + "', '"
                + l.getEmployeeType()
                + "', '"
                + l.getFirstName()
                + "', '"
                + l.getLastName()
                + "', '"
                + l.getEmail()
                + "', '"
                + l.getPhoneNum()
                + "', '"
                + l.getAddress()
                + "', '"
                + date
                + "')");
      }
    } catch (SQLException | IOException | ParseException e) {
      System.out.println("Insertion on Employee failed!");
    }
  }

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException {
    EmployeeDAO Employee = new EmployeeDerbyImpl();
    EmployeeDerbyImpl.writeEmployeeCSV(Employee.getEmployeeList(), csvFilePath);
  }
}
