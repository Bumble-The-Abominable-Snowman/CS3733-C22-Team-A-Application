package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicineDeliverySR extends SR {
  private String medicineChoice;
  private String toLocation;
  private String requestedEmployee;
  private String comments;

  public MedicineDeliverySR() {
    super();
  }

  public MedicineDeliverySR(
      String medicineChoice, String toLocation, String requestedEmployee, String comments) {
    super();
    this.medicineChoice = medicineChoice;
    this.toLocation = toLocation;
    this.requestedEmployee = requestedEmployee;
    this.comments = comments;
  }
}
