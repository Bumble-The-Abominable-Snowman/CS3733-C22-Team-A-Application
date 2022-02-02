package edu.wpi.teama.controllers;

public class foodDeliveryRequest {
  String mainDish;
  String sideDish;
  String beverage;
  String dessert;
  Integer roomNum;
  String comments;

  public foodDeliveryRequest(
      String main, String side, String drink, String dessert, Integer roomNum, String comments) {
    this.mainDish = main;
    this.sideDish = side;
    this.beverage = drink;
    this.dessert = dessert;
    this.roomNum = roomNum;
    this.comments = comments;
  }
}
