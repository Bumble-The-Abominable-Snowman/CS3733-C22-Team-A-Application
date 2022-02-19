package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class SelectionManager {
	private JFXButton editButton;
	private JFXButton saveButton;
	private JFXButton clearButton;
	private JFXButton deleteButton;
	private VBox inputVBox;
	private JFXTextArea nodeIDText;
	private JFXTextArea xPosText;
	private JFXTextArea yPosText;
	private JFXTextArea floorText;
	private JFXTextArea buildingText;
	private JFXTextArea typeText;
	private JFXTextArea longnameText;
	private JFXTextArea shortnameText;

	public SelectionManager(JFXButton editButton, JFXButton saveButton, JFXButton clearButton, JFXButton deleteButton, VBox inputVBox, JFXTextArea nodeIDText, JFXTextArea xPosText, JFXTextArea yPosText, JFXTextArea floorText, JFXTextArea buildingText, JFXTextArea typeText, JFXTextArea longnameText, JFXTextArea shortnameText) {
		this.editButton = editButton;
		this.saveButton = saveButton;
		this.clearButton = clearButton;
		this.deleteButton = deleteButton;
		this.inputVBox = inputVBox;
		this.nodeIDText = nodeIDText;
		this.xPosText = xPosText;
		this.yPosText = yPosText;
		this.floorText = floorText;
		this.buildingText = buildingText;
		this.typeText = typeText;
		this.longnameText = longnameText;
		this.shortnameText = shortnameText;
	}

	// Sets up UI states of text areas, and buttons
	public void setInitialUIStates() {
		editButton.setDisable(true);
		saveButton.setDisable(true);
		clearButton.setDisable(true);
		deleteButton.setDisable(true);

		inputVBox.setDisable(true);
	}

	// Selected Location
	public void existingLocationSelected(Location currLocation, Location selectedLocation) {
		inputVBox.setDisable(false);
		selectedLocation = currLocation;
		nodeIDText.setEditable(false);
		xPosText.setEditable(false);
		yPosText.setEditable(false);
		floorText.setEditable(false);
		buildingText.setEditable(false);
		typeText.setEditable(false);
		longnameText.setEditable(false);
		shortnameText.setEditable(false);
		nodeIDText.setText(selectedLocation.getNodeID());
		if (xPosText.getText() == null || xPosText.getText().equals("")) {
			xPosText.setText(String.valueOf(selectedLocation.getXCoord()));
			yPosText.setText(String.valueOf(selectedLocation.getYCoord()));
		}

		floorText.setText(selectedLocation.getFloor());
		buildingText.setText(selectedLocation.getBuilding());
		typeText.setText(selectedLocation.getNodeType());
		longnameText.setText(selectedLocation.getLongName());
		shortnameText.setText(selectedLocation.getShortName());
	}

	// Existing Equipment Selected
	public void existingEquipmentSelected(Equipment equipment, List<Location> locations) {
		inputVBox.setDisable(false);

		nodeIDText.setEditable(false);
		xPosText.setEditable(false);
		yPosText.setEditable(false);
		floorText.setEditable(false);
		buildingText.setEditable(false);
		typeText.setEditable(false);
		longnameText.setEditable(false);
		shortnameText.setEditable(false);

		nodeIDText.setText(equipment.getEquipmentID());
		typeText.setText(equipment.getEquipmentType());
		longnameText.setText("N/A");
		for (Location l : locations) {
			if (l.getNodeID().equals(equipment.getCurrentLocation())) {
				shortnameText.setText(l.getShortName());
				floorText.setText(l.getFloor());
				buildingText.setText(l.getBuilding());
				break;
			}
		}
	}

	// Existing Service Request Selected
	public void existingServiceRequestSelected(SR serviceRequest, List<Location> locations) {
		inputVBox.setDisable(false);

		nodeIDText.setEditable(false);
		xPosText.setEditable(false);
		yPosText.setEditable(false);
		floorText.setEditable(false);
		buildingText.setEditable(false);
		typeText.setEditable(false);
		longnameText.setEditable(false);
		shortnameText.setEditable(false);

		nodeIDText.setText(serviceRequest.getRequestID());
		typeText.setText(serviceRequest.getRequestPriority());
		longnameText.setText("N/A");

		for (Location l : locations) {
			if (l.getNodeID().equals(serviceRequest.getEndLocation())) {
				shortnameText.setText(l.getShortName());
				floorText.setText(l.getFloor());
				buildingText.setText(l.getBuilding());
				break;
			}
		}
	}

	// Edit Location
	public void editLocation() {
		editButton.setDisable(true);
		saveButton.setDisable(false);
		clearButton.setDisable(false);
		deleteButton.setDisable(false);

		nodeIDText.setEditable(true);
		xPosText.setEditable(true);
		yPosText.setEditable(true);
		floorText.setEditable(true);
		buildingText.setEditable(true);
		typeText.setEditable(true);
		longnameText.setEditable(true);
		shortnameText.setEditable(true);
	}

	// Clear Submission
	public void clearSubmission() {
		nodeIDText.setText("");
		xPosText.setText("");
		yPosText.setText("");
		floorText.setText("");
		buildingText.setText("");
		typeText.setText("");
		longnameText.setText("");
		shortnameText.setText("");
	}

	// Delete Location
	@FXML
	public void deleteLocation() {
		locationDAO.deleteLocationNode(nodeIDText.getText());
		String originalFloorName = floorName;
		floorSelectionComboBox.setValue("Choose Floor");
		floorSelectionComboBox.setValue(originalFloorName);
	}

	// Save Changes
	public void saveChanges() {
		if (newLocationMarker != null && newLocationMarker.getLocation().equals(selectedLocation)) {
			newLocationMarker.getLocation().setNodeID(nodeIDText.getText());
			newLocationMarker.getLocation().setXCoord((int) Double.parseDouble(xPosText.getText()));
			newLocationMarker.getLocation().setYCoord((int) Double.parseDouble(yPosText.getText()));
			newLocationMarker.getLocation().setFloor(floorText.getText());
			newLocationMarker.getLocation().setBuilding(buildingText.getText());
			newLocationMarker.getLocation().setNodeType(typeText.getText());
			newLocationMarker.getLocation().setLongName(longnameText.getText());
			newLocationMarker.getLocation().setShortName(shortnameText.getText());

			locationDAO.enterLocationNode(newLocationMarker.getLocation());
			newLocationMarker = null;
			clearSubmission();
			String originalFloorName = floorName;
			floorSelectionComboBox.setValue("Choose Floor");
			floorSelectionComboBox.setValue(originalFloorName);
			System.out.println("here");
			return;
		}

		locationDAO.updateLocation(
				nodeIDText.getText(), "xCoord", (int) Double.parseDouble(xPosText.getText()));
		locationDAO.updateLocation(
				nodeIDText.getText(), "yCoord", (int) Double.parseDouble(yPosText.getText()));
		locationDAO.updateLocation(nodeIDText.getText(), "floor", floorText.getText());
		locationDAO.updateLocation(nodeIDText.getText(), "building", buildingText.getText());
		locationDAO.updateLocation(nodeIDText.getText(), "nodeType", typeText.getText());
		locationDAO.updateLocation(nodeIDText.getText(), "longName", longnameText.getText());
		locationDAO.updateLocation(nodeIDText.getText(), "ShortName", shortnameText.getText());

		clearSubmission();
		String originalFloorName = floorName;
		floorSelectionComboBox.setValue("Choose Floor");
		floorSelectionComboBox.setValue(originalFloorName);
	}

	// Update Medical Equipment / Service Request on Drag Release
	public void updateOnRelease(Button button) throws SQLException {
		System.out.println("update on release is going rn");
		equipmentDAO.updateMedicalEquipment(
				buttonEquipmentMarker.get(button).getEquipment().getEquipmentID(),
				"xCoord",
				button.getLayoutX() + " ");
		equipmentDAO.updateMedicalEquipment(
				buttonEquipmentMarker.get(button).getEquipment().getEquipmentID(),
				"yCoord",
				button.getLayoutY() + " ");
    /*
    else if (buttonServiceRequestMarker.containsKey(button)){
      serDAO.updateMedicalEquipment(
              buttonEquipmentMarker.get(button).getEquipment().getEquipmentID(),
              "xCoord",
              button.getLayoutX());
      equipmentDAO.updateMedicalEquipment(
              buttonEquipmentMarker.get(button).getEquipment().getEquipmentID(),
              "yCoord",
              button.getLayoutY());
    }
     */
	}

}
