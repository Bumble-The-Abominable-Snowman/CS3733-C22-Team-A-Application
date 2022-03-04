package edu.wpi.cs3733.c22.teamA.Adb.medicalequipment;

import edu.wpi.cs3733.c22.teamA.entities.Equipment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface EquipmentDAO {
  public Equipment getMedicalEquipment(String ID) throws IOException;

  public void updateMedicalEquipment(Equipment e) throws SQLException, IOException;

  public void enterMedicalEquipment(Equipment e) throws IOException;

  public void enterMedicalEquipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable) throws IOException;

  public void deleteMedicalEquipment(String ID) throws IOException;

  public List<Equipment> getMedicalEquipmentList() throws IOException;
}
