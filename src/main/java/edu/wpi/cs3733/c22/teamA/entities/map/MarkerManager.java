package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;

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
  private boolean isSnapped;
  private Location draggedLocation;
  private AnchorPane anchorPane;

  // shit to make edit / save work
  private String floor;
  private LocationDAO locationDAO;
  private EquipmentDAO equipmentDAO;
  //ServiceRequestDAO SRDAO;

  private boolean dragPopupBool;

  public MarkerManager(LocationDAO locationDAO, EquipmentDAO equipmentDAO, AnchorPane anchorPane) throws IOException, ParseException {
    floorLocations = new ArrayList<>();
    currentFloorIDs = new HashSet<>();
    idToLocationMarker = new HashMap<>();
    allSRs = new ArrayList<>();
    locationMarkers = new ArrayList<>();
    equipmentMarkers = new ArrayList<>();
    serviceRequestMarkers = new ArrayList<>();
    floorEquipment = new ArrayList<>();
    floorSRs = new ArrayList<>();
    this.locationDAO=locationDAO;
    this.equipmentDAO=equipmentDAO;
    //this.SRDAO=SRDAO;

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

    public MarkerManager(LocationDAO locationDAO, AnchorPane anchorPane) throws IOException, ParseException {
        floorLocations = new ArrayList<>();
        currentFloorIDs = new HashSet<>();
        idToLocationMarker = new HashMap<>();
        allSRs = new ArrayList<>();
        locationMarkers = new ArrayList<>();
        equipmentMarkers = new ArrayList<>();
        serviceRequestMarkers = new ArrayList<>();
        floorEquipment = new ArrayList<>();
        floorSRs = new ArrayList<>();
        this.locationDAO=locationDAO;
        //this.SRDAO=SRDAO;

        allLocations = locationDAO.getNodeList();

        this.mapLayoutX = 0;
        this.mapLayoutY = 0;
        this.anchorPane = anchorPane;
    }

  public void initFloor(
      String floor,
      int mapLayoutX,
      int mapLayoutY,
      SelectionManager selectionManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager) {
      this.floor = floor;
    getFloorInfo(floor);
    createFloorEntities(selectionManager, checkBoxManager, gesturePaneManager);
    initialDraw();
    this.mapLayoutX = mapLayoutX;
    this.mapLayoutY = mapLayoutY;
  }

    public void initFloor(
            String floor,
            int mapLayoutX,
            int mapLayoutY) {
        this.floor = floor;
        getFloorInfo(floor);
        createFloorLocations();
        initialDraw();
        this.mapLayoutX = mapLayoutX;
        this.mapLayoutY = mapLayoutY;
    }

  public void getFloorInfo(String floor) {
    clear();
    getFloorLocations(floor);
    if (allEquipments != null){
        getEquipmentLocations();
        getSRLocations();
    }
  }

  private void getFloorLocations(String floor) {
    for (Location l : allLocations) {
      if (floor.equals(l.getStringFields().get("floor"))) {
        floorLocations.add(l);
        currentFloorIDs.add(l.getStringFields().get("node_id"));
      }
    }
  }

  private void getEquipmentLocations() {
    for (Equipment e : allEquipments) {
      if (currentFloorIDs.contains(e.getStringFields().get("current_location"))) {
        floorEquipment.add(e);
      }
    }
  }

  private void getSRLocations() {
    for (SR sr : allSRs) {
      if (currentFloorIDs.contains( sr.getStringFields().get("end_location"))) {
        floorSRs.add(sr);
      }
    }
  }

  public List<SR> returnSRLocations() {
    return floorSRs;
  }

  public List<Equipment> returnDirtyEquipmentLocations() {
      List<Equipment> equips = new ArrayList<>();
      for (Equipment equip : floorEquipment) {
          if (!Boolean.parseBoolean(equip.getStringFields().get("is_clean"))) equips.add(equip);
      }
      return equips;
  }

  public List<Equipment> returnCleanEquipmentLocations() {
      List<Equipment> equips = new ArrayList<>();
      for (Equipment equip : floorEquipment) {
          if (Boolean.parseBoolean(equip.getStringFields().get("is_clean")))
              equips.add(equip);
      }
      return equips;
  }

  private void createFloorEntities(
      SelectionManager selectionManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager) {
    createFloorLocations(selectionManager, checkBoxManager, gesturePaneManager);
    createFloorEquipments(selectionManager, checkBoxManager, gesturePaneManager);
    createFloorSRs(selectionManager, checkBoxManager, gesturePaneManager);
  }

  private void createFloorLocations(
      SelectionManager selectionManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager) {
    for (Location l : floorLocations) {
      LocationMarker newLocationMarker = MarkerMaker.makeLocationMarker(l, mapLayoutX, mapLayoutY);
      locationMarkers.add(newLocationMarker);
      idToLocationMarker.put(l.getStringFields().get("node_id"), newLocationMarker);
      setDragLocation(newLocationMarker, selectionManager, checkBoxManager, gesturePaneManager);
    }
  }

    private void createFloorLocations() {
        for (Location l : floorLocations) {
            LocationMarker newLocationMarker = MarkerMaker.makeLocationMarker(l, mapLayoutX, mapLayoutY);
            locationMarkers.add(newLocationMarker);
            idToLocationMarker.put(l.getStringFields().get("node_id"), newLocationMarker);
        }
    }

  private void createFloorEquipments(
      SelectionManager selectionManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager) {
    for (Equipment e : floorEquipment) {
      EquipmentMarker newEquipmentMarker =
          MarkerMaker.makeEquipmentMarker(
              e, idToLocationMarker.get(e.getStringFields().get("current_location")), mapLayoutX, mapLayoutY);
      equipmentMarkers.add(newEquipmentMarker);
      setDragEquipment(newEquipmentMarker, selectionManager, checkBoxManager, gesturePaneManager);
    }
  }

  private void createFloorSRs(
      SelectionManager selectionManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager) {
    for (SR sr : floorSRs) {
      SRMarker newSRMarker =
          MarkerMaker.makeSRMarker(
              sr, idToLocationMarker.get(sr.getStringFields().get("end_location")), mapLayoutX, mapLayoutY);
      serviceRequestMarkers.add(newSRMarker);
      setDragSR(newSRMarker, selectionManager, checkBoxManager, gesturePaneManager);
    }
  }

  private void initialDraw() {
    for (LocationMarker l : locationMarkers) {
        if(!l.getLocation().getStringFields().get("node_id").equals("N/A")) {
            l.draw(anchorPane);
        }
      if (l.getEquipmentMarker() != null) {
        l.getEquipmentMarker().draw(anchorPane);
      }
      if (l.getServiceRequestMarker() != null) {
        l.getServiceRequestMarker().draw(anchorPane);
      }
    }
  }
  // TODO probably move this to selectionManager
    public void newLocationPressed(SelectionManager selectionManager, CheckBoxManager checkBoxManager, GesturePaneManager gesturePaneManager) throws IOException {
        // only one new location at a time
        String newID = "New";
        int ct = 2;
        while (idToLocationMarker.containsKey(newID)){
            newID = "New" + ct;
            ct++;
        }

        // create fields so shit isnt null and the vbox component wont break
        Location newLocation = new Location(newID, 10, 10, floor, "Tower", "New", "New", "New");

        // make marker for location where its actually usable
        LocationMarker newLocationMarker = MarkerMaker.makeLocationMarker(newLocation, 10, 10);
        locationMarkers.add(newLocationMarker);
        idToLocationMarker.put(newID, newLocationMarker);
        locationDAO.enterLocationNode(newLocation);
        setDragLocation(newLocationMarker, selectionManager, checkBoxManager, gesturePaneManager);
        midRunDraw();
        selectionManager.existingLocationSelected(newLocationMarker);
    }

  public void midRunDraw() {
    for (LocationMarker l : locationMarkers) {
      l.clear(anchorPane);
        System.out.println(l.getLabel().getText());
      if (l.getEquipmentMarker() != null) {
        l.getEquipmentMarker().clear(anchorPane);
      }
      if (l.getServiceRequestMarker() != null) {
        l.getServiceRequestMarker().clear(anchorPane);
      }
    }
    for (SRMarker sr : serviceRequestMarkers) {
      sr.clear(anchorPane);
    }
    for (EquipmentMarker e : equipmentMarkers) {
      e.clear(anchorPane);
    }

    initialDraw();
  }

  public void redrawEditedLocation() throws IOException, ParseException {
      allLocations.clear();
      allLocations = locationDAO.getNodeList();
      System.out.println("redraw pre checking");
      floorLocations.clear();
      getFloorLocations(floor);
      locationMarkers.clear();

      midRunDraw();
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

  public void setDragEquipment(
      EquipmentMarker equipmentMarker,
      SelectionManager selectionManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager) {
    final Delta dragDelta = new Delta();
    Button button = equipmentMarker.getButton();
    button.setOnAction(
        event -> {
          selectionManager.existingEquipmentSelected(equipmentMarker.getEquipment());
        });
    button.setOnMousePressed(
        mouseEvent -> {
          // record a delta distance for the drag and drop operation.
          dragDelta.buttonX = button.getLayoutX();
          dragDelta.buttonY = button.getLayoutY();
          dragDelta.mouseX = mouseEvent.getSceneX();
          dragDelta.mouseY = mouseEvent.getSceneY();
          button.setCursor(Cursor.MOVE);
          selectionManager.existingEquipmentSelected(equipmentMarker.getEquipment());
            selectionManager.getEditButton().setDisable(false);
            selectionManager.getDeleteButton().setDisable(false);
            selectionManager.getSaveButton().setDisable(true);
            selectionManager.getClearButton().setDisable(true);
        });
    button.setOnMouseDragged(
        mouseEvent -> {
            if (dragPopupBool == true){
                dragPopupBool = false;
                return;
            }
            if (!Boolean.parseBoolean(equipmentMarker.getEquipment().getStringFields().get("is_clean"))){
                JOptionPane pane = new JOptionPane("Dirty equipment cannot be dragged", JOptionPane.ERROR_MESSAGE);
                JDialog dialog = pane.createDialog("Drag error");
                dialog.setVisible(true);
                dragPopupBool = true;
                return;
            }
          if (checkBoxManager.getDragCheckBox().isSelected()) {
              selectionManager.getEditButton().setDisable(true);
              selectionManager.getDeleteButton().setDisable(true);
            button.setLayoutX(
                (mouseEvent.getSceneX() - dragDelta.mouseX)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonX);
            button.setLayoutY(
                (mouseEvent.getSceneY() - dragDelta.mouseY)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonY);
            selectionManager.getCurrentList().get(3).textArea.setText("--");
              isSnapped = false;

            // standard circle radius around medical equipment markers, 20 is placeholder
            double radius = Math.sqrt(2 * Math.pow(20, 2));
            for (Location l : floorLocations) {
              // check hypotenuse between this equipment and every location on floor
              double radiusCheck =
                  Math.sqrt(
                      Math.pow(Integer.parseInt(l.getStringFields().get("xcoord")) - button.getLayoutX(), 2)
                          + (Math.pow(Integer.parseInt(l.getStringFields().get("ycoord")) - button.getLayoutY(), 2)));
              if (radius > radiusCheck) {
                button.setLayoutX(Integer.parseInt(l.getStringFields().get("xcoord")));
                button.setLayoutY(Integer.parseInt(l.getStringFields().get("ycoord")) - 20);
                  selectionManager.getCurrentList().get(3).textArea.setText(l.getStringFields().get("node_id"));
                  draggedLocation = l;
                  isSnapped = true;
              }
            }
            Label correspondingLabel = equipmentMarker.getLabel();
            correspondingLabel.setLayoutX(
                (mouseEvent.getSceneX() - dragDelta.mouseX)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonX
                    + 8);
            correspondingLabel.setLayoutY(
                (mouseEvent.getSceneY() - dragDelta.mouseY)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonY
                    - 24);
          }
        });
    button.setOnMouseReleased(
        mouseEvent -> {
          button.setCursor(Cursor.HAND);
          if (!isSnapped) {
            button.setLayoutX(Integer.parseInt(equipmentMarker.getLocationMarker().getLocation().getStringFields().get("xcoord")));
            button.setLayoutY(Integer.parseInt(equipmentMarker.getLocationMarker().getLocation().getStringFields().get("ycoord")) - 24);
              selectionManager.getCurrentList().get(3).textArea.setText(equipmentMarker.getEquipment().getStringFields().get("current_location"));
          }
            if (!(draggedLocation.getStringFields().get("node_type").equals("STOR")) && !(draggedLocation.getStringFields().get("node_type").equals("PATI"))){
                JOptionPane pane = new JOptionPane("Equipment cannot be stored here", JOptionPane.ERROR_MESSAGE);
                JDialog dialog = pane.createDialog("Drag error");
                dialog.setVisible(true);
                button.setLayoutX(dragDelta.buttonX);
                button.setLayoutY(dragDelta.buttonY);
                Label correspondingLabel = equipmentMarker.getLabel();
                correspondingLabel.setLayoutX(dragDelta.buttonX);
                correspondingLabel.setLayoutY(dragDelta.buttonY - 24);
                draggedLocation = null;
                isSnapped = false;
                return;
            }
          // update label to new location
          Label correspondingLabel = equipmentMarker.getLabel();
          correspondingLabel.setLayoutX(equipmentMarker.getButton().getLayoutX());
          correspondingLabel.setLayoutY(equipmentMarker.getButton().getLayoutY() - 24);
          // TODO this function should update database but getting errors

             try {
                 if (isSnapped) {
                     equipmentMarker.getEquipment().getStringFields().put("current_location", draggedLocation.getStringFields().get("node_id"));
                     equipmentDAO.updateMedicalEquipment(equipmentMarker.getEquipment());
                 }
             } catch (SQLException | IOException e) {
                 e.printStackTrace();
             }

             isSnapped = false;
             draggedLocation = null;
            selectionManager.getEditButton().setDisable(false);
            selectionManager.getDeleteButton().setDisable(false);
        });
  }

  public void setDragSR(
      SRMarker srMarker,
      SelectionManager selectionManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager) {
    final Delta dragDelta = new Delta();
    Button button = srMarker.getButton();
    button.setOnAction(
        event -> {
          selectionManager.existingServiceRequestSelected(srMarker.getServiceRequest());
        });
    button.setOnMousePressed(
        mouseEvent -> {
          // record a delta distance for the drag and drop operation.
          dragDelta.buttonX = button.getLayoutX();
          dragDelta.buttonY = button.getLayoutY();
          dragDelta.mouseX = mouseEvent.getSceneX();
          dragDelta.mouseY = mouseEvent.getSceneY();
          button.setCursor(Cursor.MOVE);
          selectionManager.existingServiceRequestSelected(srMarker.getServiceRequest());
            selectionManager.getEditButton().setDisable(false);
            selectionManager.getDeleteButton().setDisable(false);
            selectionManager.getSaveButton().setDisable(true);
            selectionManager.getClearButton().setDisable(true);
        });
    button.setOnMouseDragged(
        mouseEvent -> {
          if (checkBoxManager.getDragCheckBox().isSelected()) {
              selectionManager.getEditButton().setDisable(true);
              selectionManager.getDeleteButton().setDisable(true);
            button.setLayoutX(
                (mouseEvent.getSceneX() - dragDelta.mouseX)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonX);
            button.setLayoutY(
                (mouseEvent.getSceneY() - dragDelta.mouseY)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonY);
              selectionManager.getCurrentList().get(2).textArea.setText("--");
              isSnapped = false;

            // standard circle radius around medical equipment markers, 20 is placeholder
            double radius = Math.sqrt(2 * Math.pow(20, 2));
            for (Location l : floorLocations) {
              // check hypotenuse between this equipment and every location on floor
              double radiusCheck =
                  Math.sqrt(
                      Math.pow(Integer.parseInt(l.getStringFields().get("xcoord")) - button.getLayoutX(), 2)
                          + (Math.pow(Integer.parseInt(l.getStringFields().get("ycoord")) - button.getLayoutY(), 2)));
              if (radius > radiusCheck) {
                button.setLayoutX(Integer.parseInt(l.getStringFields().get("xcoord")));
                button.setLayoutY(Integer.parseInt(l.getStringFields().get("ycoord")) - 20);
                  selectionManager.getCurrentList().get(2).textArea.setText(l.getStringFields().get("node_id"));
                  isSnapped = true;
              }
            }

            Label correspondingLabel = srMarker.getLabel();
            correspondingLabel.setLayoutX(
                (mouseEvent.getSceneX() - dragDelta.mouseX)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonX
                    + 8);
            correspondingLabel.setLayoutY(
                (mouseEvent.getSceneY() - dragDelta.mouseY)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonY
                    - 24);
          }
        });
    button.setOnMouseReleased(
        mouseEvent -> {
          button.setCursor(Cursor.HAND);
          if (!isSnapped) {
              button.setLayoutX(Integer.parseInt(srMarker.getLocationMarker().getLocation().getStringFields().get("xcoord")));
              button.setLayoutY(Integer.parseInt(srMarker.getLocationMarker().getLocation().getStringFields().get("ycoord")) - 24);
              selectionManager.getCurrentList().get(2).textArea.setText(srMarker.getServiceRequest().getStringFields().get("end_location"));
          }
          // update label to new location
          Label correspondingLabel = srMarker.getLabel();
          correspondingLabel.setLayoutX(srMarker.getButton().getLayoutX());
          correspondingLabel.setLayoutY(srMarker.getButton().getLayoutY() - 24);
          // TODO this function should update database but getting errors
          /*
             try {
                 updateOnRelease(button);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
          */
            selectionManager.getEditButton().setDisable(false);
            selectionManager.getDeleteButton().setDisable(false);
            isSnapped = false;
        });
  }

  public void setDragLocation(
      LocationMarker locationMarker,
      SelectionManager selectionManager,
      CheckBoxManager checkBoxManager,
      GesturePaneManager gesturePaneManager) {
    final Delta dragDelta = new Delta();
    Button button = locationMarker.getButton();
    button.setOnAction(
        event -> {
            try {
                selectionManager.existingLocationSelected(locationMarker);
            }
            catch(Exception e){
                System.out.println("Dragged too fast :/");
            }
        });
    button.setOnMousePressed(
        mouseEvent -> {
          // record a delta distance for the drag and drop operation.
          dragDelta.buttonX = button.getLayoutX();
          dragDelta.buttonY = button.getLayoutY();
          dragDelta.mouseX = mouseEvent.getSceneX();
          dragDelta.mouseY = mouseEvent.getSceneY();
          button.setCursor(Cursor.MOVE);
          try {
              selectionManager.existingLocationSelected(locationMarker);
          }
          catch(Exception e){
              System.out.println("Dragged too fast 2");
          }

            selectionManager.getEditButton().setDisable(false);
            selectionManager.getDeleteButton().setDisable(false);
            selectionManager.getSaveButton().setDisable(true);
            selectionManager.getClearButton().setDisable(true);
        });
    button.setOnMouseDragged(
        mouseEvent -> {
          if (checkBoxManager.getDragCheckBox().isSelected()) {
              selectionManager.getEditButton().setDisable(true);
              selectionManager.getDeleteButton().setDisable(true);
            button.setLayoutX(
                (mouseEvent.getSceneX() - dragDelta.mouseX)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonX);
            button.setLayoutY(
                (mouseEvent.getSceneY() - dragDelta.mouseY)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonY);

            selectionManager
                .getCurrentList()
                .get(1)
                .textArea
                .setText(
                    String.valueOf(
                        button.getLayoutX()
                            - gesturePaneManager.getMapImageView().getLayoutX()
                            + 8));
            selectionManager
                .getCurrentList()
                .get(2)
                .textArea
                .setText(
                    String.valueOf(
                        button.getLayoutY()
                            - gesturePaneManager.getMapImageView().getLayoutY()
                            + 24));
            Label correspondingLabel = locationMarker.getLabel();
            correspondingLabel.setLayoutX(
                (mouseEvent.getSceneX() - dragDelta.mouseX)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonX
                    + 8);
            correspondingLabel.setLayoutY(
                (mouseEvent.getSceneY() - dragDelta.mouseY)
                        / (gesturePaneManager.getTransformed().getHeight()
                            / gesturePaneManager.getAnchorPane().getHeight())
                    + dragDelta.buttonY
                    - 24);
          }
        });
    button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));
    button.setOnMouseReleased(
        mouseEvent -> {
          button.setCursor(Cursor.HAND);
            selectionManager.getEditButton().setDisable(false);
            selectionManager.getDeleteButton().setDisable(false);
        });
  }

    public List<Location> getAllLocations() {
        return allLocations;
    }

    public String getFloor() {
        return floor;
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
}

 */
