package edu.wpi.teama;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Location {

  String nodeID;
  int xCoord;
  int yCoord;
  String floor;
  String building;
  String nodeType;
  String longName;
  String shortName;

  public Location() {}

  public Location(
      String nodeID,
      int xCoord,
      int yCoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {

    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setXCoord(int xCoord) {
    this.xCoord = xCoord;
  }

  public void setYCoord(int yCoord) {
    this.yCoord = yCoord;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public String getNodeID() {
    return nodeID;
  }

  public int getXCoord() {
    return xCoord;
  }

  public int getYCoord() { return yCoord; }

  public String getFloor() {
    return floor;
  }

  public String getBuilding() {
    return building;
  }

  public String getNodeType() {
    return nodeType;
  }

  public String getLongName() {
    return longName;
  }

  public String getShortName() {
    return shortName;
  }

/*  @Override
  public String toString() { // Used to format for printing to console, not useful otherwise.
    return getNodeID()
        + getXCoord()
        + getYCoord()
        + getFloor()
        + getBuilding()
        + getNodeType()
        + getLongName()
        + getShortName();
  }*/

  //Method to get node from the location table.
  public Location getLocationNode(String ID){

    String tableName = "TowerLocations";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:"+ tableName +";");
      Statement getNode = connection.createStatement();
      String str =
              String.format(
                      "select * from " + tableName + " Where nodeID = '%s'",
                      nodeID); // get node from table location

      getNode.execute(str);

      //Commented code to print out the selected node.
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

      //Return the location object
      return l;

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return null;
    }
  }

  //Method to update nodes from location table.
  public void updateLocation(String ID, String field, String change){

    String tableName = "TowerLocations";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:" + tableName + ";");
      Statement updateCoords = connection.createStatement();

      String str =
              String.format(
                      "update " + tableName + " set "+ field +" = %s where nodeID = '%s'",
                      change, ID);
      updateCoords.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
    return;
  }

  //Method to add node to location table.
  public void enterLocationNode(String ID, int xcoord, int ycoord, String floor, String building, String nodeType, String longName, String shortName){

    String tableName = "TowerLocations";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:"+ tableName +";");
      Statement enterNode = connection.createStatement();

      String str =
              String.format(
                      "INSERT INTO "+ tableName +"(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName)"
                              + "VALUES ('%s', %d, %d, '%s', '%s', '%s', '%s', '%s')",
                      nodeID, xcoord, ycoord, floor, building, nodeType, longName,
                      shortName); // insert values from input.

      enterNode.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }

  }

  //Method to delete nodes from location table.
  public void deleteLocationNode(String ID){

    String tableName = "TowerLocations";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:"+ tableName +";");
      Statement deleteNode = connection.createStatement();

      String str =
              String.format(
                      "DELETE FROM "+ tableName +" WHERE nodeID ='%s'", nodeID); // delete the selected node

      deleteNode.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  //Put all nodes in a list.
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
