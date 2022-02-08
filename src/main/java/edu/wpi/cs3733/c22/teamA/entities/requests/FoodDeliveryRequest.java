package edu.wpi.cs3733.c22.teamA.entities.requests;

public class FoodDeliveryRequest {
  private String requestID;
  private String mainDish;
  private String sideDish;
  private String beverage;
  private String dessert;
  private String roomNum;
  private String employee;
  private String comments;

  public FoodDeliveryRequest(){}
  public FoodDeliveryRequest(String requestID,
      String main,
      String side,
      String drink,
      String dessert,
      String roomNum,
      String employee,
      String comments) {
    this.requestID = requestID;
    this.mainDish = main;
    this.sideDish = side;
    this.beverage = drink;
    this.dessert = dessert;
    this.roomNum = roomNum;
    this.employee = employee;
    this.comments = comments;
  }

  // Getter
  public String getRequestID(){
    return  this.requestID;
  }

  public String getMainDish(){
    return this.mainDish;
  }

  public String getSideDish(){
    return this.sideDish;
  }

  public String getBeverage(){
    return this.beverage;
  }

  public String getDessert(){
    return this.dessert;
  }

  public String getRoomNum(){
    return this.roomNum;
  }

  public String getEmployee(){
    return this.employee;
  }

  public String getComments(){
    return this.comments;
  }

  // Setter
  public void setRequestID(String requestID){
    this.requestID = requestID;
  }

  public void setMainDish(String mainDish){
    this.mainDish = mainDish;
  }

  public void setSideDish(String sideDish){
    this.sideDish = sideDish;
  }

  public void setBeverage(String beverage){
    this.beverage = beverage;
  }

  public void setDessert(String dessert){
    this.dessert = dessert;
  }

  public void setRoomNum(String roomNum){
    this.roomNum = roomNum;
  }

  public void setEmployee(String employee){
    this.employee = employee;
  }

  public void setComments(String comments){
    this.comments = comments;
  }
}
