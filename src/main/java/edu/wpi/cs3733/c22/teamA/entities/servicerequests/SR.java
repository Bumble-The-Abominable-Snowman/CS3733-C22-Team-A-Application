package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Locale;
import lombok.Data;

@Data
public class SR {
  protected String requestID;
  protected String startLocation;
  protected String endLocation;
  protected String employeeRequested;
  protected String employeeAssigned;
  protected Timestamp requestTime;
  protected Status requestStatus; // Turn this into an Enum?
  protected String requestType;
  protected String comments;

  public SRType srType;

  public enum SRType {
    EQUIPMENT,
    FLORAL_DELIVERY,
    FOOD_DELIVERY,
    GIFT,
    LANGUAGE,
    LAUNDRY,
    MAINTENANCE,
    MEDICINE_DELIVERY,
    RELIGIOUS,
    SANITATION,
    SECURITY,
  }

  public enum Status {
    BLANK,
    WAITING,
    CANCELED,
    DONE
  }

  public void setRequestStatus(String s) throws IllegalAccessException {

    // Hack job to convert "BLANK" to Status.BLANK enum
    Field[] fields = Status.class.getFields();
    for (Field field : fields) {
      if (field.toString().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
        this.requestStatus = (Status) field.get(this.requestStatus);
      }
    }
  }

  public void setRequestTime(String s) {
    this.requestTime = Timestamp.valueOf(s);
  }

  public String getRequestTime() {
    if (this.requestTime == null) {
      return "";
    }
    return this.requestTime.toString();
  }
}
