package edu.wpi.cs3733.c22.teamA.Adb.medicalequipment;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationRESTImpl;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.*;

public class EquipmentRESTImpl implements EquipmentDAO {

  private final String url = "https://cs3733c22teama.ddns.net/api/equipment";

  public EquipmentRESTImpl() {}

  public Equipment getMedicalEquipment(String ID) throws IOException {
    HashMap<String, String> map = new HashMap<>();
    map.put("operation", "get");
    map.put("equipment_id", ID);
    HashMap<String, String> resp = Adb.getREST(url, map);
    Equipment equipment = new Equipment();
    for (String key: resp.keySet()) {
      equipment.setFieldByString(key, resp.get(key));
    }
    return equipment;
  }

  public void updateMedicalEquipment(Equipment e)
          throws SQLException, IOException {
    HashMap<String, String> map = e.getStringFields();
    map.put("operation", "update");
    Adb.postREST(url, map);
  }

  public void enterMedicalEquipment(Equipment e) throws IOException {
    HashMap<String, String> map = e.getStringFields();
    map.put("operation", "add");
    Adb.postREST(url, map);
  }


  public void enterMedicalEquipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable) throws IOException {

    Equipment equipment = new Equipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable);
    enterMedicalEquipment(equipment);
  }

  public void deleteMedicalEquipment(String ID) throws IOException {
    HashMap<String, String> map = new HashMap<>();
    map.put("equipment_id", ID);
    map.put("operation", "delete");
    Adb.postREST(url, map);
  }

  public List<Equipment> getMedicalEquipmentList() throws IOException {
    List<Equipment> arrayList = new ArrayList<>();
    List<HashMap<String, String>> mapArrayList = Adb.getAllREST(url);
    for (HashMap<String, String> map: mapArrayList) {
      Equipment l = new Equipment();
      for (String key: map.keySet()) {
        l.setFieldByString(key, map.get(key));
      }
      arrayList.add(l);
    }

    return arrayList;
  }

  // Read From MedicalEquipment CSV
  public static List<Equipment> readMedicalEquipmentCSV(String csvFilePath)
      throws IOException, ParseException {
    // System.out.println("beginning to read csv");

      ClassLoader classLoader = EquipmentRESTImpl.class.getClassLoader();
      InputStream is = classLoader.getResourceAsStream(csvFilePath);
      Scanner lineScanner = new Scanner(is);

    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<Equipment> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      Equipment thisME = new Equipment();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisME.setFieldByString("equipment_id", data);
        else if (dataIndex == 1) thisME.setFieldByString("equipment_type", data);
        else if (dataIndex == 2) {
          Boolean boolData = Boolean.parseBoolean(data);
          System.out.println("boolData: " + boolData);
          thisME.setFieldByString("is_clean", data);
        } else if (dataIndex == 3) thisME.setFieldByString("current_location", data);
        else if (dataIndex == 4) {
          Boolean boolData = Boolean.parseBoolean(data);
          thisME.setFieldByString("is_available", data);
        } else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisME);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  public static List<Equipment> readMedicalEquipmentCSVfile(String csvFilePath)
          throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    File file = new File(csvFilePath);
    Scanner lineScanner = new Scanner(file);

    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<Equipment> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      Equipment thisME = new Equipment();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisME.setFieldByString("equipment_id", data);
        else if (dataIndex == 1) thisME.setFieldByString("equipment_type", data);
        else if (dataIndex == 2) {
          thisME.setFieldByString("is_clean", data);
        } else if (dataIndex == 3) thisME.setFieldByString("current_location", data);
        else if (dataIndex == 4) {
          thisME.setFieldByString("is_available", data);
        } else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisME);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  // Write CSV for MedicalEquipment table
  public static void writeMedicalEquipmentCSV(List<Equipment> List, String csvFilePath)
      throws IOException {

    // create a writer
    File file = new File(csvFilePath);
    file.createNewFile();
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write("equipment_id, equipment_type, is_clean, current_location, is_available");
    writer.newLine();

    // write location data
    for (Equipment thisME : List) {

      String isClean = String.valueOf(thisME.getStringFields().get("is_clean"));
      String isAvailable = String.valueOf(thisME.getStringFields().get("is_available"));
      writer.write(
          String.join(
              ",",
              thisME.getStringFields().get("equipment_id"),
              thisME.getStringFields().get("equipment_type"),
              isClean,
              thisME.getStringFields().get("current_location"),
              isAvailable));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public void inputFromCSV(String csvFilePath) throws IOException, ParseException {
    List<Equipment> arrayListPrev = this.getMedicalEquipmentList();
    for (Equipment location: arrayListPrev) {
      deleteMedicalEquipment(location.getStringFields().get("equipment_id"));
    }

    List<Equipment> arrayList = readMedicalEquipmentCSVfile(csvFilePath);
    for (Equipment l: arrayList) {
      enterMedicalEquipment(l);
    }
  }

  public void inputFromCSVfile(String csvFilePath) throws IOException, ParseException {
    List<Equipment> arrayListPrev = this.getMedicalEquipmentList();
    for (Equipment location: arrayListPrev) {
      deleteMedicalEquipment(location.getStringFields().get("equipment_id"));
    }

    List<Equipment> arrayList = readMedicalEquipmentCSVfile(csvFilePath);
    for (Equipment l: arrayList) {
      enterMedicalEquipment(l);
    }
  }
}
