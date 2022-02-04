package edu.wpi.teama.Adb.MedicalEquipment;

import edu.wpi.teama.entities.MedicalEquipment;
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
}
