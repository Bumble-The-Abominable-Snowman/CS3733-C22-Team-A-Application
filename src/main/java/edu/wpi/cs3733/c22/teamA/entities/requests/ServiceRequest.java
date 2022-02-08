package edu.wpi.teama.entities.requests;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.sql.Timestamp;

public class ServiceRequest extends RecursiveTreeObject<ServiceRequest> {
  String requestID;
  String startLocation;
  String endLocation;
  String employeeRequested;
  String employeeAssigned;
  Timestamp requestTime;
  String requestStatus; // Turn this into an Enum?
  String requestType;
  String comments;

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
    return requestTime.toString();
  }

  public String getRequestStatus() {
    return requestStatus;
  }

  public String getRequestType() {
    return requestType;
  }

  public String getComments() {
    return comments;
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
    this.requestTime = Timestamp.valueOf(requestTime);
  }

  public void setRequestStatus(String requestStatus) {
    this.requestStatus = requestStatus;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}
