package edu.wpi.teama.entities;

public class LanguageInterpreterRequest {
  private String language;
  private String toLocation;
  private String requestedEmployee;
  private String comments;

  public LanguageInterpreterRequest() {}

  public LanguageInterpreterRequest(
      String language, String toLocation, String requestedEmployee, String comments) {
    this.language = language;
    this.toLocation = toLocation;
    this.requestedEmployee = requestedEmployee;
    this.comments = comments;
  }

  public String getLanguage() {
    return language;
  }

  public String getToLocation() {
    return toLocation;
  }

  public String getRequestedEmployee() {
    return requestedEmployee;
  }

  public String getComments() {
    return comments;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setToLocation(String toLocation) {
    this.toLocation = toLocation;
  }

  public void setRequestedEmployee(String requestedEmployee) {
    this.requestedEmployee = requestedEmployee;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}
