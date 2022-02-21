package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class SelectionManager {
  private VBox inputVBox;

  private List<InfoField> locationFields;
  private List<InfoField> equipmentFields;
  private List<InfoField> srFields;

  private JFXButton editButton;
  private JFXButton saveButton;
  private JFXButton clearButton;
  private JFXButton deleteButton;

  public SelectionManager(VBox inputVBox) {
    this.inputVBox = inputVBox;
    inputVBox.setDisable(true);
    instantiateButtons();
    fillBoxes();
  }

  public void instantiateButtons() {
    editButton = new JFXButton("Edit");
    saveButton = new JFXButton("Save");
    clearButton = new JFXButton("Clear");
    deleteButton = new JFXButton("Delete");
  }

  public void fillBoxes() {
    fillLocations();
    fillEquipment();
    fillSR();
  }

  public void fillLocations() {
    InfoField nodeID = new InfoField(new Label("Node ID:"), new JFXTextArea());
    InfoField xPos = new InfoField(new Label("X Coord:"), new JFXTextArea());
    InfoField yPos = new InfoField(new Label("Y Coord:"), new JFXTextArea());
    InfoField floor = new InfoField(new Label("Floor:"), new JFXTextArea());
    InfoField building = new InfoField(new Label("Building:"), new JFXTextArea());
    InfoField type = new InfoField(new Label("Type:"), new JFXTextArea());
    InfoField longName = new InfoField(new Label("Long Name:"), new JFXTextArea());
    InfoField shortName = new InfoField(new Label("Short Name"), new JFXTextArea());
    locationFields = List.of(nodeID, xPos, yPos, floor, building, type, longName, shortName);
  }

  public void fillEquipment() {
    InfoField equipID = new InfoField(new Label("Equipment ID:"), new JFXTextArea());
    InfoField type = new InfoField(new Label("Type:"), new JFXTextArea());
    InfoField isClean = new InfoField(new Label("Clean?"), new JFXTextArea());
    InfoField location = new InfoField(new Label("Location:"), new JFXTextArea());
    InfoField availability = new InfoField(new Label("Available:"), new JFXTextArea());
    equipmentFields = List.of(equipID, type, isClean, location, availability);
  }

  public void fillSR() {
    InfoField reqID = new InfoField(new Label("Request ID:"), new JFXTextArea());
    InfoField startLocation = new InfoField(new Label("Start Location:"), new JFXTextArea());
    InfoField endLocation = new InfoField(new Label("End Location:"), new JFXTextArea());
    InfoField employeeRequested =
        new InfoField(new Label("Employee Requested:"), new JFXTextArea());
    InfoField employeeAssigned = new InfoField(new Label("Employee Assigned:"), new JFXTextArea());
    InfoField time = new InfoField(new Label("Time:"), new JFXTextArea());
    InfoField status = new InfoField(new Label("Status:"), new JFXTextArea());
    InfoField priority = new InfoField(new Label("Priority:"), new JFXTextArea());
    InfoField comments = new InfoField(new Label("Comments:"), new JFXTextArea());
    srFields =
        List.of(
            reqID,
            startLocation,
            endLocation,
            employeeRequested,
            employeeAssigned,
            time,
            status,
            priority,
            comments);
  }

  // Sets up UI states of text areas, and buttons
  public void setInitialUIStates() {
    editButton.setDisable(true);
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    deleteButton.setDisable(true);
    inputVBox.setDisable(true);
  }

  public void locationVBox() {
    inputVBox.getChildren().clear();
    for (int i = 0; i < locationFields.size(); i++) {
      inputVBox.getChildren().add(locationFields.get(i).label);
      inputVBox.getChildren().add(locationFields.get(i).textArea);
      locationFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
  }

  public void equipmentVBox() {
    inputVBox.getChildren().clear();
    for (int i = 0; i < equipmentFields.size(); i++) {
      inputVBox.getChildren().add(equipmentFields.get(i).label);
      inputVBox.getChildren().add(equipmentFields.get(i).textArea);
      equipmentFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
  }

  public void srVBox() {
    inputVBox.getChildren().clear();
    for (int i = 0; i < srFields.size(); i++) {
      inputVBox.getChildren().add(srFields.get(i).label);
      inputVBox.getChildren().add(srFields.get(i).textArea);
      srFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
  }

  // Selected Location
  public void existingLocationSelected(Location selectedLocation) {
    inputVBox.setDisable(false);
    locationVBox();

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
    */

  }

  // Existing Equipment Selected
  public void existingEquipmentSelected(Equipment equipment /*List<Location> locations*/) {
    inputVBox.setDisable(false);
    equipmentVBox();
    /*
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
    8/
     */
  }

  // Existing Service Request Selected
  public void existingServiceRequestSelected(SR serviceRequest) {
    inputVBox.setDisable(false);
    srVBox();
    /*
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
    8/
     */
  }

  // Edit Location
  public void editLocation() {
    editButton.setDisable(true);
    saveButton.setDisable(false);
    clearButton.setDisable(false);
    deleteButton.setDisable(false);

    /*
    nodeIDText.setEditable(true);
    xPosText.setEditable(true);
    yPosText.setEditable(true);
    floorText.setEditable(true);
    buildingText.setEditable(true);
    typeText.setEditable(true);
    longnameText.setEditable(true);
    shortnameText.setEditable(true);

     */
  }

  // Clear Submission
  public void clearSubmission() {
    /*
    nodeIDText.setText("");
    xPosText.setText("");
    yPosText.setText("");
    floorText.setText("");
    buildingText.setText("");
    typeText.setText("");
    longnameText.setText("");
    shortnameText.setText("");

     */
  }

  // Delete Location
  public void deleteLocation(
      LocationDAO locationDAO, JFXComboBox floorSelectionComboBox, String floorName) {
    /*
    locationDAO.deleteLocationNode(nodeIDText.getText());
    String originalFloorName = floorName;
    floorSelectionComboBox.setValue("Choose Floor");
    floorSelectionComboBox.setValue(originalFloorName);

           */
  }

  // Save Changes
  public void saveChanges(
      LocationMarker newLocationMarker,
      Location selectedLocation,
      JFXComboBox floorSelectionComboBox,
      LocationDAO locationDAO,
      String floorName) {
    /*
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

     */
  }

  // Update Medical Equipment / Service Request on Drag Release
  public void updateOnRelease(
      Button button, EquipmentDAO equipmentDAO, Map<Button, EquipmentMarker> buttonEquipmentMarker)
      throws SQLException {
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

  public JFXButton getEditButton() {
    return editButton;
  }

  public JFXButton getSaveButton() {
    return saveButton;
  }

  public JFXButton getClearButton() {
    return clearButton;
  }

  public JFXButton getDeleteButton() {
    return deleteButton;
  }

  class InfoField {
    Label label;
    TextArea textArea;

    public InfoField(Label label, TextArea textArea) {
      this.label = label;
      this.textArea = textArea;
    }
  }
}
