package edu.wpi.cs3733.c22.teamA.entities.requests;

import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public @Data class FoodDeliveryServiceRequest extends ServiceRequest {

  public FoodDeliveryServiceRequest() {}

  public FoodDeliveryServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments) {
    super.requestID = requestID;
    super.startLocation = startLocation;
    super.endLocation = endLocation;
    super.employeeRequested = employeeRequested;
    super.employeeAssigned = employeeAssigned;
    super.requestTime = requestTime;
    super.requestStatus = requestStatus;
    super.requestType = requestType;
    super.comments = comments;
  }
}
