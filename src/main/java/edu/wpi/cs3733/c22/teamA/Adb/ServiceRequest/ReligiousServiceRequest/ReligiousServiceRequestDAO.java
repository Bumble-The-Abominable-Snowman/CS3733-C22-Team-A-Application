package edu.wpi.teama.Adb.ServiceRequest.ReligiousServiceRequest;

import edu.wpi.teama.entities.requests.ReligiousServiceRequest;
import java.util.List;
import java.sql.*;

public interface ReligiousServiceRequestDAO {

  ReligiousServiceRequest getReligiousServiceRequest(String id);

  void updateReligiousServiceRequest(String ID, String field, String change);

  void enterReligiousServiceRequest(ReligiousServiceRequest rsr);

  void enterReligiousServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String religion);

  void deleteReligiousServiceRequest(String id);

  List<ReligiousServiceRequest> getReligiousServiceRequestList();
}
