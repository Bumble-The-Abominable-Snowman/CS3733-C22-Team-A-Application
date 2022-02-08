package edu.wpi.cs3733.c22.teamA.entities.requests;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public @Data class LaundryServiceRequest extends RecursiveTreeObject<LaundryServiceRequest>
    implements Serializable {
  private String requestID;
  private String startLocation;
  private String endLocation;
  private String employeeRequested;
  private String employeeAssigned;
  private Timestamp requestTime;
  private String requestStatus;
  private String equipmentID;
  private String requestType;

  private String washMode;
  private String specialInstructions;

  public String toString() {
    return this.requestID
        + "\t"
        + this.startLocation
        + "\t"
        + this.endLocation
        + "\t"
        + this.employeeRequested
        + "\t"
        + this.employeeAssigned
        + "\t"
        + this.requestTime
        + "\t"
        + this.requestStatus
        + "\t"
        + this.equipmentID
        + "\t"
        + this.washMode
        + "\t"
        + this.specialInstructions;
  }
}
