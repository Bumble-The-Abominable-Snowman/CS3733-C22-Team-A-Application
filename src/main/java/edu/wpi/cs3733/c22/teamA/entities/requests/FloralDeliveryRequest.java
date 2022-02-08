package edu.wpi.cs3733.c22.teamA.entities.requests;

public class FloralDeliveryRequest {

  private String flower;
  private String bouquetType;
  private String toLocation;
  private String requestedEmployee;
  private String comments;

  public FloralDeliveryRequest() {}

  public FloralDeliveryRequest(
      String flower,
      String bouquetType,
      String toLocation,
      String requestedEmployee,
      String comments) {
    this.flower = flower;
    this.bouquetType = bouquetType;
    this.toLocation = toLocation;
    this.requestedEmployee = requestedEmployee;
    this.comments = comments;
  }

  public String getFlower() {
    return flower;
  }

  public void setFlower(String flower) {
    this.flower = flower;
  }

  public String getBouquetType() {
    return bouquetType;
  }

  public void setBouquetType(String bouquetType) {
    this.bouquetType = bouquetType;
  }

  public String getToLocation() {
    return toLocation;
  }

  public void setToLocation(String toLocation) {
    this.toLocation = toLocation;
  }

  public String getRequestedEmployee() {
    return requestedEmployee;
  }

  public void setRequestedEmployee(String requestedEmployee) {
    this.requestedEmployee = requestedEmployee;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}
