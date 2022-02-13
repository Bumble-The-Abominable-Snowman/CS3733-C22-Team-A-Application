package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FloralDeliverySR extends SR {

  private String flower;
  private String bouquetType;
  private String toLocation;
  private String requestedEmployee;
  private String comments;

  public FloralDeliverySR() {
    super();
    this.srType = SRType.FLORAL_DELIVERY;
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

    this.srType = SRType.FLORAL_DELIVERY;
  }
}
