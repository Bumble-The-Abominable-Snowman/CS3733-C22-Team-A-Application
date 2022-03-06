package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class ServiceRequestRESTImpl implements ServiceRequestDAO {

  private final String url = "https://cs3733c22teama.ddns.net/api/service_request";

  String db_name = "";
  SR.SRType srType;

  public ServiceRequestRESTImpl(SR.SRType srType)
  {
    this.srType = srType;
    switch (this.srType)
    {
      case EQUIPMENT:
        this.db_name = "db-sr-equipment-delivery";
        break;
      case FLORAL_DELIVERY:
        this.db_name = "db-sr-floral-delivery";
        break;
      case FOOD_DELIVERY:
        this.db_name = "db-sr-food-delivery";
        break;
      case GIFT_DELIVERY:
        this.db_name = "db-sr-gift-delivery";
        break;
      case LANGUAGE:
        this.db_name = "db-sr-language";
        break;
      case LAUNDRY:
        this.db_name = "db-sr-laundry";
        break;
      case MAINTENANCE:
        this.db_name = "db-sr-maintenance";
        break;
      case MEDICINE_DELIVERY:
        this.db_name = "db-sr-medicine-delivery";
        break;
      case RELIGIOUS:
        this.db_name = "db-sr-religious";
        break;
      case SANITATION:
        this.db_name = "db-sr-sanitation";
        break;
      case SECURITY:
        this.db_name = "db-sr-security";
        break;
      case CONSULTATION:
        this.db_name = "db-sr-consultation";
        break;
      case INVALID:
        this.db_name = "db-sr-INVALID";
        break;
    }
  }

  @Override
  public SR getRequest(String ID)
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "get");
    metadata.put("table-name", this.db_name);
    metadata.put("request_id", ID);
    HashMap<String, String> resp = Adb.getREST(url, metadata);
    SR sr = new SR(this.srType);
    for (String key: resp.keySet()) {
      sr.setFieldByString(key, resp.get(key));
    }
    return sr;
  }

  @Override
  public void updateServiceRequest(SR sr)
          throws IOException {

    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "update");
    metadata.put("table-name", this.db_name);
    Adb.postREST(url, metadata, sr.getStringFields());


  }

  @Override
  public void enterServiceRequest(SR sr)
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
    try
    {
      HashMap<String, String> metadata = new HashMap<>();
      metadata.put("operation", "add");
      metadata.put("table-name", this.db_name);
      Adb.postREST(url, metadata, sr.getStringFields());

    } catch (SocketTimeoutException e)
    {
      System.out.println("failed! trying again...");
    }
  }

  @Override
  public void deleteServiceRequest(SR sr) throws SQLException, IOException {
    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "delete");
    metadata.put("request_id", sr.getStringFields().get("request_id"));
    metadata.put("table-name", this.db_name);
    Adb.postREST(url, metadata, sr.getStringFields());

  }

  @Override
  public List<SR> getServiceRequestList()
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException {

    ArrayList<SR> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, HashMap<String, String>>> mapArrayList = Adb.getAllRESTSR(url);
    for (HashMap<String, HashMap<String, String>> map: mapArrayList) {
      SR sr = new SR(this.srType);
      for (String sr_struct_field: map.keySet()) {
        if (sr_struct_field.equals("start-loc"))
        {
          Location location = new Location();
          for (String sub_field :map.get(sr_struct_field).keySet()) {
            location.setFieldByString(sub_field, map.get(sr_struct_field).get(sub_field));
          }
          sr.setField("start_location", location);
        }
        if (sr_struct_field.equals("end-loc"))
        {
          Location location = new Location();
          for (String sub_field :map.get(sr_struct_field).keySet()) {
            location.setFieldByString(sub_field, map.get(sr_struct_field).get(sub_field));
          }
          sr.setField("end_location", location);
        }
        if (sr_struct_field.equals("emp-req"))
        {
          Employee employee = new Employee();
          for (String sub_field :map.get(sr_struct_field).keySet()) {
            employee.setFieldByString(sub_field, map.get(sr_struct_field).get(sub_field));
          }
          sr.setField("employee_requested", employee);
        }
        if (sr_struct_field.equals("emp-ass"))
        {
          Employee employee = new Employee();
          for (String sub_field :map.get(sr_struct_field).keySet()) {
            employee.setFieldByString(sub_field, map.get(sr_struct_field).get(sub_field));
          }
          sr.setField("employee_assigned", employee);
        }
        if (sr_struct_field.equals("sr"))
        {
          for (String sub_field :map.get(sr_struct_field).keySet()) {
            if (!(sub_field.equals("start_location")
                    || sub_field.equals("end_location")
                    || sub_field.equals("employee_requested")
                    || sub_field.equals("employee_assigned")))
            {
              sr.setFieldByString(sub_field, map.get(sr_struct_field).get(sub_field));
            }
          }
        }
      }

      arrayList.add(sr);
    }

    return arrayList;
  }


  public static List<SR> getAllServiceRequestList()
          throws SQLException, IllegalAccessException, InvocationTargetException, IOException, ParseException {
    ServiceRequestRESTImpl serviceRequestDerby = new ServiceRequestRESTImpl(SR.SRType.INVALID);
    return serviceRequestDerby.getServiceRequestList();
  }

  // Read from CSV
  public void populateFromCSV(String csvFilePath)
          throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException {

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

    ArrayList<HashMap<String, String>> sr_list = new ArrayList<>();
    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");

      HashMap<String, String> sr_h = new HashMap<>();
      while (dataScanner.hasNext()) {
        String data = dataScanner.next().strip();

        String columnName = field_list.get(dataIndex);
        sr_h.put(columnName, data);
        dataIndex++;
      }
      sr_list.add(sr_h);

      dataIndex = 0;
    }
    lineScanner.close();

    HashMap<String, String> metadata_map = new HashMap<>();
    metadata_map.put("operation", "populate");
    metadata_map.put("table-name", this.db_name);

    Adb.populate_db(url, metadata_map, sr_list);
  }

  public void populateFromCSVfile(String csvFilePath)
          throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException {

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

    ArrayList<HashMap<String, String>> sr_list = new ArrayList<>();
    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");

      HashMap<String, String> sr_h = new HashMap<>();
      while (dataScanner.hasNext()) {
        String data = dataScanner.next().strip();

        String columnName = field_list.get(dataIndex);
        sr_h.put(columnName, data);
        dataIndex++;
      }
      sr_h.put("sr_type", String.valueOf(this.srType));
      sr_list.add(sr_h);

      dataIndex = 0;
    }
    lineScanner.close();

    HashMap<String, String> metadata_map = new HashMap<>();
    metadata_map.put("operation", "populate");
    metadata_map.put("table-name", this.db_name);

    Adb.populate_db(url, metadata_map, sr_list);
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
