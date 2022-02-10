package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.medicalequipmentservicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;
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

public class MedicalEquipmentServiceRequestDerbyImpl implements MedicalEquipmentServiceRequestDAO {

  public MedicalEquipmentServiceRequestDerbyImpl() {}

  public MedicalEquipmentServiceRequest getMedicalEquipmentServiceRequest(String ID) {
    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement get = connection.createStatement();
      String str =
          String.format(
              "SELECT * FROM ServiceRequest s, MedicalEquipmentServiceRequest m WHERE (s.requestID = m.requestID) AND m.requestID = '%s'",
              ID);

      ResultSet rset = get.executeQuery(str);
      MedicalEquipmentServiceRequest mesr = new MedicalEquipmentServiceRequest();
      if (rset.next()) {
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
                requestType,
                comments,
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
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
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
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement insert = connection.createStatement();

      String strTime = "'" + requestTime.toString() + "'";
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

      insert.execute(str2);

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
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement delete = connection.createStatement();
      String str = String.format("DELETE FROM ServiceRequest WHERE requestID = '%s'", ID);
      delete.execute(str);

      delete.execute(
          String.format("DELETE FROM MedicalEquipmentServiceRequest WHERE requestID = '%s'", ID));

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public List<MedicalEquipmentServiceRequest> getMedicalEquipmentServiceRequestList() {
    List<MedicalEquipmentServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
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
                requestType,
                comments,
                equipmentID);
        reqList.add(mesr);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
    return reqList;
  }

  // Read from CSV
  public static List<MedicalEquipmentServiceRequest> readMedicalEquipmentServiceRequestCSV(
      String csvFilePath) throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    File file = new File(csvFilePath);
    Scanner lineScanner = new Scanner(file);
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
        else if (dataIndex == 7) thisMESR.setRequestType(data);
        else if (dataIndex == 8) thisMESR.setComments(data);
        else if (dataIndex == 9) thisMESR.setEquipmentID(data);
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

  // Write CSV for table
  public static void writeMedicalEquipmentServiceRequestCSV(
      List<MedicalEquipmentServiceRequest> List, String csvFilePath) throws IOException {

    // create a writer
    File file = new File(csvFilePath);
    file.createNewFile();
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "RequestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments, equipmentID");
    writer.newLine();

    // write data
    for (MedicalEquipmentServiceRequest thisMESR : List) {

      writer.write(
          String.join(
              ",",
              thisMESR.getRequestID(),
              thisMESR.getStartLocation(),
              thisMESR.getEndLocation(),
              thisMESR.getEmployeeRequested(),
              thisMESR.getEmployeeAssigned(),
              thisMESR.getRequestTime(),
              thisMESR.getRequestStatus(),
              thisMESR.getRequestType(),
              thisMESR.getComments(),
              thisMESR.getEquipmentID()));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath) {
    // Check MedicalEquipmentServiceRequest table
    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement dropTable = connection.createStatement();

      dropTable.execute("DELETE FROM MedicalEquipmentServiceRequest");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }

    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));

      List<MedicalEquipmentServiceRequest> List =
          MedicalEquipmentServiceRequestDerbyImpl.readMedicalEquipmentServiceRequestCSV(
              csvFilePath);
      for (MedicalEquipmentServiceRequest l : List) {
        Statement addStatement = connection.createStatement();
        // add to sub table
        addStatement.executeUpdate(
            "INSERT INTO MedicalEquipmentServiceRequest(requestID, equipmentID) VALUES('"
                + l.getRequestID()
                + "', '"
                + l.getEquipmentID()
                + "')");

        // add to ServiceRequest table
        addStatement.executeUpdate(
            "INSERT INTO ServiceRequest(requestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments) VALUES('"
                + l.getRequestID()
                + "', '"
                + l.getStartLocation()
                + "', '"
                + l.getEndLocation()
                + "', '"
                + l.getEmployeeRequested()
                + "', '"
                + l.getEmployeeAssigned()
                + "', '"
                + l.getRequestTime()
                + "', '"
                + l.getRequestStatus()
                + "', '"
                + l.getRequestType()
                + "', '"
                + l.getComments()
                + "')");
      }
    } catch (SQLException | IOException | ParseException e) {
      System.out.println("Insertion failed!");
    }
  }

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException {
    MedicalEquipmentServiceRequestDAO mesr = new MedicalEquipmentServiceRequestDerbyImpl();
    MedicalEquipmentServiceRequestDerbyImpl.writeMedicalEquipmentServiceRequestCSV(
        mesr.getMedicalEquipmentServiceRequestList(), csvFilePath);
  }
}
