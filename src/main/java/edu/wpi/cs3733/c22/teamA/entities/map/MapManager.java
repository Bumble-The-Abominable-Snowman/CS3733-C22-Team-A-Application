package edu.wpi.cs3733.c22.teamA.entities.map;

// Facade
public class MapManager {

  private MarkerManager markerManager;
  private CheckBoxManager checkBoxManager;
  private GesturePaneManager gesturePaneManager;
  private SelectionManager selectionManager;
  private Searcher searcher;

  public MapManager(
      MarkerManager markerManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager,
      SelectionManager selectionManager,
      Searcher searcher) {
    this.markerManager = markerManager;
    this.checkBoxManager = checkBoxManager;
    this.gesturePaneManager = gesturePaneManager;
    this.selectionManager = selectionManager;
    this.searcher = searcher;
  }

  public void init() {
    gesturePaneManager.initGesture();
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
    searcher.setAll(
        markerManager.getLocationMarkers(),
        markerManager.getEquipmentMarkers(),
        markerManager.getServiceRequestMarkers(),
        floor);
    searcher.initSearchListener();
    checkBoxManager.switchFloor(markerManager);
  }

  public void reset() {
    gesturePaneManager.reset();
  }
}
