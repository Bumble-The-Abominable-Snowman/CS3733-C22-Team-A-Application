package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.util.List;

public interface ServiceRequestDAO<T> {
  T getRequest(String ID);

  void updateServiceRequest(SR sr);

  void enterServiceRequest(SR sr);

  void deleteServiceRequest(SR sr);

  List<T> getServiceRequestList();
}
