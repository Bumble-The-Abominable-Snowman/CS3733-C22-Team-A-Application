package edu.wpi.cs3733.c22.teamA.Adb.ServiceRequest.LaundryServiceRequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.LaundryServiceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LaundryServiceRequestDerbyImpl implements LaundryServiceRequestDAO {
  public LaundryServiceRequestDerbyImpl(){}

  public LaundryServiceRequest getLaundryServiceRequest(String id) {
    try{
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
              String.format("SELECT * FROM ServiceRequest s, LaundryServiceRequest l WHERE (s.requestID = l.requestID) AND l.requestID = '%s'", id);

      ResultSet rset = get.executeQuery(str);
      LaundryServiceRequest ldr = new LaundryServiceRequest();
      if(rset.next()){
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

        ldr = new LaundryServiceRequest(requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                washMode
        );
      }
      return ldr;
    }catch(SQLException e){
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
      if(field.equals("washMode")){
        str = String.format(
                "UPDATE LaundryServiceRequest SET "
                        + field
                        + " = '%s' WHERE requestID = '%s'",
                change,
                ID);
      }else{
        str = String.format(
                "UPDATE ServiceRequest SET "
                        + field
                        + " = '%s' WHERE requestID = '%s'",
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

  public void enterLaundryServiceRequest(LaundryServiceRequest lsr){
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
            lsr.getWashMode()
    );
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
    try{
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
                      "INSERT INTO LaundryServiceRequest(requestID, language) " +
                              "VALUES('%s', '%s')",requestID, washMode);
      insert.execute(str2);

    }catch(SQLException e){
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  public void deleteLaundryServiceRequest(String id) {
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement delete = connection.createStatement();
      String str =
              String.format("DELETE FROM ServiceRequest WHERE requestID = '%s'", id);
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
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM ServiceRequest s, LaundryServiceRequest l WHERE s.requestID=l.requestID");

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
}
