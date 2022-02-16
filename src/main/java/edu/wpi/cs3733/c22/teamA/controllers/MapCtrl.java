package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.map.Edge;
import edu.wpi.cs3733.c22.teamA.entities.map.EquipmentMarker;
import edu.wpi.cs3733.c22.teamA.entities.map.LocationMarker;
import edu.wpi.cs3733.c22.teamA.entities.map.SRMarker;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import javafx.animation.Interpolator;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.util.Duration;
import net.kurobako.gesturefx.AffineEvent;
import net.kurobako.gesturefx.GesturePane;

// TODO Change all instances of looping through locations to find related short names & node ids
// with method in backend once implemented
public class MapCtrl extends MasterCtrl {

  @FXML private JFXButton newLocButton;
  @FXML private JFXButton viewTableButton;

  @FXML private JFXComboBox pfFromComboBox;
  @FXML private JFXComboBox pfToComboBox;

  @FXML private JFXCheckBox locationCheckBox;
  @FXML private JFXCheckBox dragCheckBox;
  @FXML private JFXCheckBox equipmentCheckBox;
  @FXML private JFXCheckBox serviceRequestCheckBox;
  @FXML private JFXCheckBox showTextCheckBox;

  @FXML private JFXButton deleteButton;
  @FXML private JFXComboBox floorSelectionComboBox;

  @FXML private JFXComboBox searchComboBox = new JFXComboBox();

  @FXML private JFXTextArea nodeIDText = new JFXTextArea();
  @FXML private JFXTextArea xPosText = new JFXTextArea();
  @FXML private JFXTextArea yPosText = new JFXTextArea();
  @FXML private JFXTextArea floorText = new JFXTextArea();
  @FXML private JFXTextArea buildingText = new JFXTextArea();
  @FXML private JFXTextArea typeText = new JFXTextArea();
  @FXML private JFXTextArea longnameText = new JFXTextArea();
  @FXML private JFXTextArea shortnameText = new JFXTextArea();

  @FXML private JFXButton saveButton = new JFXButton();
  @FXML private JFXButton editButton = new JFXButton();
  @FXML private JFXButton clearButton = new JFXButton();

  @FXML private VBox inputVBox = new VBox();
  @FXML private ImageView mapImageView = new ImageView();

  @FXML private GesturePane gesturePane;

  private LocationMarker newLocationMarker = null;
  private Dimension2D transformed = new Dimension2D(967, 1050);
  private AnchorPane miniAnchorPane = new AnchorPane();
  private List<Line> pfLine = new ArrayList<>();

  private List<Location> locations;
  private List<Equipment> equipments;
  private List<SR> serviceRequests;
  private Polygon locationMarkerShape;
  private Polygon equipmentMarkerShape;

  private String floor;
  private String floorName;
  private String currentID;
  private Location selectedLocation;

  private LocationDAO locationDAO = new LocationDerbyImpl();
  private EquipmentDAO equipmentDAO = new EquipmentDerbyImpl();

  private HashMap<Button, LocationMarker> buttonLocationMarker;
  private HashMap<Button, EquipmentMarker> buttonEquipmentMarker;
  private HashMap<Button, SRMarker> buttonServiceRequestMarker;

  public MapCtrl() {
    locationMarkerShape = new Polygon();
    equipmentMarkerShape = new Polygon();
    locationMarkerShape.getPoints().addAll(new Double[] {1.0, 4.0, 0.0, 2.0, 1.0, 0.0, 2.0, 2.0});
    equipmentMarkerShape.getPoints().addAll(new Double[] {0.0, 0.0, 0.0, 1.0, 4.0, 1.0, 4.0, 0.0});

    buttonLocationMarker = new HashMap<>();
    buttonEquipmentMarker = new HashMap<>();
    buttonServiceRequestMarker = new HashMap<>();

    locations = new ArrayList<>();
    equipments = new ArrayList<>();
    serviceRequests = new ArrayList<>();

    currentID = "";
    floor = "";
  }

  private HashMap<Location, HashSet<Edge>> neighborMap;

  @FXML
  public void initialize() {

    configure();

    // Pathfinding Setup
    // Setup Functions
    fillFromDB();
    setupCheckboxListeners();
    setupContextMenu();
    setInitialUIStates();
    setupSearchListener();
    setupFloor("Choose Floor:");

    floorSelectionComboBox.getItems().removeAll(floorSelectionComboBox.getItems());
    floorSelectionComboBox
        .getItems()
        .addAll("Choose Floor:", "Floor 1", "Floor 2", "Floor 3", "L1", "L2");

    floorSelectionComboBox.getSelectionModel().select("Choose Floor:");
    floorSelectionComboBox
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              fillFromDB();
              // Sets up floor on Map
              setupFloor(newValue.toString());
              clearAll();

              // Maps Location IDs to their markers
              HashMap<String, LocationMarker> locationIDs = new HashMap<>();

              List<Location> thisFloorLocations = new ArrayList<>();

              // Loops through every location & draws them if present on the floor
              for (Location location : locations) {
                if (location.getFloor().equals(floor)) {
                  LocationMarker locationMarker = newDraggableLocation(location);
                  locationMarker.draw(miniAnchorPane);
                  locationIDs.put(location.getNodeID(), locationMarker);
                  thisFloorLocations.add(location);
                }
              }

              try {
                neighborMap = getEdges();
              } catch (IOException e) {
                e.printStackTrace();
              } catch (ParseException e) {
                e.printStackTrace();
              }
              pfToComboBox
                  .getItems()
                  .addAll(
                      thisFloorLocations.stream()
                          .map(Location::getShortName)
                          .collect(Collectors.toList()));
              pfFromComboBox
                  .getItems()
                  .addAll(
                      thisFloorLocations.stream()
                          .map(Location::getShortName)
                          .collect(Collectors.toList()));

              // Loops through every medical equipment & draws them if they're on this floor
              for (Equipment equipment : equipments) {
                if (locationIDs.containsKey(equipment.getCurrentLocation())) {
                  EquipmentMarker equipmentMarker =
                      newDraggableEquipment(
                          equipment, locationIDs.get(equipment.getCurrentLocation()));
                  equipmentMarker.draw(miniAnchorPane);
                }
              }

              // Loops through every service request & draws them if they're on this floor
              for (SR serviceRequest : serviceRequests) {
                if (locationIDs.containsKey(serviceRequest.getEndLocation())) {
                  SRMarker serviceRequestMarker =
                      newDraggableServiceRequest(
                          serviceRequest, locationIDs.get(serviceRequest.getEndLocation()));
                  serviceRequestMarker.draw(miniAnchorPane);
                }
              }
            });
  }

  // Sets up CheckBoxes
  public void setupCheckboxListeners() {
    equipmentCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showEquipment(new_val);
            });

    locationCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showLocations(new_val);
            });

    serviceRequestCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showServiceRequests(new_val);
            });

    showTextCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showLabels(new_val);
            });
  }

  // Sets up the map context menu for right clicks

  double mouseX = 0;
  double mouseY = 0;

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

  // Sets up UI states of text areas, and buttons
  public void setInitialUIStates() {
    editButton.setDisable(true);
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    deleteButton.setDisable(true);

    dragCheckBox.setSelected(true);
    showTextCheckBox.setSelected(true);
    equipmentCheckBox.setSelected(true);
    serviceRequestCheckBox.setSelected(true);
    locationCheckBox.setSelected(true);

    inputVBox.setDisable(true);
  }

  // Fills info from DB
  public void fillFromDB() {
    locations.clear();
    equipments.clear();
    serviceRequests.clear();
    locations.addAll(new ArrayList<>(new LocationDerbyImpl().getNodeList()));
    for (Location l : locations) {
      if (l.getNodeID().equals("N/A")) {
        locations.remove(l);
        break;
      }
    }
    equipments.addAll(new ArrayList<>(new EquipmentDerbyImpl().getMedicalEquipmentList()));
    // TODO when implementation done
    try {
      List<?> requestList = ServiceRequestDerbyImpl.getAllServiceRequestList();
      for (Object sr : requestList) {
        serviceRequests.add((SR) sr);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  // Set up searchbar for LOCATIONS ONLY
  // TODO IMPLEMENT CHOICE OF LOCATION, EQUIPMENT, SR SEARCH (another button?)
  public void setupSearchListener() {
    searchComboBox.setEditable(true);
    // set up list of locations to be wrapped
    ObservableList<Location> searchLocationList = FXCollections.observableArrayList();
    searchLocationList.addAll(locations);

    // create filtered list, can be filtered (duh)
    FilteredList<Location> filteredLocations = new FilteredList<>(searchLocationList, p -> true);
    // add listener that checks whenever changes are made to JFXText searchText
    searchComboBox
        .getEditor()
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              filteredLocations.setPredicate(
                  location -> {
                    // if field is empty display all locations
                    if ((newValue == null
                            || searchComboBox.getSelectionModel().toString().isEmpty())
                        && location.getFloor().equals(floor)) {
                      return true;
                    }
                    // make sure case is factored out
                    String lowerCaseFilter = newValue.toLowerCase();
                    // if search matches either name or ID, display it
                    // if not, this returns false and doesn't display
                    return (location.getLongName().toLowerCase().contains(lowerCaseFilter)
                            || location.getShortName().toLowerCase().contains(lowerCaseFilter)
                            || location.getNodeID().toLowerCase().contains(lowerCaseFilter))
                        && location.getFloor().equals(floor);
                  });
              // add items to comboBox dropdown
              ArrayList<String> locationNames = new ArrayList<>();
              for (Location l : filteredLocations) {
                locationNames.add(l.getLongName());
              }
              searchComboBox.getItems().clear();
              searchComboBox.getItems().addAll(locationNames);
              if (searchComboBox.getItems().size() < 5) {
                searchComboBox.setVisibleRowCount(searchComboBox.getItems().size());
              } else {
                searchComboBox.setVisibleRowCount(5);
              }
              // select location if search complete
              if (searchComboBox.getItems().size() == 1) {
                existingLocationSelected(filteredLocations.get(0));
              }

              clearAll();
              HashMap<String, LocationMarker> locationIDs = new HashMap<>();
              // Loops through every location filtered & draws them if present on the floor
              // TODO clean this up somehow, need a new .contains type method
              for (Location l : locations) {
                System.out.println(l.getLongName());
                for (Location ls : filteredLocations) {
                  if (ls.getNodeID().equals(l.getNodeID())) {
                    LocationMarker locationMarker = newDraggableLocation(l);
                    locationMarker.draw(miniAnchorPane);
                    locationMarker.setButtonVisibility(true);
                    locationIDs.put(l.getNodeID(), locationMarker);
                    break;
                  }
                }
              }
              // Loops through every medical equipment & draws them if they're on this floor
              for (Equipment equipment : equipments) {
                if (locationIDs.containsKey(equipment.getCurrentLocation())) {
                  EquipmentMarker equipmentMarker =
                      newDraggableEquipment(
                          equipment, locationIDs.get(equipment.getCurrentLocation()));
                  equipmentMarker.draw(miniAnchorPane);
                }
              }
            });
  }

  private List<Button> sideView = new ArrayList<>();

  public void hideSideView() {
    for (Button b : sideView) {
      b.setVisible(false);
    }
  }

  // Sets up the side view
  public void showSideView() {
    String[] floorNames = {"Floor 3", "Floor 2", "Floor 1", "L1", "L2"};
    int initialY = 1025;
    for (int i = 0; i < 5; i++) {
      double buttonX = 116 + mapImageView.getLayoutX();
      double buttonY = initialY + i * 50 + mapImageView.getLayoutY();
      Button button = newButton(buttonX, buttonY, 420, 45);
      sideView.add(button);
      button.setShape(equipmentMarkerShape);
      String floor = floorNames[i];
      int srCount = 0;
      int dEquipCount = 0;
      int cEquipCount = 0;
      for (Location location : locations) {
        if (location.getFloor().equals(floor.replace("Floor ", ""))) {
          for (int j = 0; j < serviceRequests.size(); j++) {
            if (serviceRequests.get(j).getEndLocation().equals(location.getNodeID())) {
              srCount++;
            }
          }
          for (int j = 0; j < equipments.size(); j++) {
            if (equipments.get(j).getCurrentLocation().equals(location.getNodeID())) {
              if (!equipments.get(j).getIsClean()) dEquipCount++;
              else cEquipCount++;
            }
          }
        }
      }
      button.setText(
          "Reqs: " + srCount + " | Clean Equip: " + cEquipCount + " | Dirty Equip: " + dEquipCount);
      button.setOnMousePressed(mouseEvent -> floorSelectionComboBox.setValue(floor));
      miniAnchorPane.getChildren().add(button);
    }
  }

  // Sets up the floor
  public void setupFloor(String newValue) {
    if (newValue.equals("Choose Floor:")) {
      floor = "";
      floorName = "";
      URL url = App.class.getResource("images/Side View.png");
      Image image = new Image(String.valueOf(url));
      mapImageView.setImage(image);
      setupGesture();
      showSideView();
    } else if (newValue.equals("Floor 1")) {
      floor = "1";
      floorName = "Floor 1";
      URL url = App.class.getResource("images/1st Floor.png");
      Image image = new Image(String.valueOf(url));
      mapImageView.setImage(image);
      setupGesture();
      hideSideView();
    } else if (newValue.equals("Floor 2")) {
      floor = "2";
      floorName = "Floor 2";
      URL url = App.class.getResource("images/2nd Floor.png");
      Image image = new Image(String.valueOf(url));
      mapImageView.setImage(image);
      setupGesture();
      hideSideView();
    } else if (newValue.equals("Floor 3")) {
      floor = "3";
      floorName = "Floor 3";
      URL url = App.class.getResource("images/3rd Floor.png");
      Image image = new Image(String.valueOf(url));
      mapImageView.setImage(image);
      setupGesture();
      hideSideView();
    } else if (newValue.equals("L1")) {
      floor = "L1";
      floorName = "L1";
      URL url = App.class.getResource("images/LL1.png");
      Image image = new Image(String.valueOf(url));
      mapImageView.setImage(image);
      setupGesture();
      hideSideView();
    } else {
      floor = "L2";
      floorName = "L2";
      URL url = App.class.getResource("images/LL2.png");
      Image image = new Image(String.valueOf(url));
      mapImageView.setImage(image);
      setupGesture();
      hideSideView();
    }
  }

  public void setupGesture() {
    miniAnchorPane.getChildren().remove(mapImageView);
    miniAnchorPane.getChildren().add(mapImageView);
    miniAnchorPane.setLayoutX(0);
    mapImageView.setLayoutX(0);
    gesturePane.reset();
    gesturePane.setContent(miniAnchorPane);
    gesturePane.addEventFilter(
        AffineEvent.CHANGED,
        event -> {
          // System.out.println(event.getTransformedDimension());
          transformed = event.getTransformedDimension();
        });
    gesturePane.setOnMouseClicked(
        e -> {
          if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            Point2D pivotOnTarget =
                gesturePane
                    .targetPointAt(new Point2D(e.getX(), e.getY()))
                    .orElse(gesturePane.targetPointAtViewportCentre());
            // increment of scale makes more sense exponentially instead of linearly
            gesturePane
                .animate(Duration.millis(200))
                .interpolateWith(Interpolator.EASE_BOTH)
                .zoomBy(gesturePane.getCurrentScale(), pivotOnTarget);
          }
        });
  }

  // Makes a new Draggable Location Button and Label
  public LocationMarker newDraggableLocation(Location location) {
    double buttonX = location.getXCoord() + mapImageView.getLayoutX() - 8;
    double buttonY = location.getYCoord() + mapImageView.getLayoutY() - 24;
    Button button = newDraggableButton(buttonX, buttonY, 0);

    button.setStyle("-fx-background-color: #78aaf0");
    button.setShape(locationMarkerShape);

    double labelX = location.getXCoord() + mapImageView.getLayoutX() - 8 + 7.5;
    double labelY = location.getYCoord() + mapImageView.getLayoutY() - 24 - 15;
    Label label = newDraggableLabel(labelX, labelY, location.getShortName());

    LocationMarker locationMarker = new LocationMarker(button, label, location);
    buttonLocationMarker.put(button, locationMarker);
    return locationMarker;
  }

  // Makes a new Draggable Equipment button and Label
  public EquipmentMarker newDraggableEquipment(Equipment equipment, LocationMarker locationMarker) {

    double buttonX = locationMarker.getLocation().getXCoord() + mapImageView.getLayoutX() - 8 + 10;
    double buttonY = locationMarker.getLocation().getYCoord() + mapImageView.getLayoutY() - 24 + 10;
    Button button = newDraggableButton(buttonX, buttonY, 1);

    double labelX =
        locationMarker.getLocation().getXCoord() + mapImageView.getLayoutX() - 8 + 7.5 + 10;
    double labelY =
        locationMarker.getLocation().getYCoord() + mapImageView.getLayoutY() - 24 - 15 + 10;
    Label label =
        newDraggableLabel(
            labelX, labelY, equipment.getEquipmentID() + equipment.getEquipmentType());

    button.setStyle("-fx-background-color: RED");
    button.setShape(equipmentMarkerShape);

    EquipmentMarker equipmentMarker = new EquipmentMarker(equipment, button, label, locationMarker);
    buttonEquipmentMarker.put(button, equipmentMarker);
    return equipmentMarker;
  }

  // Makes a new Draggable Service Request button and Label
  public SRMarker newDraggableServiceRequest(SR serviceRequest, LocationMarker locationMarker) {

    double buttonX = locationMarker.getLocation().getXCoord() + mapImageView.getLayoutX() - 8;
    double buttonY = locationMarker.getLocation().getYCoord() + mapImageView.getLayoutY() - 24;
    Button button = newDraggableButton(buttonX, buttonY, 2);

    double labelX = locationMarker.getLocation().getXCoord() + mapImageView.getLayoutX() - 8 + 7.5;
    double labelY = locationMarker.getLocation().getYCoord() + mapImageView.getLayoutY() - 24 - 15;
    Label label = newDraggableLabel(labelX, labelY, "");

    switch (serviceRequest.getSrType()) {
      case EQUIPMENT:
        button.setStyle("-fx-background-color: YELLOW");
        break;
      case FLORAL_DELIVERY:
        button.setStyle("-fx-background-color: GREEN");
        break;
      case FOOD_DELIVERY:
        button.setStyle("-fx-background-color: ORANGE");
        break;
      case GIFT_DELIVERY:
        button.setStyle("-fx-background-color: ORCHID");
        break;
      case LANGUAGE:
        button.setStyle("-fx-background-color: WHEAT");
        break;
      case LAUNDRY:
        button.setStyle("-fx-background-color: MEDIUMBLUE");
        break;
      case MAINTENANCE:
        button.setStyle("-fx-background-color: MINTCREAM");
        break;
      case MEDICINE_DELIVERY:
        button.setStyle("-fx-background-color: SADDLEBROWN");
        break;
      case RELIGIOUS:
        button.setStyle("-fx-background-color: TOMATO");
        break;
      case SANITATION:
        button.setStyle("-fx-background-color: DIMGREY");
        break;
      case SECURITY:
        button.setStyle("-fx-background-color: MAROON");
        break;
    }
    button.setShape(locationMarkerShape);

    SRMarker serviceRequestMarker = new SRMarker(serviceRequest, button, label, locationMarker);
    buttonServiceRequestMarker.put(button, serviceRequestMarker);
    return serviceRequestMarker;
  }

  // Makes a new Draggable Label
  public Label newDraggableLabel(double posX, double posY, String text) {
    Label label = new Label();
    label.setText(text);
    label.setFont(new Font(15));

    label.setLayoutX(posX);
    label.setLayoutY(posY);

    return label;
  }

  // Makes a new Draggable Button
  public Button newDraggableButton(double posX, double posY, int markerType) {
    Button button = newButton(posX, posY, 2.0, 4.0);
    setDragFunctions(button, markerType);
    return button;
  }

  // Makes a new Button
  public Button newButton(double posX, double posY, double minW, double minH) {
    Button button = new Button();
    button.setMinWidth(minW);
    button.setMinHeight(minH);
    button.setLayoutX(posX);
    button.setLayoutY(posY);
    button.setPickOnBounds(false);
    return button;
  }

  // Sets drag functions
  public void setDragFunctions(Button button, int markerType) {
    final Delta dragDelta = new Delta();
    button.setOnAction(
        event -> {
          // highlight(button, selectedButton);
          // selectedButton = button;
          try {
            existingLocationSelected(buttonLocationMarker.get(button).getLocation());
            currentID = buttonLocationMarker.get(button).getLocation().getNodeID();
          } catch (Exception e) {
            System.out.println("This isn't a location :)");
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
          if (markerType == 0) {
            existingLocationSelected(buttonLocationMarker.get(button).getLocation());
          } else if (markerType == 1) {
            existingEquipmentSelected(buttonEquipmentMarker.get(button).getEquipment());
          } else {
            existingServiceRequestSelected(
                buttonServiceRequestMarker.get(button).getServiceRequest());
          }
          editButton.setDisable(false);
          deleteButton.setDisable(false);
          saveButton.setDisable(true);
          clearButton.setDisable(true);
        });
    button.setOnMouseDragged(
        mouseEvent -> {
          if (dragCheckBox.isSelected()) {
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
          }
        });
  }

  // Clears all Buttons and Labels from screen
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

  // Toggle Labels
  public void showLabels(boolean value) {
    if (locationCheckBox.isSelected()) {
      for (LocationMarker locationMarker : buttonLocationMarker.values()) {
        locationMarker.setLabelVisibility(value);
      }
    }
    if (equipmentCheckBox.isSelected()) {
      for (EquipmentMarker equipmentMarker : buttonEquipmentMarker.values()) {
        equipmentMarker.setLabelVisibility(value);
      }
    }
    if (serviceRequestCheckBox.isSelected()) {
      for (SRMarker serviceRequestMarker : buttonServiceRequestMarker.values()) {
        serviceRequestMarker.setLabelVisibility(value);
      }
    }
  }

  // Toggles Locations
  public void showLocations(boolean value) {
    for (LocationMarker locationMarker : buttonLocationMarker.values()) {
      locationMarker.setButtonVisibility(value);
      if (showTextCheckBox.isSelected()) {
        locationMarker.setLabelVisibility(value);
      }
    }
  }

  // Toggles Equipment
  public void showEquipment(boolean value) {
    for (EquipmentMarker equipmentMarker : buttonEquipmentMarker.values()) {
      equipmentMarker.setButtonVisibility(value);
      if (showTextCheckBox.isSelected()) {
        equipmentMarker.setLabelVisibility(value);
      }
    }
  }

  // Toggles Equipment
  public void showServiceRequests(boolean value) {
    for (SRMarker serviceRequestMarker : buttonServiceRequestMarker.values()) {
      serviceRequestMarker.setButtonVisibility(value);
      if (showTextCheckBox.isSelected()) {
        serviceRequestMarker.setLabelVisibility(value);
      }
    }
  }

  // New location through button
  @FXML
  public void newLocationPressed() {
    if (floor != "") newLocationPressed(0, 0);
  }

  public void newLocationPressedMouse(double x, double y) {
    newLocationPressed(
        (int) ((int) x - gesturePane.getLayoutX()), (int) ((int) y - gesturePane.getLayoutY()));
  }

  // New location through right click
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

  // Delete Location
  @FXML
  public void deleteLocation() {
    locationDAO.deleteLocationNode(nodeIDText.getText());
    String originalFloorName = floorName;
    floorSelectionComboBox.setValue("Choose Floor");
    floorSelectionComboBox.setValue(originalFloorName);
  }

  // Selected Location
  public void existingLocationSelected(Location currLocation) {
    inputVBox.setDisable(false);
    selectedLocation = currLocation;
    nodeIDText.setEditable(false);
    xPosText.setEditable(false);
    yPosText.setEditable(false);
    floorText.setEditable(false);
    buildingText.setEditable(false);
    typeText.setEditable(false);
    longnameText.setEditable(false);
    shortnameText.setEditable(false);
    nodeIDText.setText(selectedLocation.getNodeID());
    if (xPosText.getText() == null || xPosText.getText().equals("")) {
      xPosText.setText(String.valueOf(selectedLocation.getXCoord()));
      yPosText.setText(String.valueOf(selectedLocation.getYCoord()));
    }

    floorText.setText(selectedLocation.getFloor());
    buildingText.setText(selectedLocation.getBuilding());
    typeText.setText(selectedLocation.getNodeType());
    longnameText.setText(selectedLocation.getLongName());
    shortnameText.setText(selectedLocation.getShortName());
  }

  // Existing Equipment Selected
  public void existingEquipmentSelected(Equipment equipment) {
    inputVBox.setDisable(false);

    nodeIDText.setEditable(false);
    xPosText.setEditable(false);
    yPosText.setEditable(false);
    floorText.setEditable(false);
    buildingText.setEditable(false);
    typeText.setEditable(false);
    longnameText.setEditable(false);
    shortnameText.setEditable(false);

    nodeIDText.setText(equipment.getEquipmentID());
    typeText.setText(equipment.getEquipmentType());
    longnameText.setText("N/A");
    for (Location l : locations) {
      if (l.getNodeID().equals(equipment.getCurrentLocation())) {
        shortnameText.setText(l.getShortName());
        floorText.setText(l.getFloor());
        buildingText.setText(l.getBuilding());
        break;
      }
    }
  }

  // Existing Service Request Selected
  public void existingServiceRequestSelected(SR serviceRequest) {
    inputVBox.setDisable(false);

    nodeIDText.setEditable(false);
    xPosText.setEditable(false);
    yPosText.setEditable(false);
    floorText.setEditable(false);
    buildingText.setEditable(false);
    typeText.setEditable(false);
    longnameText.setEditable(false);
    shortnameText.setEditable(false);

    nodeIDText.setText(serviceRequest.getRequestID());
    typeText.setText(serviceRequest.getRequestPriority());
    longnameText.setText("N/A");

    for (Location l : locations) {
      if (l.getNodeID().equals(serviceRequest.getEndLocation())) {
        shortnameText.setText(l.getShortName());
        floorText.setText(l.getFloor());
        buildingText.setText(l.getBuilding());
        break;
      }
    }
  }

  // Save Changes
  public void saveChanges() {
    if (newLocationMarker != null && newLocationMarker.getLocation().equals(selectedLocation)) {
      newLocationMarker.getLocation().setNodeID(nodeIDText.getText());
      newLocationMarker.getLocation().setXCoord((int) Double.parseDouble(xPosText.getText()));
      newLocationMarker.getLocation().setYCoord((int) Double.parseDouble(yPosText.getText()));
      newLocationMarker.getLocation().setFloor(floorText.getText());
      newLocationMarker.getLocation().setBuilding(buildingText.getText());
      newLocationMarker.getLocation().setNodeType(typeText.getText());
      newLocationMarker.getLocation().setLongName(longnameText.getText());
      newLocationMarker.getLocation().setShortName(shortnameText.getText());

      locationDAO.enterLocationNode(newLocationMarker.getLocation());
      newLocationMarker = null;
      clearSubmission();
      String originalFloorName = floorName;
      floorSelectionComboBox.setValue("Choose Floor");
      floorSelectionComboBox.setValue(originalFloorName);
      System.out.println("here");
      return;
    }

    locationDAO.updateLocation(
        nodeIDText.getText(), "xCoord", (int) Double.parseDouble(xPosText.getText()));
    locationDAO.updateLocation(
        nodeIDText.getText(), "yCoord", (int) Double.parseDouble(yPosText.getText()));
    locationDAO.updateLocation(nodeIDText.getText(), "floor", floorText.getText());
    locationDAO.updateLocation(nodeIDText.getText(), "building", buildingText.getText());
    locationDAO.updateLocation(nodeIDText.getText(), "nodeType", typeText.getText());
    locationDAO.updateLocation(nodeIDText.getText(), "longName", longnameText.getText());
    locationDAO.updateLocation(nodeIDText.getText(), "ShortName", shortnameText.getText());

    clearSubmission();
    String originalFloorName = floorName;
    floorSelectionComboBox.setValue("Choose Floor");
    floorSelectionComboBox.setValue(originalFloorName);
  }

  // Edit Location
  public void editLocation() {
    editButton.setDisable(true);
    saveButton.setDisable(false);
    clearButton.setDisable(false);
    deleteButton.setDisable(false);

    nodeIDText.setEditable(true);
    xPosText.setEditable(true);
    yPosText.setEditable(true);
    floorText.setEditable(true);
    buildingText.setEditable(true);
    typeText.setEditable(true);
    longnameText.setEditable(true);
    shortnameText.setEditable(true);
  }

  // Clear Submission
  public void clearSubmission() {
    nodeIDText.setText("");
    xPosText.setText("");
    yPosText.setText("");
    floorText.setText("");
    buildingText.setText("");
    typeText.setText("");
    longnameText.setText("");
    shortnameText.setText("");
  }

  // Go to Location Table
  @FXML
  public void goToLocationTable() throws IOException {
    HomeCtrl.sceneFlag = 2;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  @FXML
  public void returnToSelectServiceScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
    locations.addAll(new ArrayList<>(new LocationDerbyImpl().getNodeList()));
  }

  // records relative x and y co-ordinates.
  private class Delta {
    double mouseX, mouseY, buttonX, buttonY;
  }

  private void highlight(Button button, Button oldButton) {
    button.setStyle("-fx-background-color: RED");
    oldButton.getStyleClass().clear();
    oldButton.setStyle(null);
    oldButton.setStyle("-fx-background-color: #78aaf0");
  }

  public HashMap<Location, HashSet<Edge>> getEdges() throws IOException, ParseException {
    HashMap<String, Location> map = new HashMap<>();
    HashMap<Location, HashSet<Edge>> resultMap = new HashMap<>();

    for (Location l : locations) {
      map.put(l.getNodeID(), l);
      resultMap.put(l, new HashSet<>());
    }

    InputStream path = App.class.getResourceAsStream("db/CSVs/AllEdgesHand.csv");
    // File file = new File(path);
    //    File file = new
    // File("src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/AllEdgesHand.csv");
    Scanner lineScanner = new Scanner(path);
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;

    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      String edge1 = "";
      String edge2 = "";
      boolean isTaxiCab = false;
      boolean isTop = false;

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) edge1 = data;
        else if (dataIndex == 1) edge2 = data;
        else if (dataIndex == 2) isTaxiCab = data.equals("TRUE") ? true : false;
        else if (dataIndex == 3) isTop = (isTaxiCab && data.equals("TRUE")) ? true : false;
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      if (map.get(edge1) == null || map.get(edge2) == null) {
        continue;
      }

      resultMap.get(map.get(edge1)).add(new Edge(map.get(edge1), map.get(edge2), isTaxiCab, isTop));
      resultMap.get(map.get(edge2)).add(new Edge(map.get(edge2), map.get(edge1), isTaxiCab, isTop));
    }

    lineIndex++;
    lineScanner.close();
    return resultMap;
  }

  public HashMap<Location, Integer> dijkstra(
      List<Location> allLocations, Location here, HashMap<Location, HashSet<Edge>> neighborMap) {
    HashMap<Location, Integer> minDistances = new HashMap<>();
    for (Location l : allLocations) {
      minDistances.put(l, Integer.MAX_VALUE);
    }

    Comparator<Location> c =
        (Location a, Location b) -> (int) (minDistances.get(a) - minDistances.get(b));
    PriorityQueue<Location> priorityQueue = new PriorityQueue<>(c);
    priorityQueue.add(here);
    minDistances.put(here, 0);

    while (!priorityQueue.isEmpty()) {
      Location current = priorityQueue.poll();
      for (Edge e : neighborMap.get(current)) {
        if (minDistances.get(current) + e.getWeight() < minDistances.get(e.getEnd())) {
          minDistances.put(e.getEnd(), (int) (minDistances.get(current) + e.getWeight()));
          priorityQueue.remove(e.getEnd());
          priorityQueue.add(e.getEnd());
        }
      }
    }
    return minDistances;
  }

  public List<Location> getPath(
      Location end,
      HashMap<Location, HashSet<Edge>> neighborMap,
      Map<Location, Integer> minDistances) {
    List<Location> path = new ArrayList<>();
    int lowestDist = Integer.MAX_VALUE;
    Location current = end;
    while (lowestDist > 0) {
      path.add(current);
      //      if (neighborMap.get(current) == null) {
      //        continue;
      //      }
      for (Edge e : neighborMap.get(current)) {
        if (lowestDist > minDistances.get(e.getEnd())) {
          current = e.getEnd();
          lowestDist = minDistances.get(e.getEnd());
        }
      }
    }
    return path;
  }

  @FXML
  public void findPath() throws IOException, ParseException {
    String shortNameFrom = pfFromComboBox.getSelectionModel().getSelectedItem().toString();
    String shortNameTo = pfToComboBox.getSelectionModel().getSelectedItem().toString();

    Location start = null;
    Location end = null;
    for (Location l : locations) {
      if (l.getShortName().equals(shortNameFrom)) {
        start = l;
      }
      if (l.getShortName().equals(shortNameTo)) {
        end = l;
      }
      if (start != null && end != null) {
        break;
      }
    }

    Map<Location, Integer> minDistances = dijkstra(locations, start, neighborMap);
    List<Location> path = getPath(end, neighborMap, minDistances);
    Location prev = path.get(0);

    int offsetX = 4;
    int offsetY = 6;
    for (int i = 1; i < path.size(); i++) {
      Line line =
          new Line(
              prev.getXCoord() + offsetX,
              prev.getYCoord() + offsetY,
              path.get(i).getXCoord() + offsetX,
              path.get(i).getYCoord() + offsetY);
      line.setStroke(Color.RED);
      line.setVisible(true);
      line.setStrokeWidth(4);
      miniAnchorPane.getChildren().add(line);
      prev = path.get(i);
      pfLine.add(line);
    }

    Line line =
        new Line(
            prev.getXCoord() + offsetX,
            prev.getYCoord() + offsetY,
            start.getXCoord() + offsetX,
            start.getYCoord() + offsetY);
    line.setStroke(Color.RED);
    line.setStrokeWidth(4);
    miniAnchorPane.getChildren().add(line);
    pfLine.add(line);
  }

  public void clearPath(ActionEvent actionEvent) {
    for (Line line : pfLine) {
      miniAnchorPane.getChildren().remove(line);
    }
    pfLine.clear();
  }
}
