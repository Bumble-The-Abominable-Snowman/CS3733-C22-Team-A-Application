package edu.wpi.cs3733.c22.teamA.Adb;

import edu.wpi.cs3733.c22.teamA.Adb.Employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.Employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.Location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.Location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.MedicalEquipment.MedicalEquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.MedicalEquipment.MedicalEquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.ServiceRequest.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.ServiceRequest.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.MedicalEquipment;
import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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

    // Check ServiceRequest table.
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
              "CREATE TABLE ServiceRequest(requestID varchar(25), startLocation varchar(25), endLocation varchar(25), employeeRequested varchar(25), employeeAssigned varchar(25), requestTime timestamp, requestStatus varchar(25), requestType varchar(25), comments varchar(255))");

    } catch (SQLException e) {
      System.out.println("Table ServiceRequest already exist");
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
      System.out.println("Table LanguageServiceRequest already exist");
    }

    //   Check Laundry  table.
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement addTable = connection.createStatement();

      addTable.execute(
              "CREATE TABLE LaundryServiceRequest(requestID varchar(25), washMode varchar(25), CONSTRAINT FOREIGN KEY requestID REFERENCES ServiceRequest(requestID))");

    } catch (SQLException e) {
      System.out.println("Table LaundryServiceRequest already exist");
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

    if (!isInitialized) {
      inputFromCSV("TowerLocations", "edu/wpi/teama/db/TowerLocations.csv");
      inputFromCSV("Employee", "edu/wpi/teama/db/Employee.csv");
      inputFromCSV(
              "MedicalEquipmentServiceRequest", "edu/wpi/teama/db/MedicalEquipmentServiceRequest.csv");
      inputFromCSV("MedicalEquipment", "edu/wpi/teama/db/MedicalEquipment.csv");
    }
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath) {
    switch (tableName) {

      // Table name = Location
      case "TowerLocations":

        // Check location table
        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
          Statement deleteTable = connection.createStatement();

          deleteTable.execute("DELETE FROM TowerLocations");
        } catch (SQLException e) {
          System.out.println("Delete failed");
        }

        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

          List<Location> locList = LocationDerbyImpl.readLocationCSV(csvFilePath);
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
          }
        } catch (SQLException | IOException e) {
          System.out.println("Insertion failed!");
          return;
        }
        return;

      // Table name = Employee
      case "Employee":

        // Check employee table
        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
          Statement dropTable = connection.createStatement();

          dropTable.execute("DELETE FROM Employee");
        } catch (SQLException e) {
          System.out.println("Delete failed");
        }

        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

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
          System.out.println("Insertion failed!");
          return;
        }
        return;

      // Table name = MedicalEquipment
      case "MedicalEquipment":

        // Check MedicalEquipment table
        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
          Statement dropTable = connection.createStatement();

          dropTable.execute("DELETE FROM MedicalEquipment");
        } catch (SQLException e) {
          System.out.println("delete failed");
        }

        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

          List<MedicalEquipment> List = MedicalEquipmentDerbyImpl.readMedicalEquipmentCSV(csvFilePath);
          for (MedicalEquipment l : List) {
            Statement addStatement = connection.createStatement();
            addStatement.executeUpdate(
                    "INSERT INTO MedicalEquipment( equipmentID, equipmentType, isClean, currentLocation, isAvailable) VALUES('"
                            + l.getEquipmentID()
                            + "', '"
                            + l.getEquipmentType()
                            + "', '"
                            + l.getIsClean()
                            + "', '"
                            + l.getCurrentLocation()
                            + "', '"
                            + l.getIsAvailable()
                            + "')");
          }
        } catch (SQLException | IOException | ParseException e) {
          System.out.println("Insertion failed!");
          return;
        }
        return;

      case "MedicalEquipmentServiceRequest":

        // Check MedicalEquipmentServiceRequest table
        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
          Statement dropTable = connection.createStatement();

          dropTable.execute("DELETE FROM MedicalEquipmentServiceRequest");
        } catch (SQLException e) {
          System.out.println("delete failed");
        }

        try {
          Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

          List<MedicalEquipmentServiceRequest> List =
                  MedicalEquipmentServiceRequestDerbyImpl.readMedicalEquipmentServiceRequestCSV(csvFilePath);
          for (MedicalEquipmentServiceRequest l : List) {
            Statement addStatement = connection.createStatement();
            addStatement.executeUpdate(
                    "INSERT INTO MedicalEquipmentServiceRequest(requestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, equipmentID, requestType) VALUES('"
                            + l.getRequestID()
                            + "', '"
                            + l.getStartLocation()
                            + "', '"
                            + l.getEndLocation()
                            + "', '"
                            + l.getEmployeeRequested()
                            + "', '"
                            + l.getEmployeeAssigned()
                            + "', '"
                            + l.getRequestTime().toString()
                            + "', '"
                            + l.getRequestStatus()
                            + "', '"
                            + l.getEquipmentID()
                            + "', '"
                            + l.getRequestType()
                            + "')");
          }
        } catch (SQLException | IOException | ParseException e) {
          System.out.println("Insertion failed!");
          return;
        }
        return;

      default:
        System.out.println("failed!");
        return;
    }
  }

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException {
    switch (tableName) {
      case "TowerLocations":
        LocationDAO Location = new LocationDerbyImpl();
        Location.writeLocationCSV(Location.getNodeList(), csvFilePath);

      case "Employee":
        EmployeeDAO Employee = new EmployeeDerbyImpl();
        Employee.writeEmployeeCSV(Employee.getEmployeeList(), csvFilePath);

      case "MedicalEquipment":
        MedicalEquipmentDAO equipment = new MedicalEquipmentDerbyImpl();
        equipment.writeMedicalEquipmentCSV(equipment.getMedicalEquipmentList(), csvFilePath);

      case "MedicalEquipmentServiceRequest":
        MedicalEquipmentServiceRequestDAO mesr = new MedicalEquipmentServiceRequestDerbyImpl();
        mesr.writeMedicalEquipmentServiceRequestCSV(
                mesr.getMedicalEquipmentServiceRequestList(), csvFilePath);
    }
  }
}

