package edu.wpi.teama.Adb.MedicalEquipment;

public class MedicalEquipment {
  private String equipmentID;
  private String equipmentType;
  private boolean isClean;
  private String currentLocation;
  private boolean isAvailable;

  public MedicalEquipment() {}

  public MedicalEquipment(
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
