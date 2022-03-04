package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ServiceRequestRESTImpl implements ServiceRequestDAO {

  private final String url = "https://cs3733c22teama.ddns.net/api/service_request";

  String db_name_part = "";
  SR.SRType srType;

  public ServiceRequestRESTImpl(SR.SRType srType)
  {
    this.srType = srType;
    switch (this.srType)
    {
      case EQUIPMENT:
        this.db_name_part = "equipment-delivery";
        break;
      case FLORAL_DELIVERY:
        this.db_name_part = "floral-delivery";
        break;
      case FOOD_DELIVERY:
        this.db_name_part = "food-delivery";
        break;
      case GIFT_DELIVERY:
        this.db_name_part = "gift-delivery";
        break;
      case LANGUAGE:
        this.db_name_part = "language";
        break;
      case LAUNDRY:
        this.db_name_part = "laundry";
        break;
      case MAINTENANCE:
        this.db_name_part = "maintenance";
        break;
      case MEDICINE_DELIVERY:
        this.db_name_part = "medicine-delivery";
        break;
      case RELIGIOUS:
        this.db_name_part = "religious";
        break;
      case SANITATION:
        this.db_name_part = "sanitation";
        break;
      case SECURITY:
        this.db_name_part = "security";
        break;
      case CONSULTATION:
        this.db_name_part = "consultation";
        break;
      case INVALID:
        this.db_name_part = "INVALID";
        break;
    }
  }

  @Override
  public SR getRequest(String ID)
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
    HashMap<String, String> map = new HashMap<>();
    map.put("operation", "get");
    map.put("request_id", ID);
    HashMap<String, String> resp = Adb.getREST(url, map);
    SR sr = new SR();
    for (String key: resp.keySet()) {
      sr.setFieldByString(key, resp.get(key));
    }
    return sr;
  }

  @Override
  public void updateServiceRequest(SR sr)
          throws IOException {

    HashMap<String, String> map = sr.getStringFields();
    map.put("operation", "update");
    Adb.postREST(url, map);
  }

  @Override
  public void enterServiceRequest(SR sr)
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
    HashMap<String, String> map = sr.getStringFields();
    map.put("operation", "add");
    try
    {
      Adb.postREST(url, map);
    } catch (SocketTimeoutException e)
    {
      System.out.println("failed! trying again...");
    }
  }

  @Override
  public void deleteServiceRequest(SR sr) throws SQLException, IOException {
    HashMap<String, String> map = new HashMap<>();
    map.put("request_id", sr.getStringFields().get("request_id"));
    map.put("operation", "delete");
    Adb.postREST(url, map);
  }

  @Override
  public List<SR> getServiceRequestList()
          throws SQLException, InvocationTargetException, IllegalAccessException, IOException {

    ArrayList<SR> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> mapArrayList = Adb.getAllREST(url);
    for (HashMap<String, String> map: mapArrayList) {
      SR sr = new SR();
      for (String key: map.keySet()) {
        sr.setFieldByString(key, map.get(key));
      }
      arrayList.add(sr);
    }

    return arrayList;
  }

  public static List<SR> getAllServiceRequestList()
          throws SQLException, IllegalAccessException, InvocationTargetException, IOException {
    ArrayList<SR> allReqList = new ArrayList<>();

    for (SR.SRType srType: SR.SRType.values()) {
      if (srType != SR.SRType.INVALID)
      {
        ServiceRequestRESTImpl serviceRequestDerby = new ServiceRequestRESTImpl(srType);
        System.out.println(srType.toString());
        allReqList.addAll(serviceRequestDerby.getServiceRequestList());
      }
    }
    return allReqList;
  }
}
