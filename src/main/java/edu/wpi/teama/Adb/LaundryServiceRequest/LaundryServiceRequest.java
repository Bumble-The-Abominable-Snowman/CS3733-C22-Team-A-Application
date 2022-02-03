package edu.wpi.teama.Adb.LaundryServiceRequest;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public @Data class LaundryServiceRequest extends RecursiveTreeObject<LaundryServiceRequest> {
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
}
