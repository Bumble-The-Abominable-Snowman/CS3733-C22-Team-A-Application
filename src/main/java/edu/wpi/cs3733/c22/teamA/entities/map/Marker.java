package edu.wpi.cs3733.c22.teamA.entities.map;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public abstract class Marker {
  private Button button;
  private Label label;
  private LocationMarker locationMarker;

  public Marker(Button button, Label label, LocationMarker locationMarker) {
    this.button = button;
    this.label = label;
    this.locationMarker = locationMarker;
  }

  public void setLabelVisibility(boolean val) {
    label.setVisible(val);
  }

  public void setButtonVisibility(boolean val) {
    button.setVisible(val);
  }

  public void draw(AnchorPane anchorPane) {
    anchorPane.getChildren().add(button);
    anchorPane.getChildren().add(label);
  }

  public void clear(AnchorPane anchorPane) {
    anchorPane.getChildren().remove(button);
    anchorPane.getChildren().remove(label);
  }

  public Button getButton() {
    return button;
  }

  public void setButton(Button button) {
    this.button = button;
  }

  public Label getLabel() {
    return label;
  }

  public void setLabel(Label label) {
    this.label = label;
  }

  public LocationMarker getLocationMarker() {
    return locationMarker;
  }

  public void setLocationMarker(LocationMarker locationMarker) {
    this.locationMarker = locationMarker;
  }
}
