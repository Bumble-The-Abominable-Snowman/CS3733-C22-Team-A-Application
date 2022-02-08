package edu.wpi.cs3733.c22.teamA.Adb.medicalequipment;

import edu.wpi.cs3733.c22.teamA.entities.MedicalEquipment;
import java.io.IOException;
import java.util.List;

public interface MedicalEquipmentDAO {
  public MedicalEquipment getMedicalEquipment(String ID);

  public void updateMedicalEquipment(String ID, String field, String change);

  public void enterMedicalEquipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable);

  public void deleteMedicalEquipment(String ID);

  public List<MedicalEquipment> getMedicalEquipmentList();

  public void writeMedicalEquipmentCSV(List<MedicalEquipment> List, String csvFilePath)
      throws IOException;
}
