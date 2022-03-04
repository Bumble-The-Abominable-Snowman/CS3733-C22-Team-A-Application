package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.DataViewCtrl;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.SneakyThrows;
import org.w3c.dom.Text;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DVSelectionManager {
  private VBox inputVBox;

  private List<InfoField> locationFields;
  private List<InfoField> equipmentFields;
  private List<InfoField> srFields;
  private List<InfoField> employeeFields;
  private List<InfoField> medicineFields;
  private List<String> srNames;

  private List<InfoField> currentList;

  private JFXButton editButton;
  private JFXButton saveButton;
  private JFXButton clearButton;
  private JFXButton deleteButton;

  private LocationDAO locationDAO;
  private EmployeeDAO employeeDAO;
  private EquipmentDAO equipmentDAO;
  private MedicineDAO medicineDAO;

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
    medicineDAO = new MedicineDerbyImpl();

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
      System.out.println("here");
      selected = obj.med;
      medicineSelected(obj.med);
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
    fillMedicine();
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

  public void fillMedicine(){
    InfoField medicineID = new InfoField(new Label("Medicine ID"), new JFXTextArea());
    InfoField genericName = new InfoField(new Label("Generic Name:"), new JFXTextArea());
    InfoField brandName = new InfoField(new Label("Brand Name:"), new JFXTextArea());
    InfoField medicineClass = new InfoField(new Label("Medicine Class:"), new JFXTextArea());
    InfoField uses = new InfoField(new Label("Uses:"), new JFXTextArea());
    InfoField warning = new InfoField(new Label("Warnings:"), new JFXTextArea());
    InfoField sideEffects= new InfoField(new Label("Side Effects:"), new JFXTextArea());
    InfoField form = new InfoField(new Label("Form:"), new JFXTextArea());
    InfoField dosage = new InfoField(new Label("Dosage:"), new JFXTextArea());
    medicineFields = List.of(medicineID, genericName, brandName, medicineClass, uses, warning, sideEffects, form, dosage);
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

  public void medicineVBox(){
    clearVBox();
    for(int i = 0; i < medicineFields.size(); i++){
      inputVBox.getChildren().add(medicineFields.get(i).label);
      inputVBox.getChildren().add(medicineFields.get(i).textArea);
      medicineFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
    inputVBox.toFront();
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

  public void employeeVBox() {
    clearVBox();
    for (int i = 0; i <employeeFields.size(); i++) {
      inputVBox.getChildren().add(employeeFields.get(i).label);
      inputVBox.getChildren().add(employeeFields.get(i).textArea);
      employeeFields.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
    inputVBox.toFront();
  }

  public void srVBox(SR sr) {
    clearVBox();
    srExtraField(sr);
    for (int i = 0; i < currentList.size(); i++) {
      inputVBox.getChildren().add(currentList.get(i).label);
      inputVBox.getChildren().add(currentList.get(i).textArea);
      currentList.get(i).textArea.setEditable(false);
    }
    inputVBox.getChildren().add(editButton);
    inputVBox.getChildren().add(clearButton);
    inputVBox.getChildren().add(saveButton);
    inputVBox.getChildren().add(deleteButton);
    inputVBox.toFront();
  }

  private List<InfoField> additionalFields;

  private void srExtraField(SR sr){
    HashMap<String, String> currentFields = sr.getStringFields();
    for (int i = 0; i < srNames.size(); i++) {
      currentList.get(i).textArea.setText(currentFields.get(srNames.get(i)));
    }

    for (String key : sr.getStringFields().keySet()) {
      if (!(Objects.equals(key, "request_id")
              || Objects.equals(key, "start_location")
              || Objects.equals(key, "end_location")
              || Objects.equals(key, "employee_requested")
              || Objects.equals(key, "employee_assigned")
              || Objects.equals(key, "request_time")
              || Objects.equals(key, "request_status")
              || Objects.equals(key, "request_priority")
              || Objects.equals(key, "comments")
              || Objects.equals(key, "sr_type"))) {

        currentList.add(new InfoField(new Label(key),  new JFXTextArea(sr.getFields_string().get(key))));
      }
    }
  }

  private void medicineSelected(Medicine medicine){
    inputVBox.setDisable(false);
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    currentList = new ArrayList<>(medicineFields);
    medicineVBox();
    List<String> currentFields = medicine.getListForm();
    for (int i = 0; i < currentFields.size(); i++) {
      medicineFields.get(i).textArea.setText(currentFields.get(i));
    }
  }

  // Selected Location
  public void locationSelected(Location selectedLocation) {
    inputVBox.setDisable(false);
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    currentList = new ArrayList<>(locationFields);
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
    currentList = new ArrayList<>(equipmentFields);
    equipmentVBox();
    List<String> currentFields = equipment.getListForm();
    for (int i = 0; i < currentFields.size(); i++) {
      equipmentFields.get(i).textArea.setText(currentFields.get(i));
    }
  }

  // Existing Service Request Selected
  private void serviceRequestSelected(SR serviceRequest) {
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    inputVBox.setDisable(false);
    currentList = new ArrayList<>(srFields);
    srVBox(serviceRequest);

  }

  public void employeeSelected(Employee employee) {
    saveButton.setDisable(true);
    clearButton.setDisable(true);
    inputVBox.setDisable(false);
    currentList = new ArrayList<>(employeeFields);
    employeeVBox();
    List<String> currentFields = employee.getListForm();

    for (int i = 0; i < currentFields.size(); i++) {
      employeeFields.get(i).textArea.setText(currentFields.get(i));
    }
  }

  public void edit() {
    for (int i = 1; i < currentList.size(); i++) {
      currentList.get(i).textArea.setEditable(true);
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

  // Delete Service Request
  public void delete() throws SQLException, IOException {
    if(selected instanceof SR){
      new ServiceRequestDerbyImpl((SR.SRType) ((SR) selected).getFields().get("sr_type")).deleteServiceRequest((SR) selected);
    } else if(selected instanceof Equipment){
      equipmentDAO.deleteMedicalEquipment(((Equipment) selected).getStringFields().get("equipment_id"));
    } else if(selected instanceof Employee){
      employeeDAO.deleteEmployee(((Employee) selected).getStringFields().get("employee_id"));
    } else if(selected instanceof Location){
      locationDAO.deleteLocationNode(((Location) selected).getStringFields().get("node_id"));
    } else if(selected instanceof Medicine){
      medicineDAO.deleteMedicine(((Medicine) selected).getMedicineID());
    }
    MasterCtrl.sceneFlags.add(MasterCtrl.sceneFlags.get(MasterCtrl.sceneFlags.size()-1));
    MasterCtrl.sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  // Save Changes
  private void save() throws SQLException, IOException, ParseException, IllegalAccessException, InvocationTargetException {
    if(selected instanceof SR){
      SR newSR = (SR)selected;
      ServiceRequestDAO dao = new ServiceRequestDerbyImpl((SR.SRType) ((SR) selected).getFields().get("sr_type"));
      int ct = 0;
      for(String key: srNames){
        newSR.setFieldByString(key, srFields.get(ct).textArea.getText());
        ct++;
      }
      dao.updateServiceRequest(newSR);
    } else if(selected instanceof Equipment){
      saveEquipment();
    } else if(selected instanceof Employee){
      saveEmployee();
    } else if(selected instanceof Location){
      saveLocation();
    }
    MasterCtrl.sceneFlags.add(MasterCtrl.sceneFlags.get(MasterCtrl.sceneFlags.size()-1));
    MasterCtrl.sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
  }

  private void saveEquipment() throws SQLException {
    Equipment newEquipment = new Equipment(currentList.get(0).textArea.getText(), currentList.get(1).textArea.getText(),
            Boolean.parseBoolean(currentList.get(2).textArea.getText()), currentList.get(3).textArea.getText(), Boolean.parseBoolean(currentList.get(4).textArea.getText()));
    Location l = locationDAO.getLocationNode(newEquipment.getStringFields().get("current_location"));
    if (l.getStringFields().get("node_id") == null) {
      JOptionPane pane = new JOptionPane("Location does not exist", JOptionPane.ERROR_MESSAGE);
      JDialog dialog = pane.createDialog("Update failed");
      dialog.setVisible(true);
      return;
    }
    if (newEquipment.getFields().get("is_clean").equals(false)) {
      JOptionPane pane = new JOptionPane("Dirty equipment cannot be moved", JOptionPane.ERROR_MESSAGE);
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
    equipmentDAO.updateMedicalEquipment(newEquipment);
  }

  private void saveEmployee() throws ParseException, SQLException {

    Employee newEmployee = new Employee(currentList.get(0).textArea.getText(), currentList.get(1).textArea.getText(),
            currentList.get(2).textArea.getText(), currentList.get(3).textArea.getText(),currentList.get(4).textArea.getText(), currentList.get(5).textArea.getText(), currentList.get(6).textArea.getText(), new SimpleDateFormat("yyyy-MM-dd").parse(currentList.get(7).textArea.getText()));

    employeeDAO.updateEmployee(newEmployee);
  }

  private void saveLocation() throws SQLException {
    Location newLocation = new Location(
            currentList.get(0).textArea.getText(),
            Integer.parseInt(currentList.get(1).textArea.getText()),
            Integer.parseInt(currentList.get(2).textArea.getText()),
            currentList.get(3).textArea.getText(),
            currentList.get(4).textArea.getText(),
            currentList.get(5).textArea.getText(),
            currentList.get(6).textArea.getText(),
            currentList.get(7).textArea.getText()
    );
    locationDAO.updateLocation(newLocation);
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
