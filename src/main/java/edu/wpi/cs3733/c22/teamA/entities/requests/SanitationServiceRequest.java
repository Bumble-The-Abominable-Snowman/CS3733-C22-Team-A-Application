package edu.wpi.cs3733.c22.teamA.entities.requests;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SanitationServiceRequest extends ServiceRequest {
  private String sanitationType;

  public SanitationServiceRequest() {}

  public SanitationServiceRequest(
          String requestID,
          String startLocation,
          String endLocation,
          String employeeRequested,
          String employeeAssigned,
          String requestTime,
          String requestStatus,
          String requestType,
          String comments,
          String sanitationType) {
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
    this.sanitationType = sanitationType;
  }

  public String getSanitationType() {
    return sanitationType;
  }

  public void setSanitationType(String sanitationType) {
    this.sanitationType = sanitationType;
  }
}