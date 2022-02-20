package edu.wpi.cs3733.c22.teamA.entities.map;

public class MarkerManager {
  /*
  private Map<Button, LocationMarker> buttonLocationMarker;
  private Map<Button, EquipmentMarker> buttonEquipmentMarker;
  private Map<Button, SRMarker> buttonServiceRequestMarker;
  private AnchorPane miniAnchorPane;

  public MarkerManager() {
    buttonLocationMarker = new HashMap<>();
    buttonEquipmentMarker = new HashMap<>();
    buttonServiceRequestMarker = new HashMap<>();
    miniAnchorPane = new AnchorPane();
  }

  // Clears all Buttons and Labels from screen
  public void newLocationPressed() {
    if (floor != "") newLocationPressed(0, 0);
  }

  public void newLocationPressedMouse(double x, double y) {
    newLocationPressed(
        (int) ((int) x - gesturePane.getLayoutX()), (int) ((int) y - gesturePane.getLayoutY()));
  }

  double mouseX = 0;
  double mouseY = 0;

  public void newLocationPressed(int xCoord, int yCoord) {
    if (newLocationMarker != null) {
      System.out.println("SAVE THIS ONE FIRST");
      return;
    }
    Location newLocation =
        new Location(
            "NEWLOCATION",
            xCoord,
            yCoord,
            floor,
            "Tower",
            "NODE TYPE TODO",
            "New Location",
            "New Location");
    LocationMarker newLocationMarker = newDraggableLocation(newLocation);
    this.newLocationMarker = newLocationMarker;
    newLocationMarker.draw(miniAnchorPane);
  }

  public void setupContextMenu() {
    ContextMenu rightClickMenu = new ContextMenu();
    MenuItem newLocation = new MenuItem("New Location");
    newLocation.setOnAction(
        (event) -> {
          newLocationPressedMouse(mouseX, mouseY);
        });
    rightClickMenu.getItems().addAll(newLocation);
    mapImageView.setOnContextMenuRequested(
        (event) -> {
          if (floor != "") {
            rightClickMenu.show(mapImageView, event.getScreenX(), event.getScreenY());
            mouseX = event.getScreenX();
            mouseY = event.getScreenY();
          }
        });
  }

  public void clearAll() {
    for (LocationMarker locationMarker : buttonLocationMarker.values()) {
      locationMarker.clear(miniAnchorPane);
    }
    for (EquipmentMarker equipmentMarker : buttonEquipmentMarker.values()) {
      equipmentMarker.clear(miniAnchorPane);
    }
    for (SRMarker serviceRequestMarker : buttonServiceRequestMarker.values()) {
      serviceRequestMarker.clear(miniAnchorPane);
    }
  }
  */

}
