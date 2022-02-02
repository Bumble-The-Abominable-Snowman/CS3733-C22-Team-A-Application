package edu.wpi.teama.Adb.MedicalEquipment;

import java.util.List;

public interface MedicalEquipmentDAO {
  public static MedicalEquipment getMedicalEquipment(String ID) {
    return null;
  }

  public static void updateMedicalEquipment(String ID, String field, String change) {}

  public static void enterMedicalEquipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable) {}

  public static void deleteMedicalEquipment(String ID) {}

  public static List<MedicalEquipment> getMedicalEquipmentList() {
    return null;
  }
}
