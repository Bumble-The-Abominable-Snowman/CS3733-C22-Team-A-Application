package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GiftDeliverySR extends SR {

  private String giftDescription;

  public GiftDeliverySR() {
    super();
    this.srType = SRType.GIFT;
  }

  public GiftDeliverySR(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      Status requestStatus,
      String requestType,
      String comments,
      String giftDescription) {
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
    this.giftDescription = giftDescription;

    this.srType = SRType.GIFT;
  }
}