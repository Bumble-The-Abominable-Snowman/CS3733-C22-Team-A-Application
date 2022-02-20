package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

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

	public MarkerManager(LocationDAO locationDAO, EquipmentDAO equipmentDAO, int mapLayoutX, int mapLayoutY) throws IllegalAccessException, SQLException, InvocationTargetException {
		floorLocations = new ArrayList<>();
		currentFloorIDs = new HashSet<>();
		idToLocationMarker = new HashMap<>();

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

		this.mapLayoutX = mapLayoutX;
		this.mapLayoutY = mapLayoutY;
	}

	public void initFloor(String floor){
		clear();
		getFloorLocations(floor);
		getEquipmentLocations();
		getSRLocations();
	}

	private void getFloorLocations(String floor){
		for(Location l: allLocations) {
			if (floor.equals(l.getFloor())) {
				floorLocations.add(l);
				currentFloorIDs.add(l.getNodeID());
			}
		}
	}

	private void getEquipmentLocations(){
		for(Equipment e: allEquipments){
			if(currentFloorIDs.contains(e.getCurrentLocation())){
				floorEquipment.add(e);
			}
		}
	}

	private void getSRLocations(){
		for(SR sr: allSRs){
			if(currentFloorIDs.contains(sr.getEndLocation())){
				floorSRs.add(sr);
			}
		}
	}

	private void clear(){
		floorLocations.clear();
	}

	private void createFloorLocations(){
		for(Location l: floorLocations){
			LocationMarker newLocationMarker = MarkerMaker.makeLocationMarker(l, mapLayoutX, mapLayoutY);
			locationMarkers.add(newLocationMarker);
			idToLocationMarker.put(l.getNodeID(), newLocationMarker);
		}
	}

	private void createFloorEquipments(){
		for(Equipment e: floorEquipment){
			EquipmentMarker newEquipmentMarker = MarkerMaker.makeEquipmentMarker(e,idToLocationMarker.get(e.getCurrentLocation()), mapLayoutX, mapLayoutY);
			equipmentMarkers.add(newEquipmentMarker);
		}
	}

	private void createFloorSRs(){
		for(Location l: floorLocations){
			LocationMarker newLocationMarker = MarkerMaker.makeLocationMarker(l, mapLayoutX, mapLayoutY);
			locationMarkers.add(newLocationMarker);
		}
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
