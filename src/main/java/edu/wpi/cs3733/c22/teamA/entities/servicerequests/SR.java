package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SR extends RecursiveTreeObject<SR> {
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
}
