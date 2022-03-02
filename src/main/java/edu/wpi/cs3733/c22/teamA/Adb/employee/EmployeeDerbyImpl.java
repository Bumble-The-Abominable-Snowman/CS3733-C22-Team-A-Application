package edu.wpi.cs3733.c22.teamA.Adb.employee;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

  public void updateEmployee(String ID, String field, Object change) {
    try {
      Statement update = Adb.connection.createStatement();

      String str = "";
      if (change instanceof String) {
        str =
            String.format(
                "UPDATE Employee SET " + field + " = '%s' WHERE employee_id = '%s'", change, ID);
      } else {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = originalFormat.format(change);

        str =
            String.format(
                "UPDATE Employee SET "
                    + field
                    + " = '"
                    + startDateStr
                    + "' WHERE employee_id = '%s'",
                ID);
      }

      update.execute(str);
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void enterEmployee(Employee e) throws ParseException {
    enterEmployee(e.getEmployeeID(), e.getEmployeeType(), e.getFirstName(), e.getLastName(), e.getEmail(), e.getPhoneNum(), e.getAddress(),new SimpleDateFormat("yyyy-MM-dd").parse(e.getStartDate()));
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
      throws IOException, ParseException {
    // System.out.println("beginning to read csv");
    Scanner lineScanner = null;
    if(!Adb.isInitialized) {
      ClassLoader classLoader = EmployeeDerbyImpl.class.getClassLoader();
      InputStream is = classLoader.getResourceAsStream(csvFilePath);
      lineScanner = new Scanner(is);
    }else{
      File file = new File(csvFilePath);
      lineScanner = new Scanner(file);
    }

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
        if (dataIndex == 0) thisEmployee.setEmployeeID(data);
        else if (dataIndex == 1) thisEmployee.setEmployeeType(data);
        else if (dataIndex == 2) thisEmployee.setFirstName(data);
        else if (dataIndex == 3) thisEmployee.setLastName(data);
        else if (dataIndex == 4) thisEmployee.setEmail(data);
        else if (dataIndex == 5) thisEmployee.setPhoneNum(data);
        else if (dataIndex == 6) thisEmployee.setAddress(data);
        else if (dataIndex == 7) {
          thisEmployee.setStartDate(data);
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
      empDerby.deleteEmployee(emp.getEmployeeID());
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
                + employee.getEmployeeID()
                + "', '"
                + employee.getEmployeeType()
                + "', '"
                + employee.getFirstName()
                + "', '"
                + employee.getLastName()
                + "', '"
                + employee.getEmail()
                + "', '"
                + employee.getPhoneNum()
                + "', '"
                + employee.getAddress()
                + "', '"
                + employee.getStartDate()
                + "')";

        System.out.println(str);
        addStatement.executeUpdate(str);
      }
    } catch (SQLException | IOException | ParseException e) {
      e.printStackTrace();
    }
  }

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException {
    EmployeeDAO Employee = new EmployeeDerbyImpl();
    EmployeeDerbyImpl.writeEmployeeCSV(Employee.getEmployeeList(), csvFilePath);
  }
}
