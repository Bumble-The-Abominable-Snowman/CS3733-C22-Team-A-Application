package edu.wpi.cs3733.c22.teamA.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.List;

public class Equipment extends RecursiveTreeObject<Equipment> {
  private String equipmentID;
  private String equipmentType;
  private boolean isClean;
  private String currentLocation;
  private boolean isAvailable;

  public Equipment() {}

  public Equipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable) {
    this.equipmentID = equipmentID;
    this.equipmentType = equipmentType;
    this.isClean = isClean;
    this.currentLocation = currentLocation;
    this.isAvailable = isAvailable;
  }

  public List<String> getListForm() {
    return List.of(
        equipmentID,
        equipmentType,
        Boolean.toString(isClean),
        currentLocation,
        Boolean.toString(isAvailable));
  }

  public String getEquipmentID() {
    return equipmentID;
  }

  public String getEquipmentType() {
    return equipmentType;
  }

  public boolean getIsClean() {
    return isClean;
  }

  public String getCurrentLocation() {
    return currentLocation;
  }

  public boolean getIsAvailable() {
    return isAvailable;
  }

  public void setEquipmentID(String id) {
    equipmentID = id;
  }

  public void setEquipmentType(String type) {
    equipmentType = type;
  }

  public void setIsClean(boolean b) {
    isClean = b;
  }

  public void setCurrentLocation(String c) {
    currentLocation = c;
  }

  public void setIsAvailable(boolean b) {
    isAvailable = b;
  }
}
