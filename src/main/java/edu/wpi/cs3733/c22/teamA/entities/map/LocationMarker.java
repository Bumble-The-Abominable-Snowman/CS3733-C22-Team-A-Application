package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.entities.Location;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class LocationMarker {
  private Button button;
  private Location location;
  private Label label;
  private EquipmentMarker equipmentMarker;
  private SRMarker serviceRequestMarker;

  public LocationMarker(Button button, Label label, Location location) {
    this.button = button;
    this.label = label;
    this.location = location;
    this.equipmentMarker = null;
    this.serviceRequestMarker = null;
  }

  public void draw(AnchorPane anchorPane) {
    anchorPane.getChildren().add(button);
    anchorPane.getChildren().add(label);
  }

  public void clear(AnchorPane anchorPane) {
    anchorPane.getChildren().remove(button);
    anchorPane.getChildren().remove(label);
  }

  public void setLabelVisibility(boolean val) {
    label.setVisible(val);
  }

  public void setButtonVisibility(boolean val) {
    button.setVisible(val);
  }

  public Button getButton() {
    return button;
  }

  public void setButton(Button button) {
    this.button = button;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Label getLabel() {
    return label;
  }

  public void setLabel(Label label) {
    this.label = label;
  }

  public EquipmentMarker getEquipmentMarker() {
    return equipmentMarker;
  }

  public void setEquipmentMarker(EquipmentMarker equipmentMarker) {
    this.equipmentMarker = equipmentMarker;
  }

  public SRMarker getServiceRequestMarker() {
    return serviceRequestMarker;
  }

  public void setServiceRequestMarker(SRMarker serviceRequestMarker) {
    this.serviceRequestMarker = serviceRequestMarker;
  }
}
