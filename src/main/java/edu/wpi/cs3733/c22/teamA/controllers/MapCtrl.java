package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.map.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.cs3733.c22.teamA.entities.servicerequests.AutoCompleteBox;
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

  // Check Box Manager
  @FXML private JFXCheckBox dragCheckBox;
  @FXML private JFXCheckBox serviceRequestCheckBox;
  @FXML private JFXCheckBox locationCheckBox;
  @FXML private JFXCheckBox showTextCheckBox;
  @FXML private JFXCheckBox equipmentCheckBox;

  // Gesture Pane Manager
  @FXML private JFXComboBox<String> floorSelectionComboBox;
  @FXML private JFXComboBox<String> pfFromComboBox;
  @FXML private JFXComboBox<String> pfToComboBox;
  @FXML private GesturePane gesturePane;
  private AnchorPane anchorPane;
  private ImageView mapImageView;
  @FXML JFXButton newLocButton = new JFXButton();
  @FXML JFXButton findPathButton = new JFXButton();
  @FXML JFXButton clearPathButton = new JFXButton();

  @FXML private JFXComboBox searchComboBox;

  private ArrayList<String> floorNames;

  private LocationDAO locationDAO;
  private EquipmentDAO equipmentDAO;
  private ServiceRequestDAO SRDAO;

  private MapManager mapManager;
  private MarkerManager markerManager;
  private CheckBoxManager checkBoxManager;
  private GesturePaneManager gesturePaneManager;
  private SelectionManager selectionManager;
  private Searcher searcher;
  private SideView sideView;
  private PathFinder pathFinder;

  public final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  public MapCtrl() {
    // Setup Floors
    mapImageView = new ImageView();
    anchorPane = new AnchorPane();

    floorNames =
        new ArrayList<>(
            Arrays.asList("Choose Floor:", "Floor 1", "Floor 2", "Floor 3", "L1", "L2"));

    locationDAO = new LocationDerbyImpl();
    equipmentDAO = new EquipmentDerbyImpl();
    //SRDAO = new ServiceRequestDerbyImpl();
  }

  /* Floor Combo Box */
  @FXML
  public void initialize() {
    configure();

    floorSelectionComboBox.toFront();
    markerManager = new MarkerManager(locationDAO, equipmentDAO, anchorPane);
    checkBoxManager =
        new CheckBoxManager(
            locationCheckBox,
            equipmentCheckBox,
            serviceRequestCheckBox,
            showTextCheckBox,
            dragCheckBox);
    gesturePaneManager = new GesturePaneManager(gesturePane, anchorPane, mapImageView);
    selectionManager = new SelectionManager(inputVBox, markerManager);
    searcher = new Searcher(searchComboBox);
    sideView = new SideView(anchorPane, mapImageView, markerManager);
    List<JFXButton> buttons = new ArrayList<>();
    buttons.add(newLocButton);
    buttons.add(findPathButton);
    buttons.add(clearPathButton);
    mapManager =
        new MapManager(
            markerManager,
            checkBoxManager,
            gesturePaneManager,
            selectionManager,
            searcher,
            sideView,
            buttons);
    pathFinder = new PathFinder("db/CSVs/AllEdgesHand.csv", pfFromComboBox, pfToComboBox, markerManager);

    mapManager.init();
    sideView.init();
    initFloorSelection();
    new AutoCompleteBox(floorSelectionComboBox);
    new AutoCompleteBox(pfToComboBox);
    new AutoCompleteBox(pfFromComboBox);
  }

  private void initFloorSelection() {
    pathFinder.updateComboBoxes();
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
              //pathFinder.updateComboBoxes();
              if (newValue.equals("Choose Floor:"))
                clearPath();
            });
  }

  public void newLocationPressed() {
    mapManager.newLocationPressed();
  }

  public void findPath() {
    pathFinder.drawPath(pathFinder.findPath(markerManager.getFloor()), anchorPane);
  }

  public void clearPath() {
    pathFinder.clearPath(anchorPane);
  }

  public void goToLocationTable() throws IOException {
    this.onSceneSwitch();
    sceneFlag = 2;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }
}
