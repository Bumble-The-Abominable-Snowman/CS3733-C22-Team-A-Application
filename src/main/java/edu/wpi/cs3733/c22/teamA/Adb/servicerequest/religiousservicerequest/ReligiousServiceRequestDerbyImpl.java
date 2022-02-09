package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.religiousservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.ReligiousServiceRequest;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReligiousServiceRequestDerbyImpl implements ReligiousServiceRequestDAO {

  public ReligiousServiceRequestDerbyImpl() {}

  public ReligiousServiceRequest getReligiousServiceRequest(String id) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
          String.format(
              "SELECT * FROM ServiceRequest s, ReligiousServiceRequest r WHERE (s.requestID = r.requestID) AND r.requestID = '%s'",
              id);

      ResultSet rset = get.executeQuery(str);
      ReligiousServiceRequest rsr = new ReligiousServiceRequest();
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
        String religion = rset.getString("religion");

        rsr =
            new ReligiousServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                religion);
      }
      return rsr;
    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public void updateReligiousServiceRequest(String ID, String field, String change) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement update = connection.createStatement();
      String str = "";
      if (field.equals("religion")) {
        str =
            String.format(
                "UPDATE ReligiousServiceRequest SET " + field + " = '%s' WHERE requestID = '%s'",
                change,
                ID);
      } else {
        str =
            String.format(
                "UPDATE ServiceRequestDerbyImpl SET " + field + " = '%s' WHERE requestID = '%s'",
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

  public void enterReligiousServiceRequest(ReligiousServiceRequest rsr) {
    Timestamp time = Timestamp.valueOf(rsr.getRequestTime());
    enterReligiousServiceRequest(
        rsr.getRequestID(),
        rsr.getStartLocation(),
        rsr.getEndLocation(),
        rsr.getEmployeeRequested(),
        rsr.getEmployeeAssigned(),
        time,
        rsr.getRequestStatus(),
        rsr.getRequestType(),
        rsr.getComments(),
        rsr.getReligion());
  }

  public void enterReligiousServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String religion) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
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

      insert.executeUpdate(str);

      String str2 =
          String.format(
              "INSERT INTO ReligiousServiceRequest(requestID, religion) " + "VALUES('%s', '%s')",
              requestID, religion);
      insert.executeUpdate(str2);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void deleteReligiousServiceRequest(String id) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement delete = connection.createStatement();
      String str = String.format("DELETE FROM ServiceRequest WHERE requestID = '%s'", id);
      delete.execute(str);

      delete.execute(
          String.format("DELETE FROM ReligiousServiceRequest WHERE requestID = '%s'", id));

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public List<ReligiousServiceRequest> getReligiousServiceRequestList() {
    List<ReligiousServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset =
          getNodeList.executeQuery(
              "SELECT * FROM ServiceRequest s, ReligiousServiceRequest r WHERE s.requestID=r.requestID");

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
        String religion = rset.getString("religion");

        ReligiousServiceRequest rsr =
            new ReligiousServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                religion);
        reqList.add(rsr);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
    return reqList;
  }

  // Read from CSV
  public static List<ReligiousServiceRequest> readReligiousServiceRequestCSV(String csvFilePath)
      throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
        new Scanner(
            ReligiousServiceRequest.class.getClassLoader().getResourceAsStream(csvFilePath));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<ReligiousServiceRequest> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      ReligiousServiceRequest thisRSR = new ReligiousServiceRequest();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisRSR.setRequestID(data);
        else if (dataIndex == 1) thisRSR.setStartLocation(data);
        else if (dataIndex == 2) thisRSR.setEndLocation(data);
        else if (dataIndex == 3) thisRSR.setEmployeeRequested(data);
        else if (dataIndex == 4) thisRSR.setEmployeeAssigned(data);
        else if (dataIndex == 5) thisRSR.setRequestTime(data);
        else if (dataIndex == 6) thisRSR.setRequestStatus(data);
        else if (dataIndex == 7) thisRSR.setRequestType(data);
        else if (dataIndex == 8) thisRSR.setComments(data);
        else if (dataIndex == 9) thisRSR.setReligion(data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisRSR);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  // Write CSV for table
  public static void writeReligiousServiceRequestCSV(
      List<ReligiousServiceRequest> List, String csvFilePath) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "RequestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments, Religion");
    writer.newLine();

    // write data
    for (ReligiousServiceRequest thisRSR : List) {

      writer.write(
          String.join(
              ",",
              thisRSR.getRequestID(),
              thisRSR.getStartLocation(),
              thisRSR.getEndLocation(),
              thisRSR.getEmployeeRequested(),
              thisRSR.getEmployeeAssigned(),
              thisRSR.getRequestTime(),
              thisRSR.getRequestStatus(),
              thisRSR.getRequestType(),
              thisRSR.getComments(),
              thisRSR.getReligion()));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath) {

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement dropTable = connection.createStatement();

      dropTable.execute("DELETE FROM ReligiousServiceRequest");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

      List<ReligiousServiceRequest> List =
          ReligiousServiceRequestDerbyImpl.readReligiousServiceRequestCSV(csvFilePath);
      for (ReligiousServiceRequest l : List) {
        Statement addStatement = connection.createStatement();

        // add to sub table
        addStatement.executeUpdate(
            "INSERT INTO ReligiousServiceRequest(requestID, religion) VALUES('"
                + l.getRequestID()
                + "', '"
                + l.getReligion()
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
    ReligiousServiceRequestDAO mesr = new ReligiousServiceRequestDerbyImpl();
    ReligiousServiceRequestDerbyImpl.writeReligiousServiceRequestCSV(
        mesr.getReligiousServiceRequestList(), csvFilePath);
  }
}
