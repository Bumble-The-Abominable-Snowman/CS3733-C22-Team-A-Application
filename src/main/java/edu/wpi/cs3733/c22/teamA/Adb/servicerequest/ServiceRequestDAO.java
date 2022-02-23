package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface ServiceRequestDAO<T> {
  T getRequest(String ID) throws SQLException, InvocationTargetException, IllegalAccessException;

  void updateServiceRequest(SR sr)
      throws SQLException, InvocationTargetException, IllegalAccessException;

  void enterServiceRequest(SR sr)
      throws SQLException, InvocationTargetException, IllegalAccessException;

  void deleteServiceRequest(SR sr) throws SQLException;

  List<T> getServiceRequestList()
      throws SQLException, InvocationTargetException, IllegalAccessException;

  void populateFromCSV(String csvFilePath)
      throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException;

  void exportToCSV(String csvFilePath)
          throws Exception;
}
