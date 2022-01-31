package edu.wpi.teama;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Adb {

  public static void initialConnection(List<Location> locList) {
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
      try {
        Connection connection = DriverManager.getConnection("jdbc:derby:TowerLocations;");
      } catch (SQLException e) {
        Connection c = DriverManager.getConnection("jdbc:derby:TowerLocations;create=true");
      }

      Connection connection = DriverManager.getConnection("jdbc:derby:TowerLocations;");
      /*Statement dropTableLocations = c.createStatement();
      dropTableLocations.execute("DROP TABLE TowerLocations");*/
      Statement addTableLocation = connection.createStatement();
      try {
        addTableLocation.execute(
            "CREATE TABLE TowerLocations(nodeID varchar(25), xcoord int, ycoord int, floor varchar(25), building varchar(25), nodeType varchar(25), longName varchar(100), shortName varchar(50))");
      } catch (SQLException e) {
        System.out.println("Table already exist");
        return;
      }

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
        // System.out.println(addition);
      }

      System.out.println("Data successfully added");

      // Example Query
      /*Statement getData = c.createStatement();
      ResultSet rset = getData.executeQuery("SELECT nodeID, longName FROM TowerLocations");
      rset.next();
      System.out.println("printing out data");
      while(rset.next()){
        System.out.println("nodeID: " + rset.getString("nodeID") +
                "   longName: " + rset.getString("longName"));
      }
      */

    } catch (SQLException e) {
      System.out.println("Connection failed");
      e.printStackTrace();
      return;
    }
  }

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
