package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodDeliverySR extends SR {
  private String mainDish;
  private String sideDish;
  private String beverage;
  private String dessert;

  public FoodDeliverySR() {
    super();
  }

  public FoodDeliverySR(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      Status requestStatus,
      String requestType,
      String comments,
      String mainDish,
      String sideDish,
      String beverage,
      String dessert) {
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
    this.mainDish = mainDish;
    this.sideDish = sideDish;
    this.beverage = beverage;
    this.dessert = dessert;

    this.srType = SRType.FOOD_DELIVERY;
  }
}
