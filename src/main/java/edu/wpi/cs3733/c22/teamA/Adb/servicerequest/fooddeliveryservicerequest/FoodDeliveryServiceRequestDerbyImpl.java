package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.languageservicerequest.LanguageServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.languageservicerequest.LanguageServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryServiceRequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.wpi.cs3733.c22.teamA.entities.requests.LanguageServiceRequest;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FoodDeliveryServiceRequestDerbyImpl implements FoodDeliveryServiceRequestDAO {

  public FoodDeliveryServiceRequestDerbyImpl(){}

  public FoodDeliveryServiceRequest getFoodDeliveryServiceRequest(String ID){
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
              String.format(
                      "SELECT * FROM ServiceRequestDerbyImpl s, FoodDeliveryServiceRequest f WHERE (s.requestID = f.requestID) AND f.requestID = '%s'",
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

  public void updateFoodDeliveryServiceRequest(String ID, String field, Object change){
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

  public void enterFoodDeliveryServiceRequest(FoodDeliveryServiceRequest fdsr){
    Timestamp time = Timestamp.valueOf(fdsr.getRequestTime());
    enterFoodDeliveryServiceRequest(
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
            fdsr.getDessert()
    );
  }

  public void enterFoodDeliveryServiceRequest(
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
          String dessert){
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

  public void deleteFoodDeliveryServiceRequest(String id){
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

  public List<FoodDeliveryServiceRequest> getFoodDeliveryServiceRequestList(){
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

  // Read from FoodDeliveryServiceRequest CSV
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
      FoodDeliveryServiceRequest FDSR = new FoodDeliveryServiceRequest();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) FDSR.setRequestID(data);
        else if (dataIndex == 1) FDSR.setMainDish(data);
        else if (dataIndex == 2) FDSR.setSideDish(data);
        else if (dataIndex == 3) FDSR.setBeverage(data);
        else if (dataIndex == 4) FDSR.setDessert(data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      list.add(FDSR);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return list;
  }

  // Write CSV for FoodDeliveryServiceRequest table
  public static void writeFoodDeliveryServiceRequestCSV(
          List<FoodDeliveryServiceRequest> List, String csvFilePath) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
            "RequestID, MainDish, SideDish, Beverage, Dessert");
    writer.newLine();

    // write location data
    for (FoodDeliveryServiceRequest thisFDSR : List) {

      writer.write(
              String.join(
                      ",",
                      thisFDSR.getRequestID(),
                      thisFDSR.getMainDish(),
                      thisFDSR.getSideDish(),
                      thisFDSR.getBeverage(),
                      thisFDSR.getDessert()));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // input from CSV
  public static void inputFromCSV(String tableName, String csvFilePath){

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
              FoodDeliveryServiceRequestDerbyImpl.readFoodDeliveryServiceRequestCSV(
                      csvFilePath);
      for (FoodDeliveryServiceRequest l : List) {
        Statement addStatement = connection.createStatement();
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
      }
    } catch (SQLException | IOException | ParseException e) {
      System.out.println("Insertion failed!");
    }
  }

  // Export to CSV
  public static void exportToCSV(String tableName, String csvFilePath) throws IOException{
    FoodDeliveryServiceRequestDAO mesr = new FoodDeliveryServiceRequestDerbyImpl();
    FoodDeliveryServiceRequestDerbyImpl.writeFoodDeliveryServiceRequestCSV(
            mesr.getFoodDeliveryServiceRequestList(), csvFilePath);
  }
}
