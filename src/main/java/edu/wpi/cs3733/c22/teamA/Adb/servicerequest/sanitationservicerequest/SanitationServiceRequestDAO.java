package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.sanitationservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.SanitationServiceRequest;
import java.sql.*;
import java.util.List;

public interface SanitationServiceRequestDAO {

  SanitationServiceRequest getSanitationServiceRequest(String id);

  void updateSanitationServiceRequest(String ID, String field, String change);

  void enterSanitationServiceRequest(SanitationServiceRequest ssr);

  void enterSanitationServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String sanitationType);

  void deleteSanitationServiceRequest(String id);

  List<SanitationServiceRequest> getSanitationServiceRequestList();
}
