package edu.wpi.teama.Adb.MedicalEquipmentServiceRequest;

import java.sql.Timestamp;
import java.util.List;

public interface MedicalEquipmentServiceRequestDAO {
    public static MedicalEquipmentServiceRequest getMedicalEquipmentServiceRequest(String ID) {
        return null;
    }

    public static void updateMedicalEquipmentServiceRequest(String ID, String field, String change) {

    }

    public static void enterMedicalEquipmentServiceRequest(String requestID, String startLocation,
                                                           String endLocation, String employeeRequested,
                                                           String employeeAssigned, Timestamp requestTime,
                                                           String requestStatus, String equipmentID, String requestType) {

    }

    public static void deleteMedicalEquipment(String ID) {

    }

    public static List<MedicalEquipmentServiceRequest> getMedicalEquipmentServiceRequestList() {
        return null;
    }

}
