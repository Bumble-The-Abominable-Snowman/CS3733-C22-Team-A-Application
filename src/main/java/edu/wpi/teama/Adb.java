package edu.wpi.teama;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Adb {

  public static void initialConnection(List<Location> locList) {

    //Connection to database driver
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

      //Check if database exist. If not then create one.
      try {
        Connection connection = DriverManager.getConnection("jdbc:derby:Adb;");//Modify the database name from TowerLocation to Adb for better recognition.
      } catch (SQLException e) {
        Connection c = DriverManager.getConnection("jdbc:derby:Adb;create=true");
      }

    } catch (SQLException e) {
      System.out.println("Connection failed");
      e.printStackTrace();
      return;
    }

    //Check if each table in the database exist. If not exist then create one.

    //Check TowerLocations table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:Adb;");
      Statement addTable = connection.createStatement();

      addTable.execute(
              "CREATE TABLE TowerLocations(nodeID varchar(25), xcoord int, ycoord int, floor varchar(25), building varchar(25), nodeType varchar(25), longName varchar(100), shortName varchar(50))");

    } catch (SQLException e) {
      System.out.println("Table TowerLocations already exist");
      return;
    }

    //Check MedicalEquipment table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:Adb;");
      Statement addTable = connection.createStatement();

      addTable.execute(
              "CREATE TABLE MedicalEquipment(equipmentID varchar(25), equipmentType varchar(25), isClean varchar(25), currentLocation varchar(25), isAvailable varchar(25))");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipment already exist");
      return;
    }

    //Check MedicalEquipmentServiceRequest table.
    try {

      Connection connection = DriverManager.getConnection("jdbc:derby:Adb;");
      Statement addTable = connection.createStatement();

      //Can split up TimeStamp to time and date?
      addTable.execute(
              "CREATE TABLE MedicalEquipmentServiceRequest(requestID varchar(25), startLocation varchar(25), endLocation varchar(25), employeeRequested varchar(25), employeeAssigned varchar(25), requestTime time, requestDate date, requestStatus varchar(25), equipmentID varchar(25), requestType varchar(25))");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipmentServiceRequest already exist");
      return;
    }

    //Check Employee table.
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

    //Put data stored in locList into TowerLocations table.
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

  //Get node from a table.
  public static void getNode(String nodeID) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:TowerLocations;");
      Statement getNode = connection.createStatement();
      String str =
          String.format(
              "select * from TowerLocations Where nodeID = '%s'",
              nodeID); // get node from table location

      getNode.execute(str);
      ResultSet rset = getNode.getResultSet();

      int getX = 0, getY = 0;
      String getID = "",
          getFloor = "",
          getBuilding = "",
          getNodeType = "",
          getLongName = "",
          getShortName = "";
      // process results
      while (rset.next()) {
        getX = rset.getInt("xcoord");
        getY = rset.getInt("ycoord");
        getFloor = rset.getString("floor");
        getID = rset.getString("nodeID");
        getBuilding = rset.getString("building");
        getNodeType = rset.getString("nodeType");
        getLongName = rset.getString("longName");
        getShortName = rset.getString("shortName");
        System.out.println(
            "nodeID: "
                + getID
                + " xcoord: "
                + getX
                + " ycoord: "
                + getY
                + " floor: "
                + getFloor
                + " building: "
                + getBuilding
                + " nodeType: "
                + getNodeType
                + " longName: "
                + getLongName
                + " shortName: "
                + getShortName);
        // print out the selected node
      }

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  //Update node from a table.
  public static void updateCoordinates(String nodeID, int xcoord, int ycoord) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:TowerLocations;");
      Statement updateCoords = connection.createStatement();

      String str =
          String.format(
              "update TowerLocations set xcoord = %d, ycoord = %d where nodeID = '%s'",
              xcoord, ycoord,
              nodeID); // update the x and y coord for specific node which ID = input nodeID.

      updateCoords.execute(str);
      System.out.println("Coordinates updated for node " + nodeID);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
    return;
  }

  //Add node to a table
  public static void enterNode(
      String nodeID,
      int xcoord,
      int ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:TowerLocations;");
      Statement enterNode = connection.createStatement();

      String str =
          String.format(
              "INSERT INTO TowerLocations(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName)"
                  + "VALUES ('%s', %d, %d, '%s', '%s', '%s', '%s', '%s')",
              nodeID, xcoord, ycoord, floor, building, nodeType, longName,
              shortName); // insert values from input.

      enterNode.execute(str);
      System.out.println("New node added");

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  //delete node from a table.
  public static void deleteNode(String nodeID) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:TowerLocations;");
      Statement deleteNode = connection.createStatement();

      String str =
          String.format(
              "DELETE FROM TowerLocations WHERE nodeID ='%s'", nodeID); // delete the selected node

      deleteNode.execute(str);
      System.out.println("node " + nodeID + " deleted");

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public static List<Location> getNodeList() {
    List<Location> locList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:TowerLocations;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM TowerLocations");

      while (rset.next()) {
        String nodeID = rset.getString("nodeID");
        int xc = rset.getInt("xCoord");
        int yc = rset.getInt("yCoord");
        String floor = rset.getString("floor");
        String building = rset.getString("building");
        String nodeType = rset.getString("nodeType");
        String longName = rset.getString("longName");
        String shortName = rset.getString("shortName");

        Location l = new Location(nodeID, xc, yc, floor, building, nodeType, longName, shortName);
        locList.add(l);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }

    return locList;
  }
}
