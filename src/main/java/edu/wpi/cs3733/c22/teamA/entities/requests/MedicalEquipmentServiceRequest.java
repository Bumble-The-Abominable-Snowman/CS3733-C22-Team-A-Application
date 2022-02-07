package edu.wpi.cs3733.c22.teamA.entities.requests;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class MedicalEquipmentServiceRequest
    extends RecursiveTreeObject<MedicalEquipmentServiceRequest> {
  private String requestID;
  private String startLocation;
  private String endLocation;
  private String employeeRequested;
  private String employeeAssigned;
  private String requestTime;
  private String requestStatus;
  private String equipmentID;
  private String requestType;

  public MedicalEquipmentServiceRequest() {}

  public MedicalEquipmentServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      String requestStatus,
      String equipmentID,
      String requestType) {
    this.requestID = requestID;
    this.startLocation = startLocation;
    this.endLocation = endLocation;
    this.employeeRequested = employeeRequested;
    this.employeeAssigned = employeeAssigned;
    this.requestTime = requestTime;
    this.requestStatus = requestStatus;
    this.equipmentID = equipmentID;
    this.requestType = requestType;
  }

  public String getRequestID() {
    return requestID;
  }

  public String getStartLocation() {
    return startLocation;
  }

  public String getEndLocation() {
    return endLocation;
  }

  public String getEmployeeRequested() {
    return employeeRequested;
  }

  public String getEmployeeAssigned() {
    return employeeAssigned;
  }

  public String getRequestTime() {
    return requestTime;
  }

  public String getRequestStatus() {
    return requestStatus;
  }

  public String getEquipmentID() {
    return equipmentID;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestID(String requestID) {
    this.requestID = requestID;
  }

  public void setStartLocation(String startLocation) {
    this.startLocation = startLocation;
  }

  public void setEndLocation(String endLocation) {
    this.endLocation = endLocation;
  }

  public void setEmployeeRequested(String employeeRequested) {
    this.employeeRequested = employeeRequested;
  }

  public void setEmployeeAssigned(String employeeAssigned) {
    this.employeeAssigned = employeeAssigned;
  }

  public void setRequestTime(String requestTime) {
    this.requestTime = requestTime;
  }

  public void setRequestStatus(String requestStatus) {
    this.requestStatus = requestStatus;
  }

  public void setEquipmentID(String equipmentID) {
    this.equipmentID = equipmentID;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }
}
