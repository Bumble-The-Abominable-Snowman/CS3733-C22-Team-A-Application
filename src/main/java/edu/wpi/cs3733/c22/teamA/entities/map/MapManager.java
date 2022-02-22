package edu.wpi.cs3733.c22.teamA.entities.map;

// Facade
public class MapManager {

  private MarkerManager markerManager;
  private CheckBoxManager checkBoxManager;
  private GesturePaneManager gesturePaneManager;
  private SelectionManager selectionManager;

  public MapManager(
      MarkerManager markerManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager,
      SelectionManager selectionManager) {
    this.markerManager = markerManager;
    this.checkBoxManager = checkBoxManager;
    this.gesturePaneManager = gesturePaneManager;
    this.selectionManager = selectionManager;
  }

  public void init() {
    gesturePaneManager.initGesture();
    initFloor(
        "Choose Floor:",
        (int) gesturePaneManager.getMapImageView().getLayoutX(),
        (int) gesturePaneManager.getMapImageView().getLayoutY());
  }

  public void initFloor(String floor, int mapLayoutX, int mapLayoutY) {
    gesturePaneManager.setMapFloor(floor);
    // gesturePaneManager.initGesture();
    markerManager.initFloor(
        gesturePaneManager.getCurrentFloor(),
        mapLayoutX,
        mapLayoutY,
        selectionManager,
        checkBoxManager,
        gesturePaneManager);
    checkBoxManager.switchFloor(markerManager, gesturePaneManager.getCurrentFloor().equals(""));
    selectionManager.clearVBox();
  }

  public void reset() {
    gesturePaneManager.reset();
  }
}
