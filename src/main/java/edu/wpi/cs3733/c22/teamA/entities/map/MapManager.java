package edu.wpi.cs3733.c22.teamA.entities.map;

// Facade
public class MapManager {

  private MarkerManager markerManager;
  private CheckBoxManager checkBoxManager;
  private GesturePaneManager gesturePaneManager;

  public MapManager(
      MarkerManager markerManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager) {
    this.markerManager = markerManager;
    this.checkBoxManager = checkBoxManager;
    this.gesturePaneManager = gesturePaneManager;
  }

  public void init() {
    gesturePaneManager.initGesture();
  }

  public void initFloor(String floor, int mapLayoutX, int mapLayoutY) {
    gesturePaneManager.setMapFloor(floor);
    // gesturePaneManager.initGesture();
    markerManager.initFloor(gesturePaneManager.getCurrentFloor(), mapLayoutX, mapLayoutY);
    checkBoxManager.switchFloor(markerManager);
  }

  public void reset() {
    gesturePaneManager.reset();
  }
}
