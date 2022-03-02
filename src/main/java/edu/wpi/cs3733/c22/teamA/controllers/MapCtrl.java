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
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import javax.swing.*;

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

  @FXML private ImageView bumbleBlinkHead;
  @FXML private JFXButton previousButton;
  @FXML private JFXButton nextButton;
  @FXML private JFXButton previous1Button;
  @FXML private JFXButton next1Button;
  @FXML private JFXButton previous2Button;
  @FXML private JFXButton next2Button;
  @FXML private JFXButton previous3Button;
  @FXML private JFXButton next3Button;
  @FXML private JFXButton previous4Button;
  @FXML private JFXButton next4Button;
  @FXML private JFXButton previous5Button;
  @FXML private JFXButton next5Button;
  @FXML private Label bubble1Text;
  @FXML private Label bubble2Text;
  @FXML private Label bubble3Text;
  @FXML private Label bubble4Text;
  @FXML private Label bubble5Text;
  @FXML private Label bubble6Text;

  @FXML private JFXComboBox searchComboBox;

  private ArrayList<String> floorNames;
  private boolean drawPathOnSwitch;

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

    double previousTextSize = previousButton.getFont().getSize();
    double nextTextSize = nextButton.getFont().getSize();
    double previous1TextSize = previous1Button.getFont().getSize();
    double next1TextSize = next1Button.getFont().getSize();
    double previous2TextSize = previous2Button.getFont().getSize();
    double next2TextSize = next2Button.getFont().getSize();
    double previous3TextSize = previous3Button.getFont().getSize();
    double next3TextSize = next3Button.getFont().getSize();
    double previous4TextSize = previous4Button.getFont().getSize();
    double next4TextSize = next4Button.getFont().getSize();
    double previous5TextSize = previous5Button.getFont().getSize();
    double next5TextSize = next5Button.getFont().getSize();
    double bubble1TextSize = bubble1Text.getFont().getSize();
    double bubble2TextSize = bubble2Text.getFont().getSize();
    double bubble3TextSize = bubble3Text.getFont().getSize();
    double bubble4TextSize = bubble4Text.getFont().getSize();
    double bubble5TextSize = bubble5Text.getFont().getSize();
    double bubble6TextSize = bubble6Text.getFont().getSize();

    App.getStage()
            .widthProperty()
            .addListener(
                    (obs, oldVal, newVal) -> {
                      previousButton.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previousTextSize)
                                      + "pt;");
                      nextButton.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * nextTextSize)
                                      + "pt;");
                      previous1Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previous1TextSize)
                                      + "pt;");
                      next1Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * next1TextSize)
                                      + "pt;");
                      previous2Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previous2TextSize)
                                      + "pt;");
                      next2Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * next2TextSize)
                                      + "pt;");
                      previous3Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previous3TextSize)
                                      + "pt;");
                      next3Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * next3TextSize)
                                      + "pt;");
                      previous4Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previous4TextSize)
                                      + "pt;");
                      next4Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * next4TextSize)
                                      + "pt;");
                      previous5Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previous5TextSize)
                                      + "pt;");
                      next5Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * next5TextSize)
                                      + "pt;");
                      bubble1Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble1TextSize)
                                      + "pt;");
                      bubble2Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble2TextSize)
                                      + "pt;");
                      bubble3Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble3TextSize)
                                      + "pt;");
                      bubble4Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble4TextSize)
                                      + "pt;");
                      bubble5Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble5TextSize)
                                      + "pt;");
                      bubble6Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble6TextSize)
                                      + "pt;");
                    });
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
              pathFinder.clearPath(anchorPane, false);
              if (drawPathOnSwitch)
                pathFinder.drawPath(pathFinder.findPath(markerManager.getFloor()), anchorPane);
            });
  }

  public void newLocationPressed() {
    mapManager.newLocationPressed();
  }

  public void findPath() {
    pathFinder.clearPath(anchorPane, true);
    pathFinder.drawPath(pathFinder.findPath(markerManager.getFloor()), anchorPane);
    if (!pathFinder.getDestinationFloor().equals(""))
      JOptionPane.showMessageDialog(null, "This path will take you on an elevator to Floor " + pathFinder.getDestinationFloor() + ".");
    drawPathOnSwitch = true;
  }

  public void clearPath() {
    pathFinder.clearPath(anchorPane, true);
    drawPathOnSwitch = false;
  }

  public void goToLocationTable() throws IOException {
    this.onSceneSwitch();
    sceneFlag = 2;
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  public void activateBumble(){
    helpButton.setVisible(false);
    bumbleXButton.setVisible(true);
    bumbleHead.setVisible(true);
    bubbleText.setVisible(true);
    nextButton.setVisible(true);
  }

  public void terminateBumble(){
    helpButton.setVisible(true);
    bumbleXButton.setVisible(false);
    bumbleHead.setVisible(false);
    bubbleText.setVisible(false);
    bubble1Text.setVisible(false);
    bubble2Text.setVisible(false);
    bubble3Text.setVisible(false);
    bubble4Text.setVisible(false);
    bubble5Text.setVisible(false);
    bubble6Text.setVisible(false);
    previousButton.setVisible(false);
    nextButton.setVisible(false);
    previous1Button.setVisible(false);
    next1Button.setVisible(false);
    previous2Button.setVisible(false);
    next2Button.setVisible(false);
    previous3Button.setVisible(false);
    next3Button.setVisible(false);
    previous4Button.setVisible(false);
    next4Button.setVisible(false);
    previous5Button.setVisible(false);
    next5Button.setVisible(false);
  }

  public void next(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previousButton.setVisible(true);
    nextButton.setVisible(false);
    next1Button.setVisible(true);
    bubbleText.setVisible(false);
    bubble1Text.setVisible(true);
  }

  public void previous(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previousButton.setVisible(false);
    nextButton.setVisible(true);
    next1Button.setVisible(false);
    bubbleText.setVisible(true);
    bubble1Text.setVisible(false);
  }

  public void next1(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(true);
    next1Button.setVisible(false);
    next2Button.setVisible(true);
    bubble1Text.setVisible(false);
    bubble2Text.setVisible(true);
  }

  public void previous1() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(false);
    next1Button.setVisible(true);
    next2Button.setVisible(false);
    bubble1Text.setVisible(true);
    bubble2Text.setVisible(false);
  }

  public void next2(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(false);
    previous2Button.setVisible(true);
    next2Button.setVisible(false);
    next3Button.setVisible(true);
    bubble2Text.setVisible(false);
    bubble3Text.setVisible(true);
  }

  public void previous2() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(true);
    previous2Button.setVisible(false);
    next2Button.setVisible(true);
    next3Button.setVisible(false);
    bubble2Text.setVisible(true);
    bubble3Text.setVisible(false);
  }

  public void next3(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous2Button.setVisible(false);
    previous3Button.setVisible(true);
    next3Button.setVisible(false);
    next4Button.setVisible(true);
    bubble3Text.setVisible(false);
    bubble4Text.setVisible(true);
  }

  public void previous3() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous2Button.setVisible(true);
    previous3Button.setVisible(false);
    next3Button.setVisible(true);
    next4Button.setVisible(false);
    bubble3Text.setVisible(true);
    bubble4Text.setVisible(false);
  }

  public void next4(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous3Button.setVisible(false);
    previous4Button.setVisible(true);
    next4Button.setVisible(false);
    next5Button.setVisible(true);
    bubble4Text.setVisible(false);
    bubble5Text.setVisible(true);
  }

  public void previous4() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous3Button.setVisible(true);
    previous4Button.setVisible(false);
    next4Button.setVisible(true);
    next5Button.setVisible(false);
    bubble4Text.setVisible(true);
    bubble5Text.setVisible(false);
  }

  public void next5(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous4Button.setVisible(false);
    previous5Button.setVisible(true);
    next5Button.setVisible(false);
    bubble5Text.setVisible(false);
    bubble6Text.setVisible(true);
  }

  public void previous5() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous4Button.setVisible(true);
    previous5Button.setVisible(false);
    next5Button.setVisible(true);
    bubble5Text.setVisible(true);
    bubble6Text.setVisible(false);
  }
}
