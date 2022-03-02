package edu.wpi.cs3733.c22.teamA.Adb.medicalequipment;

import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import java.sql.SQLException;
import java.util.List;

public interface EquipmentDAO {
  public Equipment getMedicalEquipment(String ID);

  public void updateMedicalEquipment(String ID, String field, String change) throws SQLException;

  public void enterMedicalEquipment(Equipment e);

  public void enterMedicalEquipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable);

  public void deleteMedicalEquipment(String ID);

  public List<Equipment> getMedicalEquipmentList();
}
