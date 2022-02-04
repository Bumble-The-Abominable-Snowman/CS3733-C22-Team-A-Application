package edu.wpi.teama.entities;

import java.util.Date;

public class Employee {
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

  public String getEmployeeID() {
    return employeeID;
  }

  public String getEmployeeType() {
    return employeeType;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNum() {
    return phoneNum;
  }

  public String getAddress() {
    return address;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setEmployeeID(String employeeID) {
    this.employeeID = employeeID;
  }

  public void setEmployeeType(String employeeType) {
    this.employeeType = employeeType;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
}
