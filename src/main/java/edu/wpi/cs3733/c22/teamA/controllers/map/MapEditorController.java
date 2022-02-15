package edu.wpi.cs3733.c22.teamA.controllers.map;

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
import edu.wpi.cs3733.c22.teamA.entities.map.EquipmentMarker;
import edu.wpi.cs3733.c22.teamA.entities.map.LocationMarker;
import edu.wpi.cs3733.c22.teamA.entities.map.SRMarker;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.EquipmentSR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

// TODO Add Service Request marker to all necessary places
public class MapEditorController {
  @FXML private JFXCheckBox locationCheckBox;
  @FXML private JFXCheckBox dragCheckBox;
  @FXML private JFXCheckBox equipmentCheckBox;
  @FXML private JFXCheckBox showTextCheckBox;
  @FXML private JFXButton deleteButton;
  @FXML private ComboBox floorSelectionComboBox;
  // @FXML private JFXComboBox testComboBox;
  @FXML private AnchorPane anchorPane;

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

  @FXML private JFXButton backButton = new JFXButton();

  @FXML private VBox inputVBox = new VBox();
  @FXML private ImageView mapImageView = new ImageView();

  private List<Location> locations;
  private List<Equipment> equipments;
  private List<SR> serviceRequests;
  private Polygon locationMarkerShape;
  private Polygon equipmentMarkerShape;
  private Polygon serviceRequestMarkerShape;

  String floor;
  String currentID;
  Location selectedLocation;

  LocationDAO locationDAO = new LocationDerbyImpl();
  EquipmentDAO equipmentDAO = new EquipmentDerbyImpl();

  HashMap<Button, LocationMarker> buttonLocationMarker;
  HashMap<Button, EquipmentMarker> buttonEquipmentMarker;
  HashMap<Button, SRMarker> buttonServiceRequestMarker;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  public MapEditorController() {
    locationMarkerShape = new Polygon();
    equipmentMarkerShape = new Polygon();
    serviceRequestMarkerShape = new Polygon();
    locationMarkerShape.getPoints().addAll(new Double[] {1.0, 4.0, 0.0, 2.0, 1.0, 0.0, 2.0, 2.0});
    equipmentMarkerShape.getPoints().addAll(new Double[] {0.0, 0.0, 0.0, 1.0, 4.0, 1.0, 4.0, 0.0});
    serviceRequestMarkerShape
        .getPoints()
        .addAll(
            new Double[] {
              0.0, 0.0, 0.0, 1.0, 4.0, 1.0, 4.0, 0.0
            }); // TODO Modify shape to be different

    // testComboBox.setEditable(true);
    // String test = testComboBox.get
    buttonLocationMarker = new HashMap<>();
    buttonEquipmentMarker = new HashMap<>();
    buttonServiceRequestMarker = new HashMap<>();

    locations = new ArrayList<>();
    equipments = new ArrayList<>();
    serviceRequests = new ArrayList<>();

    currentID = "";
    floor = "";
  }

  @FXML
  public void initialize() {

    // Setup Functions
    setupCheckboxListeners();
    setInitialUIStates();
    fillFromDB();
    setupSearchListener();

    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

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

              // Sets up floor on Map
              setupFloor(newValue.toString());
              clearAll();

              // Maps Location IDs to their markers
              HashMap<String, LocationMarker> locationIDs = new HashMap<>();

              // Loops through every location & draws them if present on the floor
              for (Location location : locations) {
                if (location.getFloor().equals(floor)) {
                  LocationMarker locationMarker = newDraggableLocation(location);
                  locationMarker.draw(anchorPane);
                  locationIDs.put(location.getNodeID(), locationMarker);
                }
              }

              // Loops through every medical equipment & draws them if they're on this floor
              for (Equipment equipment : equipments) {
                if (locationIDs.containsKey(equipment.getCurrentLocation())) {
                  EquipmentMarker equipmentMarker =
                      newDraggableEquipment(
                          equipment, locationIDs.get(equipment.getCurrentLocation()));
                  equipmentMarker.draw(anchorPane);
                }
              }

              // Loops through every service request & draws them if they're on this floor
              for (SR serviceRequest : serviceRequests) {
                if (locationIDs.containsKey(serviceRequest.getEndLocation())) {
                  SRMarker serviceRequestMarker =
                      newDraggableServiceRequest(
                          serviceRequest, locationIDs.get(serviceRequest.getEndLocation()));
                  serviceRequestMarker.draw(anchorPane);
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

    showTextCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showLabels(new_val);
            });
  }

  // Sets up UI states of text areas, and buttons
  public void setInitialUIStates() {
    mapImageView.setVisible(false);

    editButton.setDisable(true);
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    deleteButton.setDisable(true);

    dragCheckBox.setSelected(true);
    showTextCheckBox.setSelected(true);
    equipmentCheckBox.setSelected(true);
    locationCheckBox.setSelected(true);

    inputVBox.setDisable(true);
  }

  // Fills info from DB
  public void fillFromDB() {
    locations.addAll(new ArrayList<>(new LocationDerbyImpl().getNodeList()));
    equipments.addAll(new ArrayList<>(new EquipmentDerbyImpl().getMedicalEquipmentList()));
    // TODO add other types, currently just medical equipment requests
    serviceRequests.addAll(
        new ArrayList<>(new ServiceRequestDerbyImpl<>(new EquipmentSR()).getServiceRequestList()));
  }

  // Set up searchbar for LOCATIONS ONLY
  // TODO IMPLEMENT CHOICE OF LOCATION, EQUIPMENT, SR SEARCH (another button?)
  // TODO MAKE IT LIVE UPDATE ON KEYSTROKE
  public void setupSearchListener() {
    searchComboBox.setEditable(true);
    searchComboBox.setVisibleRowCount(5);
    // set up list of locations to be wrapped
    ObservableList<Location> searchLocationList = FXCollections.observableArrayList();
    searchLocationList.addAll(locations);

    // create filtered list, can be filtered (duh)
    FilteredList<Location> filteredLocations = new FilteredList<>(searchLocationList, p -> true);
    // add listener that checks whenever changes are made to JFXText searchText
    searchComboBox
        .getSelectionModel()
        .selectedItemProperty()
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
                    String lowerCaseFilter = newValue.toString().toLowerCase();
                    // if search matches either name or ID, display it
                    // if not, this returns false and doesnt display
                    return (location.getLongName().toLowerCase().contains(lowerCaseFilter)
                            || location.getShortName().toLowerCase().contains(lowerCaseFilter)
                            || location.getNodeID().toLowerCase().contains(lowerCaseFilter))
                        && location.getFloor().equals(floor);
                  });
              ArrayList<String> locationNames = new ArrayList<>();
              for (Location l : filteredLocations) {
                locationNames.add(l.getLongName());
              }
              searchComboBox.getItems().clear();
              searchComboBox.getItems().addAll(locationNames);
              clearAll();

              HashMap<String, LocationMarker> locationIDs = new HashMap<>();
              // Loops through every location filtered & draws them if present on the floor
              for (Location location : locations) {
                if (filteredLocations.contains(location)) {
                  LocationMarker locationMarker = newDraggableLocation(location);
                  locationMarker.draw(anchorPane);
                  locationIDs.put(location.getNodeID(), locationMarker);
                }
              }
            });
  }

  // Sets up the floor
  public void setupFloor(String newValue) {
    if (newValue.equals("Choose Floor:")) {
      floor = "";
      mapImageView.setVisible(false);
    } else {
      if (newValue.equals("Floor 1")) {
        mapImageView.setVisible(true);
        floor = "1";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/1st Floor.png");
        Image image = new Image(String.valueOf((map)));
        mapImageView.setImage(image);
      } else if (newValue.equals("Floor 2")) {
        mapImageView.setVisible(true);
        floor = "2";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/2nd Floor.png");
        Image image = new Image(String.valueOf((map)));
        mapImageView.setImage(image);
      } else if (newValue.equals("Floor 3")) {
        mapImageView.setVisible(true);
        floor = "3";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/3rd Floor.png");
        Image image = new Image(String.valueOf((map)));
        mapImageView.setImage(image);
      } else if (newValue.equals("L1")) {
        mapImageView.setVisible(true);
        floor = "L1";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/LL1.png");
        Image image = new Image(String.valueOf((map)));
        mapImageView.setImage(image);
      } else {
        mapImageView.setVisible(true);
        floor = "L2";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/LL2.png");
        Image image = new Image(String.valueOf((map)));
        mapImageView.setImage(image);
      }
    }
  }

  // Makes a new Draggable Location Button and Label
  public LocationMarker newDraggableLocation(Location location) {
    double buttonX = location.getXCoord() + mapImageView.getLayoutX() - 8;
    double buttonY = location.getYCoord() + mapImageView.getLayoutY() - 24;
    Button button = newDraggableButton(buttonX, buttonY, true);

    button.setPickOnBounds(false);
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
    Button button = newDraggableButton(buttonX, buttonY, false);

    double labelX =
        locationMarker.getLocation().getXCoord() + mapImageView.getLayoutX() - 8 + 7.5 + 10;
    double labelY =
        locationMarker.getLocation().getYCoord() + mapImageView.getLayoutY() - 24 - 15 + 10;
    Label label =
        newDraggableLabel(
            labelX, labelY, equipment.getEquipmentID() + equipment.getEquipmentType());

    button.setPickOnBounds(false);
    button.setStyle("-fx-background-color: RED");
    button.setShape(equipmentMarkerShape);

    EquipmentMarker equipmentMarker = new EquipmentMarker(equipment, button, label, locationMarker);
    buttonEquipmentMarker.put(button, equipmentMarker);
    return equipmentMarker;
  }

  // Makes a new Draggable Service Request button and Label
  public SRMarker newDraggableServiceRequest(SR serviceRequest, LocationMarker locationMarker) {

    double buttonX = locationMarker.getLocation().getXCoord() + mapImageView.getLayoutX() - 8 + 10;
    double buttonY = locationMarker.getLocation().getYCoord() + mapImageView.getLayoutY() - 24 + 10;
    Button button = newDraggableButton(buttonX, buttonY, false);

    double labelX =
        locationMarker.getLocation().getXCoord() + mapImageView.getLayoutX() - 8 + 7.5 + 10;
    double labelY =
        locationMarker.getLocation().getYCoord() + mapImageView.getLayoutY() - 24 - 15 + 10;
    Label label =
        newDraggableLabel(
            labelX, labelY, serviceRequest.getRequestID() + serviceRequest.getRequestType());

    button.setPickOnBounds(false);
    // TODO Wait for refactoring of database before implementing
    switch (serviceRequest.getRequestType()) {
      default:
        button.setStyle("-fx-background-color: RED");
        break;
    }
    button.setShape(serviceRequestMarkerShape);

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
  public Button newDraggableButton(double posX, double posY, boolean isLocationMarker) {
    Button button = new Button();
    button.setMinWidth(4.0);
    button.setMinHeight(2.0);
    button.setLayoutX(posX);
    button.setLayoutY(posY);
    button.setPickOnBounds(false);
    setDragFunctions(button, isLocationMarker);
    return button;
  }

  // Sets drag functions
  public void setDragFunctions(Button button, boolean isLocationMarker) {
    final Delta dragDelta = new Delta();
    button.setOnAction(
        event -> {
          // highlight(button, selectedButton);
          // selectedButton = button;
          existingLocationSelected(buttonLocationMarker.get(button).getLocation());
          currentID = buttonLocationMarker.get(button).getLocation().getNodeID();
        });
    button.setOnMousePressed(
        mouseEvent -> {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = button.getLayoutX() - mouseEvent.getSceneX();
          dragDelta.y = button.getLayoutY() - mouseEvent.getSceneY();
          button.setCursor(Cursor.MOVE);
          if (isLocationMarker) {
            existingLocationSelected(buttonLocationMarker.get(button).getLocation());
          } else {
            existingEquipmentSelected(buttonEquipmentMarker.get(button).getEquipment());
          }
          editButton.setDisable(false);
          deleteButton.setDisable(false);
          saveButton.setDisable(true);
          clearButton.setDisable(true);
        });
    button.setOnMouseReleased(mouseEvent -> button.setCursor(Cursor.HAND));
    button.setOnMouseDragged(
        mouseEvent -> {
          if (dragCheckBox.isSelected()) {
            button.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
            button.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
            xPosText.setText(String.valueOf(button.getLayoutX() - mapImageView.getLayoutX() + 8));
            yPosText.setText(String.valueOf(button.getLayoutY() - mapImageView.getLayoutY() + 24));
            Label correspondingLabel;
            if (isLocationMarker) {
              correspondingLabel = buttonLocationMarker.get(button).getLabel();
            } else {
              correspondingLabel = buttonEquipmentMarker.get(button).getLabel();
            }
            correspondingLabel.setLayoutX(mouseEvent.getSceneX() + dragDelta.x + 8);
            correspondingLabel.setLayoutY(mouseEvent.getSceneY() + dragDelta.y - 24);
          }
        });
    button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));
  }

  // Clears all Buttons and Labels from screen
  public void clearAll() {
    for (LocationMarker locationMarker : buttonLocationMarker.values()) {
      locationMarker.clear(anchorPane);
    }
    for (EquipmentMarker equipmentMarker : buttonEquipmentMarker.values()) {
      equipmentMarker.clear(anchorPane);
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

  // New Location
  @FXML
  public void newLocationPressed() {
    Location newLocation =
        new Location(
            "NEWLOCATION", 0, 0, floor, "Tower", "NODE TYPE TODO", "New Location", "New Location");
    LocationMarker newLocationMarker = newDraggableLocation(newLocation);
    newLocationMarker.draw(this.anchorPane);
  }

  // Delete Location
  @FXML
  public void deleteLocation() {
    locationDAO.deleteLocationNode(nodeIDText.getText());
    this.initialize();
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
    floorText.setText("");
    buildingText.setText("");
    typeText.setText(equipment.getEquipmentType());
    longnameText.setText("N/A");
    shortnameText.setText(equipment.getCurrentLocation());
  }

  // Save Changes
  public void saveChanges() {

    locationDAO.deleteLocationNode(currentID);
    Location l =
        new Location(
            nodeIDText.getText(),
            Integer.parseInt(xPosText.getText()),
            Integer.parseInt(yPosText.getText()),
            buildingText.getText(),
            floorText.getText(),
            typeText.getText(),
            longnameText.getText(),
            shortnameText.getText());
    locationDAO.enterLocationNode(
        l.getNodeID(),
        l.getXCoord(),
        l.getYCoord(),
        l.getFloor(),
        l.getBuilding(),
        l.getNodeType(),
        l.getLongName(),
        l.getShortName());
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
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.VIEW_LOCATIONS);
  }

  // Return to Home Screen
  @FXML
  public void returnToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  @FXML
  public void returnToSelectServiceScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
    locations.addAll(new ArrayList<>(new LocationDerbyImpl().getNodeList()));
  }

  // records relative x and y co-ordinates.
  private class Delta {
    double x, y;
  }

  private void highlight(Button button, Button oldButton) {
    button.setStyle("-fx-background-color: RED");
    oldButton.getStyleClass().clear();
    oldButton.setStyle(null);
    oldButton.setStyle("-fx-background-color: #78aaf0");
  }
}
