package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    refreshVariables();
    System.out.println(this.tableName);
  }

  private void refreshVariables() {

    if (this.t instanceof EquipmentSR) {
      this.tableName = "MedicalEquipmentServiceRequest";
      this.t = (T) new EquipmentSR();
    } else if (this.t instanceof FloralDeliverySR) {
      this.tableName = "FloralDeliveryServiceRequest";
      this.t = (T) new FloralDeliverySR();
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
  public T getRequest(String ID) {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement get = connection.createStatement();

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

      connection.close();
      return this.t;
    } catch (SQLException e) {
      System.out.println("Connection Failed");
      e.printStackTrace();
    } catch (InvocationTargetException | IllegalAccessException e) {
      System.out.println("SR Object Creation Failed");
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void updateServiceRequest(SR sr) {
    refreshVariables();

    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement get = connection.createStatement();

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

          Statement update = connection.createStatement();

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
                        "UPDATE ServiceRequest SET "
                            + columnName
                            + " = '%s' WHERE requestID = '%s'",
                        method.invoke(sr),
                        sr.getRequestID());
                update.execute(str);
              }
            }
          }
        }
      }
      connection.close();
    } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    }
  }

  @Override
  public void enterServiceRequest(SR sr) {
    refreshVariables();

    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement insert = connection.createStatement();

      String str =
          String.format(
              "INSERT INTO ServiceRequest(requestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments) "
                  + " VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
              sr.getRequestID(),
              sr.getStartLocation(),
              sr.getEndLocation(),
              sr.getEmployeeRequested(),
              sr.getEmployeeAssigned(),
              sr.getRequestTime(),
              sr.getRequestStatus().toString(),
              sr.getRequestType(),
              sr.getComments());
      insert.execute(str);

      for (Method method : this.sr_get_data_methods) {
        String str2 =
            String.format(
                "INSERT INTO %s(requestID, %s)" + " VALUES('%s', '%s')",
                this.tableName,
                method.getName().substring(3),
                sr.getRequestID(),
                method.invoke(sr));

        insert.execute(str2);
      }

      connection.close();

    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
      return;
    } catch (InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
    refreshVariables();
  }

  @Override
  public void deleteServiceRequest(SR sr) {
    refreshVariables();

    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement delete = connection.createStatement();

      delete.execute(
          String.format(
              "DELETE FROM %s WHERE requestID = '%s'", this.tableName, sr.getRequestID()));

      delete.execute(
          String.format("DELETE FROM ServiceRequest WHERE requestID = '%s'", sr.getRequestID()));

      connection.close();
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    }
  }

  @Override
  public List<T> getServiceRequestList() {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement getNodeList = connection.createStatement();

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
      connection.close();
      return reqList;
    } catch (SQLException e) {
      System.out.println("Failed");
      e.printStackTrace();
    } catch (InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static List<Object> getAllServiceRequestList() {
    ArrayList<Object> allReqList = new ArrayList<>();

    String[] tableNameList = {
      "MedicalEquipmentServiceRequest",
      "FloralDeliveryServiceRequest",
      "GiftDeliveryServiceRequest",
      "LanguageServiceRequest",
      "LaundryServiceRequest",
      "MaintenanceServiceRequest",
      "MedicineDeliveryServiceRequest",
      "ReligiousServiceRequest",
      "SanitationServiceRequest",
      "SecurityServiceRequest"
    };

    ServiceRequestDerbyImpl<EquipmentSR> equipmentSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new EquipmentSR());
    allReqList.addAll(equipmentSRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<FloralDeliverySR> floralDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new FloralDeliverySR());
    allReqList.addAll(floralDeliverySRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<GiftDeliverySR> giftDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new GiftDeliverySR());
    allReqList.addAll(giftDeliverySRServiceRequestDerby.getServiceRequestList());

    ServiceRequestDerbyImpl<LanguageSR> languageSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new LanguageSR());
    allReqList.addAll(languageSRServiceRequestDerby.getServiceRequestList());

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
      throws IOException, ParseException, InvocationTargetException, IllegalAccessException {
    refreshVariables();

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
      field_list.add(dataScanner.next().toLowerCase(Locale.ROOT).replaceAll("\\s+", ""));
    }

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");

      while (dataScanner.hasNext()) {
        String data = dataScanner.next();

        String columnName = field_list.get(dataIndex);
        for (Method method : this.all_sr_set_data_methods) {
          String methodName = method.getName().toLowerCase(Locale.ROOT);
          if (methodName.contains(columnName)) {
            method.invoke(this.t, data);
          }
        }
        dataIndex++;
      }
      this.enterServiceRequest((SR) this.t);
      refreshVariables();
      dataIndex = 0;
    }
    lineScanner.close();
  }

  // Write CSV for table
  public static void writeMedicalEquipmentServiceRequestCSV(String csvFilePath) throws IOException {

    //    // create a writer
    //    File file = new File(csvFilePath);
    //    file.createNewFile();
    //    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));
    //
    //    writer.write(
    //            "RequestID, startLocation, endLocation, employeeRequested, employeeAssigned,
    // requestTime, requestStatus, requestType, comments, equipmentID");
    //    writer.newLine();
    //
    //    // write data
    //    for (MedicalEquipmentServiceRequest thisMESR : List) {
    //
    //      writer.write(
    //              String.join(
    //                      ",",
    //                      thisMESR.getRequestID(),
    //                      thisMESR.getStartLocation(),
    //                      thisMESR.getEndLocation(),
    //                      thisMESR.getEmployeeRequested(),
    //                      thisMESR.getEmployeeAssigned(),
    //                      thisMESR.getRequestTime(),
    //                      thisMESR.getRequestStatus(),
    //                      thisMESR.getRequestType(),
    //                      thisMESR.getComments(),
    //                      thisMESR.getEquipmentID()));
    //
    //      writer.newLine();
    //    }
    //    writer.close(); // close the writer
  }
}
