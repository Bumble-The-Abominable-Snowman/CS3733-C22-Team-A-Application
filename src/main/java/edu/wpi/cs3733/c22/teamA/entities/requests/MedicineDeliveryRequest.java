package edu.wpi.cs3733.c22.teamA.entities.requests;

public class MedicineDeliveryRequest {
  private String medicineChoice;
  private String toLocation;
  private String requestedEmployee;
  private String comments;

  public MedicineDeliveryRequest() {}

  public MedicineDeliveryRequest(
      String medicineChoice, String toLocation, String requestedEmployee, String comments) {
    this.medicineChoice = medicineChoice;
    this.toLocation = toLocation;
    this.requestedEmployee = requestedEmployee;
    this.comments = comments;
  }

  public String getMedicineChoice() {
    return medicineChoice;
  }

  public void setMedicineChoice(String medicineChoice) {
    this.medicineChoice = medicineChoice;
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
