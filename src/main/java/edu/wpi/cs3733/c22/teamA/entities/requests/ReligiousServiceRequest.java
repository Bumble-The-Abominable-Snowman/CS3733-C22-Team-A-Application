package edu.wpi.cs3733.c22.teamA.entities.requests;

import java.sql.Timestamp;

public class ReligiousServiceRequest extends ServiceRequest {

  String religion;

  public ReligiousServiceRequest() {}

  public ReligiousServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String religions) {
    super.requestID = requestID;
    super.startLocation = startLocation;
    super.endLocation = endLocation;
    super.employeeRequested = employeeRequested;
    super.employeeAssigned = employeeAssigned;
    Timestamp rt = Timestamp.valueOf(requestTime);
    super.requestTime = rt;
    super.requestStatus = requestStatus;
    super.requestType = requestType;
    super.comments = comments;
    this.religion = religion;
  }

  public String getReligion() {
    return religion;
  }

  public void setReligion(String religion) {
    this.religion = religion;
  }
}
