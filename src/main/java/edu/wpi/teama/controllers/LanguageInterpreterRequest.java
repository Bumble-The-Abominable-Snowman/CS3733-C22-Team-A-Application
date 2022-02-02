package edu.wpi.teama.controllers;

public class LanguageInterpreterRequest {
  private String language;
  private String toLocation;
  private String employee;
  private String comments;

  public LanguageInterpreterRequest() {}

  public LanguageInterpreterRequest(
      String language, String toLocation, String employee, String comments) {
    this.language = language;
    this.toLocation = toLocation;
    this.employee = employee;
    this.comments = comments;
  }

  public String getLanguage() {
    return language;
  }

  public String getToLocation() {
    return toLocation;
  }

  public String getEmployee() {
    return employee;
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

  public void setEmployee(String employee) {
    this.employee = employee;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}
