package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXCheckBox;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;

public class CheckBoxManager {
  private JFXCheckBox equipmentCheckBox;
  private JFXCheckBox locationCheckBox;
  private JFXCheckBox serviceRequestCheckBox;
  private JFXCheckBox showTextCheckBox;
  private JFXCheckBox dragCheckBox;

  private List<LocationMarker> locationMarkers;
  private List<EquipmentMarker> equipmentMarkers;
  private List<SRMarker> serviceRequestMarkers;

  public CheckBoxManager(
      JFXCheckBox locationCheckBox,
      JFXCheckBox equipmentCheckBox,
      JFXCheckBox serviceRequestCheckBox,
      JFXCheckBox showTextCheckBox,
      JFXCheckBox dragCheckBox) {
    this.locationCheckBox = locationCheckBox;
    this.equipmentCheckBox = equipmentCheckBox;
    this.serviceRequestCheckBox = serviceRequestCheckBox;
    this.showTextCheckBox = showTextCheckBox;
    this.dragCheckBox = dragCheckBox;

    locationMarkers = new ArrayList<>();
    equipmentMarkers = new ArrayList<>();
    serviceRequestMarkers = new ArrayList<>();

    initCheckboxListeners();
  }

  public void initCheckboxListeners() {
    equipmentCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showEquipment(new_val);
            });

    locationCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showLocations(new_val);
            });

    serviceRequestCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showServiceRequests(new_val);
            });

    showTextCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              showLabels(new_val);
            });

    dragCheckBox
        .selectedProperty()
        .addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
              // TODO here
            });
  }

  public void switchFloor(MarkerManager markerManager) {
    initCheckBoxInfo(markerManager);
    setIntitialUIState();
  }

  public void initCheckBoxInfo(MarkerManager markerManager) {
    this.locationMarkers = markerManager.getLocationMarkers();
    this.equipmentMarkers = markerManager.getEquipmentMarkers();
    this.serviceRequestMarkers = markerManager.getServiceRequestMarkers();
  }

  public void setIntitialUIState() {
    dragCheckBox.setSelected(true);
    showTextCheckBox.setSelected(true);
    equipmentCheckBox.setSelected(true);
    serviceRequestCheckBox.setSelected(true);
    locationCheckBox.setSelected(true);
  }

  public void showServiceRequests(boolean value) {
    for (SRMarker serviceRequestMarker : serviceRequestMarkers) {
      serviceRequestMarker.setButtonVisibility(value);
      if (showTextCheckBox.isSelected()) {
        serviceRequestMarker.setLabelVisibility(value);
      }
    }
  }

  public void showEquipment(boolean value) {
    for (EquipmentMarker equipmentMarker : equipmentMarkers) {
      equipmentMarker.setButtonVisibility(value);
      if (showTextCheckBox.isSelected()) {
        equipmentMarker.setLabelVisibility(value);
      }
    }
  }

  public void showLocations(boolean value) {
    for (LocationMarker locationMarker : locationMarkers) {
      locationMarker.setButtonVisibility(value);
      if (showTextCheckBox.isSelected()) {
        locationMarker.setLabelVisibility(value);
      }
    }
  }

  public void showLabels(boolean value) {
    if (locationCheckBox.isSelected()) {
      for (LocationMarker locationMarker : locationMarkers) {
        locationMarker.setLabelVisibility(value);
      }
    }
    if (equipmentCheckBox.isSelected()) {
      for (EquipmentMarker equipmentMarker : equipmentMarkers) {
        equipmentMarker.setLabelVisibility(value);
      }
    }
    if (serviceRequestCheckBox.isSelected()) {
      for (SRMarker serviceRequestMarker : serviceRequestMarkers) {
        serviceRequestMarker.setLabelVisibility(value);
      }
    }
  }

  public JFXCheckBox getDragCheckBox() {
    return dragCheckBox;
  }
}
