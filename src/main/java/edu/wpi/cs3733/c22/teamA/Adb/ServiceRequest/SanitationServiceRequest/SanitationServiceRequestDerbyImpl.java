package edu.wpi.cs3733.c22.teamA.Adb.ServiceRequest.SanitationServiceRequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.SanitationServiceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanitationServiceRequestDerbyImpl {

  public SanitationServiceRequestDerbyImpl(){}
  public SanitationServiceRequest getSanitationServiceRequest(String id) {
    try{
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
              String.format("SELECT * FROM ServiceRequest s, SanitationServiceRequest r WHERE (s.requestID = r.requestID) AND r.requestID = '%s'", id);

      ResultSet rset = get.executeQuery(str);
      SanitationServiceRequest lan = new SanitationServiceRequest();
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
        String sanitationType = rset.getString("sanitationType");

        lan = new SanitationServiceRequest(requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                sanitationType
        );
      }
      return lan;
    }catch(SQLException e){
      System.out.println("Connection Failed");
      e.printStackTrace();
      return null;
    }
  }

  public void updateSanitationServiceRequest(String ID, String field, String change){
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement update = connection.createStatement();
      String str = "";
      if(field.equals("sanitationType")){
        str = String.format(
                "UPDATE SanitationServiceRequest SET "
                        + field
                        + " = '%s' WHERE requestID = '%s'",
                change,
                ID);
      } else {
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

  public void enterSanitationServiceRequest(SanitationServiceRequest ssr){
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
            ssr.getSanitationType()
    );
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
                      "INSERT INTO SanitationServiceRequest(requestID, language) " +
                              "VALUES('%s', '%s')",requestID, sanitationType);
      insert.execute(str2);

    }catch(SQLException e){
      System.out.println("Failed");
      e.printStackTrace();
      return;

    }
  }

  public void deleteSanitationServiceRequest(String id) {
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

  public List<SanitationServiceRequest> getSanitationServiceRequestList() {
    List<SanitationServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM ServiceRequest s, SanitationServiceRequest r WHERE s.requestID = r.requestID");

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
}
