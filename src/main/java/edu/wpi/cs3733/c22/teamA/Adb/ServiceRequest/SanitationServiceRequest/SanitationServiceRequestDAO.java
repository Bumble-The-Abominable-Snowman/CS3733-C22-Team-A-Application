package edu.wpi.teama.Adb.ServiceRequest.SanitationServiceRequest;

import edu.wpi.teama.entities.requests.SanitationServiceRequest;
import java.util.List;
import java.sql.*;

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
