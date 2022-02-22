package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SR {
  protected String requestID;
  protected String startLocation;
  protected String endLocation;
  protected String employeeRequested;
  protected String employeeAssigned;

  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  protected Timestamp requestTime;

  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  protected Status requestStatus;

  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  protected Priority requestPriority;

  protected String comments;

  public SRType srType;

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
    this.requestStatus = Status.BLANK;
    this.srType = SRType.INVALID;
  }

  // generic sr constructor
  public SR(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      Status requestStatus,
      Priority requestPriority,
      String comments) {
    this.requestID = requestID;
    this.startLocation = startLocation;
    this.endLocation = endLocation;
    this.employeeRequested = employeeRequested;
    this.employeeAssigned = employeeAssigned;
    this.requestTime = requestTime;
    this.requestStatus = requestStatus;
    this.requestPriority = requestPriority;
    this.comments = comments;

    this.srType = SRType.INVALID;
  }

  public List<String> getListForm() {
    return List.of(
        requestID,
        startLocation,
        endLocation,
        employeeRequested,
        employeeAssigned,
        requestTime.toString(),
        requestStatus.toString(),
        requestPriority.toString(),
        comments);
  }

  // status get/set
  public String getRequestStatus() {
    return this.requestStatus.toString();
  }

  public void setRequestStatus(String s) throws IllegalAccessException {
    // convert "BLANK" to Status.BLANK enum
    Field[] fields = Status.class.getFields();
    for (Field field : fields) {
      if (field.toString().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
        this.requestStatus = (Status) field.get(this.requestStatus);
      }
    }
  }

  // priority get/set
  public String getRequestPriority() {
    return this.requestPriority.toString();
  }

  public void setRequestPriority(String s) throws IllegalAccessException {
    // convert "REGULAR" to Priority.REGULAR enum
    Field[] fields = Priority.class.getFields();
    for (Field field : fields) {
      if (field.toString().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
        this.requestPriority = (Priority) field.get(this.requestPriority);
      }
    }
  }

  // timestamp get/set
  public String getRequestTime() {
    if (this.requestTime == null) {
      return "";
    }
    return this.requestTime.toString();
  }

  public void setRequestTime(String s) {
    this.requestTime = Timestamp.valueOf(s);
  }
}
