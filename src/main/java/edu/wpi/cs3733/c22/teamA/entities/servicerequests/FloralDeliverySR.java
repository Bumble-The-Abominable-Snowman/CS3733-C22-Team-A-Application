package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FloralDeliverySR extends SR {

  private String flower;
  private String bouquetType;
  private String toLocation;
  private String requestedEmployee;
  private String comments;

  public FloralDeliverySR() {
    super();
  }

  public FloralDeliverySR(
      String flower,
      String bouquetType,
      String toLocation,
      String requestedEmployee,
      String comments) {
    super();
    this.flower = flower;
    this.bouquetType = bouquetType;
    this.toLocation = toLocation;
    this.requestedEmployee = requestedEmployee;
    this.comments = comments;
  }
}
