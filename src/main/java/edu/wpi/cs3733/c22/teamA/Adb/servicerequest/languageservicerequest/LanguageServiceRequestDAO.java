package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.languageservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.LanguageServiceRequest;
import java.sql.*;
import java.util.List;

public interface LanguageServiceRequestDAO {
  LanguageServiceRequest getLanguageServiceRequest(String ID);

  void updateLanguageServiceRequest(String ID, String field, String change);

  void enterLanguageServiceRequest(LanguageServiceRequest lsr);

  void enterLanguageServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String language);

  void deleteLanguageServiceRequest(String id);

  List<LanguageServiceRequest> getLanguageServiceRequestList();
}
