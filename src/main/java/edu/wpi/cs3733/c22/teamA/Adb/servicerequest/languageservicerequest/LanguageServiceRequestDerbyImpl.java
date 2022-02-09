package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.languageservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.LanguageServiceRequest;
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

public class LanguageServiceRequestDerbyImpl implements LanguageServiceRequestDAO {

  public LanguageServiceRequestDerbyImpl() {}

  public LanguageServiceRequest getLanguageServiceRequest(String ID) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
          String.format(
              "SELECT * FROM ServiceRequestDerbyImpl s, LanguageServiceRequest l WHERE (s.requestID = l.requestID) AND l.requestID = '%s'",
              ID);

      ResultSet rset = get.executeQuery(str);
      LanguageServiceRequest lan = new LanguageServiceRequest();
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
        String language = rset.getString("language");

        lan =
            new LanguageServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                language);
      }
      return lan;
    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public void updateLanguageServiceRequest(String ID, String field, String change) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement update = connection.createStatement();
      String str = "";
      if (field.equals("language")) {
        str =
            String.format(
                "UPDATE LanguageServiceRequest SET " + field + " = '%s' WHERE requestID = '%s'",
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

  public void enterLanguageServiceRequest(LanguageServiceRequest lsr) {
    Timestamp time = Timestamp.valueOf(lsr.getRequestTime());
    enterLanguageServiceRequest(
        lsr.getRequestID(),
        lsr.getStartLocation(),
        lsr.getEndLocation(),
        lsr.getEmployeeRequested(),
        lsr.getEmployeeAssigned(),
        time,
        lsr.getRequestStatus(),
        lsr.getRequestType(),
        lsr.getComments(),
        lsr.getLanguage());
  }

  public void enterLanguageServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String language) {
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
              "INSERT INTO LanguageServiceRequest(requestID, language) " + "VALUES('%s', '%s')",
              requestID, language);
      insert.execute(str2);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void deleteLanguageServiceRequest(String id) {
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

  public List<LanguageServiceRequest> getLanguageServiceRequestList() {
    List<LanguageServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset =
          getNodeList.executeQuery(
              "SELECT * FROM ServiceRequest s JOIN LanguageServiceRequest l ON s.requestID=l.requestID");

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
        String language = rset.getString("language");

        LanguageServiceRequest lsr =
            new LanguageServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                language);
        reqList.add(lsr);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
    return reqList;
  }

  // Read from Location CSV
  public static List<LanguageServiceRequest> readLanguageServiceRequestCSV(String csvFilePath)
      throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
        new Scanner(LanguageServiceRequest.class.getClassLoader().getResourceAsStream(csvFilePath));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<LanguageServiceRequest> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      LanguageServiceRequest thisLSR = new LanguageServiceRequest();

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
        else if (dataIndex == 9) thisLSR.setLanguage(data);
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

  // Write CSV for MedicalEquipmentServiceRequest table
  public static void writeLanguageServiceRequestCSV(
      List<LanguageServiceRequest> List, String csvFilePath) throws IOException {

    // create a writer
    File file = new File(csvFilePath);
    file.createNewFile();
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "RequestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments, Language");
    writer.newLine();

    // write location data
    for (LanguageServiceRequest thisLSR : List) {

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
              thisLSR.getLanguage()));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath) {

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement dropTable = connection.createStatement();

      dropTable.execute("DELETE FROM LanguageServiceRequest");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

      List<LanguageServiceRequest> List =
          LanguageServiceRequestDerbyImpl.readLanguageServiceRequestCSV(csvFilePath);
      for (LanguageServiceRequest l : List) {
        Statement addStatement = connection.createStatement();

        // add sub table
        addStatement.executeUpdate(
            "INSERT INTO LanguageServiceRequest(requestID, language) VALUES('"
                + l.getRequestID()
                + "', '"
                + l.getLanguage()
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
    LanguageServiceRequestDAO mesr = new LanguageServiceRequestDerbyImpl();
    LanguageServiceRequestDerbyImpl.writeLanguageServiceRequestCSV(
        mesr.getLanguageServiceRequestList(), csvFilePath);
  }
}
