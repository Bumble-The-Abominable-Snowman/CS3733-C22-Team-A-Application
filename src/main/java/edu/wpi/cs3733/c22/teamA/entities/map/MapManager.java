package edu.wpi.cs3733.c22.teamA.entities.map;

// Facade
public class MapManager {

  private MarkerManager markerManager;
  private CheckBoxManager checkBoxManager;

  public MapManager(MarkerManager markerManager, CheckBoxManager checkBoxManager) {
    this.markerManager = markerManager;
    this.checkBoxManager = checkBoxManager;
  }

  public void initFloor(String floor, int mapLayoutX, int mapLayoutY) {
    markerManager.initFloor(floor, mapLayoutX, mapLayoutY);
    checkBoxManager.switchFloor(markerManager);
  }
}
