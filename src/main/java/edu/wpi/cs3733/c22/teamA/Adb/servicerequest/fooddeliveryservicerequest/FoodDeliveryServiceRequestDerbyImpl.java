package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryServiceRequest;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FoodDeliveryServiceRequestDerbyImpl implements FoodDeliveryServiceRequestDAO {

  public FoodDeliveryServiceRequestDerbyImpl() {}

  public FoodDeliveryServiceRequest getFoodDeliveryRequest(String ID) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
          String.format(
              "SELECT * FROM ServiceRequest s, FoodDeliveryServiceRequest f WHERE (s.requestID = f.requestID) AND f.requestID = '%s'",
              ID);

      ResultSet rset = get.executeQuery(str);
      FoodDeliveryServiceRequest fd = new FoodDeliveryServiceRequest();
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
        String mainDish = rset.getString("mainDish");
        String sideDish = rset.getString("sideDish");
        String beverage = rset.getString("beverage");
        String dessert = rset.getString("dessert");

        fd =
            new FoodDeliveryServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                mainDish,
                sideDish,
                beverage,
                dessert);
      }
      return fd;
    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public void updateFoodDeliveryRequest(String ID, String field, Object change) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement update = connection.createStatement();
      List<String> foodColumns = new ArrayList<String>();
      foodColumns.add("mainDish");
      foodColumns.add("sideDish");
      foodColumns.add("beverage");
      foodColumns.add("dessert");
      String str = "";
      if (foodColumns.contains(field)) {
        str =
            String.format(
                "UPDATE FoodDeliveryServiceRequest SET " + field + " = '%s' WHERE requestID = '%s'",
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

  public void enterFoodDeliveryRequest(FoodDeliveryServiceRequest fdsr) {
    Timestamp time = Timestamp.valueOf(fdsr.getRequestTime());
    enterFoodDeliveryRequest(
        fdsr.getRequestID(),
        fdsr.getStartLocation(),
        fdsr.getEndLocation(),
        fdsr.getEmployeeRequested(),
        fdsr.getEmployeeAssigned(),
        time,
        fdsr.getRequestStatus(),
        fdsr.getRequestType(),
        fdsr.getComments(),
        fdsr.getMainDish(),
        fdsr.getSideDish(),
        fdsr.getBeverage(),
        fdsr.getDessert());
  }

  public void enterFoodDeliveryRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String mainDish,
      String sideDish,
      String beverage,
      String dessert) {
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

      insert.executeQuery(str);

      String str2 =
          String.format(
              "INSERT INTO FoodDeliveryServiceRequest(requestID, mainDish, sideDish, beverage, dessert) "
                  + "VALUES('%s', '%s', '%s', '%s', '%s')",
              requestID, mainDish, sideDish, beverage, dessert);
      insert.execute(str2);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void deleteFoodDeliveryRequest(String id) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement delete = connection.createStatement();
      String str = String.format("DELETE FROM ServiceRequest WHERE requestID = '%s'", id);
      delete.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public List<FoodDeliveryServiceRequest> getFoodDeliveryRequestList() {
    List<FoodDeliveryServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset =
          getNodeList.executeQuery(
              "SELECT * FROM ServiceRequest s, FoodDeliveryServiceRequest f WHERE s.requestID=f.requestID");

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
        String mainDish = rset.getString("mainDish");
        String sideDish = rset.getString("sideDish");
        String beverage = rset.getString("beverage");
        String dessert = rset.getString("dessert");

        FoodDeliveryServiceRequest fdsr =
            new FoodDeliveryServiceRequest(
                requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                mainDish,
                sideDish,
                beverage,
                dessert);
        reqList.add(fdsr);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
    return reqList;
  }

  // Read from Location CSV
  public static List<FoodDeliveryServiceRequest> readFoodDeliveryServiceRequestCSV(
      String csvFilePath) throws IOException, ParseException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
        new Scanner(
            FoodDeliveryServiceRequest.class.getClassLoader().getResourceAsStream(csvFilePath));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<FoodDeliveryServiceRequest> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      FoodDeliveryServiceRequest thisLSR = new FoodDeliveryServiceRequest();

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
        else if (dataIndex == 9) thisLSR.setMainDish(data);
        else if (dataIndex == 10) thisLSR.setSideDish(data);
        else if (dataIndex == 11) thisLSR.setBeverage(data);
        else if (dataIndex == 12) thisLSR.setDessert(data);
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
  public static void writeFoodDeliveryServiceRequestCSV(
      List<FoodDeliveryServiceRequest> List, String csvFilePath) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "RequestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments, mainDish, sideDish, beverage, dessert");
    writer.newLine();

    // write location data
    for (FoodDeliveryServiceRequest thisLSR : List) {

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
              thisLSR.getMainDish(),
              thisLSR.getSideDish(),
              thisLSR.getBeverage(),
              thisLSR.getDessert()));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath) {

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement dropTable = connection.createStatement();

      dropTable.execute("DELETE FROM FoodDeliveryServiceRequest");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }

    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");

      List<FoodDeliveryServiceRequest> List =
          FoodDeliveryServiceRequestDerbyImpl.readFoodDeliveryServiceRequestCSV(csvFilePath);
      for (FoodDeliveryServiceRequest l : List) {
        Statement addStatement = connection.createStatement();

        // add sub table
        addStatement.executeUpdate(
            "INSERT INTO FoodDeliveryServiceRequest(requestID, mainDish, sideDish, beverage, dessert) VALUES('"
                + l.getRequestID()
                + "', '"
                + l.getMainDish()
                + "', '"
                + l.getSideDish()
                + "', '"
                + l.getBeverage()
                + "', '"
                + l.getDessert()
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
    FoodDeliveryServiceRequestDAO mesr = new FoodDeliveryServiceRequestDerbyImpl();
    FoodDeliveryServiceRequestDerbyImpl.writeFoodDeliveryServiceRequestCSV(
        mesr.getFoodDeliveryRequestList(), csvFilePath);
  }
}
