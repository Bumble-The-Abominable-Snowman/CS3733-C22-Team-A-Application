package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.util.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MarkerManager {
  private List<Location> allLocations;
  private List<Equipment> allEquipments;
  private List<SR> allSRs;

  private List<Location> floorLocations;
  private List<Equipment> floorEquipment;
  private List<SR> floorSRs;

  private Set<String> currentFloorIDs;
  private Map<String, LocationMarker> idToLocationMarker;

  private List<LocationMarker> locationMarkers;
  private List<EquipmentMarker> equipmentMarkers;
  private List<SRMarker> serviceRequestMarkers;
  private int mapLayoutX;
  private int mapLayoutY;
  private AnchorPane anchorPane;

  public MarkerManager(LocationDAO locationDAO, EquipmentDAO equipmentDAO, AnchorPane anchorPane) {
    floorLocations = new ArrayList<>();
    currentFloorIDs = new HashSet<>();
    idToLocationMarker = new HashMap<>();
    allSRs = new ArrayList<>();
    locationMarkers = new ArrayList<>();
    equipmentMarkers = new ArrayList<>();
    serviceRequestMarkers = new ArrayList<>();
    floorEquipment = new ArrayList<>();
    floorSRs = new ArrayList<>();

    allLocations = locationDAO.getNodeList();
    allEquipments = equipmentDAO.getMedicalEquipmentList();
    try {
      List<?> requestList = ServiceRequestDerbyImpl.getAllServiceRequestList();
      for (Object sr : requestList) {
        allSRs.add((SR) sr);
      }
    } catch (Exception e) {
      System.out.println(e);
    }

    this.mapLayoutX = 0;
    this.mapLayoutY = 0;
    this.anchorPane = anchorPane;
  }

  public void initFloor(String floor, int mapLayoutX, int mapLayoutY) {
    clear();
    getFloorInfo(floor);
    createFloorEntities();
    initialDraw();
    this.mapLayoutX = mapLayoutX;
    this.mapLayoutY = mapLayoutY;
  }

  private void getFloorInfo(String floor) {
    getFloorLocations(floor);
    getEquipmentLocations();
    getSRLocations();
  }

  private void getFloorLocations(String floor) {
    for (Location l : allLocations) {
      if (floor.equals(l.getFloor())) {
        floorLocations.add(l);
        currentFloorIDs.add(l.getNodeID());
      }
    }
  }

  private void getEquipmentLocations() {
    for (Equipment e : allEquipments) {
      if (currentFloorIDs.contains(e.getCurrentLocation())) {
        floorEquipment.add(e);
      }
    }
  }

  private void getSRLocations() {
    for (SR sr : allSRs) {
      if (currentFloorIDs.contains(sr.getEndLocation())) {
        floorSRs.add(sr);
      }
    }
  }

  private void createFloorEntities() {
    createFloorLocations();
    createFloorEquipments();
    createFloorSRs();
  }

  private void createFloorLocations() {
    for (Location l : floorLocations) {
      LocationMarker newLocationMarker = MarkerMaker.makeLocationMarker(l, mapLayoutX, mapLayoutY);
      locationMarkers.add(newLocationMarker);
      idToLocationMarker.put(l.getNodeID(), newLocationMarker);
    }
  }

  private void createFloorEquipments() {
    for (Equipment e : floorEquipment) {
      EquipmentMarker newEquipmentMarker =
          MarkerMaker.makeEquipmentMarker(
              e, idToLocationMarker.get(e.getCurrentLocation()), mapLayoutX, mapLayoutY);
      equipmentMarkers.add(newEquipmentMarker);
    }
  }

  private void createFloorSRs() {
    for (SR sr : floorSRs) {
      SRMarker newSRMarker =
          MarkerMaker.makeSRMarker(
              sr, idToLocationMarker.get(sr.getEndLocation()), mapLayoutX, mapLayoutY);
      serviceRequestMarkers.add(newSRMarker);
    }
  }

  private void initialDraw() {
    for (LocationMarker l : locationMarkers) {
      l.draw(anchorPane);
      if (l.getEquipmentMarker() != null) {
        l.getEquipmentMarker().draw(anchorPane);
      }
      if (l.getServiceRequestMarker() != null) {
        l.getServiceRequestMarker().draw(anchorPane);
      }
    }
  }

  private void clear() {
    for (LocationMarker l : locationMarkers) {
      l.clear(anchorPane);
      if (l.getEquipmentMarker() != null) {
        l.getEquipmentMarker().clear(anchorPane);
      }
      if (l.getServiceRequestMarker() != null) {
        l.getServiceRequestMarker().clear(anchorPane);
      }
    }
    floorLocations.clear();
    floorEquipment.clear();
    floorSRs.clear();
    currentFloorIDs.clear();
    idToLocationMarker.clear();
    locationMarkers.clear();
    equipmentMarkers.clear();
    serviceRequestMarkers.clear();
  }

  public List<LocationMarker> getLocationMarkers() {
    return locationMarkers;
  }

  public List<EquipmentMarker> getEquipmentMarkers() {
    return equipmentMarkers;
  }

  public List<SRMarker> getServiceRequestMarkers() {
    return serviceRequestMarkers;
  }

  public void setDragLocation(
      LocationMarker locationMarker,
      SelectionManager selectionManager,
      CheckBoxManager checkBoxManager) {
    final Delta dragDelta = new Delta();
    Button button = locationMarker.getButton();
    button.setOnAction(
        event -> {
          selectionManager.existingLocationSelected(locationMarker.getLocation());
        });
    button.setOnMousePressed(
        mouseEvent -> {
          // record a delta distance for the drag and drop operation.
          dragDelta.buttonX = button.getLayoutX();
          dragDelta.buttonY = button.getLayoutY();
          dragDelta.mouseX = mouseEvent.getSceneX();
          dragDelta.mouseY = mouseEvent.getSceneY();
          button.setCursor(Cursor.MOVE);
          selectionManager.existingLocationSelected(locationMarker.getLocation());

          selectionManager.getEditButton().setDisable(false);
          selectionManager.getDeleteButton().setDisable(false);
          selectionManager.getSaveButton().setDisable(true);
          selectionManager.getClearButton().setDisable(true);
        });
    /*
      button.setOnMouseDragged(
              mouseEvent -> {
                if (checkBoxManager.getDragCheckBox().isSelected()) {
                  button.setLayoutX(
                          (mouseEvent.getSceneX() - dragDelta.mouseX)
                                  / (transformed.getHeight() / miniAnchorPane.getHeight())
                                  + dragDelta.buttonX);
                  button.setLayoutY(
                          (mouseEvent.getSceneY() - dragDelta.mouseY)
                                  / (transformed.getHeight() / miniAnchorPane.getHeight())
                                  + dragDelta.buttonY);

                  // TODO make sure math on this is right
                  if (markerType == 1 || markerType == 2) {
                    // standard circle radius around medical equipment markers, 30 is placeholder
                    double radius = Math.sqrt(2 * Math.pow(10, 2));
                    for (Location l : locations) {
                      // check hypotenuse between this equipment and every location on floor
                      double radiusCheck =
                              Math.sqrt(
                                      Math.pow(l.getXCoord() - button.getLayoutX(), 2)
                                              + (Math.pow(l.getYCoord() - button.getLayoutY(), 2)));
                      if (l.getFloor().equals(floor) && (radius > radiusCheck)) {
                        button.setLayoutX(l.getXCoord());
                        button.setLayoutY(l.getYCoord());
                      }
                    }
                  }

                  xPosText.setText(String.valueOf(button.getLayoutX() - mapImageView.getLayoutX() + 8));
                  yPosText.setText(String.valueOf(button.getLayoutY() - mapImageView.getLayoutY() + 24));
                  Label correspondingLabel;
                  if (markerType == 0) {
                    correspondingLabel = buttonLocationMarker.get(button).getLabel();
                  } else if (markerType == 1) {
                    correspondingLabel = buttonEquipmentMarker.get(button).getLabel();
                  } else {
                    correspondingLabel = buttonServiceRequestMarker.get(button).getLabel();
                  }
                  correspondingLabel.setLayoutX(
                          (mouseEvent.getSceneX() - dragDelta.mouseX)
                                  / (transformed.getHeight() / miniAnchorPane.getHeight())
                                  + dragDelta.buttonX
                                  + 8);
                  correspondingLabel.setLayoutY(
                          (mouseEvent.getSceneY() - dragDelta.mouseY)
                                  / (transformed.getHeight() / miniAnchorPane.getHeight())
                                  + dragDelta.buttonY
                                  - 24);
                }
              });
      button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));
      button.setOnMouseReleased(
              mouseEvent -> {
                button.setCursor(Cursor.HAND);
                if (markerType == 1 || markerType == 2) {
                  boolean isSnapped = false;
                  Location nearestLocation = locations.get(0);
                  double radiusOfNearest = Integer.MAX_VALUE;
                  for (Location l : locations) {
                    if (l.getFloor().equals(floor)) {
                      double radiusCheck =
                              Math.sqrt(
                                      Math.pow(l.getXCoord() - button.getLayoutX(), 2)
                                              + (Math.pow(l.getYCoord() - button.getLayoutY(), 2)));
                      // update nearest location
                      if (radiusCheck < radiusOfNearest) {
                        radiusOfNearest = radiusCheck;
                        nearestLocation = l;
                      }
                      // when it finds the location already snapped to, do this
                      if (button.getLayoutX() == l.getXCoord() && button.getLayoutY() == l.getYCoord()) {
                        nearestLocation = l;
                        isSnapped = true;
                        break;
                      }
                    }
                  }
                  if (!isSnapped) {
                    button.setLayoutX(nearestLocation.getXCoord());
                    button.setLayoutY(nearestLocation.getYCoord());
                  }
                  // update label to new location
                  xPosText.setText(String.valueOf(button.getLayoutX() - mapImageView.getLayoutX() + 8));
                  yPosText.setText(String.valueOf(button.getLayoutY() - mapImageView.getLayoutY() + 24));
                  Label correspondingLabel;
                  if (markerType == 1) {
                    correspondingLabel = buttonEquipmentMarker.get(button).getLabel();
                  } else {
                    correspondingLabel = buttonServiceRequestMarker.get(button).getLabel();
                  }
                  correspondingLabel.setLayoutX(button.getLayoutX());
                  correspondingLabel.setLayoutY(button.getLayoutY() - 20);

                  // TODO this function should update database but getting errors
                  try {
                    updateOnRelease(button);
                  } catch (SQLException e) {
                    e.printStackTrace();
                  }
                }
              });

    }


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
    */
  }
}
