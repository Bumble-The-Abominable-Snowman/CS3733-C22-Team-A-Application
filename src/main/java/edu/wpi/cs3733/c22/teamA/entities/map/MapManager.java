package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;

import com.jfoenix.controls.JFXButton;

import java.util.List;

// Facade
public class MapManager {

  private MarkerManager markerManager;
  private CheckBoxManager checkBoxManager;
  private GesturePaneManager gesturePaneManager;
  private SelectionManager selectionManager;
  private Searcher searcher;
  private SideView sideView;
  private List<JFXButton> buttons;

  public MapManager(
      MarkerManager markerManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager,
      SelectionManager selectionManager,
      Searcher searcher,
      SideView sideView,
      List<JFXButton> buttons) {
    this.markerManager = markerManager;
    this.checkBoxManager = checkBoxManager;
    this.gesturePaneManager = gesturePaneManager;
    this.selectionManager = selectionManager;
    this.searcher = searcher;
    this.sideView = sideView;
    this.buttons = buttons;
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
    if (floor.equals("Choose Floor:")) {
      sideView.clearLabel();
      sideView.toggleVisibility(true);
      for (JFXButton btn : buttons)
        btn.setDisable(true);
    } else {
      sideView.toggleVisibility(false);
      for (JFXButton btn : buttons)
        btn.setDisable(false);
    }
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
    checkBoxManager.switchFloor(markerManager, gesturePaneManager.getCurrentFloor().equals(""));
    selectionManager.clearVBox();
  }

  public void newLocationPressed() {
    markerManager.newLocationPressed(selectionManager, checkBoxManager, gesturePaneManager);
  }

  public void reset() {
    gesturePaneManager.reset();
  }
}
