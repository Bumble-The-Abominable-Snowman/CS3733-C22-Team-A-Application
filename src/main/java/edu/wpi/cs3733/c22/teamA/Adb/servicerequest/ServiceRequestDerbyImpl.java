package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class ServiceRequestDerbyImpl<T> implements ServiceRequestDAO {

  T t;
  String tableName = "-";
  ArrayList<Method> sr_set_data_methods = new ArrayList<>();
  ArrayList<Method> sr_get_data_methods = new ArrayList<>();
  ArrayList<Method> super_sr_set_data_methods = new ArrayList<>();
  ArrayList<Method> super_sr_get_data_methods = new ArrayList<>();
  ArrayList<Method> all_sr_set_data_methods = new ArrayList<>();
  ArrayList<Method> all_sr_get_data_methods = new ArrayList<>();

  public ServiceRequestDerbyImpl(T t) {
    this.t = t;

    String get_regex = "^get";
    String set_regex = "^set";

    Method[] sr_methods = t.getClass().getMethods();

    for (Method sr_method : sr_methods) {

      boolean is_the_method_of_super = sr_method.getDeclaringClass().equals(SR.class);
      boolean is_the_method_exclusive = sr_method.getDeclaringClass().equals(this.t.getClass());

      boolean starts_with_get = sr_method.getName().split(get_regex)[0].equals("");
      boolean starts_with_set = sr_method.getName().split(set_regex)[0].equals("");

      //      sr_method.getGenericParameterTypes() &&
      // sr_method.getReturnType().isInstance(String.class)
      if (true) {
        if (starts_with_get && is_the_method_of_super) {
          this.super_sr_get_data_methods.add(sr_method);
          this.all_sr_get_data_methods.add(sr_method);
        }
        if (starts_with_get && is_the_method_exclusive) {
          this.sr_get_data_methods.add(sr_method);
          this.all_sr_get_data_methods.add(sr_method);
        }
        if (starts_with_set && is_the_method_of_super) {
          this.super_sr_set_data_methods.add(sr_method);
          this.all_sr_set_data_methods.add(sr_method);
        }
        if (starts_with_set && is_the_method_exclusive) {
          this.sr_set_data_methods.add(sr_method);
          this.all_sr_set_data_methods.add(sr_method);
        }
      }
    }

    refreshVariables();
  }

  private void refreshVariables() {

    if (this.t instanceof EquipmentSR) {
      this.tableName = "MedicalEquipmentServiceRequest";
      this.t = (T) new EquipmentSR();
    } else if (this.t instanceof FloralDeliverySR) {
      this.tableName = "FloralDeliveryServiceRequest";
      this.t = (T) new FloralDeliverySR();
    } else if (this.t instanceof FoodDeliverySR) {
      this.tableName = "FoodDeliveryServiceRequest";
      this.t = (T) new FoodDeliverySR();
    } else if (this.t instanceof GiftDeliverySR) {
      this.tableName = "GiftDeliveryServiceRequest";
      this.t = (T) new GiftDeliverySR();
    } else if (this.t instanceof LanguageSR) {
      this.tableName = "LanguageServiceRequest";
      this.t = (T) new LanguageSR();
    } else if (this.t instanceof LaundrySR) {
      this.tableName = "LaundryServiceRequest";
      this.t = (T) new LaundrySR();
    } else if (this.t instanceof MaintenanceSR) {
      this.tableName = "MaintenanceServiceRequest";
      this.t = (T) new MaintenanceSR();
    } else if (this.t instanceof MedicineDeliverySR) {
      this.tableName = "MedicineDeliveryServiceRequest";
      this.t = (T) new MedicineDeliverySR();
    } else if (this.t instanceof ReligiousSR) {
      this.tableName = "ReligiousServiceRequest";
      this.t = (T) new ReligiousSR();
    } else if (this.t instanceof SanitationSR) {
      this.tableName = "SanitationServiceRequest";
      this.t = (T) new SanitationSR();
    } else if (this.t instanceof SecuritySR) {
      this.tableName = "SecurityServiceRequest";
      this.t = (T) new SecuritySR();
    } else {
      this.t = (T) new SR();
    }
  }

  @Override
  public T getRequest(String ID)
      throws SQLException, InvocationTargetException, IllegalAccessException {
    Statement get = Adb.connection.createStatement();

    refreshVariables();

    String str =
        String.format(
            "SELECT * FROM ServiceRequest s, %s r WHERE (s.requestID = r.requestID) AND r.requestID = '%s'",
            this.tableName, ID);

    ResultSet resultSet = get.executeQuery(str);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

    if (resultSet.next()) {
      for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
        for (Method method : this.all_sr_set_data_methods) {
          String methodName = method.getName().toLowerCase(Locale.ROOT);
          String columnName = resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT);
          if (methodName.contains(columnName)) {
            method.invoke(this.t, resultSet.getString(i));
          }
        }
      }
    }

    return this.t;
  }

  @Override
  public void updateServiceRequest(SR sr)
      throws SQLException, InvocationTargetException, IllegalAccessException {
    refreshVariables();

    Statement get = Adb.connection.createStatement();

    String str =
        String.format(
            "SELECT * FROM ServiceRequest s, %s r WHERE (s.requestID = r.requestID) AND r.requestID = '%s'",
            this.tableName, sr.getRequestID());

    ResultSet resultSet = get.executeQuery(str);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

    if (resultSet.next()) {
      for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
        String returnValOld = resultSet.getString(i);
        String columnName = resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT);

        Statement update = Adb.connection.createStatement();

        for (Method method : this.sr_get_data_methods) {
          String methodName = method.toString().toLowerCase(Locale.ROOT);
          if (methodName.contains(columnName)) {
            String returnValNew = (String) method.invoke(sr);
            if (!returnValOld.equals(returnValNew)) {
              str =
                  String.format(
                      "UPDATE %s SET " + columnName + " = '%s' WHERE requestID = '%s'",
                      this.tableName,
                      returnValNew,
                      sr.getRequestID());
              update.execute(str);
            }
          }
        }

        for (Method method : this.super_sr_get_data_methods) {
          String methodName = method.toString().toLowerCase(Locale.ROOT);
          if (methodName.contains(columnName)) {
            //              String returnValNew = (String) method.invoke(sr);
            if (!returnValOld.equals(method.invoke(sr))) {
              str =
                  String.format(
                      "UPDATE ServiceRequest SET " + columnName + " = '%s' WHERE requestID = '%s'",
                      method.invoke(sr),
                      sr.getRequestID());
              update.execute(str);
            }
          }
        }
      }
    }
  }

  @Override
  public void enterServiceRequest(SR sr)
      throws SQLException, InvocationTargetException, IllegalAccessException {
    refreshVariables();

    Statement insert = Adb.connection.createStatement();

    String str =
        String.format(
            "INSERT INTO ServiceRequest(requestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestPriority, comments) "
                + " VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
            sr.getRequestID(),
            sr.getStartLocation(),
            sr.getEndLocation(),
            sr.getEmployeeRequested(),
            sr.getEmployeeAssigned(),
            sr.getRequestTime(),
            sr.getRequestStatus(),
            sr.getRequestPriority(),
            sr.getComments());
    System.out.println("getRequestTime: " + sr.getRequestTime());
    insert.execute(str);

    StringBuilder str2_2 = new StringBuilder();
    str2_2.append(String.format("INSERT INTO %s(requestID", this.tableName));
    StringBuilder str2_3 = new StringBuilder();
    str2_3.append(String.format(" VALUES('%s'", sr.getRequestID()));
    for (Method method : this.sr_get_data_methods) {
      str2_2.append(String.format(", %s", method.getName().substring(3)));
      str2_3.append(String.format(", '%s'", method.invoke(sr)));
    }
    String str2 = str2_2.toString() + ")" + str2_3.toString() + ")";
    insert.execute(str2);

    refreshVariables();
  }

  @Override
  public void deleteServiceRequest(SR sr) throws SQLException {
    refreshVariables();

    Statement delete = Adb.connection.createStatement();

    delete.execute(
        String.format("DELETE FROM %s WHERE requestID = '%s'", this.tableName, sr.getRequestID()));

    delete.execute(
        String.format("DELETE FROM ServiceRequest WHERE requestID = '%s'", sr.getRequestID()));
  }

  @Override
  public List<T> getServiceRequestList()
      throws SQLException, InvocationTargetException, IllegalAccessException {

    Statement getNodeList = Adb.connection.createStatement();

    ArrayList<T> reqList = new ArrayList<>();

    String str =
        String.format(
            "SELECT * FROM ServiceRequest s, %s m WHERE s.requestID=m.requestID", this.tableName);

    ResultSet resultSet = getNodeList.executeQuery(str);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

    while (resultSet.next()) {
      for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
        for (Method method : this.all_sr_set_data_methods) {
          String methodName = method.getName().toLowerCase(Locale.ROOT);
          String columnName = resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT);
          if (methodName.contains(columnName)) {
            method.invoke(this.t, resultSet.getString(i));
          }
        }
      }
      reqList.add(this.t);
      refreshVariables();
    }

    return reqList;
  }

  public static List<Object> getAllServiceRequestList()
      throws SQLException, InvocationTargetException, IllegalAccessException {
    ArrayList<Object> allReqList = new ArrayList<>();

    ServiceRequestDerbyImpl<EquipmentSR> equipmentSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new EquipmentSR());
    allReqList.addAll(equipmentSRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<FloralDeliverySR> floralDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new FloralDeliverySR());
    allReqList.addAll(floralDeliverySRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<FoodDeliverySR> foodDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new FoodDeliverySR());
    allReqList.addAll(foodDeliverySRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<GiftDeliverySR> giftDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new GiftDeliverySR());
    allReqList.addAll(giftDeliverySRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<LanguageSR> languageSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new LanguageSR());
    allReqList.addAll(languageSRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<LaundrySR> laundrySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new LaundrySR());
    allReqList.addAll(laundrySRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<MaintenanceSR> maintenanceSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new MaintenanceSR());
    allReqList.addAll(maintenanceSRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<MedicineDeliverySR> medicineDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new MedicineDeliverySR());
    allReqList.addAll(medicineDeliverySRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<ReligiousSR> religiousSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new ReligiousSR());
    allReqList.addAll(religiousSRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<SanitationSR> sanitationSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new SanitationSR());
    allReqList.addAll(sanitationSRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<SecuritySR> securitySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new SecuritySR());
    allReqList.addAll(securitySRServiceRequestDerby.getServiceRequestList());

    return allReqList;
  }

  // Read from CSV
  public void populateFromCSV(String csvFilePath)
      throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException {
    refreshVariables();
    // System.out.println("STARTING SERVICEREQUEST.POPULATEFROMCSV");

    // delete all before populating
    List<T> serviceRequestList = this.getServiceRequestList();
    for (T value : serviceRequestList) {
      this.deleteServiceRequest((SR) value);
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

      while (dataScanner.hasNext()) {
        String data = dataScanner.next().strip();

        String columnName = field_list.get(dataIndex);
        for (Method method : this.all_sr_set_data_methods) {
          String methodName = method.getName().toLowerCase(Locale.ROOT);
          if (methodName.contains(columnName)) {
            // System.out.println("name: " + method.getName());
            method.invoke(this.t, data);
          }
        }
        dataIndex++;
      }
      // System.out.println(this.t);
      this.enterServiceRequest((SR) this.t);
      refreshVariables();
      dataIndex = 0;
    }
    lineScanner.close();
  }

  public void exportToCSV(String csvFilePath)
      throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException {
    // System.out.println("EXPORT TO CSV TO BE IMPLEMENTED!");

    // Get list of this type of service Requests
    List<T> list = getServiceRequestList();
    // System.out.println("list: " + list);
    // create a writer
    File file = new File(csvFilePath);
    file.createNewFile();
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    // Generate title String (first line)
    //    String tleString = "";
    //    Field[] flds = t.getClass().getSuperclass().getDeclaredFields();
    //    Field[] fldsSub = t.getClass().getDeclaredFields();
    //    for (int x = 0; x < flds.length - 1; x++) {
    //      tleString = tleString + flds[x].getName() + ", ";
    //    }
    //    for (int x = 0; x < fldsSub.length; x++) {
    //      tleString = tleString + fldsSub[x].getName();
    //      if (!(x == fldsSub.length - 1)) {
    //        tleString = tleString + ", ";
    //      }
    //    }

    StringBuilder tleString = new StringBuilder();
    //    Field[] flds = t.getClass().getSuperclass().getDeclaredFields();
    //    Field[] fldsSub = t.getClass().getDeclaredFields();
    //    for (int x = 0; x < this.all_sr_get_data_methods.size(); x++) {
    //      tleString.append(flds[x].getName()).append(", ");
    //    }
    //    for (int x = 0; x < fldsSub.length; x++) {
    //      tleString.append(fldsSub[x].getName());
    //      if (!(x == fldsSub.length - 1)) {
    //        tleString.append(", ");
    //      }
    //    }

    for (Method method : this.all_sr_get_data_methods) {
      String data = method.getName().substring(3) + ", ";
      if (!(data.equals("SrType, "))) {
        tleString.append(data);
      }
    }

    String firstLine = tleString.toString().substring(0, tleString.toString().length() - 2);

    // System.out.println("Final tleSting: " + firstLine);
    writer.write(firstLine);
    writer.newLine();
    // System.out.println("allSrGetDataMethods.size: " + this.all_sr_get_data_methods.size());
    // write data
    for (Object thisSR : list) {
      // System.out.println("-----------------starting for loop----------------");
      String str = "";

      for (int x = 0; x < this.all_sr_get_data_methods.size(); x++) {
        if (!(this.all_sr_get_data_methods.get(x).getName().contains("SrType"))) {

          String data = all_sr_get_data_methods.get(x).invoke(thisSR).toString();
          // System.out.println("data: " + data);
          if (x == 0) {
            str = data;
          } else {
            str = String.join(",", str, data);
          }
        }
      }

      // System.out.println("str: " + str);
      writer.write(str);
      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // Write CSV for table currently unused
  public void writeServiceRequestCSV(ArrayList<Object> list, String csvFilePath)
      throws IOException, InvocationTargetException, IllegalAccessException {
    refreshVariables();
  }
}
