package edu.wpi.cs3733.c22.teamA.entities;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import lombok.Data;

@Data
public class Employee {

  // TODO implement getting column names and add as static List to all
  // SEE DOCUMENT FOR SPECIFICS
  protected HashMap<String, Object> fields = new HashMap<>();
  protected HashMap<String, String> fields_string = new HashMap<>();

  public Employee() {}

  public Employee(
      String employeeID,
      String employeeType,
      String firstName,
      String lastName,
      String email,
      String phoneNum,
      String address,
      Date startDate) {
    this.fields.put("employee_id", employeeID);
    this.fields.put("employee_type", employeeType);
    this.fields.put("first_name", firstName);
    this.fields.put("last_name", lastName);
    this.fields.put("email", email);
    this.fields.put("phone_num", phoneNum);
    this.fields.put("address", address);
    this.fields.put("start_date", startDate);
  }

  public HashMap<String, String> getStringFields() {
    for (String key : this.fields.keySet()) {
      this.fields_string.put(key, String.valueOf(this.fields.get(key)));
    }
    return this.fields_string;
  }

  public void setField(String key, Object value) {
    this.fields.put(key, value);
  }

  public void setFieldByString(String key, String value) throws IllegalAccessException {
    if (Objects.equals(key, "start_date")) {
      this.fields.put(key, Date.parse(value));
    } else {
      this.fields.put(key, value);
    }
  }

  public String getFullName(){
    StringBuilder str = new StringBuilder();
    str.append(this.getStringFields().get("last_name")).append(" ").append(this.getStringFields().get("first_name"));
    return str.toString();
  }
}
