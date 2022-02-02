package edu.wpi.teama.Adb;

import edu.wpi.teama.Adb.Employee.Employee;
import edu.wpi.teama.Adb.Location.Location;
import edu.wpi.teama.Adb.MedicalEquipment.MedicalEquipment;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequest;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadCSV {

  //Read from Location CSV
  public static List<Location> readLocationCSV() throws IOException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
            new Scanner(ReadCSV.class.getClassLoader().getResourceAsStream("TowerLocations.csv"));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<Location> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      Location thisLocation = new Location();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisLocation.setNodeID(data);
        else if (dataIndex == 1) {
          intData = Integer.parseInt(data);
          thisLocation.setXCoord(intData);
        } else if (dataIndex == 2) {
          intData = Integer.parseInt(data);
          thisLocation.setYCoord(intData);
        } else if (dataIndex == 3) thisLocation.setFloor(data);
        else if (dataIndex == 4) thisLocation.setBuilding(data);
        else if (dataIndex == 5) thisLocation.setNodeType(data);
        else if (dataIndex == 6) thisLocation.setLongName(data);
        else if (dataIndex == 7) thisLocation.setShortName(data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisLocation);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  //Read From Employees CSV
  public static List<Employee> readEmployeeCSV() throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
            new Scanner(ReadCSV.class.getClassLoader().getResourceAsStream("Employee.csv"));
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
          Date date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(data);
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

  //Read From MedicalEquipment CSV
  public static List<MedicalEquipment> readMedicalEquipmentCSV() throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
            new Scanner(ReadCSV.class.getClassLoader().getResourceAsStream("MedicalEquipment.csv"));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<MedicalEquipment> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      MedicalEquipment thisME = new MedicalEquipment();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisME.setEquipmentID(data);
        else if (dataIndex == 1) thisME.setEquipmentType(data);
        else if (dataIndex == 2) {
          Boolean boolData = Boolean.parseBoolean(data);
          thisME.setIsClean(boolData);
        } else if (dataIndex == 3) thisME.setCurrentLocation(data);
        else if (dataIndex == 4) {
          Boolean boolData = Boolean.parseBoolean(data);
          thisME.setIsClean(boolData);
          thisME.setIsAvailable(boolData);
        } else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisME);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  //Read from Location CSV
  public static List<MedicalEquipmentServiceRequest> readMedicalEquipmentServiceRequestCSV() throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
            new Scanner(ReadCSV.class.getClassLoader().getResourceAsStream("MedicalEquipmentServiceRequest.csv"));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<MedicalEquipmentServiceRequest> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      MedicalEquipmentServiceRequest thisMESR = new MedicalEquipmentServiceRequest();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisMESR.setRequestID(data);
        else if (dataIndex == 1) thisMESR.setStartLocation(data);
        else if (dataIndex == 2) thisMESR.setEndLocation(data);
        else if (dataIndex == 3) thisMESR.setEmployeeRequested(data);
        else if (dataIndex == 4) thisMESR.setEmployeeAssigned(data);
        else if (dataIndex == 5) {
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
          Date parsedDate = dateFormat.parse(data);
          Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
          thisMESR.setRequestTime(timestamp);
        }
        else if (dataIndex == 6) thisMESR.setRequestStatus(data);
        else if (dataIndex == 7) thisMESR.setEquipmentID(data);
        else if (dataIndex == 8) thisMESR.setRequestType(data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisMESR);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

}
