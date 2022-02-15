package edu.wpi.cs3733.c22.teamA.Adb.medicalequipment;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement get = connection.createStatement();
      String str = String.format("SELECT * FROM MedicalEquipment WHERE equipmentID = '%s'", ID);

      ResultSet rset = get.executeQuery(str);
      Equipment me = new Equipment();
      if (rset.next()) {
        String equipmentID = rset.getString("equipmentID");
        String equipmentType = rset.getString("equipmentType");
        boolean isClean = rset.getBoolean("isClean");
        String currentLocation = rset.getString("currentLocation");
        boolean isAvailable = rset.getBoolean("isAvailable");

        me = new Equipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable);
      }
      return me;

    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public void updateMedicalEquipment(String ID, String field, Object change) {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement update = connection.createStatement();

      String str = "";
      if (change instanceof String) {
        str =
            String.format(
                "UPDATE MedicalEquipment SET " + field + " = %s WHERE equipmentID = '%s'",
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
                    + "' WHERE equipmentID = '%s'",
                ID);
      }

      update.execute(str);
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void enterMedicalEquipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable) {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement insert = connection.createStatement();

      String str =
          String.format(
              "INSERT INTO MedicalEquipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable)"
                  + " VALUES('%s', '%s', '%b', '%s', '%b')",
              equipmentID, equipmentType, isClean, currentLocation, isAvailable);
      insert.execute(str);
      connection.close();
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void deleteMedicalEquipment(String ID) {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      System.out.println("Connection MAde");
      Statement delete = connection.createStatement();
      String str = String.format("DELETE FROM MedicalEquipment WHERE equipmentID = '%s'", ID);
      delete.execute(str);
      connection.close();
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public List<Equipment> getMedicalEquipmentList() {
    List<Equipment> equipList = new ArrayList<>();
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement getNodeList = connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM MedicalEquipment");

      while (rset.next()) {
        String equipmentID = rset.getString("equipmentID");
        String equipmentType = rset.getString("equipmentType");
        boolean isClean = rset.getBoolean("isClean");
        String currentLocation = rset.getString("currentLocation");
        boolean isAvailable = rset.getBoolean("isAvailable");

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
        if (dataIndex == 0) thisME.setEquipmentID(data);
        else if (dataIndex == 1) thisME.setEquipmentType(data);
        else if (dataIndex == 2) {
          Boolean boolData = Boolean.parseBoolean(data);
          thisME.setIsClean(boolData);
        } else if (dataIndex == 3) thisME.setCurrentLocation(data);
        else if (dataIndex == 4) {
          Boolean boolData = Boolean.parseBoolean(data);
          thisME.setIsClean(boolData);
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

    writer.write("getEquipmentID, getEquipmentType, isClean, getCurrentLocation, isAvailable");
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
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement dropTable = connection.createStatement();

      dropTable.execute("DELETE FROM MedicalEquipment");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }

    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));

      List<Equipment> List = EquipmentDerbyImpl.readMedicalEquipmentCSV(csvFilePath);
      for (Equipment l : List) {
        Statement addStatement = connection.createStatement();
        addStatement.executeUpdate(
            "INSERT INTO MedicalEquipment( equipmentID, equipmentType, isClean, currentLocation, isAvailable) VALUES('"
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
