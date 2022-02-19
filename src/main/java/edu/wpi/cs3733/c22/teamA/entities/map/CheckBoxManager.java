package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;

import java.util.Map;

public class CheckBoxManager {
	private JFXCheckBox equipmentCheckBox;
	private JFXCheckBox locationCheckBox;
	private JFXCheckBox serviceRequestCheckBox;
	private JFXCheckBox showTextCheckBox;
	private JFXCheckBox dragCheckBox;

	private Map<Button, LocationMarker> buttonLocationMarker;
	private Map<Button, EquipmentMarker> buttonEquipmentMarker;
	private Map<Button, SRMarker> buttonServiceRequestMarker;

	public CheckBoxManager(JFXCheckBox locationCheckBox, JFXCheckBox equipmentCheckBox, JFXCheckBox serviceRequestCheckBox, JFXCheckBox showTextCheckBox, JFXCheckBox dragCheckBox){
		this.locationCheckBox = locationCheckBox;
		this.equipmentCheckBox = equipmentCheckBox;
		this.serviceRequestCheckBox = serviceRequestCheckBox;
		this.showTextCheckBox = showTextCheckBox;
		this.dragCheckBox = dragCheckBox;
	}

	// Sets up CheckBoxes
	public void setupCheckboxListeners() {
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
	}

	public void setIntitialUIState(){
		dragCheckBox.setSelected(true);
		showTextCheckBox.setSelected(true);
		equipmentCheckBox.setSelected(true);
		serviceRequestCheckBox.setSelected(true);
		locationCheckBox.setSelected(true);
	}

	public void showServiceRequests(boolean value) {
		for (SRMarker serviceRequestMarker : buttonServiceRequestMarker.values()) {
			serviceRequestMarker.setButtonVisibility(value);
			if (showTextCheckBox.isSelected()) {
				serviceRequestMarker.setLabelVisibility(value);
			}
		}
	}

	public void showEquipment(boolean value) {
		for (EquipmentMarker equipmentMarker : buttonEquipmentMarker.values()) {
			equipmentMarker.setButtonVisibility(value);
			if (showTextCheckBox.isSelected()) {
				equipmentMarker.setLabelVisibility(value);
			}
		}
	}

	public void showLocations(boolean value) {
		for (LocationMarker locationMarker : buttonLocationMarker.values()) {
			locationMarker.setButtonVisibility(value);
			if (showTextCheckBox.isSelected()) {
				locationMarker.setLabelVisibility(value);
			}
		}
	}

	public void showLabels(boolean value) {
		if (locationCheckBox.isSelected()) {
			for (LocationMarker locationMarker : buttonLocationMarker.values()) {
				locationMarker.setLabelVisibility(value);
			}
		}
		if (equipmentCheckBox.isSelected()) {
			for (EquipmentMarker equipmentMarker : buttonEquipmentMarker.values()) {
				equipmentMarker.setLabelVisibility(value);
			}
		}
		if (serviceRequestCheckBox.isSelected()) {
			for (SRMarker serviceRequestMarker : buttonServiceRequestMarker.values()) {
				serviceRequestMarker.setLabelVisibility(value);
			}
		}
	}
}
