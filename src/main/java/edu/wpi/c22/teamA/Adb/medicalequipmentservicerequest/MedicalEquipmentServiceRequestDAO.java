package edu.wpi.c22.teamA.Adb.medicalequipmentservicerequest;

import edu.wpi.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;

import java.sql.Timestamp;
import java.util.List;

public interface MedicalEquipmentServiceRequestDAO {
  public MedicalEquipmentServiceRequest getMedicalEquipmentServiceRequest(String ID);

  public void updateMedicalEquipmentServiceRequest(String ID, String field, String change);

  public void enterMedicalEquipmentServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String equipmentID,
      String requestType);

  public void deleteMedicalEquipment(String ID);

  public List<MedicalEquipmentServiceRequest> getMedicalEquipmentServiceRequestList();
}
