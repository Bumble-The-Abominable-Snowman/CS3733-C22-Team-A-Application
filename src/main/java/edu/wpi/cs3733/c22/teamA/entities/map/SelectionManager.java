package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.util.ArrayList;
import java.util.HashMap;
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
  private List<String> srNames;

  private List<InfoField> currentList;

  private JFXButton editButton;
  private JFXButton saveButton;
  private JFXButton clearButton;
  private JFXButton deleteButton;

  private MarkerManager markerManager;
  private CheckBoxManager checkBoxManager;
  private GesturePaneManager gesturePaneManager;

  private LocationDAO locationDatabase;

  private LocationMarker selectedLocation;

  public SelectionManager(VBox inputVBox, MarkerManager markerManager) {
    this.inputVBox = inputVBox;
    this.markerManager = markerManager;
    inputVBox.setDisable(true);
    instantiateButtons();
    fillBoxes();

    locationDatabase = new LocationDerbyImpl();
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
            save(selectedLocation);
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
            delete(selectedLocation);
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
    srNames = List.of("request_id", "start_location", "end_location", "employee_requested", "employee_assigned", "request_time", "request_status", "request_priority", "comments" );
  }

  public void clearVBox() {
    inputVBox.getChildren().clear();
  }

  public void locationVBox() {
    clearVBox();
    for (int i = 0; i < locationFields.size(); i++) {
      inputVBox.getChildren().add(locationFields.get(i).label);
      inputVBox.getChildren().add(locationFields.get(i).textArea);
      locationFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
    inputVBox.toFront();
  }

  public void equipmentVBox() {
    clearVBox();
    for (int i = 0; i < equipmentFields.size(); i++) {
      inputVBox.getChildren().add(equipmentFields.get(i).label);
      inputVBox.getChildren().add(equipmentFields.get(i).textArea);
      equipmentFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
    inputVBox.toFront();
  }

  public void srVBox() {
    clearVBox();
    for (int i = 0; i < srFields.size(); i++) {
      inputVBox.getChildren().add(srFields.get(i).label);
      inputVBox.getChildren().add(srFields.get(i).textArea);
      srFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
    inputVBox.toFront();
  }

  // Selected Location
  public void existingLocationSelected(LocationMarker selectedLocation) {
    inputVBox.setDisable(false);
    currentList = locationFields;
    locationVBox();
    List<String> currentFields = selectedLocation.getLocation().getListForm();
    for (int i = 0; i < currentFields.size(); i++) {
      locationFields.get(i).textArea.setText(currentFields.get(i));
    }
    this.selectedLocation = selectedLocation;
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
      if(i == 2){
        System.out.println(currentFields.get(i));
      }
    }
  }

  // Existing Service Request Selected
  public void existingServiceRequestSelected(SR serviceRequest) {
    inputVBox.setDisable(false);
    currentList = srFields;
    srVBox();
    HashMap<String, String> currentFields = serviceRequest.getStringFields();

    for (int i = 0; i < srFields.size(); i++) {
      srFields.get(i).textArea.setText(currentFields.get(srNames.get(i)));
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
  public void delete(LocationMarker location) {
    locationDatabase.deleteLocationNode(location.getLocation().getNodeID());
    location.setButtonVisibility(false);
    location.setLabelVisibility(false);
    clear();
    markerManager.getLocationMarkers().remove(location);
    markerManager.midRunDraw();
  }

  // Save Changes
  public void save(LocationMarker location) {
    String nodeID = locationFields.get(0).textArea.getText();
    locationDatabase.updateLocation(
        nodeID, "xCoord", (int) Double.parseDouble(locationFields.get(1).textArea.getText()));
    locationDatabase.updateLocation(
        nodeID, "yCoord", (int) Double.parseDouble(locationFields.get(2).textArea.getText()));
    locationDatabase.updateLocation(nodeID, "floor", locationFields.get(3).textArea.getText());
    locationDatabase.updateLocation(nodeID, "building", locationFields.get(4).textArea.getText());
    locationDatabase.updateLocation(nodeID, "node_type", locationFields.get(5).textArea.getText());
    locationDatabase.updateLocation(nodeID, "long_Name", locationFields.get(6).textArea.getText());
    locationDatabase.updateLocation(nodeID, "Short_Name", locationFields.get(7).textArea.getText());
    markerManager.redrawEditedLocation();
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
