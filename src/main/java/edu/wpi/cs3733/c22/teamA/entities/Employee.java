package edu.wpi.cs3733.c22.teamA.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Employee {

  // TODO implement getting column names and add as static List to all
  // SEE DOCUMENT FOR SPECIFICS
  public static List<String> columnNames = null;

  private String employeeID;
  private String employeeType;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNum;
  private String address;
  private Date startDate;

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
    this.employeeID = employeeID;
    this.employeeType = employeeType;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNum = phoneNum;
    this.address = address;
    this.startDate = startDate;
  }

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }

  // timestamp get/set
  public String getStartDate() {
    if (this.startDate == null) {
      return "";
    }
    return this.startDate.toString();
  }

  public void setStartDate(String s) {
    this.startDate = Timestamp.valueOf(s);
  }
}
