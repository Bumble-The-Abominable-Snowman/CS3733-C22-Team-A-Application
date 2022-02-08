package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.medicalequipmentservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MedicalEquipmentServiceRequestDerbyImpl implements MedicalEquipmentServiceRequestDAO {

  public MedicalEquipmentServiceRequestDerbyImpl() {}

  public MedicalEquipmentServiceRequest getMedicalEquipmentServiceRequest(String ID) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
          String.format(
              "SELECT * FROM ServiceRequest s, MedicalEquipmentServiceRequest m WHERE (s.requestID = m.requestID) AND m.requestID = '%s'",
              ID);

      ResultSet rset = get.executeQuery(str);
      MedicalEquipmentServiceRequest mesr = new MedicalEquipmentServiceRequest();
      System.out.println("hello");
      if (rset.next()) {
        System.out.println("hi");
        String requestID = rset.getString("requestID");
        String startLocation = rset.getString("startLocation");
        String endLocation = rset.getString("endLocation");
        String employeeRequested = rset.getString("employeeRequested");
        String employeeAssigned = rset.getString("employeeAssigned");
        String requestTime = rset.getString("requestTime");
        String requestStatus = rset.getString("requestStatus");
        String requestType = rset.getString("requestType");
        String comments = rset.getString("comments");
        String equipmentID = rset.getString("equipmentID");
        System.out.println(requestType);
        mesr =
            new MedicalEquipmentServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                comments,
                requestType,
                equipmentID);
      }
      return mesr;

    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public void updateMedicalEquipmentServiceRequest(String ID, String field, String change) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement update = connection.createStatement();
      String str = "";
      if (field.equals("equipmentID")) {
        str =
            String.format(
                "UPDATE MedicalEquipmentServiceRequest SET "
                    + field
                    + " = '%s' WHERE requestID = '%s'",
                change,
                ID);
      } else {
        str =
            String.format(
                "UPDATE ServiceRequest SET " + field + " = '%s' WHERE requestID = '%s'",
                change,
                ID);
      }
      update.execute(str);
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void enterMedicalEquipmentServiceRequest(MedicalEquipmentServiceRequest mesr) {
    Timestamp time = Timestamp.valueOf(mesr.getRequestTime());
    enterMedicalEquipmentServiceRequest(
        mesr.getRequestID(),
        mesr.getStartLocation(),
        mesr.getEndLocation(),
        mesr.getEmployeeRequested(),
        mesr.getEmployeeAssigned(),
        time,
        mesr.getRequestStatus(),
        mesr.getRequestType(),
        mesr.getComments(),
        mesr.getEquipmentID());
  }

  public void enterMedicalEquipmentServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String equipmentID) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement insert = connection.createStatement();

      String strTime = "'" + requestTime.toString() + "'";
      System.out.println(strTime);
      String str =
          String.format(
              "INSERT INTO ServiceRequest(requestID, startLocation, endLocation, "
                  + "employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments) "
                  + " VALUES('%s', '%s', '%s', '%s', '%s', %s, '%s', '%s', '%s')",
              requestID,
              startLocation,
              endLocation,
              employeeRequested,
              employeeAssigned,
              strTime,
              requestStatus,
              requestType,
              comments);

      insert.execute(str);

      String str2 =
          String.format(
              "INSERT INTO MedicalEquipmentServiceRequest(requestID, equipmentID)"
                  + " VALUES('%s', '%s')",
              requestID, equipmentID);

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
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement delete = connection.createStatement();
      String str = String.format("DELETE FROM ServiceRequest WHERE requestID = '%s'", ID);
      delete.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public List<MedicalEquipmentServiceRequest> getMedicalEquipmentServiceRequestList() {
    List<MedicalEquipmentServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset =
          getNodeList.executeQuery(
              "SELECT * FROM ServiceRequest s, MedicalEquipmentServiceRequest m WHERE s.requestID=m.requestID");

      while (rset.next()) {
        String requestID = rset.getString("requestID");
        String startLocation = rset.getString("startLocation");
        String endLocation = rset.getString("endLocation");
        String employeeRequested = rset.getString("employeeRequested");
        String employeeAssigned = rset.getString("employeeAssigned");
        String requestTime = rset.getString("requestTime");
        String requestStatus = rset.getString("requestStatus");
        String requestType = rset.getString("requestType");
        String comments = rset.getString("comments");
        String equipmentID = rset.getString("equipmentID");

        MedicalEquipmentServiceRequest mesr =
            new MedicalEquipmentServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                comments,
                requestType,
                equipmentID);
        reqList.add(mesr);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
    return reqList;
  }

  // Read from Location CSV
  public static List<MedicalEquipmentServiceRequest> readMedicalEquipmentServiceRequestCSV(
      String csvFilePath) throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
        new Scanner(
            MedicalEquipmentServiceRequest.class.getClassLoader().getResourceAsStream(csvFilePath));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<MedicalEquipmentServiceRequest> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      MedicalEquipmentServiceRequest thisMESR = new MedicalEquipmentServiceRequest();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisMESR.setRequestID(data);
        else if (dataIndex == 1) thisMESR.setStartLocation(data);
        else if (dataIndex == 2) thisMESR.setEndLocation(data);
        else if (dataIndex == 3) thisMESR.setEmployeeRequested(data);
        else if (dataIndex == 4) thisMESR.setEmployeeAssigned(data);
        else if (dataIndex == 5) thisMESR.setRequestTime(data);
        else if (dataIndex == 6) thisMESR.setRequestStatus(data);
        else if (dataIndex == 7) thisMESR.setEquipmentID(data);
        else if (dataIndex == 8) thisMESR.setRequestType(data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisMESR);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  // Write CSV for MedicalEquipmentServiceRequest table
  public void writeMedicalEquipmentServiceRequestCSV(
      List<MedicalEquipmentServiceRequest> List, String csvFilePath) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "RequestID, getStartLocation, getEndLocation, getEmployeeRequested, getEmployeeAssigned, requestTime, getRequestStatus, getEquipmentID, getRequestType");
    writer.newLine();

    // write location data
    for (MedicalEquipmentServiceRequest thisMESR : List) {

      String requestTime = String.valueOf(thisMESR.getRequestTime());
      writer.write(
          String.join(
              ",",
              thisMESR.getRequestID(),
              thisMESR.getStartLocation(),
              thisMESR.getEndLocation(),
              thisMESR.getEmployeeRequested(),
              thisMESR.getEmployeeAssigned(),
              requestTime,
              thisMESR.getRequestStatus(),
              thisMESR.getEquipmentID(),
              thisMESR.getRequestType()));

      writer.newLine();
    }
    writer.close(); // close the writer
  }
}
