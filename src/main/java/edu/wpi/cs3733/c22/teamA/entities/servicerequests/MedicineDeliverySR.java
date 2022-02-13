package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicineDeliverySR extends SR {
  private String medicineChoice;
  private String toLocation;
  private String requestedEmployee;
  private String comments;

  public MedicineDeliverySR() {
    super();
    this.srType = SRType.MEDICINE_DELIVERY;
  }

  public MedicineDeliverySR(
      String medicineChoice, String toLocation, String requestedEmployee, String comments) {
    super();
    this.medicineChoice = medicineChoice;
    this.toLocation = toLocation;
    this.requestedEmployee = requestedEmployee;
    this.comments = comments;

    this.srType = SRType.MEDICINE_DELIVERY;
  }
}
