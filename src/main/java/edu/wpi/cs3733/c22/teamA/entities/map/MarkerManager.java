package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.util.*;
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
