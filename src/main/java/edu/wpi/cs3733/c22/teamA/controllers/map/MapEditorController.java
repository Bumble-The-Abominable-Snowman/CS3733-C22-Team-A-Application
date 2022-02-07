package edu.wpi.cs3733.c22.teamA.controllers.map;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class MapEditorController {
  @FXML private ComboBox floorSelectionComboBox;
  @FXML private AnchorPane anchorPane;
  private List<Button> locationMarkers;

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
                    .addAll(new Double[] {7.5, 20.0, 0.0, 10.0, 7.5, 0.0, 15.0, 10.0});
                polygon.setFill(Color.color(0, 0.6, 1.0));
                Button a = new Button();
                a.setShape(polygon);
                a.setLayoutX(0); // top left coord
                a.setLayoutY(0);// center at (7.5, 10)
                a.setPickOnBounds(false);
                a.setStyle("-fx-background-color: Blue");
                locationMarkers.add(a);
                draw();
              }
            });
  }

  private void draw() {
    anchorPane.getChildren().add(locationMarkers.get(0));
  }
}
