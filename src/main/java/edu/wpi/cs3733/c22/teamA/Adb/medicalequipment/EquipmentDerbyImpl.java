package edu.wpi.cs3733.c22.teamA.Adb.medicalequipment;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EquipmentDerbyImpl implements EquipmentDAO {

  public EquipmentDerbyImpl() {}

  public Equipment getMedicalEquipment(String ID) {
    try {
      Statement get = Adb.connection.createStatement();
      String str = String.format("SELECT * FROM MedicalEquipment WHERE equipment_id = '%s'", ID);

      ResultSet rset = get.executeQuery(str);
      Equipment me = new Equipment();
      if (rset.next()) {
        String equipmentID = rset.getString("equipment_id");
        String equipmentType = rset.getString("equipment_type");
        boolean isClean = rset.getBoolean("is_clean");
        String currentLocation = rset.getString("current_location");
        boolean isAvailable = rset.getBoolean("is_available");

        me = new Equipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable);
      }
      return me;

    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void updateMedicalEquipment(String ID, String field, String change) throws SQLException {
    Statement update = Adb.connection.createStatement();

    String str = "";

    if (change instanceof String) {
      str =
          String.format(
              "UPDATE MedicalEquipment SET " + field + " = '%s' WHERE equipment_id = '%s'",
              change,
              ID);
    } else {
      String change1 = String.valueOf(change);
      str =
          String.format(
              "UPDATE MedicalEquipment SET "
                  + field
                  + " = '"
                  + change1
                  + "' WHERE equipment_id = '%s'",
              ID);
    }

    update.execute(str);
  }

  public void enterMedicalEquipment(Equipment e){
    enterMedicalEquipment(e.getEquipmentID(), e.getEquipmentType(), e.getIsClean(), e.getCurrentLocation(), e.getIsAvailable());
  }

  public void enterMedicalEquipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable) {
    try {
      Statement insert = Adb.connection.createStatement();

      String str =
          String.format(
              "INSERT INTO MedicalEquipment(equipment_id, equipment_type, is_clean, current_location, is_available)"
                  + " VALUES('%s', '%s', '%b', '%s', '%b')",
              equipmentID, equipmentType, isClean, currentLocation, isAvailable);
      insert.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void deleteMedicalEquipment(String ID) {
    try {
      System.out.println("Connection MAde");
      Statement delete = Adb.connection.createStatement();
      String str = String.format("DELETE FROM MedicalEquipment WHERE equipment_id = '%s'", ID);
      delete.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public List<Equipment> getMedicalEquipmentList() {
    List<Equipment> equipList = new ArrayList<>();
    try {
      Statement getNodeList = Adb.connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM MedicalEquipment");

      while (rset.next()) {
        String equipmentID = rset.getString("equipment_id");
        String equipmentType = rset.getString("equipment_type");
        boolean isClean = rset.getBoolean("is_clean");
        String currentLocation = rset.getString("current_location");
        boolean isAvailable = rset.getBoolean("is_available");

        Equipment e =
            new Equipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable);
        equipList.add(e);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }

    return equipList;
  }

  // Read From MedicalEquipment CSV
  public static List<Equipment> readMedicalEquipmentCSV(String csvFilePath)
      throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    ClassLoader classLoader = EquipmentDerbyImpl.class.getClassLoader();
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
        if (dataIndex == 0) thisME.setEquipmentID(data);
        else if (dataIndex == 1) thisME.setEquipmentType(data);
        else if (dataIndex == 2) {
          Boolean boolData = Boolean.parseBoolean(data);
          System.out.println("boolData: " + boolData);
          thisME.setIsClean(boolData);
        } else if (dataIndex == 3) thisME.setCurrentLocation(data);
        else if (dataIndex == 4) {
          Boolean boolData = Boolean.parseBoolean(data);
          thisME.setIsAvailable(boolData);
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

      String isClean = String.valueOf(thisME.getIsClean());
      String isAvailable = String.valueOf(thisME.getIsAvailable());
      writer.write(
          String.join(
              ",",
              thisME.getEquipmentID(),
              thisME.getEquipmentType(),
              isClean,
              thisME.getCurrentLocation(),
              isAvailable));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath) {
    // Check MedicalEquipment table
    try {
      Statement dropTable = Adb.connection.createStatement();

      dropTable.execute("DELETE FROM MedicalEquipment");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }

    try {

      List<Equipment> List = EquipmentDerbyImpl.readMedicalEquipmentCSV(csvFilePath);
      for(Equipment equip : List){
        System.out.println("equip IsClean: " + equip.getIsClean());
      }

      for (Equipment l : List) {
        Statement addStatement = Adb.connection.createStatement();
        addStatement.executeUpdate(
            "INSERT INTO MedicalEquipment( equipment_id, equipment_type, is_clean, current_location, is_available) VALUES('"
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
    }
  }

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException {
    EquipmentDAO equipment = new EquipmentDerbyImpl();
    EquipmentDerbyImpl.writeMedicalEquipmentCSV(equipment.getMedicalEquipmentList(), csvFilePath);
  }
}
