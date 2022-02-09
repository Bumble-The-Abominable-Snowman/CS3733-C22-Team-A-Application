package edu.wpi.cs3733.c22.teamA.Adb.location;

import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LocationDerbyImpl implements LocationDAO {
  List<Location> Location;

  public LocationDerbyImpl() {
    try {

      Location = new ArrayList<Location>();
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
  }

  // Put all nodes in a list.
  public List<Location> getNodeList() {
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
  public void updateLocation(String ID, String field, Object change) {

    String tableName = "TowerLocations";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement updateCoords = connection.createStatement();

      String str = "";
      if (change instanceof String) {
        str =
            String.format(
                "update " + tableName + " set " + field + " = %s where nodeID = '%s'", change, ID);
      } else {
        str =
            String.format(
                "update " + tableName + " set " + field + " = " + change + " where nodeID = '%s'",
                ID);
      }
      updateCoords.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
    return;
  }

  // Method to get node from the location table.
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
      Location l = new Location();
      // process results
      if (rset.next()) {
        String nodeID = rset.getString("nodeID");
        int xc = rset.getInt("xCoord");
        int yc = rset.getInt("yCoord");
        String floor = rset.getString("floor");
        String building = rset.getString("building");
        String nodeType = rset.getString("nodeType");
        String longName = rset.getString("longName");
        String shortName = rset.getString("shortName");

        l = new Location(nodeID, xc, yc, floor, building, nodeType, longName, shortName);
      }

      // Return the location object
      return l;

    } catch (SQLException e) {
      System.out.println("Failed to get node");
      e.printStackTrace();
      return null;
    }
  }

  // Read from Location CSV
  public static List<Location> readLocationCSV(String csvFilePath) throws IOException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
        new Scanner(Location.class.getClassLoader().getResourceAsStream(csvFilePath));
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

  // Write CSV for location table
  public static void writeLocationCSV(List<Location> List, String csvFilePath) throws IOException {

    // create a writer
    File file = new File(csvFilePath);
    file.createNewFile();
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "getNodeID, xCord, yCord, getFloor(),getBuilding, getNodeType, getLongName, getShortName");
    writer.newLine();

    // write location data
    for (Location thisLocation : List) {

      String xCord = String.valueOf(thisLocation.getXCoord());
      String yCord = String.valueOf(thisLocation.getYCoord());
      writer.write(
          String.join(
              ",",
              thisLocation.getNodeID(),
              xCord,
              yCord,
              thisLocation.getFloor(),
              thisLocation.getBuilding(),
              thisLocation.getNodeType(),
              thisLocation.getLongName(),
              thisLocation.getShortName()));
      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // Input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath) {
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
  }

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException {
    LocationDAO Location = new LocationDerbyImpl();
    LocationDerbyImpl.writeLocationCSV(Location.getNodeList(), csvFilePath);
  }
}
