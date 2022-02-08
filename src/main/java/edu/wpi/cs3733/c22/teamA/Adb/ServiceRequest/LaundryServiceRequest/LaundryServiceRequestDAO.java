package edu.wpi.teama.Adb.ServiceRequest.LaundryServiceRequest;

import edu.wpi.teama.entities.requests.LaundryServiceRequest;

import java.sql.*;
import java.util.List;

public interface LaundryServiceRequestDAO {

  LaundryServiceRequest getLaundryServiceRequest(String id);

  void updateLaundryServiceRequest(String ID, String field, String change);

  void enterLaundryServiceRequest(LaundryServiceRequest lsr);

  void enterLaundryServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String washMode);

  void deleteLaundryServiceRequest(String id);

  List<LaundryServiceRequest> getLaundryServiceRequestList();
}
