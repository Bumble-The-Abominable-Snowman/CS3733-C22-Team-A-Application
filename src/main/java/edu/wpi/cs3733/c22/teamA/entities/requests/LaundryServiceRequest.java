package edu.wpi.cs3733.c22.teamA.entities.requests;

import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public @Data class LaundryServiceRequest extends ServiceRequest {

  private String washMode;

  public LaundryServiceRequest() {}

  public LaundryServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String washMode) {
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
    this.washMode = washMode;
  }

  public String getWashMode() {
    return washMode;
  }

  public void setWashMode(String washMode) {
    this.washMode = washMode;
  }
}
