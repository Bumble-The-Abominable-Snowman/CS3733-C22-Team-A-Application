package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EquipmentSR extends SR implements Cloneable {

  private String equipmentID;

  public EquipmentSR() {
    super();
    this.srType = SRType.EQUIPMENT;
    this.requestStatus = Status.BLANK;
  }

  public EquipmentSR(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      Status requestStatus,
      String requestType,
      String comments,
      String equipmentID) {
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
    this.equipmentID = equipmentID;

    this.srType = SRType.EQUIPMENT;
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
