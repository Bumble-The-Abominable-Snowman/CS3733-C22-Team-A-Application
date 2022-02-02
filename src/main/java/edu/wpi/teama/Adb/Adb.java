package edu.wpi.teama.Adb;

import edu.wpi.teama.Adb.Employee.Employee;
import edu.wpi.teama.Adb.Location.Location;
import edu.wpi.teama.Adb.MedicalEquipment.MedicalEquipment;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequest;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.List;

public class Adb {

  public static void initialConnection() {

    // Connection to database driver
    System.out.println("----- Apache Derby Connection Testing -----");
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    } catch (ClassNotFoundException e) {
      System.out.println("Apache Derby Not Found");
      e.printStackTrace();
      return;
    }

    System.out.println("Apache Derby driver registered!");

    try {

      // Check if database exist. If not then create one.
      try {
        Connection connection =
            DriverManager.getConnection(
                "jdbc:derby:HospitalDBA;"); // Modify the database name from TowerLocation to Adb
        // for better
        // recognition.
      } catch (SQLException e) {
        Connection c = DriverManager.getConnection("jdbc:derby:HospitalDBA;create=true");
      }

    } catch (SQLException e) {
      System.out.println("Connection failed");
      e.printStackTrace();
      return;
    }

    // Check if each table in the database exist. If not exist then create one.
    // Check TowerLocations table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE TowerLocations(nodeID varchar(25), xcoord int, ycoord int, floor varchar(25), building varchar(25), nodeType varchar(25), longName varchar(100), shortName varchar(50))");

    } catch (SQLException e) {
      System.out.println("Table TowerLocations already exist");
    }

    // Check MedicalEquipment table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE MedicalEquipment(equipmentID varchar(25), equipmentType varchar(25), isClean varchar(25), currentLocation varchar(25), isAvailable varchar(25))");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipment already exist");
    }

    // Check MedicalEquipmentServiceRequest table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      // Can split up TimeStamp to time and date?
      addTable.execute(
          "CREATE TABLE MedicalEquipmentServiceRequest(requestID varchar(25), startLocation varchar(25), endLocation varchar(25), employeeRequested varchar(25), employeeAssigned varchar(25), requestTime time, requestDate date, requestStatus varchar(25), equipmentID varchar(25), requestType varchar(25))");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipmentServiceRequest already exist");
    }

    // Check Employee table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE Employee(employeeID varchar(25), employeeType varchar(25), firstName varchar(25), lastName varchar(25), email varchar(25), phoneNum varchar(25), address varchar(25), startDate date)");

    } catch (SQLException e) {
      System.out.println("Table Employee already exist");
    }
  }

  public static void inputFromCSV(String tableName) {
    switch (tableName) {

        // Table name = Location
      case "TowerLocations":
        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

          List<edu.wpi.teama.Adb.Location.Location> locList = ReadCSV.readLocationCSV();
          for (Location l : locList) {
            Statement addStatement = connection.createStatement();
            addStatement.executeUpdate(
                "INSERT INTO TowerLocations(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) VALUES('"
                    + l.getNodeID()
                    + "', "
                    + l.getXCoord()
                    + ", "
                    + l.getYCoord()
                    + ", '"
                    + l.getFloor()
                    + "', '"
                    + l.getBuilding()
                    + "', '"
                    + l.getNodeType()
                    + "', '"
                    + l.getLongName()
                    + "', '"
                    + l.getShortName()
                    + "')");
            return;
          }
        } catch (SQLException | IOException e) {
          System.out.println("Insertion failed!");
          return;
        }

        // Table name = Employee
      case "Employee":
        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

          List<edu.wpi.teama.Adb.Employee.Employee> List = ReadCSV.readEmployeeCSV();
          for (Employee l : List) {
            Statement addStatement = connection.createStatement();
            addStatement.executeUpdate(
                "INSERT INTO Employee(employeeID varchar(25), employeeType varchar(25), firstName varchar(25), lastName varchar(25), email varchar(25), phoneNum varchar(25), address varchar(25), startDate date) VALUES('"
                    + l.getEmployeeID()
                    + "', "
                    + l.getEmployeeType()
                    + ", "
                    + l.getFirstName()
                    + ", '"
                    + l.getLastName()
                    + "', '"
                    + l.getEmail()
                    + "', '"
                    + l.getPhoneNum()
                    + "', '"
                    + l.getAddress()
                    + "', '"
                    + l.getStartDate()
                    + "')");
            return;
          }
        } catch (SQLException | IOException | ParseException e) {
          System.out.println("Insertion failed!");
          return;
        }

        // Table name = MedicalEquipment
      case "MedicalEquipment":
        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

          List<edu.wpi.teama.Adb.MedicalEquipment.MedicalEquipment> List =
              ReadCSV.readMedicalEquipmentCSV();
          for (MedicalEquipment l : List) {
            Statement addStatement = connection.createStatement();
            addStatement.executeUpdate(
                "INSERT INTO MedicalEquipment( equipmentID varchar(25), equipmentType varchar(25), isClean varchar(25), currentLocation varchar(25), isAvailable varchar(25)) VALUES('"
                    + l.getEquipmentID()
                    + "', "
                    + l.getEquipmentType()
                    + ", "
                    + l.getIsClean()
                    + ", '"
                    + l.getCurrentLocation()
                    + "', '"
                    + l.getIsAvailable()
                    + "')");
            return;
          }
        } catch (SQLException | IOException | ParseException e) {
          System.out.println("Insertion failed!");
          return;
        }

      case "MedicalEquipmentServiceRequest":
        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

          List<edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequest>
              List = ReadCSV.readMedicalEquipmentServiceRequestCSV();
          for (MedicalEquipmentServiceRequest l : List) {
            Statement addStatement = connection.createStatement();
            addStatement.executeUpdate(
                "INSERT INTO MedicalEquipment(requestID varchar(25), startLocation varchar(25), endLocation varchar(25), employeeRequested varchar(25), employeeAssigned varchar(25), requestTime timestamp, requestStatus varchar(25), equipmentID varchar(25), requestType varchar(25)) VALUES('"
                    + l.getRequestID()
                    + "', "
                    + l.getStartLocation()
                    + ", "
                    + l.getEndLocation()
                    + ", '"
                    + l.getEmployeeRequested()
                    + "', '"
                    + l.getEmployeeAssigned()
                    + ", '"
                    + l.getRequestTime()
                    + ", '"
                    + l.getRequestStatus()
                    + ", '"
                    + l.getEquipmentID()
                    + ", '"
                    + l.getRequestType()
                    + "')");
            return;
          }
        } catch (SQLException | IOException | ParseException e) {
          System.out.println("Insertion failed!");
          return;
        }

      default:
        System.out.println("failed!");
        return;
    }
  }
}
