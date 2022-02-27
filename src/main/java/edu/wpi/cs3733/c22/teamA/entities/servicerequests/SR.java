package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import lombok.Data;

@Data
public class SR {

  protected HashMap<String, Object> fields = new HashMap<>();
  protected HashMap<String, String> fields_string = new HashMap<>();

  public enum SRType {
    EQUIPMENT,
    FLORAL_DELIVERY,
    FOOD_DELIVERY,
    GIFT_DELIVERY,
    LANGUAGE,
    LAUNDRY,
    MAINTENANCE,
    MEDICINE_DELIVERY,
    RELIGIOUS,
    SANITATION,
    SECURITY,
    CONSULTATION,
    INVALID,
  }

  public enum Status {
    BLANK,
    WAITING,
    CANCELED,
    DONE;
  }

  public enum Priority {
    REGULAR,
    MID_HIGH,
    HIGH,
    EMERGENCY
  }

  // default constructor
  public SR() {
    this(SRType.INVALID);
  }

  public SR(SRType srType) {
    this(
        "N/A",
        new Location(),
        new Location(),
        new Employee(),
        new Employee(),
        new Timestamp((new Date()).getTime()),
        Status.BLANK,
        Priority.REGULAR,
        "N/A",
        srType);
  }

  // generic sr constructor
  public SR(
      String requestID,
      Location startLocation,
      Location endLocation,
      Employee employeeRequested,
      Employee employeeAssigned,
      Timestamp requestTime,
      Status requestStatus,
      Priority requestPriority,
      String comments,
      SRType srType) {

    this.fields.put("request_id", requestID);
    this.fields.put("start_location", startLocation);
    this.fields.put("end_location", endLocation);
    this.fields.put("employee_requested", employeeRequested);
    this.fields.put("employee_assigned", employeeAssigned);
    this.fields.put("request_time", requestTime);
    this.fields.put("request_status", requestStatus);
    this.fields.put("request_priority", requestPriority);
    this.fields.put("comments", comments);

    this.fields.put("sr_type", srType);
  }

  public HashMap<String, String> getStringFields() {
    for (String key : this.fields.keySet()) {
      if (Objects.equals(key, "start_location") || Objects.equals(key, "end_location")) {
        this.fields_string.put(key, ((Location) this.fields.get(key)).getNodeID());
      } else if (Objects.equals(key, "employee_requested")
          || Objects.equals(key, "employee_assigned")) {
        this.fields_string.put(key, ((Employee) this.fields.get(key)).getStringFields().get("employee_id"));
      } else {
        this.fields_string.put(key, String.valueOf(this.fields.get(key)));
      }
    }
    return this.fields_string;
  }

  public void setField(String key, Object value) {
    this.fields.put(key, value);
  }

  public void setFieldByString(String key, String value) throws IllegalAccessException {
    if (Objects.equals(key, "start_location") || Objects.equals(key, "end_location")) {
      LocationDerbyImpl locationDerby = new LocationDerbyImpl();
      this.fields.put(key, locationDerby.getLocationNode(value));
    } else if (Objects.equals(key, "employee_requested")
            || Objects.equals(key, "employee_assigned")) {
      EmployeeDerbyImpl employeeDerby = new EmployeeDerbyImpl();
      this.fields.put(key, employeeDerby.getEmployee(value));
    } else if (Objects.equals(key, "request_time")) {
      this.fields.put(key, Timestamp.valueOf(value));
    } else if (Objects.equals(key, "request_status")) {
      for (Field field : Status.class.getFields()) {
        if (field.toString().toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT))) {
          this.fields.put("request_status", (Status) field.get(this.fields.get("request_status")));
        }
      }
    } else if (Objects.equals(key, "request_priority")) {
      for (Field field : Priority.class.getFields()) {
        if (field.toString().toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT))) {
          this.fields.put("request_priority", (Priority) field.get(this.fields.get("request_priority")));
        }
      }
    } else {
      this.fields.put(key, value);
    }
  }
}
