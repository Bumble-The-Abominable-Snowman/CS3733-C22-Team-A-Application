package edu.wpi.cs3733.c22.teamA.controllers.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.MedicalEquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.MedicalEquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.MedicalEquipment;
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
  private List<Button> locationMarkers;
  private List<Location> locations;
  private List<MedicalEquipment> medicalEquipments;
  private Polygon locationMarkerShape = new Polygon();
  private Polygon equipmentMarkerShape = new Polygon();
  private Button selectedButton;

  @FXML JFXTextArea nodeIDText = new JFXTextArea();
  @FXML JFXTextArea xPosText = new JFXTextArea();
  @FXML JFXTextArea yPosText = new JFXTextArea();
  @FXML JFXTextArea floorText = new JFXTextArea();
  @FXML JFXTextArea buildingText = new JFXTextArea();
  @FXML JFXTextArea typeText = new JFXTextArea();
  @FXML JFXTextArea longnameText = new JFXTextArea();
  @FXML JFXTextArea shortnameText = new JFXTextArea();

  @FXML JFXButton newLocButton = new JFXButton();
  @FXML JFXButton viewTableButton = new JFXButton();

  @FXML JFXButton saveButton = new JFXButton();
  @FXML JFXButton editButton = new JFXButton();
  @FXML JFXButton clearButton = new JFXButton();

  @FXML JFXButton backButton = new JFXButton();
  @FXML JFXButton returnHomeButton = new JFXButton();

  @FXML VBox inputVBox = new VBox();
  @FXML ImageView mapDisplay = new ImageView();

  LocationDAO locationBase = new LocationDerbyImpl();
  MedicalEquipmentDAO medicalEquipmentDao = new MedicalEquipmentDerbyImpl();
  Location selectedLocation;
  HashMap<Button, Location> buttonLocation = new HashMap<>();
  HashMap<Button, Label> buttonLabel = new HashMap<>();
  HashMap<Button, MedicalEquipment> buttonEquipment = new HashMap<>();
  ArrayList<Label> labels = new ArrayList<>();
  ArrayList<Button> medicalButtons = new ArrayList<>();
  String currentID = "";

  private final SceneController sceneController = Aapp.sceneController;

  public MapEditorController() {
    locationMarkers = new ArrayList<>();
    locations = new ArrayList<>();
    medicalEquipments = new ArrayList<>();
    // locationMarkerShape.getPoints().addAll(new Double[] {2.0, 8.0, 0.0, 4.0, 2.0, 0.0, 4.0,
    // 4.0});
    locationMarkerShape.getPoints().addAll(new Double[] {1.0, 4.0, 0.0, 2.0, 1.0, 0.0, 2.0, 2.0});
    equipmentMarkerShape.getPoints().addAll(new Double[] {0.0, 0.0, 0.0, 1.0, 4.0, 1.0, 4.0, 0.0});
  }

  @FXML
  public void initialize() {
    mapDisplay.setVisible(false);
    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    editButton.setDisable(true);
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    deleteButton.setDisable(true);

    Polygon polygon = new Polygon();
    polygon.getPoints().addAll(new Double[] {1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0});
    inputVBox.setDisable(true);
    locations.addAll(new ArrayList<>(new LocationDerbyImpl().getNodeList()));
    medicalEquipments.addAll(
        new ArrayList<>(new MedicalEquipmentDerbyImpl().getMedicalEquipmentList()));
    for (MedicalEquipment m : medicalEquipments) {
      System.out.println(m.getCurrentLocation());
    }

    dragToggleBox.setSelected(true);
    textToggleBox.setSelected(true);
    equipToggleBox.setSelected(true);
    locationToggle.setSelected(true);

    floorSelectionComboBox.getItems().removeAll(floorSelectionComboBox.getItems());
    floorSelectionComboBox
        .getItems()
        .addAll("Choose Floor:", "Floor 1", "Floor 2", "Floor 3", "L1", "L2");

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
    floorSelectionComboBox.getSelectionModel().select("Choose Floor:");
    floorSelectionComboBox
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              clearButtonsLabel();
              if (newValue.equals("Choose Floor:")) {
                mapDisplay.setVisible(false);
              } else {
                String floor;
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

                // anchorPane.getChildren().clear();
                // anchorPane.getChildren().add(floorSelectionComboBox);
                HashMap<String, Location> roomNames = new HashMap<>();

                for (Location location : locations) {
                  roomNames.put(location.getNodeID(), location);
                  int count = 0;
                  if (location.getFloor().equals(floor)) {
                    Button button = new Button();
                    Label label = new Label();
                    labels.add(label);
                    label.setText(location.getShortName());
                    label.setFont(new Font(15));
                    final Delta dragDelta = new Delta();
                    button.setMinWidth(4.0);
                    button.setMinHeight(2.0);
                    button.setShape(locationMarkerShape);
                    button.setLayoutX(location.getXCoord() + mapDisplay.getLayoutX() - 8);
                    button.setLayoutY(location.getYCoord() + mapDisplay.getLayoutY() - 24);
                    if (count % 2 == 0) {
                      label.setLayoutX(location.getXCoord() + mapDisplay.getLayoutX() - 8 + 7.5);
                      label.setLayoutY(location.getYCoord() + mapDisplay.getLayoutY() - 24 - 15);
                    } else {
                      label.setLayoutX(location.getXCoord() + mapDisplay.getLayoutX() - 8 - 20);
                      label.setLayoutY(location.getYCoord() + mapDisplay.getLayoutY() - 24 - 15);
                    }
                    count++;

                    buttonLabel.put(button, label);
                    buttonLocation.put(button, location);
                    button.setPickOnBounds(false);
                    button.setStyle("-fx-background-color: #78aaf0");

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
                          existingLocSelected(buttonLocation.get(button));
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
                            xPosText.setText(
                                String.valueOf(button.getLayoutX() - mapDisplay.getLayoutX() + 8));
                            yPosText.setText(
                                String.valueOf(button.getLayoutY() - mapDisplay.getLayoutY() + 24));
                            //
                            // xPosText.setText(String.valueOf(mouseEvent.getSceneX() +
                            // dragDelta.x));
                            //
                            // yPosText.setText(String.valueOf(mouseEvent.getSceneY() +
                            // dragDelta.y));
                            Label correspondingLabel = buttonLabel.get(button);
                            correspondingLabel.setLayoutX(mouseEvent.getSceneX() + dragDelta.x + 8);
                            correspondingLabel.setLayoutY(
                                mouseEvent.getSceneY() + dragDelta.y - 24);
                          }
                        });
                    button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));
                    draw(button, label);
                  }
                }
                for (MedicalEquipment medicalEquipment : medicalEquipments) {
                  System.out.println(medicalEquipment.getCurrentLocation());
                  System.out.println(roomNames.containsKey(medicalEquipment.getCurrentLocation()));
                  if (roomNames.containsKey(medicalEquipment.getCurrentLocation())) {

                    Button button = new Button();
                    Label label = new Label();
                    labels.add(label);
                    label.setText(medicalEquipment.getEquipmentType());
                    label.setFont(new Font(15));
                    final Delta dragDelta = new Delta();
                    button.setMinWidth(4.0);
                    button.setMinHeight(2.0);
                    button.setShape(equipmentMarkerShape);
                    button.setLayoutX(
                        roomNames.get(medicalEquipment.getCurrentLocation()).getXCoord()
                            + mapDisplay.getLayoutX()
                            - 8
                            + 10);
                    button.setLayoutY(
                        roomNames.get(medicalEquipment.getCurrentLocation()).getYCoord()
                            + mapDisplay.getLayoutY()
                            - 24
                            + 10);

                    label.setLayoutX(
                        roomNames.get(medicalEquipment.getCurrentLocation()).getXCoord()
                            + mapDisplay.getLayoutX()
                            - 8
                            + 7.5
                            + 10);
                    label.setLayoutY(
                        roomNames.get(medicalEquipment.getCurrentLocation()).getYCoord()
                            + mapDisplay.getLayoutY()
                            - 24
                            - 15
                            + 10);

                    buttonEquipment.put(button, medicalEquipment);
                    medicalButtons.add(button);
                    buttonLabel.put(button, label);
                    buttonLocation.put(
                        button, roomNames.get(medicalEquipment.getCurrentLocation()));
                    button.setPickOnBounds(false);
                    button.setStyle("-fx-background-color: RED");

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
                          existingEquipmentSelected(buttonEquipment.get(button));
                          editButton.setDisable(false);
                          deleteButton.setDisable(false);
                          saveButton.setDisable(true);
                          clearButton.setDisable(true);
                        });
                    button.setOnMouseReleased(mouseEvent -> button.setCursor(Cursor.HAND));
                    //                    button.setOnMouseDragged(
                    //                            mouseEvent -> {
                    //                              if (dragToggleBox.isSelected()) {
                    //                                button.setLayoutX(mouseEvent.getSceneX() +
                    // dragDelta.x);
                    //                                button.setLayoutY(mouseEvent.getSceneY() +
                    // dragDelta.y);
                    //                                xPosText.setText(
                    //                                        String.valueOf(button.getLayoutX() -
                    // mapDisplay.getLayoutX() + 8));
                    //                                yPosText.setText(
                    //                                        String.valueOf(button.getLayoutY() -
                    // mapDisplay.getLayoutY() + 24));
                    //                                //
                    //                                //
                    // xPosText.setText(String.valueOf(mouseEvent.getSceneX() +
                    //                                // dragDelta.x));
                    //                                //
                    //                                //
                    // yPosText.setText(String.valueOf(mouseEvent.getSceneY() +
                    //                                // dragDelta.y));
                    //                                Label correspondingLabel =
                    // buttonLabel.get(button);
                    //
                    // correspondingLabel.setLayoutX(mouseEvent.getSceneX() + dragDelta.x + 8);
                    //                                correspondingLabel.setLayoutY(
                    //                                        mouseEvent.getSceneY() + dragDelta.y -
                    // 24);
                    //                              }
                    //                            });
                    button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));
                    draw(button, label);
                  }
                }
              }
            });
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
    Button button = new Button();
    Label label = new Label();
    labels.add(label);
    label.setText("New Location");
    label.setFont(new Font(15));
    final Delta dragDelta = new Delta();
    button.setMinWidth(4.0);
    button.setMinHeight(2.0);
    button.setShape(locationMarkerShape);
    button.setLayoutX(mapDisplay.getLayoutX() - 8);
    button.setLayoutY(mapDisplay.getLayoutY() - 24);

    label.setLayoutX(mapDisplay.getLayoutX() - 8 + 7.5);
    label.setLayoutY(mapDisplay.getLayoutY() - 24 - 15);

    buttonLabel.put(button, label);
    buttonLocation.put(button, new Location());
    labels.add(label);

    button.setPickOnBounds(false);
    button.setStyle("-fx-background-color: #78aaf0");

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
          existingLocSelected(buttonLocation.get(button));
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

            Label correspondingLabel = buttonLabel.get(button);
            correspondingLabel.setLayoutX(mouseEvent.getSceneX() + dragDelta.x + 8);
            correspondingLabel.setLayoutY(mouseEvent.getSceneY() + dragDelta.y - 24);
          }
        });
    button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));
    draw(button, label);
  }

  @FXML
  public void deleteLocation() {
    locationBase.deleteLocationNode(nodeIDText.getText());
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

  public void existingEquipmentSelected(MedicalEquipment medicalEquipment) {
    inputVBox.setDisable(false);

    nodeIDText.setEditable(false);
    xPosText.setEditable(false);
    yPosText.setEditable(false);
    floorText.setEditable(false);
    buildingText.setEditable(false);
    typeText.setEditable(false);
    longnameText.setEditable(false);
    shortnameText.setEditable(false);

    nodeIDText.setText(medicalEquipment.getEquipmentID());
    floorText.setText("");
    buildingText.setText("");
    typeText.setText(medicalEquipment.getEquipmentType());
    longnameText.setText("N/A");
    shortnameText.setText(medicalEquipment.getCurrentLocation());
  }

  public void saveChanges() {

    locationBase.deleteLocationNode(currentID);
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
    locationBase.enterLocationNode(
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
    // next line may need to be refactored when Alex / Ethan's implementation comes through.
    // basically, right now it takes the ID of the location selected in the
    // existingLocSelected function above. Depending on implementation, this may change.

    // also, right now it makes a new, nearly-identical object to the previous one instead of
    // directly editing, as directly editing resulted in several errors.

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
    sceneController.switchScene(SceneController.SCENES.VIEW_LOCATION_DATA_SCENE);
  }

  @FXML
  public void returnToHomeScene() throws IOException {
    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
  }

  @FXML
  public void returnToSelectServiceScene() throws IOException {
    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
    locations.addAll(new ArrayList<>(new LocationDerbyImpl().getNodeList()));
  }

  // records relative x and y co-ordinates.
  private class Delta {
    double x, y;
  }

  private void draw(Button button, Label label) {
    anchorPane.getChildren().add(button);
    anchorPane.getChildren().add(label);
  }

  private void highlight(Button button, Button oldButton) {
    button.setStyle("-fx-background-color: RED");
    oldButton.getStyleClass().clear();
    oldButton.setStyle(null);
    oldButton.setStyle("-fx-background-color: #78aaf0");
  }

  private static class LocationMarker {
    Button button;
    Location location;
    Label label;
  }
}
