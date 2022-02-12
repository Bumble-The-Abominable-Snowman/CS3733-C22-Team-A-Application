package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReligiousSR extends SR {

  private String religion;

  public ReligiousSR() {
    super();
  }

  public ReligiousSR(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String religion) {
    super();
    this.requestID = requestID;
    this.startLocation = startLocation;
    this.endLocation = endLocation;
    this.employeeRequested = employeeRequested;
    this.employeeAssigned = employeeAssigned;
    this.requestTime = Timestamp.valueOf(requestTime);
    this.requestStatus = requestStatus;
    this.requestType = requestType;
    this.comments = comments;
    this.religion = religion;
  }
}
