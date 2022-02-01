package edu.wpi.teama.Adb;

import edu.wpi.teama.Adb.Location.Location;

import java.sql.*;
import java.util.List;

public class Adb {

  public static void initialConnection(List<Location> locList) {

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
                "jdbc:derby:Adb;"); // Modify the database name from TowerLocation to Adb for better
        // recognition.
      } catch (SQLException e) {
        Connection c = DriverManager.getConnection("jdbc:derby:Adb;create=true");
      }

    } catch (SQLException e) {
      System.out.println("Connection failed");
      e.printStackTrace();
      return;
    }

    // Check if each table in the database exist. If not exist then create one.
    // Check TowerLocations table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:Adb;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE TowerLocations(nodeID varchar(25), xcoord int, ycoord int, floor varchar(25), building varchar(25), nodeType varchar(25), longName varchar(100), shortName varchar(50))");

    } catch (SQLException e) {
      System.out.println("Table TowerLocations already exist");
      return;
    }

    // Check MedicalEquipment table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:Adb;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE MedicalEquipment(equipmentID varchar(25), equipmentType varchar(25), isClean varchar(25), currentLocation varchar(25), isAvailable varchar(25))");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipment already exist");
      return;
    }

    // Check MedicalEquipmentServiceRequest table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:Adb;");
      Statement addTable = connection.createStatement();

      // Can split up TimeStamp to time and date?
      addTable.execute(
          "CREATE TABLE MedicalEquipmentServiceRequest(requestID varchar(25), startLocation varchar(25), endLocation varchar(25), employeeRequested varchar(25), employeeAssigned varchar(25), requestTime time, requestDate date, requestStatus varchar(25), equipmentID varchar(25), requestType varchar(25))");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipmentServiceRequest already exist");
      return;
    }

    // Check Employee table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:Adb;");
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE Employee(employeeID varchar(25), employeeType varchar(25), firstName varchar(25), lastName varchar(25), email varchar(25), phoneNum varchar(25), address varchar(25), startDate date)");

    } catch (SQLException e) {
      System.out.println("Table Employee already exist");
      return;
    }
  }

  // Put data stored in locList into TowerLocations table.
  /*    for (Location l : locList) {
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
                  + "')");*/
  // System.out.println(addition);

}
