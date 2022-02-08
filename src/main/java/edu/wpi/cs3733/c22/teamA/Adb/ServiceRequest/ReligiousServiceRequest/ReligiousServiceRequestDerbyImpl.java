package edu.wpi.teama.Adb.ServiceRequest.ReligiousServiceRequest;

import edu.wpi.teama.entities.requests.LanguageServiceRequest;
import edu.wpi.teama.entities.requests.ReligiousServiceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReligiousServiceRequestDerbyImpl implements ReligiousServiceRequestDAO {

  public ReligiousServiceRequestDerbyImpl(){}

  public ReligiousServiceRequest getReligiousServiceRequest(String id) {
    try{
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement get = connection.createStatement();
      String str =
              String.format("SELECT * FROM ServiceRequest s, ReligiousServiceRequest r WHERE (s.requestID = r.requestID) AND r.requestID = '%s'", id);

      ResultSet rset = get.executeQuery(str);
      ReligiousServiceRequest rsr = new ReligiousServiceRequest();
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
        String religion = rset.getString("religion");

        rsr = new ReligiousServiceRequest(requestID,
                startLocation,
                endLocation,
                employeeRequested,
                employeeAssigned,
                requestTime,
                requestStatus,
                requestType,
                comments,
                religion
        );
      }
      return rsr;
    }catch(SQLException e){
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
      if(field.equals("religion")){
        str = String.format(
                "UPDATE ReligionServiceRequest SET "
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

  public void enterReligiousServiceRequest(ReligiousServiceRequest rsr){
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
            rsr.getReligion()
    );
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
                      "INSERT INTO ReligionServiceRequest(requestID, language) " +
                              "VALUES('%s', '%s')",requestID, religion);
      insert.execute(str2);

    }catch(SQLException e){
      System.out.println("Failed");
      e.printStackTrace();
      return;

    }
  }

  public void deleteReligiousServiceRequest(String id) {
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

  public List<ReligiousServiceRequest> getReligiousServiceRequestList() {
    List<ReligiousServiceRequest> reqList = new ArrayList<>();
    try {
      Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
      Statement getNodeList = connection.createStatement();
      ResultSet rset = getNodeList.executeQuery("SELECT * FROM ServiceRequest s, ReligiousServiceRequest r WHERE s.requestID=r.requestID");

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
                        language);
        reqList.add(rsr);
      }
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
    return reqList;
  }
}
