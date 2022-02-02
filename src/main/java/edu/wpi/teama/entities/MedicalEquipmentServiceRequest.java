package edu.wpi.teama.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalEquipmentServiceRequest {
  enum EquipmentType {
    BED,
    XRAY,
    INFUSION_PUMP,
    PATIENT_RECLINER,
  }

  private EquipmentType equipmentType;
  private String toLocation;
  private String fromLocation;
  private String specialNote;
  private Status status;
}
