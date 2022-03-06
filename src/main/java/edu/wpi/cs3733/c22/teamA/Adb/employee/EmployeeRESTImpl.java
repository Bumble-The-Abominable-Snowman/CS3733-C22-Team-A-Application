package edu.wpi.cs3733.c22.teamA.Adb.employee;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import okhttp3.*;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeRESTImpl implements EmployeeDAO {

  private final String url = "https://cs3733c22teama.ddns.net/api/employees";

  public void EmployeeDerbyImpl() {}

  @Override
  public Employee getEmployee(String ID) throws IOException, ParseException {
    HashMap<String, String> map = new HashMap<>();
    map.put("operation", "get");
    map.put("employee_id", ID);
    HashMap<String, String> resp = Adb.getREST(url, map);
    Employee employee = new Employee();
    for (String key: resp.keySet()) {
      employee.setFieldByString(key, resp.get(key));
    }
    return employee;

  }

  // Method to update employee from employee table.
  public void updateEmployee(Employee e) throws SQLException, IOException {

    HashMap<String, String> map = e.getStringFields();
    map.put("operation", "update");
    Adb.postREST(url, map);

  }

  public void enterEmployee(Employee e) throws IOException {

    HashMap<String, String> map = e.getStringFields();
    map.put("operation", "add");
    Adb.postREST(url, map);
  }

  public void enterEmployee(
      String employeeID,
      String employeeType,
      String firstName,
      String lastName,
      String email,
      String phoneNum,
      String address,
      Date startDate) throws IOException {

    Employee employee = new Employee(employeeID,
            employeeType,
            firstName,
            lastName,
            email,
            phoneNum,
            address,
            startDate);

    enterEmployee(employee);
  }

  public void deleteEmployee(Employee e) throws IOException {

    HashMap<String, String> map = new HashMap<>();
    map.put("employee_id", e.getStringFields().get("employee_id"));
    map.put("employee_type", e.getStringFields().get("employee_type"));
    map.put("operation", "delete");
    Adb.postREST(url, map);
  }

  public List<Employee> getEmployeeList() throws IOException, ParseException {

    List<Employee> arrayList = new ArrayList<>();
    List<HashMap<String, String>> mapArrayList = Adb.getAllREST(url);
    for (HashMap<String, String> map: mapArrayList) {
      Employee l = new Employee();
      for (String key: map.keySet()) {
        l.setFieldByString(key, map.get(key));
      }
      arrayList.add(l);
    }

    return arrayList;
  }

  // Read From Employees CSV
  public static List<Employee> readEmployeeCSV(String csvFilePath)
          throws IOException, ParseException, IllegalAccessException {
    // System.out.println("beginning to read csv");
      ClassLoader classLoader = EmployeeRESTImpl.class.getClassLoader();
      InputStream is = classLoader.getResourceAsStream(csvFilePath);
     Scanner lineScanner = new Scanner(is);

    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<Employee> list = new ArrayList<>();
    lineScanner.nextLine();
    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");

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

  public static List<Employee> readEmployeeCSVfile(String csvFilePath)
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
  public void inputFromCSV(String csvFilePath) throws IOException, ParseException { // Check employee table
    EmployeeRESTImpl employeeREST = new EmployeeRESTImpl();
    List<Employee> employeeList = employeeREST.getEmployeeList();
    for (Employee emp : employeeList) {
      employeeREST.deleteEmployee(emp);
    }

    try {

      List<Employee> employeeList1 = EmployeeRESTImpl.readEmployeeCSV(csvFilePath);
      for (Employee employee : employeeList1) {
        this.enterEmployee(employee);
      }
    } catch (IOException | ParseException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public void inputFromCSVfile(String csvFilePath) throws IOException, ParseException { // Check employee table


    ArrayList<HashMap<String, String>> map_list = new ArrayList<>();

    try {

      List<Employee> employeeList1 = EmployeeRESTImpl.readEmployeeCSVfile(csvFilePath);

      for (Employee employee : employeeList1) {
        map_list.add(employee.getStringFields());
      }

      HashMap<String, String> metadata_map = new HashMap<>();
      metadata_map.put("operation", "populate");
      metadata_map.put("employee_type", "");

      Adb.populate_db(url, metadata_map, map_list);
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }

  }

  // Export to CSV
  public static void exportToCSV(String csvFilePath) throws IOException, ParseException {
    EmployeeDAO Employee = new EmployeeRESTImpl();
    EmployeeRESTImpl.writeEmployeeCSV(Employee.getEmployeeList(), csvFilePath);
  }
}
