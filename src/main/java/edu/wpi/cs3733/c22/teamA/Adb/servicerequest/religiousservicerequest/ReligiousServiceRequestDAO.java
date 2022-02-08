package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.religiousservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.ReligiousServiceRequest;
import java.sql.*;
import java.util.List;

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
