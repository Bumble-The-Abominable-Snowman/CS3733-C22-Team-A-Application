package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.laundryservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.LaundryServiceRequest;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LaundryServiceRequestDerbyImpl implements LaundryServiceRequestDAO {
  public LaundryServiceRequestDerbyImpl() {}

  public LaundryServiceRequest getLaundryServiceRequest(String id) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
          String.format(
              "SELECT * FROM ServiceRequestDerbyImpl s, laundryservicerequest l WHERE (s.requestID = l.requestID) AND l.requestID = '%s'",
              id);

      ResultSet rset = get.executeQuery(str);
      LaundryServiceRequest ldr = new LaundryServiceRequest();
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
        String washMode = rset.getString("washMode");

        ldr =
            new LaundryServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                washMode);
      }
      return ldr;
    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public void updateLaundryServiceRequest(String ID, String field, String change) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement update = connection.createStatement();
      String str = "";
      if (field.equals("washMode")) {
        str =
            String.format(
                "UPDATE laundryservicerequest SET " + field + " = '%s' WHERE requestID = '%s'",
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

  public void enterLaundryServiceRequest(LaundryServiceRequest lsr) {
    Timestamp time = Timestamp.valueOf(lsr.getRequestTime());
    enterLaundryServiceRequest(
        lsr.getRequestID(),
        lsr.getStartLocation(),
        lsr.getEndLocation(),
        lsr.getEmployeeRequested(),
        lsr.getEmployeeAssigned(),
        time,
        lsr.getRequestStatus(),
        lsr.getRequestType(),
        lsr.getComments(),
        lsr.getWashMode());
  }

  public void enterLaundryServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String washMode) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement insert = connection.createStatement();
      String strTime = "'" + requestTime.toString() + "'";

      String str =
          String.format(
              "INSERT INTO ServiceRequestDerbyImpl(requestID, startLocation, endLocation, "
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

      insert.executeQuery(str);

      String str2 =
          String.format(
              "INSERT INTO laundryservicerequest(requestID, language) " + "VALUES('%s', '%s')",
              requestID, washMode);
      insert.execute(str2);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void deleteLaundryServiceRequest(String id) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement delete = connection.createStatement();
      String str = String.format("DELETE FROM ServiceRequestDerbyImpl WHERE requestID = '%s'", id);
      delete.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public List<LaundryServiceRequest> getLaundryServiceRequestList() {
    List<LaundryServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset =
          getNodeList.executeQuery(
              "SELECT * FROM ServiceRequest s, LaundryServiceRequest l WHERE s.requestID=l.requestID");

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
        String washMode = rset.getString("washMode");

        LaundryServiceRequest lsr =
            new LaundryServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                washMode);
        reqList.add(lsr);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
    return reqList;
  }

  // Read from CSV
  public static List<LaundryServiceRequest> readLaundryServiceRequestCSV(String csvFilePath)
      throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
        new Scanner(LaundryServiceRequest.class.getClassLoader().getResourceAsStream(csvFilePath));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<LaundryServiceRequest> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      LaundryServiceRequest thisLSR = new LaundryServiceRequest();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisLSR.setRequestID(data);
        else if (dataIndex == 1) thisLSR.setStartLocation(data);
        else if (dataIndex == 2) thisLSR.setEndLocation(data);
        else if (dataIndex == 3) thisLSR.setEmployeeRequested(data);
        else if (dataIndex == 4) thisLSR.setEmployeeAssigned(data);
        else if (dataIndex == 5) thisLSR.setRequestTime(data);
        else if (dataIndex == 6) thisLSR.setRequestStatus(data);
        else if (dataIndex == 7) thisLSR.setRequestType(data);
        else if (dataIndex == 8) thisLSR.setComments(data);
        else if (dataIndex == 9) thisLSR.setWashMode(data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisLSR);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  // Write CSV for table
  public static void writeLaundryServiceRequestCSV(
      List<LaundryServiceRequest> List, String csvFilePath) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "RequestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments, WashMode");
    writer.newLine();

    // write location data
    for (LaundryServiceRequest thisLSR : List) {

      writer.write(
          String.join(
              ",",
              thisLSR.getRequestID(),
              thisLSR.getStartLocation(),
              thisLSR.getEndLocation(),
              thisLSR.getEmployeeRequested(),
              thisLSR.getEmployeeAssigned(),
              thisLSR.getRequestTime(),
              thisLSR.getRequestStatus(),
              thisLSR.getRequestType(),
              thisLSR.getComments(),
              thisLSR.getWashMode()));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath) {

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement dropTable = connection.createStatement();

      dropTable.execute("DELETE FROM LaundryServiceRequest");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

      List<LaundryServiceRequest> List =
          LaundryServiceRequestDerbyImpl.readLaundryServiceRequestCSV(csvFilePath);
      for (LaundryServiceRequest l : List) {
        Statement addStatement = connection.createStatement();

        // add to sub table
        addStatement.executeUpdate(
            "INSERT INTO LaundryServiceRequest(requestID, washMode) VALUES('"
                + l.getRequestID()
                + "', '"
                + l.getWashMode()
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
    LaundryServiceRequestDAO mesr = new LaundryServiceRequestDerbyImpl();
    LaundryServiceRequestDerbyImpl.writeLaundryServiceRequestCSV(
        mesr.getLaundryServiceRequestList(), csvFilePath);
  }
}
