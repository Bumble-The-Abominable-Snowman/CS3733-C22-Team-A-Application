package edu.wpi.cs3733.c22.teamA.entities.requests;

import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public @Data class FoodDeliveryServiceRequest extends ServiceRequest {
  private String mainDish;
  private String sideDish;
  private String beverage;
  private String dessert;

  public FoodDeliveryServiceRequest() {}

  public FoodDeliveryServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String mainDish,
      String sideDish,
      String beverage,
      String dessert) {
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
    this.mainDish = mainDish;
    this.sideDish = sideDish;
    this.beverage = beverage;
    this.dessert = dessert;
  }
}
