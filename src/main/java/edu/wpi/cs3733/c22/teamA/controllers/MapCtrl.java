package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.map.*;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.kurobako.gesturefx.GesturePane;

// TODO Change all instances of looping through locations to find related short names & node ids
// with method in backend once implemented
public class MapCtrl extends MasterCtrl {
  // Selection Manager
  @FXML private VBox inputVBox;
  @FXML private JFXTextArea nodeIDText;
  @FXML private JFXTextArea xPosText;
  @FXML private JFXTextArea yPosText;
  @FXML private JFXTextArea floorText;
  @FXML private JFXTextArea towerText;
  @FXML private JFXTextArea typeText;
  @FXML private JFXTextArea longnameText;
  @FXML private JFXTextArea shortnameText;
  @FXML private JFXButton editButton;
  @FXML private JFXButton clearButton;
  @FXML private JFXButton saveButton;
  @FXML private JFXButton deleteButton;

  // Check Box Manager
  @FXML private JFXCheckBox dragCheckBox;
  @FXML private JFXCheckBox serviceRequestCheckBox;
  @FXML private JFXCheckBox locationCheckBox;
  @FXML private JFXCheckBox showTextCheckBox;
  @FXML private JFXCheckBox equipmentCheckBox;

  // Gesuture Pane Manager
  @FXML private JFXComboBox<String> floorSelectionComboBox;
  @FXML private GesturePane gesturePane;
  private AnchorPane anchorPane;
  private ImageView mapImageView;

  private ArrayList<String> floorNames;

  private LocationDAO locationDAO;
  private EquipmentDAO equipmentDAO;

  private MapManager mapManager;
  private MarkerManager markerManager;
  private CheckBoxManager checkBoxManager;
  private GesturePaneManager gesturePaneManager;
  private SelectionManager selectionManager;

  public MapCtrl() {
    // Setup Floors
    mapImageView = new ImageView();
    anchorPane = new AnchorPane();

    floorNames =
        new ArrayList<>(
            Arrays.asList("Choose Floor:", "Floor 1", "Floor 2", "Floor 3", "L1", "L2"));

    locationDAO = new LocationDerbyImpl();
    equipmentDAO = new EquipmentDerbyImpl();
  }

  /** Floor Combo Box */
  @FXML
  public void initialize() {

    configure();
    initFloorSelection();

    markerManager = new MarkerManager(locationDAO, equipmentDAO, anchorPane);
    checkBoxManager =
        new CheckBoxManager(
            locationCheckBox,
            equipmentCheckBox,
            serviceRequestCheckBox,
            showTextCheckBox,
            dragCheckBox);
    gesturePaneManager = new GesturePaneManager(gesturePane, anchorPane, mapImageView);
    selectionManager =
        new SelectionManager(
            editButton,
            saveButton,
            clearButton,
            deleteButton,
            inputVBox,
            nodeIDText,
            xPosText,
            yPosText,
            floorText,
            towerText,
            typeText,
            longnameText,
            shortnameText);
    mapManager =
        new MapManager(markerManager, checkBoxManager, gesturePaneManager, selectionManager);

    mapManager.init();
  }

  private void initFloorSelection() {
    floorSelectionComboBox.getItems().removeAll(floorNames);
    floorSelectionComboBox.getItems().addAll(floorNames);
    floorSelectionComboBox.getSelectionModel().select("Choose Floor:");
    floorSelectionComboBox
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              mapManager.reset();
              mapManager.initFloor(
                  newValue, ((int) mapImageView.getLayoutX()), (int) mapImageView.getLayoutY());
            });
  }

  public void editLocation(ActionEvent actionEvent) {}

  public void clearSubmission(ActionEvent actionEvent) {}

  public void saveChanges(ActionEvent actionEvent) {}

  public void deleteLocation(ActionEvent actionEvent) {}

  public void newLocationPressed(ActionEvent actionEvent) {}

  public void findPath(ActionEvent actionEvent) {}

  public void clearPath(ActionEvent actionEvent) {}

  public void goToLocationTable(ActionEvent actionEvent) {}
}
