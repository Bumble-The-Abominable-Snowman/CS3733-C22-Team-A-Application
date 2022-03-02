package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.DataViewCtrl;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.map.CheckBoxManager;
import edu.wpi.cs3733.c22.teamA.entities.map.GesturePaneManager;
import edu.wpi.cs3733.c22.teamA.entities.map.LocationMarker;
import edu.wpi.cs3733.c22.teamA.entities.map.MarkerManager;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DVSelectionManager {
  private VBox inputVBox;

  private List<InfoField> locationFields;
  private List<InfoField> equipmentFields;
  private List<InfoField> srFields;
  private List<InfoField> employeeFields;
  private List<String> srNames;

  private List<InfoField> currentList;

  private JFXButton editButton;
  private JFXButton saveButton;
  private JFXButton clearButton;
  private JFXButton deleteButton;

  private LocationDAO locationDAO;
  private EmployeeDAO employeeDAO;
  private EquipmentDAO equipmentDAO;

  private DataViewCtrl dataViewCtrl;

  private Object selected;

  public DVSelectionManager(VBox inputVBox, DataViewCtrl dataViewCtrl) {
    this.inputVBox = inputVBox;
    this.inputVBox.setDisable(true);
    this.inputVBox.setVisible(false);
    instantiateButtons();
    fillBoxes();

    locationDAO = new LocationDerbyImpl();
    employeeDAO = new EmployeeDerbyImpl();
    equipmentDAO = new EquipmentDerbyImpl();
    currentList = new ArrayList<>();
    this.dataViewCtrl = dataViewCtrl;
  }

  public void select(RecursiveObj obj, int sceneFlag){
    this.inputVBox.setVisible(true);
    if(sceneFlag == 1){
      selected = obj.sr;
      serviceRequestSelected(obj.sr);
    } else if(sceneFlag == 2){
      selected = obj.loc;
      locationSelected(obj.loc);
    } else if(sceneFlag == 3){
      selected = obj.equip;
      equipmentSelected(obj.equip);
    } else if(sceneFlag == 4) {
      selected = obj.employee;
      employeeSelected(obj.employee);
    }  else if(sceneFlag == 5) {
      //medicineSelected(obj.med);
    }
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
            //save(selectedLocation);
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
          @SneakyThrows
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
    fillEmployee();
  }

  public void fillEmployee() {
    InfoField empID = new InfoField(new Label("Employee ID:"), new JFXTextArea());
    InfoField type = new InfoField(new Label("Type:"), new JFXTextArea());
    InfoField firstName = new InfoField(new Label("First Name:"), new JFXTextArea());
    InfoField lastName = new InfoField(new Label("Last Name:"), new JFXTextArea());
    InfoField email = new InfoField(new Label("Email:"), new JFXTextArea());
    InfoField number = new InfoField(new Label("Number:"), new JFXTextArea());
    InfoField address = new InfoField(new Label("Address:"), new JFXTextArea());
    InfoField startDate = new InfoField(new Label("Start Date:"), new JFXTextArea());
    employeeFields = List.of(empID, type, firstName, lastName, email, number, address, startDate);
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
      if(i != 0) locationFields.get(i).textArea.setEditable(false);
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
      if(i != 0) equipmentFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
    inputVBox.toFront();
  }

  public void employeeVBox() {
    clearVBox();
    for (int i = 0; i <employeeFields.size(); i++) {
      inputVBox.getChildren().add(employeeFields.get(i).label);
      inputVBox.getChildren().add(employeeFields.get(i).textArea);
      if(i != 0) employeeFields.get(i).textArea.setEditable(false);
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
      if(i != 0) srFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
    inputVBox.toFront();
  }

  // Selected Location
  public void locationSelected(Location selectedLocation) {
    inputVBox.setDisable(false);
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    currentList = locationFields;
    locationVBox();
    List<String> currentFields = selectedLocation.getListForm();
    for (int i = 0; i < currentFields.size(); i++) {
      locationFields.get(i).textArea.setText(currentFields.get(i));
    }
  }

  public void equipmentSelected(Equipment equipment) {
    inputVBox.setDisable(false);
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    currentList = equipmentFields;
    equipmentVBox();
    List<String> currentFields = equipment.getListForm();
    for (int i = 0; i < currentFields.size(); i++) {
      equipmentFields.get(i).textArea.setText(currentFields.get(i));
    }
  }

  // Existing Service Request Selected
  public void serviceRequestSelected(SR serviceRequest) {
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    inputVBox.setDisable(false);
    currentList = srFields;
    srVBox();
    HashMap<String, String> currentFields = serviceRequest.getStringFields();

    for (int i = 0; i < srFields.size(); i++) {
      srFields.get(i).textArea.setText(currentFields.get(srNames.get(i)));
    }
  }

  public void employeeSelected(Employee employee) {
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    inputVBox.setDisable(false);
    currentList = employeeFields;
    employeeVBox();
    List<String> currentFields = employee.getListForm();

    for (int i = 0; i < currentFields.size(); i++) {
      employeeFields.get(i).textArea.setText(currentFields.get(i));
    }
  }

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
  public void delete() throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
    if(selected instanceof SR){
      new ServiceRequestDerbyImpl((SR.SRType) ((SR) selected).getFields().get("sr_type")).deleteServiceRequest((SR) selected);
    } else if(selected instanceof Equipment){
      equipmentDAO.deleteMedicalEquipment(((Equipment) selected).getEquipmentID());
    } else if(selected instanceof Employee){
      employeeDAO.deleteEmployee(((Employee) selected).getEmployeeID());
    } else if(selected instanceof Location){
      locationDAO.deleteLocationNode(((Location) selected).getNodeID());
    }
    MasterCtrl.sceneFlags.add(MasterCtrl.sceneFlags.get(MasterCtrl.sceneFlags.size()-1));
    MasterCtrl.sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  // Save Changes
  public void save(LocationMarker location) {
    String nodeID = locationFields.get(0).textArea.getText();
    locationDAO.updateLocation(
        nodeID, "xCoord", (int) Double.parseDouble(locationFields.get(1).textArea.getText()));
    locationDAO.updateLocation(
        nodeID, "yCoord", (int) Double.parseDouble(locationFields.get(2).textArea.getText()));
    locationDAO.updateLocation(nodeID, "floor", locationFields.get(3).textArea.getText());
    locationDAO.updateLocation(nodeID, "building", locationFields.get(4).textArea.getText());
    locationDAO.updateLocation(nodeID, "node_type", locationFields.get(5).textArea.getText());
    locationDAO.updateLocation(nodeID, "long_Name", locationFields.get(6).textArea.getText());
    locationDAO.updateLocation(nodeID, "Short_Name", locationFields.get(7).textArea.getText());
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
