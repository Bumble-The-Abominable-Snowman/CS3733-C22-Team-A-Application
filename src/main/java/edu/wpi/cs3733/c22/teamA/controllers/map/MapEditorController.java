package edu.wpi.cs3733.c22.teamA.controllers.map;

import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
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

  @FXML
  public void initialize() {
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
                } else if (newValue.equals("Floor 2")) {
                  floor = "2";
                } else if (newValue.equals("Floor 3")) {
                  floor = "3";
                } else if (newValue.equals("L1")) {
                  floor = "L1";
                } else if (newValue.equals("L2")) {
                  floor = "L2";
                } else {
                  floor = "Ground";
                }

                anchorPane.getChildren().clear();
                anchorPane.getChildren().add(floorSelectionComboBox);
                for (Location location : locations) {
                  if (location.getFloor().equals(floor)) {
                    Button button = new Button();
                    final Delta dragDelta = new Delta();
                    button.setMinWidth(4.0);
                    button.setMinHeight(2.0);
                    button.setShape(locationMarkerShape);
                    button.setLayoutX(location.getXCoord() / 2.0 - 600); // top left coord
                    button.setLayoutY(location.getYCoord() / 2.0 - 400); // center at (7.5, 10)
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
