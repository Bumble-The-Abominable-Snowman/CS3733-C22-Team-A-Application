package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class SelectionManager {
  private VBox inputVBox;

  private List<InfoField> locationFields;
  private List<InfoField> equipmentFields;
  private List<InfoField> srFields;

  private List<InfoField> currentList;

  private JFXButton editButton;
  private JFXButton saveButton;
  private JFXButton clearButton;
  private JFXButton deleteButton;

  public SelectionManager(VBox inputVBox) {
    this.inputVBox = inputVBox;
    inputVBox.setDisable(true);
    instantiateButtons();
    fillBoxes();

    currentList = new ArrayList<>();
  }

  public void instantiateButtons() {
    editButton = new JFXButton("Edit");
    editButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            edit();
          }
        });
    saveButton = new JFXButton("Save");
    saveButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            save();
          }
        });
    clearButton = new JFXButton("Clear");
    clearButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            clear();
          }
        });
    deleteButton = new JFXButton("Delete");
    deleteButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            delete();
          }
        });
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
    currentList = locationFields;
    locationVBox();
    List<String> currentFields = selectedLocation.getListForm();
    for (int i = 0; i < currentFields.size(); i++) {
      locationFields.get(i).textArea.setText(currentFields.get(i));
    }
    /*
    if (xPosText.getText() == null || xPosText.getText().equals("")) {
      xPosText.setText(String.valueOf(selectedLocation.getXCoord()));
      yPosText.setText(String.valueOf(selectedLocation.getYCoord()));
    }
    */
  }

  // Existing Equipment Selected
  public void existingEquipmentSelected(Equipment equipment /*List<Location> locations*/) {
    inputVBox.setDisable(false);
    currentList = equipmentFields;
    equipmentVBox();
    List<String> currentFields = equipment.getListForm();
    for (int i = 0; i < currentFields.size(); i++) {
      equipmentFields.get(i).textArea.setText(currentFields.get(i));
    }
  }

  // Existing Service Request Selected
  public void existingServiceRequestSelected(SR serviceRequest) {
    inputVBox.setDisable(false);
    currentList = srFields;
    srVBox();
    List<String> currentFields = serviceRequest.getListForm();
    for (int i = 0; i < currentFields.size(); i++) {
      srFields.get(i).textArea.setText(currentFields.get(i));
    }
  }

  // Edit
  public void edit() {
    for (InfoField i : currentList) {
      i.textArea.setEditable(true);
    }
    saveButton.setDisable(false);
    clearButton.setDisable(false);
  }

  // Clear
  public void clear() {
    for (InfoField i : currentList) {
      i.textArea.setText("");
    }
  }

  // Delete Location
  public void delete() {
    // TODO delete
    /*
    locationDAO.deleteLocationNode(nodeIDText.getText());
    String originalFloorName = floorName;
    floorSelectionComboBox.setValue("Choose Floor");
    floorSelectionComboBox.setValue(originalFloorName);

           */
  }

  // Save Changes
  public void save() {
    // TODO
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
  /*
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

  }
  */

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

  public List<InfoField> getCurrentList() {
    return currentList;
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
