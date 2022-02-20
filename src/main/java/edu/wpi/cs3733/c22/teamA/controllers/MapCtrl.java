package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.map.MarkerManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.kurobako.gesturefx.AffineEvent;
import net.kurobako.gesturefx.GesturePane;

// TODO Change all instances of looping through locations to find related short names & node ids
// with method in backend once implemented
public class MapCtrl extends MasterCtrl {
  @FXML private JFXComboBox<String> floorSelectionComboBox;
  @FXML private GesturePane gesturePane;

  private AnchorPane anchorPane;
  private ImageView mapImageView;
  private ArrayList<String> floorNames;
  private String currentFloor;
  private MarkerManager markerManager;
  private LocationDAO locationDAO;
  private EquipmentDAO equipmentDAO;

  public MapCtrl() {
    // Setup Floors
    floorNames =
        new ArrayList<>(
            Arrays.asList("Choose Floor:", "Floor 1", "Floor 2", "Floor 3", "L1", "L2"));

    locationDAO = new LocationDerbyImpl();
    equipmentDAO = new EquipmentDerbyImpl();
  }

  /** Floor Combo Box */
  @FXML
  public void initialize() {
    mapImageView = new ImageView();
    anchorPane = new AnchorPane();

    configure();
    initGesture();
    initFloorSelection();

    markerManager = new MarkerManager(locationDAO, equipmentDAO, anchorPane);
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
              setMapFloor(newValue);
              markerManager.initFloor(
                  currentFloor, ((int) mapImageView.getLayoutX()), (int) mapImageView.getLayoutY());
            });
  }

  private void setMapFloor(String floor) {
    URL url;
    switch (floor) {
      case "Choose Floor: ":
        currentFloor = "";
        url = App.class.getResource("images/Side View.png");
        break;
      case "Floor 1":
        currentFloor = "1";
        url = App.class.getResource("images/1st Floor.png");
        break;
      case "Floor 2":
        currentFloor = "2";
        url = App.class.getResource("images/2nd Floor.png");
        break;
      case "Floor 3":
        currentFloor = "3";
        url = App.class.getResource("images/3rd Floor.png");
        break;
      case "L1":
        currentFloor = "L1";
        url = App.class.getResource("images/LL1.png");
        break;
      case "L2":
        currentFloor = "L2";
        url = App.class.getResource("images/LL2.png");
        break;
      default:
        currentFloor = "";
        url = App.class.getResource("images/Side View.png");
        break;
    }

    Image image = new Image(String.valueOf(url));
    mapImageView.setImage(image);
  }

  public void initGesture() {
    anchorPane.getChildren().add(mapImageView);
    anchorPane.setLayoutX(0);
    anchorPane.setLayoutY(0);
    //    anchorPane.setPrefHeight(gesturePane.getHeight());
    //    anchorPane.setPrefWidth(gesturePane.getWidth());
    mapImageView.setStyle(
        " -fx-border-color: black;\n"
            + "    -fx-border-style: solid;\n"
            + "    -fx-border-width: 5;");
    mapImageView.setPreserveRatio(true);
    // mapImageView.setLayoutX(layoutStart);
    mapImageView.setLayoutY(0);
    gesturePane.setMinScale(0.75f);
    gesturePane.reset();
    gesturePane.setContent(anchorPane);
    gesturePane.addEventFilter(
        AffineEvent.CHANGED,
        event -> {
          System.out.println(
              event.getTransformedDimension().getHeight()
                  + " "
                  + event.getTransformedDimension().getWidth());
          System.out.println(gesturePane.getCurrentX() + " " + gesturePane.getTranslateY());
          // transformed = event.getTransformedDimension();
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

  public void editLocation(ActionEvent actionEvent) {}

  public void clearSubmission(ActionEvent actionEvent) {}

  public void saveChanges(ActionEvent actionEvent) {}

  public void deleteLocation(ActionEvent actionEvent) {}

  public void newLocationPressed(ActionEvent actionEvent) {}

  public void findPath(ActionEvent actionEvent) {}

  public void clearPath(ActionEvent actionEvent) {}

  public void goToLocationTable(ActionEvent actionEvent) {}
}
