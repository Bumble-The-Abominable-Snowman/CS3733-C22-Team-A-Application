package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public interface ServiceRequestDAO {
  SR getRequest(String ID) throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException;

  void updateServiceRequest(SR sr)
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException;

  void enterServiceRequest(SR sr)
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException;

  void deleteServiceRequest(SR sr) throws SQLException, IOException;

  List<SR> getServiceRequestList()
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException;


}
