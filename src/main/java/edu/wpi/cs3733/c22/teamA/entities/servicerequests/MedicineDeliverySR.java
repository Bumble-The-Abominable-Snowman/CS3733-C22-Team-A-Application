package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicineDeliverySR extends SR {
  private String medicineChoice;

  public MedicineDeliverySR() {
    super();
    this.srType = SRType.MEDICINE_DELIVERY;
  }

  public MedicineDeliverySR(String medicineChoice, String comments) {
    super();
    this.medicineChoice = medicineChoice;
    this.comments = comments;

    this.srType = SRType.MEDICINE_DELIVERY;
  }
}
