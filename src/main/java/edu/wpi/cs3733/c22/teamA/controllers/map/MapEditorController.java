package edu.wpi.cs3733.c22.teamA.controllers.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

public class MapEditorController {

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
  @FXML JFXButton clearButton = new JFXButton();

  @FXML JFXButton backButton = new JFXButton();
  @FXML JFXButton homeButton = new JFXButton();

  @FXML VBox inputVBox = new VBox();

  LocationDAO locationBase = new LocationDerbyImpl();
  List<Location> locationFromDatabase = locationBase.getNodeList();
  Location fillerLoc = locationFromDatabase.get(0);

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  public void initialize() {
    Polygon polygon = new Polygon();
    polygon.getPoints().addAll(new Double[] {1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0});
    inputVBox.setDisable(true);
  }

  @FXML
  public void newLocPressed() {
    inputVBox.setDisable(false);
  }

  public void existingLocSelected() {
    inputVBox.setDisable(false);
    // saveButton.setOnAction(editCurrLocation(););
  }

  // write filler code w/ filler location object to edit / create location data
  public void saveNewLocation() {
    Location l = new Location();
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
    // i dont wanna clutter the database with tests, remove the following line and this works
    locationBase.deleteLocationNode(l.getNodeID());
  }

  public void editCurrLocation() {}

  public void clearSubmission() {}

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
  }
}
