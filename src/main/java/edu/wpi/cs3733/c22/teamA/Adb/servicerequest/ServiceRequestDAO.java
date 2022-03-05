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
  SR getRequest(String ID) throws SQLException, InvocationTargetException, IllegalAccessException, IOException;

  void updateServiceRequest(SR sr)
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException;

  void enterServiceRequest(SR sr)
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException;

  void deleteServiceRequest(SR sr) throws SQLException, IOException;

  List<SR> getServiceRequestList()
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException;


  // Read from CSV
  public default void populateFromCSV(String csvFilePath)
          throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException {

    // delete all before populating
    List<SR> serviceRequestList = this.getServiceRequestList();
    for (SR sr : serviceRequestList) {
      this.deleteServiceRequest(sr);
    }

    ClassLoader classLoader = ServiceRequestDerbyImpl.class.getClassLoader();
    InputStream is = classLoader.getResourceAsStream(csvFilePath);
    Scanner lineScanner = new Scanner(is);

    Scanner dataScanner;
    int dataIndex = 0;
    List<String> field_list = new ArrayList<>();

    dataScanner = new Scanner(lineScanner.nextLine());
    dataScanner.useDelimiter(",");
    while (dataScanner.hasNext()) {
      field_list.add(dataScanner.next().toLowerCase(Locale.ROOT).strip());
    }

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");

      SR sr = new SR();
      while (dataScanner.hasNext()) {
        String data = dataScanner.next().strip();

        String columnName = field_list.get(dataIndex);
        sr.setFieldByString(columnName, data);
        dataIndex++;
      }
//       System.out.println(sr);
      this.enterServiceRequest(sr);
      dataIndex = 0;
    }
    lineScanner.close();
  }

  public default void populateFromCSVfile(String csvFilePath)
          throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException {

    // delete all before populating
    List<SR> serviceRequestList = this.getServiceRequestList();
    for (SR sr : serviceRequestList) {
      this.deleteServiceRequest(sr);
    }

    File file = new File(csvFilePath);
    Scanner lineScanner = new Scanner(file);

    Scanner dataScanner;
    int dataIndex = 0;
    List<String> field_list = new ArrayList<>();

    dataScanner = new Scanner(lineScanner.nextLine());
    dataScanner.useDelimiter(",");
    while (dataScanner.hasNext()) {
      field_list.add(dataScanner.next().toLowerCase(Locale.ROOT).strip());
    }

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");

      SR sr = new SR();
      while (dataScanner.hasNext()) {
        String data = dataScanner.next().strip();

        String columnName = field_list.get(dataIndex);
        sr.setFieldByString(columnName, data);
        dataIndex++;
      }
//       System.out.println(sr);
      this.enterServiceRequest(sr);
      dataIndex = 0;
    }
    lineScanner.close();
  }

  public default void exportToCSV(String csvFilePath)
          throws Exception {

    // Get list of this type of service Requests
    List<SR> list = getServiceRequestList();
    if (list.size() > 0) {

      File file = new File(csvFilePath);
      file.createNewFile();
      BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

      StringBuilder tleString = new StringBuilder();

      // column line
      for (String key : list.get(0).getStringFields().keySet()) {
        key = key + ", ";
        if (!(key.equals("sr_type, "))) {
          tleString.append(key);
        }
      }
      String firstLine = tleString.toString().substring(0, tleString.toString().length() - 2);

      writer.write(firstLine);
      writer.newLine();

      // rows
      for (SR thisSR : list) {
        String str = "";

        boolean first_column = true;
        for (String key : thisSR.getStringFields().keySet()) {
          if (!(key.equals("sr_type"))) {
            if (first_column) {
              str = thisSR.getStringFields().get(key);
              first_column = false;
            } else {
              str = String.join(",", str, thisSR.getStringFields().get(key));
            }
          }
        }

        writer.write(str);
        writer.newLine();
      }
      writer.close(); // close the writer
    }
    else
    {
      throw new Exception("SR list is empty!");
    }
  }

  // Write CSV for table currently unused
  public default void writeServiceRequestCSV(ArrayList<Object> list, String csvFilePath)
          throws IOException, InvocationTargetException, IllegalAccessException {
//    refreshVariables();
  }
}
