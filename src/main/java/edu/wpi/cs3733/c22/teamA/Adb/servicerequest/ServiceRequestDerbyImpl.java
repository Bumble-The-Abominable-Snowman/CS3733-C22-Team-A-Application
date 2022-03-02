package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class ServiceRequestDerbyImpl implements ServiceRequestDAO {

  String tableName = "";
  SR.SRType srType;

  public ServiceRequestDerbyImpl(SR.SRType srType)
  {
    this.srType = srType;
    switch (this.srType)
    {
      case EQUIPMENT:
        this.tableName = "MedicalEquipmentServiceRequest";
        break;
      case FLORAL_DELIVERY:
        this.tableName = "FloralDeliveryServiceRequest";
        break;
      case FOOD_DELIVERY:
        this.tableName = "FoodDeliveryServiceRequest";
        break;
      case GIFT_DELIVERY:
        this.tableName = "GiftDeliveryServiceRequest";
        break;
      case LANGUAGE:
        this.tableName = "LanguageServiceRequest";
        break;
      case LAUNDRY:
        this.tableName = "LaundryServiceRequest";
        break;
      case MAINTENANCE:
        this.tableName = "MaintenanceServiceRequest";
        break;
      case MEDICINE_DELIVERY:
        this.tableName = "MedicineDeliveryServiceRequest";
        break;
      case RELIGIOUS:
        this.tableName = "ReligiousServiceRequest";
        break;
      case SANITATION:
        this.tableName = "SanitationServiceRequest";
        break;
      case SECURITY:
        this.tableName = "SecurityServiceRequest";
        break;
      case CONSULTATION:
        this.tableName = "ConsultationServiceRequest";
        break;
      case INVALID:
        this.tableName = "INVALID";
        break;
    }
  }

  @Override
  public SR getRequest(String ID)
      throws SQLException, InvocationTargetException, IllegalAccessException {
    Statement get = Adb.connection.createStatement();

    String str =
        String.format(
            "SELECT * FROM ServiceRequest s, %s r WHERE (s.request_id = r.request_id) AND r.request_id = '%s'",
            this.tableName, ID);

    ResultSet resultSet = get.executeQuery(str);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

    SR sr = new SR();

    if (resultSet.next()) {
      for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
        sr.setField(resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT), resultSet.getString(i));
      }
    }

    return sr;
  }

  @Override
  public void updateServiceRequest(SR sr)
      throws SQLException, InvocationTargetException, IllegalAccessException {

    Statement get = Adb.connection.createStatement();

    HashMap<String, String> sr_string_fields = sr.getStringFields();

    String str =
        String.format(
            "SELECT * FROM ServiceRequest s, %s r WHERE (s.request_id = r.request_id) AND r.request_id = '%s'",
            this.tableName, sr_string_fields.get("request_id"));

    ResultSet resultSet = get.executeQuery(str);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

    if (resultSet.next()) {
      for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
        String returnValOld = resultSet.getString(i);
        String columnName = resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT);

        Statement update = Adb.connection.createStatement();

        if (!returnValOld.equals(sr_string_fields.get(columnName)))
        {
          if (columnName.equals("request_id")
                  || columnName.equals("start_location")
                  || columnName.equals("end_location")
                  || columnName.equals("employee_requested")
                  || columnName.equals("employee_assigned")
                  || columnName.equals("request_time")
                  || columnName.equals("request_status")
                  || columnName.equals("request_priority")
                  || columnName.equals("comments"))
          {
            str =
                    String.format(
                            "UPDATE ServiceRequest SET " + columnName + " = '%s' WHERE request_id = '%s'",
                            sr_string_fields.get(columnName),
                            sr_string_fields.get("request_id"));
          } else {
            str =
                    String.format(
                            "UPDATE %s SET " + columnName + " = '%s' WHERE request_id = '%s'",
                            this.tableName,
                            sr_string_fields.get(columnName),
                            sr_string_fields.get("request_id"));

          }
          update.execute(str);
        }

      }
    }
  }

  @Override
  public void enterServiceRequest(SR sr)
      throws SQLException, InvocationTargetException, IllegalAccessException {
    Statement insert = Adb.connection.createStatement();

    HashMap<String, String> sr_string_fields = sr.getStringFields();
    System.out.println(sr_string_fields);
    String str =
        String.format(
            "INSERT INTO ServiceRequest(request_id, start_location, end_location, employee_requested, employee_assigned, request_time, request_status, request_priority, comments) "
                + " VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                sr_string_fields.get("request_id"),
                sr_string_fields.get("start_location"),
                sr_string_fields.get("end_location"),
                sr_string_fields.get("employee_requested"),
                sr_string_fields.get("employee_assigned"),
                sr_string_fields.get("request_time"),
                sr_string_fields.get("request_status"),
                sr_string_fields.get("request_priority"),
                sr_string_fields.get("comments"));
    System.out.println(str);
    insert.execute(str);



    StringBuilder str2_2 = new StringBuilder();
    str2_2.append(String.format("INSERT INTO %s(request_id", this.tableName));
    StringBuilder str2_3 = new StringBuilder();
    str2_3.append(String.format(" VALUES('%s'", sr_string_fields.get("request_id")));
    for (String key : sr_string_fields.keySet()) {
      if (!(Objects.equals(key, "request_id")
              || Objects.equals(key, "start_location")
              || Objects.equals(key, "end_location")
              || Objects.equals(key, "employee_requested")
              || Objects.equals(key, "employee_assigned")
              || Objects.equals(key, "request_time")
              || Objects.equals(key, "request_status")
              || Objects.equals(key, "request_priority")
              || Objects.equals(key, "comments")
              || Objects.equals(key, "sr_type")))
      {
        str2_2.append(String.format(", %s", key));
        str2_3.append(String.format(", '%s'", sr_string_fields.get(key)));

      }
    }

    String str2 = str2_2.toString() + ")" + str2_3.toString() + ")";
    insert.execute(str2);
  }

  @Override
  public void deleteServiceRequest(SR sr) throws SQLException {
    Statement delete = Adb.connection.createStatement();

    delete.execute(
        String.format("DELETE FROM %s WHERE request_id = '%s'", this.tableName, sr.getFields().get("request_id")));

    delete.execute(
        String.format("DELETE FROM ServiceRequest WHERE request_id = '%s'", sr.getFields().get("request_id")));
  }

  @Override
  public List<SR> getServiceRequestList()
      throws SQLException, InvocationTargetException, IllegalAccessException {

    Statement getNodeList = Adb.connection.createStatement();

    ArrayList<SR> reqList = new ArrayList<>();

    String str =
        String.format(
            "SELECT * FROM ServiceRequest s, %s m WHERE s.request_id=m.request_id", this.tableName);
    System.out.println(str);

    ResultSet resultSet = getNodeList.executeQuery(str);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

    while (resultSet.next()) {
      SR sr = new SR(this.srType);
      for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
        String columnName = resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT);
        sr.setFieldByString(columnName, resultSet.getString(i));
      }
      reqList.add(sr);
    }

    return reqList;
  }

  public static List<SR> getAllServiceRequestList()
          throws SQLException, IllegalAccessException, InvocationTargetException {
    ArrayList<SR> allReqList = new ArrayList<>();

    for (SR.SRType srType: SR.SRType.values()) {
      if (srType != SR.SRType.INVALID)
      {
        ServiceRequestDerbyImpl serviceRequestDerby = new ServiceRequestDerbyImpl(srType);
        System.out.println(srType.toString());
        allReqList.addAll(serviceRequestDerby.getServiceRequestList());
      }
    }
    return allReqList;
  }

  // Read from CSV
  public void populateFromCSV(String csvFilePath)
      throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException {

    // delete all before populating
    List<SR> serviceRequestList = this.getServiceRequestList();
    for (SR sr : serviceRequestList) {
      this.deleteServiceRequest(sr);
    }

    Scanner lineScanner = null;
    if(!Adb.isInitialized) {
      ClassLoader classLoader = ServiceRequestDerbyImpl.class.getClassLoader();
      InputStream is = classLoader.getResourceAsStream(csvFilePath);
      lineScanner = new Scanner(is);
    }else{
      File file = new File(csvFilePath);
      lineScanner = new Scanner(file);
    }

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

  public void exportToCSV(String csvFilePath)
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
  public void writeServiceRequestCSV(ArrayList<Object> list, String csvFilePath)
      throws IOException, InvocationTargetException, IllegalAccessException {
//    refreshVariables();
  }
}
