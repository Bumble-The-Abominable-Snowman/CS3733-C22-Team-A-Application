package edu.wpi.teama.Adb.Location;

import java.sql.*;
import java.util.List;

public class LocationDerbyImpl implements LocationDAO {

  public LocationDerbyImpl() {}

  // Put all nodes in a list.
  public List<Location> getNodeList() {
    List<Location> Location = null;
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
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
        Location.add(l);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
    return Location;
  }

  // Method to delete nodes from location table.
  public void deleteLocationNode(String ID) {

    String tableName = "TowerLocations";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement deleteNode = connection.createStatement();

      String str =
          String.format(
              "DELETE FROM " + tableName + " WHERE nodeID ='%s'", ID); // delete the selected node

      deleteNode.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  // Method to add node to location table.
  @Override
  public void enterLocationNode(
      String ID,
      int xcoord,
      int ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {

    String tableName = "TowerLocations";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement enterNode = connection.createStatement();

      String str =
          String.format(
              "INSERT INTO "
                  + tableName
                  + "(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName)"
                  + "VALUES ('%s', %d, %d, '%s', '%s', '%s', '%s', '%s')",
              ID,
              xcoord,
              ycoord,
              floor,
              building,
              nodeType,
              longName,
              shortName); // insert values from input.

      enterNode.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  // Method to update nodes from location table.
  public void updateLocation(String ID, String field, String change) {

    String tableName = "TowerLocations";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement updateCoords = connection.createStatement();

      String str =
          String.format(
              "update " + tableName + " set " + field + " = %s where nodeID = '%s'", change, ID);
      updateCoords.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
    return;
  }

  // Method to get node from the location table.
  @Override
  public Location getLocationNode(String ID) {

    String tableName = "TowerLocations";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNode = connection.createStatement();
      String str =
          String.format(
              "select * from " + tableName + " Where nodeID = '%s'",
              ID); // get node from table location

      getNode.execute(str);

      // Commented code to print out the selected node.
      ResultSet rset = getNode.getResultSet();
      // process results
      String nodeID = rset.getString("nodeID");
      int xc = rset.getInt("xCoord");
      int yc = rset.getInt("yCoord");
      String floor = rset.getString("floor");
      String building = rset.getString("building");
      String nodeType = rset.getString("nodeType");
      String longName = rset.getString("longName");
      String shortName = rset.getString("shortName");

      Location l = new Location(nodeID, xc, yc, floor, building, nodeType, longName, shortName);

      // Return the location object
      return l;

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return null;
    }
  }
}
