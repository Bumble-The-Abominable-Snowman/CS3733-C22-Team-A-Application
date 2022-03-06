package edu.wpi.cs3733.c22.teamA;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestWrapperImpl;
import edu.wpi.cs3733.c22.teamA.auth0.AuthUser;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import lombok.val;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;


public class RESTfulAPITest {
    @Test
    public void privateAuthTest() throws Exception {

//        val jsonObject = JSONObject()
//        jsonObject.put("name", "Ancd test")
//        jsonObject.put("city", "delhi")
//        jsonObject.put("age", "23")
//        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        App.DB_CHOICE = "nosql";
        App.authUser = new AuthUser("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InNyMDMtZ3MyT0FScWRBT1Y4VnFJcSJ9.eyJpc3MiOiJodHRwczovL2Rldi14N2JqdDYyaS51cy5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NjIxYzZkY2Q0MWYyY2MwMDY5ZTZkNDY3IiwiYXVkIjpbImh0dHBzOi8vY3MzNzMzYzIydGVhbWEuZGRucy5uZXQvYXBpLyIsImh0dHBzOi8vZGV2LXg3Ymp0NjJpLnVzLmF1dGgwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2NDY1OTQ2NzIsImV4cCI6MTY0NjY4MTA3MiwiYXpwIjoiMXZVM2tyVW5FTjdpY2FFNEVIVDhsRXRRTEZmenlSMFkiLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwicGVybWlzc2lvbnMiOlsicmVhZDpkYi1lbXBsb3llZS1hZG1pbiIsInJlYWQ6ZGItZW1wbG95ZWUtYWlkZSIsInJlYWQ6ZGItZW1wbG95ZWUtY291cmllciIsInJlYWQ6ZGItZW1wbG95ZWUtY3VzdG9kaWFuIiwicmVhZDpkYi1lbXBsb3llZS1kb2N0b3IiLCJyZWFkOmRiLWVtcGxveWVlLWZvb2QiLCJyZWFkOmRiLWVtcGxveWVlLWxhbmd1YWdlIiwicmVhZDpkYi1lbXBsb3llZS1sYXVuZHJ5IiwicmVhZDpkYi1lbXBsb3llZS1tYWludGVuYW5jZSIsInJlYWQ6ZGItZW1wbG95ZWUtbnVyc2UiLCJyZWFkOmRiLWVtcGxveWVlLXJlbGlnaW91cyIsInJlYWQ6ZGItZW1wbG95ZWUtc2VjdXJpdHkiLCJyZWFkOmRiLWVtcGxveWVlLXN0YWZmIiwicmVhZDpkYi1lcXVpcG1lbnQiLCJyZWFkOmRiLWxvY2F0aW9ucyIsInJlYWQ6ZGItbWVkaWNpbmUiLCJyZWFkOmRiLXNyLWNvbnN1bHRhdGlvbiIsInJlYWQ6ZGItc3ItZXF1aXBtZW50LWRlbGl2ZXJ5IiwicmVhZDpkYi1zci1mbG9yYWwtZGVsaXZlcnkiLCJyZWFkOmRiLXNyLWZvb2QtZGVsaXZlcnkiLCJyZWFkOmRiLXNyLWdpZnQtZGVsaXZlcnkiLCJyZWFkOmRiLXNyLWxhbmd1YWdlIiwicmVhZDpkYi1zci1sYXVuZHJ5IiwicmVhZDpkYi1zci1tYWludGVuYW5jZSIsInJlYWQ6ZGItc3ItbWVkaWNpbmUtZGVsaXZlcnkiLCJyZWFkOmRiLXNyLXJlbGlnaW91cyIsInJlYWQ6ZGItc3Itc2FuaXRhdGlvbiIsInJlYWQ6ZGItc3Itc2VjdXJpdHkiLCJ1c2VyLWFkbWluIiwidXNlci1haWRlIiwidXNlci1jb3VyaWVyIiwidXNlci1jdXN0b2RpYW4iLCJ1c2VyLWRvY3RvciIsInVzZXItZm9vZCIsInVzZXItbGFuZ3VhZ2UiLCJ1c2VyLWxhdW5kcnkiLCJ1c2VyLW1haW50ZW5hbmNlIiwidXNlci1udXJzZSIsInVzZXItcmVsaWdpb3VzIiwidXNlci1zZWN1cml0eSIsInVzZXItc3RhZmYiLCJ3cml0ZTpkYi1lbXBsb3llZSIsIndyaXRlOmRiLWVtcGxveWVlLWFkbWluIiwid3JpdGU6ZGItZW1wbG95ZWUtYWlkZSIsIndyaXRlOmRiLWVtcGxveWVlLWNvdXJpZXIiLCJ3cml0ZTpkYi1lbXBsb3llZS1jdXN0b2RpYW4iLCJ3cml0ZTpkYi1lbXBsb3llZS1kb2N0b3IiLCJ3cml0ZTpkYi1lbXBsb3llZS1mb29kIiwid3JpdGU6ZGItZW1wbG95ZWUtbGFuZ3VhZ2UiLCJ3cml0ZTpkYi1lbXBsb3llZS1sYXVuZHJ5Iiwid3JpdGU6ZGItZW1wbG95ZWUtbWFpbnRlbmFuY2UiLCJ3cml0ZTpkYi1lbXBsb3llZS1udXJzZSIsIndyaXRlOmRiLWVtcGxveWVlLXJlbGlnaW91cyIsIndyaXRlOmRiLWVtcGxveWVlLXNlY3VyaXR5Iiwid3JpdGU6ZGItZW1wbG95ZWUtc3RhZmYiLCJ3cml0ZTpkYi1lcXVpcG1lbnQiLCJ3cml0ZTpkYi1sb2NhdGlvbnMiLCJ3cml0ZTpkYi1tZWRpY2luZSIsIndyaXRlOmRiLXNyLWNvbnN1bHRhdGlvbiIsIndyaXRlOmRiLXNyLWVxdWlwbWVudC1kZWxpdmVyeSIsIndyaXRlOmRiLXNyLWZsb3JhbC1kZWxpdmVyeSIsIndyaXRlOmRiLXNyLWZvb2QtZGVsaXZlcnkiLCJ3cml0ZTpkYi1zci1naWZ0LWRlbGl2ZXJ5Iiwid3JpdGU6ZGItc3ItbGFuZ3VhZ2UiLCJ3cml0ZTpkYi1zci1sYXVuZHJ5Iiwid3JpdGU6ZGItc3ItbWFpbnRlbmFuY2UiLCJ3cml0ZTpkYi1zci1tZWRpY2luZS1kZWxpdmVyeSIsIndyaXRlOmRiLXNyLXJlbGlnaW91cyIsIndyaXRlOmRiLXNyLXNhbml0YXRpb24iLCJ3cml0ZTpkYi1zci1zZWN1cml0eSJdfQ.cORxSGjZZ-H-j2fQxJFVExVo7Oz3n_qJGyFxreLouZq4NHMmJUqWqGzTrfnMibefy5H3oYY06zbT_94XxZBRiKx7DPy9m9G__dixdGRzyZr3WSwxhFKLnmbt23eubvWD_ZoUvzytd1DAGVJM4VF46BP4C6-ZQeDChBMDAp3UvSqFYd_dqKWgwKSoIH37OJfzYEcDI7JdRaIgqSTIc66JD2lCkJaa5vUsFIGtlY7y6yoUrHePAF2Nw4ERAIg9mqaSqOWJbxCyey67L1oxrwEKMlcFvpe6AHeENJEPg7V2EEq-99WMHGyY-PmthGnbwhPeHXEXa3xCztZRPiPMgdv6_w");
//        LocationRESTImpl locationREST = new LocationRESTImpl();
//        locationREST.inputFromCSVfile("/home/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/TowerLocations.csv");
////
//        EquipmentRESTImpl equipmentREST = new EquipmentRESTImpl();
//        equipmentREST.inputFromCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipment.csv");
//
//        EmployeeRESTImpl employeeREST = new EmployeeRESTImpl();
//        employeeREST.inputFromCSVfile("/home/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/Employee.CSV");


//        Ser locationREST = new LocationRESTImpl();
//        locationREST.inputFromCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/TowerLocations.csv");

//        ServiceRequestRESTImpl serviceRequestREST = new ServiceRequestRESTImpl(SR.SRType.EQUIPMENT);
//        serviceRequestREST.populateFromCSVfile("/home/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipmentServiceRequest.CSV");

//        serviceRequestREST = new ServiceRequestRESTImpl(SR.SRType.EQUIPMENT);
//        serviceRequestREST.populateFromCSVfile("/home/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipmentServiceRequest.CSV");



        ServiceRequestWrapperImpl serviceRequestWrapper = new ServiceRequestWrapperImpl(SR.SRType.EQUIPMENT);
//        serviceRequestWrapper.populateFromCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipmentServiceRequest.CSV");
        serviceRequestWrapper.exportToCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipmentServiceRequestReplica.CSV");

        MedicineWrapperImpl medicineWrapper = new MedicineWrapperImpl();
//        medicineWrapper.importMedicineFromCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/Medicine.CSV");
//        medicineWrapper.importDosagesFromCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicineDosage.CSV");
        medicineWrapper.exportMedicineToCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicineReplica.CSV");
        medicineWrapper.exportDosagesToCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicineDosageReplica.CSV");

        EquipmentWrapperImpl equipmentWrapper = new EquipmentWrapperImpl();
        equipmentWrapper.exportToCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipmentReplica.csv");

        LocationWrapperImpl locationWrapper = new LocationWrapperImpl();
        locationWrapper.exportToCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/LocationReplica.csv");

        EmployeeWrapperImpl employeeWrapper = new EmployeeWrapperImpl();
        employeeWrapper.exportToCSV("/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/EmployeeReplica.csv");


    }
}
