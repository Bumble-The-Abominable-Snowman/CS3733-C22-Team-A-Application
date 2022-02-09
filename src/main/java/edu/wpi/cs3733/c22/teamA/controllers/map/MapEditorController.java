package edu.wpi.cs3733.c22.teamA.controllers.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class MapEditorController {
  @FXML private ComboBox floorSelectionComboBox;
  @FXML private AnchorPane anchorPane;
  private List<Button> locationMarkers;
  private List<Location> locations;
  private Polygon locationMarkerShape = new Polygon();
  private Button selectedButton;

  public MapEditorController() {
    locationMarkers = new ArrayList<>();
    locations = new ArrayList<>();
    // locationMarkerShape.getPoints().addAll(new Double[] {2.0, 8.0, 0.0, 4.0, 2.0, 0.0, 4.0,
    // 4.0});
    locationMarkerShape.getPoints().addAll(new Double[] {1.0, 4.0, 0.0, 2.0, 1.0, 0.0, 2.0, 2.0});
  }

  @FXML JFXButton groundFloorButton = new JFXButton();
  @FXML JFXButton lowerFloorOneButton = new JFXButton();
  @FXML JFXButton lowerFloorTwoButton = new JFXButton();
  @FXML JFXButton floorOneButton = new JFXButton();
  @FXML JFXButton floorTwoButton = new JFXButton();
  @FXML JFXButton floorThreeButton = new JFXButton();
  @FXML JFXButton floorFourteenButton = new JFXButton();

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
  List<Location> locationFromDatabase = locationBase.getNodeList();
  Location newFillerLoc = locationFromDatabase.get(0);
  Location selectedLocation;

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  public void initialize() {
    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    Polygon polygon = new Polygon();
    polygon.getPoints().addAll(new Double[] {1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0});
    inputVBox.setDisable(true);
    locations.addAll(new ArrayList<>(new LocationDerbyImpl().getNodeList()));

    floorSelectionComboBox.getItems().removeAll(floorSelectionComboBox.getItems());
    floorSelectionComboBox
        .getItems()
        .addAll("Choose Floor:", "Floor 1", "Floor 2", "Floor 3", "L1", "L2", "Ground");
    floorSelectionComboBox.getSelectionModel().select("Choose Floor:");
    floorSelectionComboBox
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue.equals("Choose Floor:")) {
              } else {
                String floor;
                if (newValue.equals("Floor 1")) {
                  floor = "1";
                  File map = new File("edu/wpi/cs3733/c22/teamA/images/1st Floor.png");
                  Image image = new Image(String.valueOf((map)));
                  mapDisplay.setImage(image);
                } else if (newValue.equals("Floor 2")) {
                  floor = "2";
                  File map = new File("edu/wpi/cs3733/c22/teamA/images/2nd Floor.png");
                  Image image = new Image(String.valueOf((map)));
                  mapDisplay.setImage(image);
                } else if (newValue.equals("Floor 3")) {
                  floor = "3";
                  File map = new File("edu/wpi/cs3733/c22/teamA/images/3rd Floor.png");
                  Image image = new Image(String.valueOf((map)));
                  mapDisplay.setImage(image);
                } else if (newValue.equals("L1")) {
                  floor = "L1";
                  File map = new File("edu/wpi/cs3733/c22/teamA/images/LL1.png");
                  Image image = new Image(String.valueOf((map)));
                  mapDisplay.setImage(image);
                } else if (newValue.equals("L2")) {
                  floor = "L2";
                  File map = new File("edu/wpi/cs3733/c22/teamA/images/LL2.png");
                  Image image = new Image(String.valueOf((map)));
                  mapDisplay.setImage(image);
                } else {
                  floor = "Ground";
                  File map = new File("edu/wpi/cs3733/c22/teamA/images/Ground Floor.png");
                  Image image = new Image(String.valueOf((map)));
                  mapDisplay.setImage(image);
                }

                // anchorPane.getChildren().clear();
                // anchorPane.getChildren().add(floorSelectionComboBox);
                for (Location location : locations) {
                  if (location.getFloor().equals(floor)) {
                    Button button = new Button();
                    final Delta dragDelta = new Delta();
                    button.setMinWidth(4.0);
                    button.setMinHeight(2.0);
                    button.setShape(locationMarkerShape);
                    button.setLayoutX(location.getXCoord() - 512);
                    button.setLayoutY(location.getYCoord() - 512);
                    button.setPickOnBounds(false);
                    button.setStyle("-fx-background-color: Blue");

                    button.setOnAction(
                        event -> {
                          highlight(button, selectedButton);
                        });
                    button.setOnMousePressed(
                        mouseEvent -> {
                          // record a delta distance for the drag and drop operation.
                          dragDelta.x = button.getLayoutX() - mouseEvent.getSceneX();
                          dragDelta.y = button.getLayoutY() - mouseEvent.getSceneY();
                          button.setCursor(Cursor.MOVE);
                        });
                    button.setOnMouseReleased(mouseEvent -> button.setCursor(Cursor.HAND));
                    button.setOnMouseDragged(
                        mouseEvent -> {
                          button.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                          button.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                        });
                    button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));
                    drawButton(button);
                  }
                }
              }
            });
  }

  @FXML
  public void newLocPressed() {
    inputVBox.setDisable(false);
  }

  // as long as each diamond on the map is assigned a whole
  // location object, this function works fine. if each diamond is assigned a location
  // NodeID to save space or smth, can easily be refactored
  public void existingLocSelected(Location currLocation) {
    nodeIDText.setEditable(false);
    editButton.setDisable(false);
    saveButton.setDisable(true);
    inputVBox.setDisable(false);
    selectedLocation = currLocation;
    nodeIDText.setText(selectedLocation.getNodeID());
    xPosText.setText(String.valueOf(selectedLocation.getXCoord()));
    yPosText.setText(String.valueOf(selectedLocation.getYCoord()));
    floorText.setText(selectedLocation.getFloor());
    buildingText.setText(selectedLocation.getBuilding());
    typeText.setText(selectedLocation.getNodeType());
    longnameText.setText(selectedLocation.getLongName());
    shortnameText.setText(selectedLocation.getShortName());
  }

  // write filler code w/ filler location object to edit / create location data
  public void saveNewLocation() {
    // could probably cut first chunk and draw directly into the database to cut down
    // on lines of code + repetitiveness, havent tested tho
    // editButton.setDisable(true);
    saveButton.setDisable(false);
    Location l = newFillerLoc;
    l.setNodeID(nodeIDText.getText());
    l.setXCoord(Integer.parseInt(xPosText.getText()));
    l.setYCoord(Integer.parseInt(yPosText.getText()));
    l.setBuilding(buildingText.getText());
    l.setFloor(floorText.getText());
    l.setNodeType(typeText.getText());
    l.setLongName(longnameText.getText());
    l.setShortName(shortnameText.getText());

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

    String ID = selectedLocation.getNodeID();

    Location l = new Location();
    l.setXCoord(Integer.parseInt(xPosText.getText()));
    l.setYCoord(Integer.parseInt(yPosText.getText()));
    l.setBuilding(buildingText.getText());
    l.setFloor(floorText.getText());
    l.setNodeType(typeText.getText());
    l.setLongName(longnameText.getText());
    l.setShortName(shortnameText.getText());

    locationBase.deleteLocationNode(ID);

    l.setNodeID(ID);
    locationBase.enterLocationNode(
        l.getNodeID(),
        l.getXCoord(),
        l.getYCoord(),
        l.getFloor(),
        l.getBuilding(),
        l.getNodeType(),
        l.getLongName(),
        l.getShortName());

    /*
    locationBase.updateLocation(ID, "xcoord", xPosText.getText());
    locationBase.updateLocation(ID, "ycoord", yPosText.getText());
    locationBase.updateLocation(ID, "floor", floorText.getText());
    locationBase.updateLocation(ID, "building", buildingText.getText());
    locationBase.updateLocation(ID, "nodeType", typeText.getText());
    locationBase.updateLocation(ID, "longName", longnameText.getText());
    locationBase.updateLocation(ID, "shortName", shortnameText.getText());
    */
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

  private void drawButton(Button button) {
    anchorPane.getChildren().add(button);
  }

  private void highlight(Button button, Button oldButton) {
    button.setStyle("-fx-border-style: dashed");
    oldButton.setStyle(null);
    oldButton.setStyle("-fx-background-color: Blue");
  }

  private static class LocationMarker {
    Button button;
    Location location;
    Label label;
  }
}
