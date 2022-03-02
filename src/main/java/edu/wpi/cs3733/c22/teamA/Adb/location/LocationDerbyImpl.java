package edu.wpi.cs3733.c22.teamA.Adb.location;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
      Statement getNodeList = Adb.connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM TowerLocations");

      while (rset.next()) {
        String nodeID = rset.getString("node_id");
        int xc = rset.getInt("xcoord");
        int yc = rset.getInt("ycoord");
        String floor = rset.getString("floor");
        String building = rset.getString("building");
        String nodeType = rset.getString("node_type");
        String longName = rset.getString("long_name");
        String shortName = rset.getString("short_name");

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
      Statement deleteNode = Adb.connection.createStatement();

      String str =
          String.format(
              "DELETE FROM " + tableName + " WHERE node_id ='%s'", ID); // delete the selected node

      deleteNode.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void enterLocationNode(Location location) {
    enterLocationNode(
        location.getNodeID(),
        location.getXCoord(),
        location.getYCoord(),
        location.getFloor(),
        location.getBuilding(),
        location.getNodeType(),
        location.getLongName(),
        location.getShortName());
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
      Statement enterNode = Adb.connection.createStatement();

      String str =
          String.format(
              "INSERT INTO "
                  + tableName
                  + "(node_id,xcoord,ycoord,floor,building,node_type,long_name,short_name)"
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
      Statement updateCoords = Adb.connection.createStatement();

      String str = "";
      if (change instanceof String) {
        str =
            String.format(
                "update " + tableName + " set " + field + " = '%s' where node_id = '%s'",
                change,
                ID);
      } else {
        str =
            String.format(
                "update " + tableName + " set " + field + " = " + change + " where node_id = '%s'",
                ID);
      }
      updateCoords.execute(str);

    } catch (SQLException e) {
      System.out.println("Error Caught :");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  // Method to get node from the location table.
  public Location getLocationNode(String ID) {

    String tableName = "TowerLocations";
    try {
      Statement getNode = Adb.connection.createStatement();
      String str =
          String.format(
              "select * from " + tableName + " Where node_id = '%s'",
              ID); // get node from table location

      getNode.execute(str);

      // Commented code to print out the selected node.
      ResultSet rset = getNode.getResultSet();
      Location l = new Location();
      // process results
      if (rset.next()) {
        String nodeID = rset.getString("node_id");
        int xc = rset.getInt("xcoord");
        int yc = rset.getInt("ycoord");
        String floor = rset.getString("floor");
        String building = rset.getString("building");
        String nodeType = rset.getString("node_type");
        String longName = rset.getString("long_name");
        String shortName = rset.getString("short_name");

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


      ClassLoader classLoader = LocationDerbyImpl.class.getClassLoader();
      InputStream is = classLoader.getResourceAsStream(csvFilePath);
      Scanner lineScanner = new Scanner(is);

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
    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  public static List<Location> readLocationCSVfile(String csvFilePath) throws IOException {
    // System.out.println("beginning to read csv");

    File file = new File(csvFilePath);
    Scanner lineScanner = new Scanner(file);

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
        "node_id,xcoord,ycoord,floor,building,node_type,long_name,short_name");
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
  public static void inputFromCSV(String csvFilePath) {
    try {
      Statement deleteTable = Adb.connection.createStatement();

      deleteTable.execute("DELETE FROM TowerLocations");
    } catch (SQLException e) {
      System.out.println("Delete on TowerLocations failed");
    }

    try {

      List<Location> locList = LocationDerbyImpl.readLocationCSV(csvFilePath);
      for (Location l : locList) {
        Statement addStatement = Adb.connection.createStatement();
        addStatement.executeUpdate(
            "INSERT INTO TowerLocations(node_id,xcoord,ycoord,floor,building,node_type,long_name,short_name) VALUES('"
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
      System.out.println("Insertion on TowerLocations failed!");
      e.printStackTrace();
      return;
    }
    return;
  }

  // Input from CSV
  public static void inputFromCSVfile(String csvFilePath) {
    try {
      Statement deleteTable = Adb.connection.createStatement();

      deleteTable.execute("DELETE FROM TowerLocations");
    } catch (SQLException e) {
      System.out.println("Delete on TowerLocations failed");
    }

    try {

      List<Location> locList = LocationDerbyImpl.readLocationCSVfile(csvFilePath);
      for (Location l : locList) {
        Statement addStatement = Adb.connection.createStatement();
        addStatement.executeUpdate(
                "INSERT INTO TowerLocations(node_id,xcoord,ycoord,floor,building,node_type,long_name,short_name) VALUES('"
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
      System.out.println("Insertion on TowerLocations failed!");
      e.printStackTrace();
      return;
    }
    return;}

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException {
    LocationDAO Location = new LocationDerbyImpl();
    LocationDerbyImpl.writeLocationCSV(Location.getNodeList(), csvFilePath);
  }
}
