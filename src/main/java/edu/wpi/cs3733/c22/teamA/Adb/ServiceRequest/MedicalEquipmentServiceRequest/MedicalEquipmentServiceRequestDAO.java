package edu.wpi.cs3733.c22.teamA.Adb.ServiceRequest.MedicalEquipmentServiceRequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

public interface MedicalEquipmentServiceRequestDAO {
  MedicalEquipmentServiceRequest getMedicalEquipmentServiceRequest(String ID);

  void updateMedicalEquipmentServiceRequest(String ID, String field, String change);

  void enterMedicalEquipmentServiceRequest(MedicalEquipmentServiceRequest mesr);
  void enterMedicalEquipmentServiceRequest(
          String requestID,
          String startLocation,
          String endLocation,
          String employeeRequested,
          String employeeAssigned,
          Timestamp requestTime,
          String requestStatus,
          String requestType,
          String comments,
          String equipmentID);

  void deleteMedicalEquipment(String ID);

  List<MedicalEquipmentServiceRequest> getMedicalEquipmentServiceRequestList();

  // Write CSV for MedicalEquipmentServiceRequest table
  public void writeMedicalEquipmentServiceRequestCSV(
          List<MedicalEquipmentServiceRequest> List, String csvFilePath) throws IOException;
}
