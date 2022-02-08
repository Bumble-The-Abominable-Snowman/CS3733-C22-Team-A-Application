package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryServiceRequest;
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

public class FoodDeliveryServiceRequestDerbyImpl implements FoodDeliveryServiceRequestDAO {

  List<FoodDeliveryServiceRequest> FoodDeliveryServiceRequest;

  public void FoodDeliveryRequestDerbyImpl() {
    try {

      FoodDeliveryServiceRequest = new ArrayList<FoodDeliveryServiceRequest>();
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM FoodDeliveryServiceRequest");

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
        FoodDeliveryServiceRequest f =
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
        FoodDeliveryServiceRequest.add(f);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
  }

  public List<FoodDeliveryServiceRequest> getNodeList() {
    return FoodDeliveryServiceRequest;
  }

  public void deleteRequest(String requestID) {

    String tableName = "FoodDeliveryServiceRequest";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement deleteNode = connection.createStatement();

      String str =
          String.format(
              "DELETE FROM " + tableName + " WHERE requestID ='%s'",
              requestID); // delete the selected node

      deleteNode.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void enterRequest(
      String requestID,
      String mainDish,
      String sideDish,
      String beverage,
      String dessert,
      String roomNum,
      String employee,
      String comments) {

    String tableName = "FoodDeliveryServiceRequest";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement enterNode = connection.createStatement();

      String str =
          String.format(
              "INSERT INTO "
                  + tableName
                  + "(requestID, mainDish, sideDish, beverage, dessert, roomNum, employee, comments)"
                  + "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
              requestID,
              mainDish,
              sideDish,
              beverage,
              dessert,
              roomNum,
              employee,
              comments); // insert values from input.

      enterNode.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
  }

  public void enterRequest(FoodDeliveryServiceRequest foodDeliveryServiceRequest) {

    String requestID = foodDeliveryServiceRequest.getRequestID();
    String mainDish = foodDeliveryServiceRequest.getRequestID();
    String sideDish = foodDeliveryServiceRequest.getRequestID();
    String beverage = foodDeliveryServiceRequest.getRequestID();
    String dessert = foodDeliveryServiceRequest.getRequestID();
    String roomNum = foodDeliveryServiceRequest.getRequestID();
    String comments = foodDeliveryServiceRequest.getRequestID();
    String employee = foodDeliveryServiceRequest.getEmployeeAssigned();

    String tableName = "FoodDeliveryServiceRequest";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement enterNode = connection.createStatement();

      String str =
          String.format(
              "INSERT INTO "
                  + tableName
                  + "(requestID, mainDish, sideDish, beverage, dessert, roomNum, employee, comments)"
                  + "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
              requestID,
              mainDish,
              sideDish,
              beverage,
              dessert,
              roomNum,
              employee,
              comments); // insert values from input.

      enterNode.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
  }

  public void updateRequest(String ID, String field, Object change) {

    String tableName = "FoodDeliveryServiceRequest";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement updateCoords = connection.createStatement();

      String str =
          String.format(
              "update " + tableName + " set " + field + " = '%s' where requestID = '%s'",
              change,
              ID);
      updateCoords.execute(str);

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
  }

  public FoodDeliveryServiceRequest getRequest(String ID) {

    String tableName = "FoodDeliveryServiceRequest";
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNode = connection.createStatement();
      String str =
          String.format(
              "select * from " + tableName + " Where requestID = '%s'",
              ID); // get node from table location

      getNode.execute(str);

      // Commented code to print out the selected node.
      ResultSet rset = getNode.getResultSet();
      FoodDeliveryServiceRequest f = new FoodDeliveryServiceRequest();
      // process results
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
        f =
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

      // Return the location object
      return f;

    } catch (SQLException e) {
      System.out.println("Failed to get node");
      e.printStackTrace();
      return null;
    }
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
  public void writeFoodDeliveryServiceRequestCSV(
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
}
