package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.sanitationservicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.medicalequipmentservicerequest.MedicalEquipmentServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.medicalequipmentservicerequest.MedicalEquipmentServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;
import edu.wpi.cs3733.c22.teamA.entities.requests.ReligiousServiceRequest;
import edu.wpi.cs3733.c22.teamA.entities.requests.SanitationServiceRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SanitationServiceRequestDerbyImpl implements SanitationServiceRequestDAO {

  public SanitationServiceRequestDerbyImpl() {}

  public SanitationServiceRequest getSanitationServiceRequest(String id) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
          String.format(
              "SELECT * FROM ServiceRequestDerbyImpl s, SanitationServiceRequest r WHERE (s.requestID = r.requestID) AND r.requestID = '%s'",
              id);

      ResultSet rset = get.executeQuery(str);
      SanitationServiceRequest lan = new SanitationServiceRequest();
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
        String sanitationType = rset.getString("sanitationType");

        lan =
            new SanitationServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                sanitationType);
      }
      return lan;
    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public void updateSanitationServiceRequest(String ID, String field, String change) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement update = connection.createStatement();
      String str = "";
      if (field.equals("sanitationType")) {
        str =
            String.format(
                "UPDATE SanitationServiceRequest SET " + field + " = '%s' WHERE requestID = '%s'",
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

  public void enterSanitationServiceRequest(SanitationServiceRequest ssr) {
    Timestamp time = Timestamp.valueOf(ssr.getRequestTime());
    enterSanitationServiceRequest(
        ssr.getRequestID(),
        ssr.getStartLocation(),
        ssr.getEndLocation(),
        ssr.getEmployeeRequested(),
        ssr.getEmployeeAssigned(),
        time,
        ssr.getRequestStatus(),
        ssr.getRequestType(),
        ssr.getComments(),
        ssr.getSanitationType());
  }

  public void enterSanitationServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String sanitationType) {
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
              "INSERT INTO SanitationServiceRequest(requestID, language) " + "VALUES('%s', '%s')",
              requestID, sanitationType);
      insert.execute(str2);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void deleteSanitationServiceRequest(String id) {
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

  public List<SanitationServiceRequest> getSanitationServiceRequestList() {
    List<SanitationServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset =
          getNodeList.executeQuery(
              "SELECT * FROM ServiceRequest s, SanitationServiceRequest r WHERE s.requestID = r.requestID");

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
        String sanitationType = rset.getString("sanitationType");

        SanitationServiceRequest lsr =
            new SanitationServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                sanitationType);
        reqList.add(lsr);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
    return reqList;
  }

  // Read from CSV
  public static List<SanitationServiceRequest> readSanitationServiceRequestCSV(
          String csvFilePath) throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
            new Scanner(
                    SanitationServiceRequest.class.getClassLoader().getResourceAsStream(csvFilePath));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<SanitationServiceRequest> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      SanitationServiceRequest thisSSR = new SanitationServiceRequest();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisSSR.setRequestID(data);
        else if (dataIndex == 1) thisSSR.setSanitationType(data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(thisSSR);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  // Write CSV for table
  public static void writeSanitationServiceRequestCSV(
          List<SanitationServiceRequest> List, String csvFilePath) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
            "RequestID, SanitationType");
    writer.newLine();

    // write location data
    for (SanitationServiceRequest thisSSR : List) {

      writer.write(
              String.join(
                      ",",
                      thisSSR.getRequestID(),
                      thisSSR.getSanitationType()));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath){

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement dropTable = connection.createStatement();

      dropTable.execute("DELETE FROM SanitationServiceRequest");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

      List<SanitationServiceRequest> List =
              SanitationServiceRequestDerbyImpl.readSanitationServiceRequestCSV(
                      csvFilePath);
      for (SanitationServiceRequest l : List) {
        Statement addStatement = connection.createStatement();
        addStatement.executeUpdate(
                "INSERT INTO SanitationServiceRequest(requestID, sanitationType) VALUES('"
                        + l.getRequestID()
                        + "', '"
                        + l.getSanitationType()
                        + "')");
      }
    } catch (SQLException | IOException | ParseException e) {
      System.out.println("Insertion failed!");
    }
  }

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException{
    SanitationServiceRequestDAO mesr = new SanitationServiceRequestDerbyImpl();
    SanitationServiceRequestDerbyImpl.writeSanitationServiceRequestCSV(
            mesr.getSanitationServiceRequestList(), csvFilePath);
  }
}
