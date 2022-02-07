package edu.wpi.cs3733.c22.teamA.controllers.map;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class MapEditorController {
  @FXML private ComboBox floorSelectionComboBox;
  @FXML private AnchorPane anchorPane;
  private List<Polygon> locationMarkers;

  public MapEditorController() {
    locationMarkers = new ArrayList<>();
  }

  @FXML
  public void initialize() {
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

              } else if (newValue.equals("Floor 1")) {
                Polygon polygon = new Polygon();
                polygon
                    .getPoints()
                    .addAll(new Double[] {100.0, 110.0, 92.5, 100.0, 100.0, 90.0, 107.5, 100.0});
                polygon.setFill(Color.color(0, 0.6, 1.0));
                locationMarkers.add(polygon);
                draw();
              }
            });
  }

  private void draw() {
    anchorPane.getChildren().add(locationMarkers.get(0));
  }
}
