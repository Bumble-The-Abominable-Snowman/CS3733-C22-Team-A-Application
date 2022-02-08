package edu.wpi.cs3733.c22.teamA.entities.requests;

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
  private String requestType;

  private String washMode;
  private String specialInstructions;

  public LaundryServiceRequest(){}

  public LaundryServiceRequest(String requestID,
          String startLocation,
          String endLocation,
          String employeeRequested,
          String employeeAssigned,
          Timestamp requestTime,
          String requestStatus,
          String requestType){
    this.requestID = requestID;
    this.startLocation = startLocation;
    this.endLocation = endLocation;
    this.employeeRequested = employeeRequested;
    this.employeeAssigned = employeeAssigned;
    this.requestTime = requestTime;
    this.requestStatus = requestStatus;
    this.requestType = requestType;
  }

  // Getter
  public String getRequestID() {
    return this.requestID;
  }

  public String getStartLocation() {
    return this.startLocation;
  }

  public String getEndLocation() {
    return this.endLocation;
  }

  public String getEmployeeRequested() {
    return this.employeeRequested;
  }

  public String getEmployeeAssigned() {
    return this.employeeAssigned;
  }

  public Timestamp getRequestTime() {
    return this.requestTime;
  }

  public String getRequestStatus() {
    return this.requestStatus;
  }

  public String getRequestType() {
    return this.requestType;
  }

  public String getWashMode(){
    return this.washMode;
  }

  public String getSpecialInstructions(){
    return this.specialInstructions;
  }

  // Setter
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

  public void setRequestTime(Timestamp requestTime) {
    this.requestTime = requestTime;
  }

  public void setRequestStatus(String requestStatus) {
    this.requestStatus = requestStatus;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public void setSpecialInstructions(String specialInstructions){ this.specialInstructions = specialInstructions; }
}
