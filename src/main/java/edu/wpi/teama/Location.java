package edu.wpi.teama;

import java.util.Scanner;

public class Location {

  String nodeID;
  int xCoord;
  int yCoord;
  String floor;
  String building;
  String nodeType;
  String longName;
  String shortName;

  public Location() {}

  public Location(
      String nodeID,
      int xCoord,
      int yCoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {

    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setXCoord(int xCoord) {
    this.xCoord = xCoord;
  }

  public void setYCoord(int yCoord) {
    this.yCoord = yCoord;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public String getNodeID() {
    return nodeID;
  }

  public int getXCoord() {
    return xCoord;
  }

  public int getYCoord() {
    return yCoord;
  }

  public String getFloor() {
    return floor;
  }

  public String getBuilding() {
    return building;
  }

  public String getNodeType() {
    return nodeType;
  }

  public String getLongName() {
    return longName;
  }

  public String getShortName() {
    return shortName;
  }

/*  @Override
  public String toString() { // Used to format for printing to console, not useful otherwise.
    return getNodeID()
        + getXCoord()
        + getYCoord()
        + getFloor()
        + getBuilding()
        + getNodeType()
        + getLongName()
        + getShortName();
  }*/

  //Method to get node from the location table.
  public void getLocationNode(String ID){

    Adb.getNode(ID);
    System.out.println("Action complete");
    //Don't know the format of the output.
    //Should add up output to connect to app.

  }

  //Method to update nodes from location table.
  public void updateLocationCoords(String ID, int xcoord, int ycoord){

    Adb.updateCoordinates(ID, xcoord, ycoord);
    System.out.println("Action complete");

  }

  //Method to add node to location table.
  public void enterLocationNode(String ID, int xcoord, int ycoord, String floor, String building, String nodeType, String longName, String shortName){

    Adb.enterNode(
            ID, xcoord, ycoord, floor, building, nodeType, longName, shortName);
    System.out.println("Action complete");

  }

  //Method to delete nodes from location table.
  public void deleteLocationNode(String ID){

    Adb.deleteNode(ID);
    System.out.println("Action complete");

  }
}
