package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FoodDeliverySR extends SR {
  private String mainDish;
  private String sideDish;
  private String beverage;
  private String dessert;

  public FoodDeliverySR() {
    super();
    this.srType = SRType.FOOD_DELIVERY;
  }

  public FoodDeliverySR(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      Status requestStatus,
      Priority requestPriority,
      String comments) {
    super(
        requestID,
        startLocation,
        endLocation,
        employeeRequested,
        employeeAssigned,
        requestTime,
        requestStatus,
        requestPriority,
        comments);
    this.srType = SRType.FOOD_DELIVERY;
  }
}
