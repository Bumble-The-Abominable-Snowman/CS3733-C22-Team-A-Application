package edu.wpi.teama.Adb.ServiceRequest.LanguageServiceRequest;
import edu.wpi.teama.entities.requests.LanguageServiceRequest;
import java.util.List;
import java.sql.*;

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
