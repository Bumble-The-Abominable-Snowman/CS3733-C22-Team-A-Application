package edu.wpi.c22.teamA.entities.requests;

public class FoodDeliveryRequest {
  String mainDish;
  String sideDish;
  String beverage;
  String dessert;
  String roomNum;
  String employee;
  String comments;

  public FoodDeliveryRequest(
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
