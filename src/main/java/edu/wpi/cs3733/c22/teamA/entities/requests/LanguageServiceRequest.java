package edu.wpi.teama.entities.requests;

import java.sql.Timestamp;

public class LanguageServiceRequest extends ServiceRequest {
  private String language;

  public LanguageServiceRequest() {}

  public LanguageServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      String requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String language) {
    super.requestID = requestID;
    super.startLocation = startLocation;
    super.endLocation = endLocation;
    super.employeeRequested = employeeRequested;
    super.employeeAssigned = employeeAssigned;
    Timestamp rt = Timestamp.valueOf(requestTime);
    super.requestTime = rt;
    super.requestStatus = requestStatus;
    super.requestType = requestType;
    super.comments = comments;
    this.language = language;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
