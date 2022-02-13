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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ServiceRequestDerbyImpl<T> implements ServiceRequestDAO {

  T t;

  public ServiceRequestDerbyImpl(T t) {
    this.t = t;
  }

  private String refreshVariables() {
    String tableName = "-";

    if (this.t instanceof EquipmentSR) {
      tableName = "MedicalEquipmentServiceRequest";
      this.t = (T) new EquipmentSR();
    }
    if (this.t instanceof FloralDeliverySR) {
      tableName = "FloralDeliveryServiceRequest";
      this.t = (T) new FloralDeliverySR();
    }
    if (this.t instanceof GiftSR) {
      tableName = "GiftDeliveryServiceRequest";
      this.t = (T) new GiftSR();
    }
    if (this.t instanceof LanguageSR) {
      tableName = "LanguageServiceRequest";
      this.t = (T) new LanguageSR();
    }
    if (this.t instanceof LaundrySR) {
      tableName = "LaundryServiceRequest";
      this.t = (T) new LaundrySR();
    }
    if (this.t instanceof MaintenanceSR) {
      tableName = "MaintenanceServiceRequest";
      this.t = (T) new MaintenanceSR();
    }
    if (this.t instanceof MedicineDeliverySR) {
      tableName = "MedicineDeliveryServiceRequest";
      this.t = (T) new MedicineDeliverySR();
    }
    if (this.t instanceof ReligiousSR) {
      tableName = "ReligiousServiceRequest";
      this.t = (T) new ReligiousSR();
    }
    if (this.t instanceof SanitationSR) {
      tableName = "SanitationServiceRequest";
      this.t = (T) new SanitationSR();
    }
    if (this.t instanceof SecuritySR) {
      tableName = "SecurityServiceRequest";
      this.t = (T) new SecuritySR();
    }

    return tableName;
  }

  @Override
  public T getRequest(String ID) {
    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement get = connection.createStatement();

      String tableName = refreshVariables();

      String str =
          String.format(
              "SELECT * FROM ServiceRequest s, %s r WHERE (s.requestID = r.requestID) AND r.requestID = '%s'",
              tableName, ID);

      ResultSet resultSet = get.executeQuery(str);
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

      Method[] methods = this.t.getClass().getMethods();
      if (resultSet.next()) {
        for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
          for (Method method : methods) {
            String methodName = method.toString().toLowerCase(Locale.ROOT);
            String columnName = resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT);
            if (methodName.contains("set")
                && methodName.contains("string")
                && methodName.contains(columnName)) {
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
    String tableName = refreshVariables();

    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement get = connection.createStatement();

      String str =
          String.format(
              "SELECT * FROM ServiceRequest s, %s r WHERE (s.requestID = r.requestID) AND r.requestID = '%s'",
              tableName, sr.getRequestID());

      ResultSet resultSet = get.executeQuery(str);
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

      Method[] methods = sr.getClass().getMethods();
      Method[] super_methods = SR.class.getMethods();
      if (resultSet.next()) {
        for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
          for (Method method : methods) {
            String methodName = method.toString().toLowerCase(Locale.ROOT);
            String columnName = resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT);
            if (methodName.contains("get")
                && methodName.contains("string")
                && methodName.contains(columnName)) {
              String returnValOld = resultSet.getString(i);
              String returnValNew = (String) method.invoke(sr);

              if (!returnValOld.equals(returnValNew)) {
                System.out.printf("%s is changed\n", columnName);

                Statement update = connection.createStatement();

                boolean val_exists_in_super = false;
                for (Method super_method : super_methods) {
                  String sr_methodName = super_method.toString().toLowerCase(Locale.ROOT);

                  if (sr_methodName.contains(methodName)) {
                    str =
                        String.format(
                            "UPDATE ServiceRequest SET "
                                + columnName
                                + " = '%s' WHERE requestID = '%s'",
                            returnValNew,
                            sr.getRequestID());
                    update.execute(str);
                    val_exists_in_super = true;
                  }
                }

                if (!val_exists_in_super) {
                  str =
                      String.format(
                          "UPDATE %s SET " + columnName + " = '%s' WHERE requestID = '%s'",
                          tableName,
                          returnValNew,
                          sr.getRequestID());
                  update.execute(str);
                }
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
    //    this.t = (T) sr;
    String tableName = refreshVariables();

    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
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

      Method[] methods = sr.getClass().getMethods();
      Method[] super_methods = SR.class.getMethods();
      for (Method method : methods) {
        String methodName = method.toString().toLowerCase(Locale.ROOT);
        StringBuilder sr_methodName = new StringBuilder();
        for (Method super_method : super_methods) {
          sr_methodName.append(super_method.toString().toLowerCase(Locale.ROOT));
        }
        if (!sr_methodName.toString().contains(methodName)
            && methodName.contains("get")
            && methodName.contains("string")) {
          String str2 =
              String.format(
                  "INSERT INTO %s(requestID, equipmentID)" + " VALUES('%s', '%s')",
                  tableName, sr.getRequestID(), (String) method.invoke(sr));

          insert.execute(str2);
        }
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
    String tableName = refreshVariables();

    try {
      Connection connection =
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement delete = connection.createStatement();
      delete.execute(
          String.format("DELETE FROM ServiceRequest WHERE requestID = '%s'", sr.getRequestID()));

      delete.execute(
          String.format("DELETE FROM %s WHERE requestID = '%s'", tableName, sr.getRequestID()));
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
          DriverManager.getConnection(String.format("jdbc:derby:%s;", Adb.pathToDBA));
      Statement getNodeList = connection.createStatement();

      ArrayList<T> reqList = new ArrayList<>();
      String tableName = refreshVariables();

      String str =
          String.format(
              "SELECT * FROM ServiceRequest s, %s m WHERE s.requestID=m.requestID", tableName);

      ResultSet resultSet = getNodeList.executeQuery(str);
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

      Method[] methods = this.t.getClass().getMethods();
      while (resultSet.next()) {
        for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
          for (Method method : methods) {
            String methodName = method.toString().toLowerCase(Locale.ROOT);
            String columnName = resultSetMetaData.getColumnName(i).toLowerCase(Locale.ROOT);
            if (methodName.contains("set")
                && methodName.contains("string")
                && methodName.contains(columnName)) {
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

  // Read from CSV
  public List<T> readCSV(String csvFilePath)
      throws IOException, ParseException, InvocationTargetException, IllegalAccessException {
    String tableName = refreshVariables();

    File file = new File(csvFilePath);
    Scanner lineScanner = new Scanner(file);
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<T> list = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");

      while (dataScanner.hasNext()) {
        String data = dataScanner.next();

        Method[] methods = this.t.getClass().getMethods();
        for (Method method : methods) {
          String methodName = method.toString().toLowerCase(Locale.ROOT);
          if (methodName.contains("set") && methodName.contains("string")) {
            if (dataIndex == 0 && methodName.contains("requestid")) method.invoke(this.t, data);
            else if (dataIndex == 1 && methodName.contains("startlocation"))
              method.invoke(this.t, data);
            else if (dataIndex == 2 && methodName.contains("endlocation"))
              method.invoke(this.t, data);
            else if (dataIndex == 3 && methodName.contains("employeerequested"))
              method.invoke(this.t, data);
            else if (dataIndex == 4 && methodName.contains("employeeassigned"))
              method.invoke(this.t, data);
            else if (dataIndex == 5 && methodName.contains("requesttime"))
              method.invoke(this.t, data);
            else if (dataIndex == 6 && methodName.contains("requeststatus"))
              method.invoke(this.t, data);
            else if (dataIndex == 7 && methodName.contains("requesttype"))
              method.invoke(this.t, data);
            else if (dataIndex == 8 && methodName.contains("comments")) method.invoke(this.t, data);
            else if (dataIndex == 9 && methodName.contains("equipmentid"))
              method.invoke(this.t, data);
          }
        }
        dataIndex++;
      }
      this.enterServiceRequest((SR) this.t);
      list.add(this.t);
      refreshVariables();
      dataIndex = 0;
    }
    lineScanner.close();
    return list;
  }

  // Write CSV for table
  public static void writeMedicalEquipmentServiceRequestCSV(String csvFilePath) throws IOException {

//    // create a writer
//    File file = new File(csvFilePath);
//    file.createNewFile();
//    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));
//
//    writer.write(
//            "RequestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments, equipmentID");
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
