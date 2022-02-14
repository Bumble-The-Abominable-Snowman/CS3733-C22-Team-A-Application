package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EquipmentMarker extends Marker {
  private Equipment equipment;

  public EquipmentMarker(
      Equipment equipment, Button button, Label label, LocationMarker locationMarker) {
    super(button, label, locationMarker);

    this.equipment = equipment;
  }

  public Equipment getEquipment() {
    return equipment;
  }

  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }
}
