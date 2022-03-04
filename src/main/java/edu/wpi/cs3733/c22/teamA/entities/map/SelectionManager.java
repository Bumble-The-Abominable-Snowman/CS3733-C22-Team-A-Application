package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;

import javax.swing.*;

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
  private MapManager mapManager;
  private GesturePaneManager gesturePaneManager;

  private LocationDAO locationDatabase;
  private EquipmentDAO equipmentDatabase;
  private LocationMarker selectedLocation;
  private Object selectedObject;

  public SelectionManager(VBox inputVBox, MarkerManager markerManager, GesturePaneManager gesturePaneManager) {
    this.inputVBox = inputVBox;
    this.markerManager = markerManager;
    this.gesturePaneManager = gesturePaneManager;
    inputVBox.setDisable(true);
    instantiateButtons();
    fillBoxes();

    locationDatabase = new LocationDerbyImpl();
    equipmentDatabase = new EquipmentDerbyImpl();
    currentList = new ArrayList<>();
  }

  public void setMapManager(MapManager mapManager){
    this.mapManager = mapManager;
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
          @SneakyThrows
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
      if(i != 1 && i != 2){
        locationFields.get(i).textArea.setText(currentFields.get(i));
      }
      if((i == 1 || i == 2) && (locationFields.get(i).textArea.getText().equals("") || locationFields.get(i).textArea.getText().equals(null))){
        locationFields.get(i).textArea.setText(currentFields.get(i));
      }

    }
    this.selectedLocation = selectedLocation;
    selectedObject = selectedLocation.getLocation();

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
    selectedObject = equipment;
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
    selectedObject = serviceRequest;
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
    locationDatabase.deleteLocationNode(location.getLocation().getStringFields().get("node_id"));
    location.setButtonVisibility(false);
    location.setLabelVisibility(false);
    clear();
    markerManager.getLocationMarkers().remove(location);
    markerManager.midRunDraw();
  }

  // Save Changes
  private void save() throws SQLException, IllegalAccessException, InvocationTargetException {
    if(selectedObject instanceof SR){
      SR newSR = (SR)selectedObject;
      ServiceRequestDAO dao = new ServiceRequestDerbyImpl((SR.SRType) ((SR) selectedObject).getFields().get("sr_type"));
      int ct = 0;
      for(String key: srNames){
        newSR.setFieldByString(key, srFields.get(ct).textArea.getText());
        ct++;
      }
      dao.updateServiceRequest(newSR);
    } else if(selectedObject instanceof Equipment){
      saveEquipment();
    } else if(selectedObject instanceof Location){
      saveLocation();
    }
    mapManager.initFloor("Floor " + gesturePaneManager.getCurrentFloor(), (int)gesturePaneManager.getMapImageView().getLayoutX(), (int)gesturePaneManager.getMapImageView().getLayoutY());
  }

  private void saveEquipment() throws SQLException {
    Equipment oldEquipment = (Equipment)selectedObject;
    Location lOld = locationDatabase.getLocationNode(oldEquipment.getStringFields().get("current_location"));
    Equipment newEquipment = new Equipment(currentList.get(0).textArea.getText(), currentList.get(1).textArea.getText(),
            Boolean.parseBoolean(currentList.get(2).textArea.getText()), currentList.get(3).textArea.getText(), Boolean.parseBoolean(currentList.get(4).textArea.getText()));
    Location l = locationDatabase.getLocationNode(newEquipment.getStringFields().get("current_location"));
    if (l.getStringFields().get("node_id") == null) {
      JOptionPane pane = new JOptionPane("Location does not exist", JOptionPane.ERROR_MESSAGE);
      JDialog dialog = pane.createDialog("Update failed");
      dialog.setVisible(true);
      return;
    }
    if (!(l.getStringFields().get("node_type").equals("STOR")) && !(l.getStringFields().get("node_type").equals("PATI"))) {
      JOptionPane pane = new JOptionPane("Equipment cannot be stored here", JOptionPane.ERROR_MESSAGE);
      JDialog dialog = pane.createDialog("Update failed");
      dialog.setVisible(true);
      return;
    }
    if (!oldEquipment.getFields().get("is_clean").equals(true) && newEquipment.getFields().get("is_clean").equals(false) && !l.getStringFields().get("node_id").equals(lOld.getStringFields().get("node_id"))) {
      JOptionPane pane = new JOptionPane("Dirty equipment cannot be moved", JOptionPane.ERROR_MESSAGE);
      JDialog dialog = pane.createDialog("Update failed");
      dialog.setVisible(true);
      return;
    }
    equipmentDatabase.updateMedicalEquipment(newEquipment);
  }

  private void saveLocation() throws SQLException {
    Location newLocation = new Location(
            currentList.get(0).textArea.getText(),
            (int)Double.parseDouble(currentList.get(1).textArea.getText()),
            (int)Double.parseDouble(currentList.get(2).textArea.getText()),
            currentList.get(3).textArea.getText(),
            currentList.get(4).textArea.getText(),
            currentList.get(5).textArea.getText(),
            currentList.get(6).textArea.getText(),
            currentList.get(7).textArea.getText()
    );
    locationDatabase.updateLocation(newLocation);
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
