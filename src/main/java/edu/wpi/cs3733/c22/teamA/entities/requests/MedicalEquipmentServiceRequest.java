package edu.wpi.cs3733.c22.teamA.entities.requests;

import java.sql.Timestamp;

public class MedicalEquipmentServiceRequest extends ServiceRequest {

  private String equipmentID;

  public MedicalEquipmentServiceRequest() {}

  public MedicalEquipmentServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String equipmentID) {
    super.requestID = requestID;
    super.startLocation = startLocation;
    super.endLocation = endLocation;
    super.employeeRequested = employeeRequested;
    super.employeeAssigned = employeeAssigned;
    Timestamp rt = Timestamp.valueOf(requestTime);
    super.requestTime = rt;
    super.requestStatus = requestStatus;
    this.equipmentID = equipmentID;
    super.requestType = requestType;
    super.comments = comments;
  }

  public String getEquipmentID() {
    return equipmentID;
  }

  public void setEquipmentID(String equipmentID) {
    this.equipmentID = equipmentID;
  }
}
