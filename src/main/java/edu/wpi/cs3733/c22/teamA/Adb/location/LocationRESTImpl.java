package edu.wpi.cs3733.c22.teamA.Adb.location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import okhttp3.*;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.*;

public class LocationRESTImpl implements LocationDAO {

  private final String url = "https://cs3733c22teama.ddns.net/api/locations";

  public LocationRESTImpl() {

  }

  // Put all nodes in a list.
  public ArrayList<Location> getNodeList() throws IOException, ParseException {

    ArrayList<Location> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> mapArrayList = Adb.getAllREST(url);
    for (HashMap<String, String> map: mapArrayList) {
      Location l = new Location();
      for (String key: map.keySet()) {
        l.setFieldByString(key, map.get(key));
      }
      arrayList.add(l);
    }

    return arrayList;
  }

  // Method to delete nodes from location table.
  public void deleteLocationNode(String ID) throws IOException {

    HashMap<String, String> map = new HashMap<>();
    map.put("node_id", ID);
    map.put("operation", "delete");
    Adb.postREST(url, map);
  }

  public void enterLocationNode(Location location) throws IOException {

    HashMap<String, String> map = location.getStringFields();
    map.put("operation", "add");
    try
    {
      Adb.postREST(url, map);
    } catch (SocketTimeoutException e)
    {
      System.out.println("failed! trying again...");
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
      String shortName) throws IOException {

    Location location = new Location( ID,
                                       xcoord,
                                       ycoord,
                                       floor,
                                       building,
                                       nodeType,
                                       longName,
                                       shortName);
    enterLocationNode(location);
  }

  // Method to update nodes from location table.
  public void updateLocation(Location location) throws SQLException, IOException {

    HashMap<String, String> map = location.getStringFields();
    map.put("operation", "update");
    Adb.postREST(url, map);

  }

  // Method to get node from the location table.
  public Location getLocationNode(String ID) throws IOException, ParseException {

    HashMap<String, String> map = new HashMap<>();
    map.put("operation", "get");
    map.put("node_id", ID);
    HashMap<String, String> resp = Adb.getREST(url, map);
    Location location = new Location();
    for (String key: resp.keySet()) {
      location.setFieldByString(key, resp.get(key));
    }
    return location;
  }

  // Read from Location CSV
  public static List<Location> readLocationCSV(String csvFilePath) throws IOException, ParseException {
    // System.out.println("beginning to read csv");


      ClassLoader classLoader = LocationRESTImpl.class.getClassLoader();
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
        if (dataIndex == 0) thisLocation.setFieldByString("node_id",data);
        else if (dataIndex == 1) {
          intData = Integer.parseInt(data);
          thisLocation.setFieldByString("xcoord", data);
        } else if (dataIndex == 2) {
          intData = Integer.parseInt(data);
          thisLocation.setFieldByString("ycoord", data);
        } else if (dataIndex == 3) thisLocation.setFieldByString("floor", data);
        else if (dataIndex == 4) thisLocation.setFieldByString("building", data);
        else if (dataIndex == 5) thisLocation.setFieldByString("node_type", data);
        else if (dataIndex == 6) thisLocation.setFieldByString("long_name", data);
        else if (dataIndex == 7) thisLocation.setFieldByString("short_name", data);
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

  public static List<Location> readLocationCSVfile(String csvFilePath) throws IOException, ParseException {
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
        if (dataIndex == 0) thisLocation.setFieldByString("node_id", data);
        else if (dataIndex == 1) {
          thisLocation.setFieldByString("xcoord", data);
        } else if (dataIndex == 2) {
          thisLocation.setFieldByString("ycoord", data);
        } else if (dataIndex == 3) thisLocation.setFieldByString("floor", data);
        else if (dataIndex == 4) thisLocation.setFieldByString("building", data);
        else if (dataIndex == 5) thisLocation.setFieldByString("node_type", data);
        else if (dataIndex == 6) thisLocation.setFieldByString("long_name", data);
        else if (dataIndex == 7) thisLocation.setFieldByString("short_name", data);
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

      String xCord = String.valueOf(thisLocation.getStringFields().get("xcoord"));
      String yCord = String.valueOf(thisLocation.getStringFields().get("ycoord"));
      writer.write(
          String.join(
              ",",
              thisLocation.getStringFields().get("node_id"),
              xCord,
              yCord,
              thisLocation.getStringFields().get("floor"),
              thisLocation.getStringFields().get("building"),
              thisLocation.getStringFields().get("node_type"),
              thisLocation.getStringFields().get("long_name"),
              thisLocation.getStringFields().get("short_name")));
      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // Input from CSV
  public void inputFromCSVfile(String csvFilePath) throws IOException, ParseException {
    List<Location> locationArrayListPrev = getNodeList();
    for (Location location: locationArrayListPrev) {
      deleteLocationNode(location.getStringFields().get("node_id"));
    }

    List<Location> locationArrayList = LocationRESTImpl.readLocationCSVfile(csvFilePath);
    for (Location l: locationArrayList) {
      enterLocationNode(l);
    }
  }

  // Export to CSV
  public static void exportToCSV(String csvFilePath) throws IOException, ParseException {
    LocationDAO Location = new LocationRESTImpl();
    LocationRESTImpl.writeLocationCSV(Location.getNodeList(), csvFilePath);
  }
}
