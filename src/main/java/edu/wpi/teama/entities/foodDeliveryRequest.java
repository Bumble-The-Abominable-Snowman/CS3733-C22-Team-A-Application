package edu.wpi.teama.entities;

public class foodDeliveryRequest {
  String mainDish;
  String sideDish;
  String beverage;
  String dessert;
  String roomNum;
  String employee;
  String comments;

  public foodDeliveryRequest(
      String main,
      String side,
      String drink,
      String dessert,
      String roomNum,
      String employee,
      String comments) {
    this.mainDish = main;
    this.sideDish = side;
    this.beverage = drink;
    this.dessert = dessert;
    this.roomNum = roomNum;
    this.employee = employee;
    this.comments = comments;
  }
}
