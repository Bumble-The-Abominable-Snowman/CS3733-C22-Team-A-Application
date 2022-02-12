package edu.wpi.cs3733.c22.teamA.controllers.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.map.EquipmentMarker;
import edu.wpi.cs3733.c22.teamA.entities.map.LocationMarker;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.value.ObservableValue;
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

public class MapEditorController {
  @FXML private JFXCheckBox locationToggle;
  @FXML private JFXCheckBox dragToggleBox;
  @FXML private JFXCheckBox equipToggleBox;
  @FXML private JFXCheckBox textToggleBox;
  @FXML private JFXButton deleteButton;
  @FXML private ComboBox floorSelectionComboBox;
  @FXML private AnchorPane anchorPane;

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
  @FXML private ImageView mapDisplay = new ImageView();

  private List<Location> locations;
  private List<Equipment> equipments;
  private Polygon locationMarkerShape;
  private Polygon equipmentMarkerShape;

  String floor = "";

  LocationDAO locationDAO = new LocationDerbyImpl();
  EquipmentDAO equipmentDAO = new EquipmentDerbyImpl();

  Location selectedLocation;
  HashMap<Button, Location> buttonLocation;
  HashMap<Button, LocationMarker> buttonLocationMarker;
  HashMap<Button, EquipmentMarker> buttonEquipmentMarker;
  ArrayList<Label> labels;
  ArrayList<Button> medicalButtons;
  String currentID;

  private final SceneSwitcher sceneSwitcher = Aapp.sceneSwitcher;

  public MapEditorController() {
    locationMarkerShape = new Polygon();
    equipmentMarkerShape = new Polygon();
    locationMarkerShape.getPoints().addAll(new Double[] {1.0, 4.0, 0.0, 2.0, 1.0, 0.0, 2.0, 2.0});
    equipmentMarkerShape.getPoints().addAll(new Double[] {0.0, 0.0, 0.0, 1.0, 4.0, 1.0, 4.0, 0.0});

    buttonLocation = new HashMap<>();
    buttonLocationMarker = new HashMap<>();
    buttonEquipmentMarker = new HashMap<>();

    labels = new ArrayList<>();
    medicalButtons = new ArrayList<>();
    locations = new ArrayList<>();
    equipments = new ArrayList<>();

    currentID = "";
  }

  @FXML
  public void initialize() {
    setupCheckboxListeners();
    setInitialUIStates();
    fillFromDB();

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
              setupFloor(newValue.toString());

              HashMap<String, LocationMarker> locationIDs = new HashMap<>();

              for (Location location : locations) {
                if (location.getFloor().equals(floor)) {
                  LocationMarker locationMarker = newDraggableLocation(location);
                  locationMarker.draw(anchorPane);
                  locationIDs.put(location.getNodeID(), locationMarker);
                }
              }

              for (Equipment equipment : equipments) {
                if (locationIDs.containsKey(equipment.getCurrentLocation())) {
                  EquipmentMarker equipmentMarker =
                      newDraggableEquipment(
                          equipment, locationIDs.get(equipment.getCurrentLocation()));
                  equipmentMarker.draw(anchorPane);
                }
              }
            });
  }

  public void setupCheckboxListeners() {
    equipToggleBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showMedicalButtons(new_val);
            });

    locationToggle
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showButtons(new_val);
            });

    textToggleBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              drawLabels(new_val);
            });
  }

  public void setInitialUIStates() {
    mapDisplay.setVisible(false);

    editButton.setDisable(true);
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    deleteButton.setDisable(true);

    dragToggleBox.setSelected(true);
    textToggleBox.setSelected(true);
    equipToggleBox.setSelected(true);
    locationToggle.setSelected(true);

    inputVBox.setDisable(true);
  }

  public void fillFromDB() {
    locations.addAll(new ArrayList<>(new LocationDerbyImpl().getNodeList()));
    equipments.addAll(new ArrayList<>(new EquipmentDerbyImpl().getMedicalEquipmentList()));
  }

  public void setupFloor(String newValue) {
    if (newValue.equals("Choose Floor:")) {
      floor = "";
      mapDisplay.setVisible(false);
    } else {
      if (newValue.equals("Floor 1")) {
        mapDisplay.setVisible(true);
        floor = "1";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/1st Floor.png");
        Image image = new Image(String.valueOf((map)));
        mapDisplay.setImage(image);
      } else if (newValue.equals("Floor 2")) {
        mapDisplay.setVisible(true);
        floor = "2";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/2nd Floor.png");
        Image image = new Image(String.valueOf((map)));
        mapDisplay.setImage(image);
      } else if (newValue.equals("Floor 3")) {
        mapDisplay.setVisible(true);
        floor = "3";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/3rd Floor.png");
        Image image = new Image(String.valueOf((map)));
        mapDisplay.setImage(image);
      } else if (newValue.equals("L1")) {
        mapDisplay.setVisible(true);
        floor = "L1";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/LL1.png");
        Image image = new Image(String.valueOf((map)));
        mapDisplay.setImage(image);
      } else {
        mapDisplay.setVisible(true);
        floor = "L2";
        File map = new File("edu/wpi/cs3733/c22/teamA/images/LL2.png");
        Image image = new Image(String.valueOf((map)));
        mapDisplay.setImage(image);
      }
    }
  }

  public LocationMarker newDraggableLocation(Location location) {
    double buttonX = location.getXCoord() + mapDisplay.getLayoutX() - 8;
    double buttonY = location.getYCoord() + mapDisplay.getLayoutY() - 24;
    Button button = newDraggableButton(buttonX, buttonY, true);

    button.setPickOnBounds(false);
    button.setStyle("-fx-background-color: #78aaf0");
    button.setShape(locationMarkerShape);

    double labelX = location.getXCoord() + mapDisplay.getLayoutX() - 8 + 7.5;
    double labelY = location.getYCoord() + mapDisplay.getLayoutY() - 24 - 15;
    Label label = newDraggableLabel(labelX, labelY, location.getShortName());

    LocationMarker locationMarker = new LocationMarker(button, label, location);
    buttonLocationMarker.put(button, locationMarker);
    return locationMarker;
  }

  public EquipmentMarker newDraggableEquipment(Equipment equipment, LocationMarker locationMarker) {

    double buttonX = locationMarker.getLocation().getXCoord() + mapDisplay.getLayoutX() - 8 + 10;
    double buttonY = locationMarker.getLocation().getYCoord() + mapDisplay.getLayoutY() - 24 + 10;
    Button button = newDraggableButton(buttonX, buttonY, false);

    double labelX =
        locationMarker.getLocation().getXCoord() + mapDisplay.getLayoutX() - 8 + 7.5 + 10;
    double labelY =
        locationMarker.getLocation().getYCoord() + mapDisplay.getLayoutY() - 24 - 15 + 10;
    Label label = newDraggableLabel(labelX, labelY, locationMarker.getLocation().getShortName());

    button.setPickOnBounds(false);
    button.setStyle("-fx-background-color: RED");
    button.setShape(equipmentMarkerShape);

    EquipmentMarker equipmentMarker = new EquipmentMarker(equipment, button, label, locationMarker);
    buttonEquipmentMarker.put(button, equipmentMarker);
    return equipmentMarker;
  }

  public Label newDraggableLabel(double posX, double posY, String text) {
    Label label = new Label();
    label.setText(text);
    label.setFont(new Font(15));

    label.setLayoutX(posX);
    label.setLayoutY(posY);

    return label;
  }

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

  public void setDragFunctions(Button button, boolean isLocationMarker) {
    final Delta dragDelta = new Delta();
    button.setOnAction(
        event -> {
          // highlight(button, selectedButton);
          // selectedButton = button;
          existingLocSelected(buttonLocation.get(button));
          currentID = buttonLocation.get(button).getNodeID();
        });
    button.setOnMousePressed(
        mouseEvent -> {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = button.getLayoutX() - mouseEvent.getSceneX();
          dragDelta.y = button.getLayoutY() - mouseEvent.getSceneY();
          button.setCursor(Cursor.MOVE);
          if (isLocationMarker) {
            existingLocSelected(buttonLocationMarker.get(button).getLocation());
          } else {
            // ExistingMedicalEquipment
          }
          editButton.setDisable(false);
          deleteButton.setDisable(false);
          saveButton.setDisable(true);
          clearButton.setDisable(true);
        });
    button.setOnMouseReleased(mouseEvent -> button.setCursor(Cursor.HAND));
    button.setOnMouseDragged(
        mouseEvent -> {
          if (dragToggleBox.isSelected()) {
            button.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
            button.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
            xPosText.setText(String.valueOf(button.getLayoutX() - mapDisplay.getLayoutX() + 8));
            yPosText.setText(String.valueOf(button.getLayoutY() - mapDisplay.getLayoutY() + 24));
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

  public void clearButtonsLabel() {
    for (Button b : buttonLocation.keySet()) {
      anchorPane.getChildren().remove(b);
    }
    for (Label l : labels) {
      anchorPane.getChildren().remove(l);
    }
    labels.clear();
  }

  public void drawLabels(boolean value) {
    for (Label l : labels) {
      l.setVisible(value);
    }
  }

  public void showButtons(boolean value) {
    for (Button b : buttonLocation.keySet()) {
      b.setVisible(value);
    }
  }

  public void showMedicalButtons(boolean value) {
    for (Button b : medicalButtons) {
      b.setVisible(value);
    }
  }

  @FXML
  public void newLocPressed() {
    Location newLocation =
        new Location("NEWLOCATION", 0, 0, floor, "Tower", "NODE TYPE TODO", "", "");
    LocationMarker newLocationMarker = newDraggableLocation(newLocation);
    newLocationMarker.draw(this.anchorPane);
  }

  @FXML
  public void deleteLocation() {
    locationDAO.deleteLocationNode(nodeIDText.getText());
    this.initialize();
  }

  // as long as each diamond on the map is assigned a whole
  // location object, this function works fine. if each diamond is assigned a location
  // NodeID to save space or smth, can easily be refactored
  public void existingLocSelected(Location currLocation) {
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

  @FXML
  public void goToLocationTable() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.VIEW_LOCATION_DATA_SCENE);
  }

  @FXML
  public void returnToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME_SCENE);
  }

  @FXML
  public void returnToSelectServiceScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME_SCENE);
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
