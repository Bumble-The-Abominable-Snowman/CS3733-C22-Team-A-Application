package edu.wpi.cs3733.c22.teamA.Adb;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.MedicalEquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.medicalequipmentservicerequest.MedicalEquipmentServiceRequestDerbyImpl;
import java.sql.*;

public class Adb {

  public static void initialConnection() {

    boolean isInitialized = false;
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
        isInitialized = true;
        // for better
        // recognition.
      } catch (SQLException e) {
        Connection c = DriverManager.getConnection("jdbc:derby:HospitalDBA;create=true");
        isInitialized = false;
      }

    } catch (SQLException e) {
      System.out.println("Connection failed");
      e.printStackTrace();
      return;
    }

    /*    try {
         Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
         Statement drop = connection.createStatement();
         drop.execute("DROP TABLE MedicalEquipmentServiceRequest");

       } catch (SQLException e) {

       }
    */

    // Check if tables exist
    // Check Locations table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE TowerLocations(nodeID varchar(25), xcoord int, ycoord int, floor varchar(25), building varchar(25), nodeType varchar(25), longName varchar(100), shortName varchar(50))");

    } catch (SQLException e) {
      System.out.println("Table TowerLocations already exist");
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

    // Check MedicalEquipment table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE MedicalEquipment(equipmentID varchar(25), equipmentType varchar(25), isClean varchar(25), currentLocation varchar(25), isAvailable varchar(25))");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipment already exist");
    }

    // Check ServiceRequestDerbyImpl table.
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE ServiceRequest(requestID varchar(25), startLocation varchar(25), endLocation varchar(25), employeeRequested varchar(25), employeeAssigned varchar(25), requestTime timestamp, requestStatus varchar(25), requestType varchar(25), comments varchar(255))");

    } catch (SQLException e) {
      System.out.println("Table ServiceRequestDerbyImpl already exist");
    }

    // Check MedicalEquipmentServiceRequest table.
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE MedicalEquipmentServiceRequest(requestID varchar(25), equipmentID varchar(25), CONSTRAINT FOREIGN KEY requestID REFERENCES ServiceRequest(requestID))");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipmentServiceRequest already exist");
    }

    // Check Food Delivery Table
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE FoodDeliveryServiceRequest(requestID varchar(25), mainDish varchar(50), sideDish varchar(50), beverage varchar(50), dessert varchar(50), CONSTRAINT FOREIGN KEY requestID REFERENCES ServiceRequest(requestID))");

    } catch (SQLException e) {
      System.out.println("Table FoodDeliveryServiceRequest already exist");
    }

    // Check Language  table.
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE LanguageServiceRequest(requestID varchar(25), language varchar(25), CONSTRAINT FOREIGN KEY requestID REFERENCES ServiceRequest(requestID))");

    } catch (SQLException e) {
      System.out.println("Table languageservicerequest already exist");
    }

    //   Check Laundry  table.
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE LaundryServiceRequest(requestID varchar(25), washMode varchar(25), CONSTRAINT FOREIGN KEY requestID REFERENCES ServiceRequest(requestID))");

    } catch (SQLException e) {
      System.out.println("Table laundryservicerequest already exist");
    }

    //  Check Religious  table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE ReligiousServiceRequest(requestID varchar(25), religion varchar(25), CONSTRAINT FOREIGN KEY requestID REFERENCES ServiceRequest(requestID))");

    } catch (SQLException e) {
      System.out.println("Table ReligiousServiceRequest already exist");
    }

    // Check Sanitation  table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE SanitationServiceRequest(requestID varchar(25), sanitationType varchar(25), CONSTRAINT FOREIGN KEY requestID REFERENCES ServiceRequest(requestID))");

    } catch (SQLException e) {
      System.out.println("Table SanitationServiceRequest already exist");
    }

    // Initialize the database and input data
    if (!isInitialized) {
      LocationDerbyImpl.inputFromCSV("TowerLocations", "edu/wpi/teama/db/TowerLocations.csv");
      EmployeeDerbyImpl.inputFromCSV("Employee", "edu/wpi/teama/db/Employee.csv");
      MedicalEquipmentDerbyImpl.inputFromCSV(
          "MedicalEquipment", "edu/wpi/teama/db/MedicalEquipment.csv");
      MedicalEquipmentServiceRequestDerbyImpl.inputFromCSV(
          "MedicalEquipmentServiceRequest", "edu/wpi/teama/db/MedicalEquipmentServiceRequest.csv");
    }
  }
}
