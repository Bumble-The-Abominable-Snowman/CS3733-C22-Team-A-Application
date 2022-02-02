package edu.wpi.teama.Adb.MedicalEquipment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalEquipmentImpl implements MedicalEquipmentDAO {

  public static MedicalEquipmentServiceRequest getMedicalEquipment(String ID) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str = String.format("SELECT * FROM MedicalEquipment WHERE equipmentID = '%s'", ID);

      ResultSet rset = get.executeQuery(str);
      String equipmentID = rset.getString("equipmentID");
      String equipmentType = rset.getString("equipmentType");
      boolean isClean = rset.getBoolean("isClean");
      String currentLocation = rset.getString("currentLocation");
      boolean isAvailable = rset.getBoolean("isAvailable");

      MedicalEquipmentServiceRequest me =
          new MedicalEquipmentServiceRequest(
              equipmentID, equipmentType, isClean, currentLocation, isAvailable);

      return me;

    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public static void updateMedicalEquipment(String ID, String field, String change) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement update = connection.createStatement();
      String str =
          String.format(
              "UPDATE MedicalEquipment SET " + field + " = %s WHERE nodeID = '%s'", change, ID);
      update.execute(str);
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public static void enterMedicalEquipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement insert = connection.createStatement();

      String str =
          String.format(
              "INSERT INTO MedicalEquipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable)"
                  + " VALUES('%s', '%s', '%b', '%s', '%b')",
              equipmentID, equipmentType, isClean, currentLocation, isAvailable);
      insert.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public static void deleteMedicalEquipment(String ID) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement delete = connection.createStatement();
      String str = String.format("DELETE FROM MedicalEquipment WHERE equipmentID = '%s'", ID);
      delete.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public static List<MedicalEquipmentServiceRequest> getMedicalEquipmentList() {
    List<MedicalEquipmentServiceRequest> equipList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM MedicalEquipment");

      while (rset.next()) {
        String equipmentID = rset.getString("equipmentID");
        String equipmentType = rset.getString("equipmentType");
        boolean isClean = rset.getBoolean("isClean");
        String currentLocation = rset.getString("currentLocation");
        boolean isAvailable = rset.getBoolean("isAvailable");

        MedicalEquipmentServiceRequest e =
            new MedicalEquipmentServiceRequest(
                equipmentID, equipmentType, isClean, currentLocation, isAvailable);
        equipList.add(e);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }

    return equipList;
  }
}
