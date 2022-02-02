package edu.wpi.teama.Adb.MedicalEquipmentServiceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalEquipmentServiceRequestImpl implements MedicalEquipmentServiceRequestDAO {

  public static MedicalEquipmentServiceRequest getMedicalEquipmentServiceRequest(String ID) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
          String.format(
              "SELECT * FROM MedicalEquipmentServiceRequest WHERE equipmentID = '%s'", ID);

      ResultSet rset = get.executeQuery(str);
      String requestID = rset.getString("requestID");
      String startLocation = rset.getString("startLocation");
      String endLocation = rset.getString("endLocation");
      String employeeRequested = rset.getString("employeeRequested");
      String employeeAssigned = rset.getString("employeeAssigned");
      Timestamp requestTime = rset.getTimestamp("requestTime");
      String requestStatus = rset.getString("requestStatus");
      String equipmentID = rset.getString("equipmentID");
      String requestType = rset.getString("requestType");

      MedicalEquipmentServiceRequest mesr =
          new MedicalEquipmentServiceRequest(
              requestID,
              startLocation,
              endLocation,
              employeeRequested,
              employeeAssigned,
              requestTime,
              requestStatus,
              equipmentID,
              requestType);

      return mesr;

    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public static void updateMedicalEquipmentServiceRequest(String ID, String field, String change) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement update = connection.createStatement();
      String str =
          String.format(
              "UPDATE MedicalEquipmentServiceRequest SET " + field + " = %s WHERE requestID = '%s'",
              change,
              ID);
      update.execute(str);
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public static void enterMedicalEquipmentServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String equipmentID,
      String requestType) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement insert = connection.createStatement();

      String str =
          String.format(
              "INSERT INTO MedicalEquipmentServiceRequest(requestID, startLocation, endLocation, "
                  + "employeeRequested, employeeAssigned, requestTime, requestStatus, equipmentID, requestType) "
                  + " VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%1$TD %1$TT', '%s', '%s', '%s');",
              requestID,
              startLocation,
              endLocation,
              employeeRequested,
              employeeAssigned,
              requestTime,
              requestStatus,
              equipmentID,
              requestType);
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
      String str =
          String.format("DELETE FROM MedicalEquipmentServiceRequest WHERE requestID = '%s'", ID);
      delete.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public static List<MedicalEquipmentServiceRequest> getMedicalEquipmentServiceRequestList() {
    List<MedicalEquipmentServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM MedicalEquipmentServiceRequest");

      while (rset.next()) {
        String requestID = rset.getString("requestID");
        String startLocation = rset.getString("startLocation");
        String endLocation = rset.getString("endLocation");
        String employeeRequested = rset.getString("employeeRequested");
        String employeeAssigned = rset.getString("employeeAssigned");
        Timestamp requestTime = rset.getTimestamp("requestTime");
        String requestStatus = rset.getString("requestStatus");
        String equipmentID = rset.getString("equipmentID");
        String requestType = rset.getString("requestType");

        MedicalEquipmentServiceRequest mesr =
            new MedicalEquipmentServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                equipmentID,
                requestType);
        reqList.add(mesr);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }

    return reqList;
  }
}
