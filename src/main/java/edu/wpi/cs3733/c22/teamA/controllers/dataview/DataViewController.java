package edu.wpi.cs3733.c22.teamA.controllers.dataview;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.HomeCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import lombok.SneakyThrows;

public class DataViewController implements Initializable {

  @FXML
  public void deleteSelected(ActionEvent actionEvent)
      throws SQLException, InvocationTargetException, IllegalAccessException {
    System.out.println(table.getSelectionModel().getSelectedItem().getValue().sr);

    if (HomeCtrl.sceneFlag == 1) {
      ServiceRequestDerbyImpl<SR> serviceRequestDerby =
          new ServiceRequestDerbyImpl<>(table.getSelectionModel().getSelectedItem().getValue().sr);
      serviceRequestDerby.deleteServiceRequest(
          table.getSelectionModel().getSelectedItem().getValue().sr);
      titleLabel.setText("Service Requests");
      initializeRequestsTable();
    } else if (HomeCtrl.sceneFlag == 2) {
      LocationDAO locationDAO = new LocationDerbyImpl();
      locationDAO.deleteLocationNode(
          table.getSelectionModel().getSelectedItem().getValue().loc.getNodeID());
      titleLabel.setText("Rooms");
      initializeLocationTable();
    } else if (HomeCtrl.sceneFlag == 3) {
      EquipmentDAO equipmentDAO = new EquipmentDerbyImpl();
      equipmentDAO.deleteMedicalEquipment(
          table.getSelectionModel().getSelectedItem().getValue().equip.getEquipmentID());
      titleLabel.setText("Medical Equipment");
      initializeEquipmentTable();
    } else if (HomeCtrl.sceneFlag == 4) {
      EmployeeDAO employeeDAO = new EmployeeDerbyImpl();
      employeeDAO.deleteEmployee(
          table.getSelectionModel().getSelectedItem().getValue().employee.getEmployeeID());
      titleLabel.setText("Employees");
      initializeEmployeeTable();
    } else {
      // wait what how did you get here
    }
  }

  static class RecursiveObj extends RecursiveTreeObject<RecursiveObj> {
    public SR sr;
    public Location loc;
    public Equipment equip;
    public Employee employee;
  }

  @FXML JFXButton backButton;
  @FXML Label titleLabel;
  @FXML JFXTreeTableView<RecursiveObj> table;

  boolean fillerYes = true;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @SneakyThrows
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    double backTextSize = backButton.getFont().getSize();
    double titleTextSize = titleLabel.getFont().getSize();
    // double tableTextSize = table.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              backButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * backTextSize) + "pt;");
              titleLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * titleTextSize) + "pt;");
            });

    if (HomeCtrl.sceneFlag == 1) {
      titleLabel.setText("Service Requests");
      initializeRequestsTable();
    } else if (HomeCtrl.sceneFlag == 2) {
      titleLabel.setText("Rooms");
      initializeLocationTable();
    } else if (HomeCtrl.sceneFlag == 3) {
      titleLabel.setText("Medical Equipment");
      initializeEquipmentTable();
    } else if (HomeCtrl.sceneFlag == 4) {
      titleLabel.setText("Employees");
      initializeEmployeeTable();
    } else {
      // wait what how did you get here
    }
  }

  public void initializeLocationTable() {
    // Create all columns in the tracker table
    JFXTreeTableColumn<RecursiveObj, String> id = new JFXTreeTableColumn<>("ID");
    JFXTreeTableColumn<RecursiveObj, String> name = new JFXTreeTableColumn<>("Room Name");
    JFXTreeTableColumn<RecursiveObj, String> floorNum = new JFXTreeTableColumn<>("Floor Number");
    JFXTreeTableColumn<RecursiveObj, String> storeXRAY = new JFXTreeTableColumn<>("Store XRAY?");
    JFXTreeTableColumn<RecursiveObj, String> storeBed = new JFXTreeTableColumn<>("Store Beds?");
    JFXTreeTableColumn<RecursiveObj, String> storePump =
        new JFXTreeTableColumn<>("Store I. Pumps?");
    JFXTreeTableColumn<RecursiveObj, String> storeRecliner =
        new JFXTreeTableColumn<>("Store Recliners?");

    id.setPrefWidth(80);
    name.setPrefWidth(80);
    floorNum.setPrefWidth(80);
    storeXRAY.setPrefWidth(80);
    storeBed.setPrefWidth(80);
    storePump.setPrefWidth(80);
    storeRecliner.setPrefWidth(80);

    id.setStyle("-fx-alignment: center ;");
    name.setStyle("-fx-alignment: center ;");
    floorNum.setStyle("-fx-alignment: center ;");
    storeXRAY.setStyle("-fx-alignment: center ;");
    storeBed.setStyle("-fx-alignment: center ;");
    storePump.setStyle("-fx-alignment: center ;");
    storeRecliner.setStyle("-fx-alignment: center ;");

    id.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().loc.getNodeID()));
    name.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().loc.getLongName()));
    floorNum.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().loc.getFloor()));
    // the following code has to be implemented when a location's medical equipment is known.
    // eahc is currently filled with filler Yes values, which represent that atm every room
    // can store every equipment. In the future, this will not be true and must be updated
    storeXRAY.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreXRAY()));
    storeBed.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreBed()));
    storePump.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    // new SimpleStringProperty(param.getValue().getValue().getCanStorePump()));
    storeRecliner.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreRecliner()));

    // Grab location / equipment from database, these are dummies
    LocationDAO locationBase = new LocationDerbyImpl();
    List<Location> locationFromDatabase = locationBase.getNodeList();
    ObservableList<RecursiveObj> locations = FXCollections.observableArrayList();
    for (Location currLoc : locationFromDatabase) {
      RecursiveObj recursiveLoc = new RecursiveObj();
      recursiveLoc.loc = currLoc;
      locations.add(recursiveLoc);
    }
    // Sets up the table and puts the location data under the columns
    final TreeItem<RecursiveObj> root =
        new RecursiveTreeItem<>(locations, RecursiveTreeObject::getChildren);
    this.table
        .getColumns()
        .setAll(id, name, floorNum, storeXRAY, storeBed, storePump, storeRecliner);
    this.table.setRoot(root);
  }

  public void initializeRequestsTable()
      throws SQLException, InvocationTargetException, IllegalAccessException {

    // Create all columns in the tracker table
    JFXTreeTableColumn<RecursiveObj, String> reqType = new JFXTreeTableColumn<>("Type");
    JFXTreeTableColumn<RecursiveObj, String> reqID = new JFXTreeTableColumn<>("ID");
    JFXTreeTableColumn<RecursiveObj, String> startLoc = new JFXTreeTableColumn<>("Start Location");
    JFXTreeTableColumn<RecursiveObj, String> endLoc = new JFXTreeTableColumn<>("End Location");
    JFXTreeTableColumn<RecursiveObj, String> employeeReq = new JFXTreeTableColumn<>("Requested By");
    JFXTreeTableColumn<RecursiveObj, String> employeeAss =
        new JFXTreeTableColumn<>("Employee Assigned");
    JFXTreeTableColumn<RecursiveObj, String> reqTime = new JFXTreeTableColumn<>("Request Time");
    JFXTreeTableColumn<RecursiveObj, String> reqStatus = new JFXTreeTableColumn<>("Status");
    JFXTreeTableColumn<RecursiveObj, String> reqPriority = new JFXTreeTableColumn<>("Priority");
    JFXTreeTableColumn<RecursiveObj, String> comments = new JFXTreeTableColumn<>("Comments");

    reqType.setPrefWidth(80);
    reqID.setPrefWidth(80);
    startLoc.setPrefWidth(80);
    endLoc.setPrefWidth(80);
    employeeReq.setPrefWidth(80);
    employeeAss.setPrefWidth(80);
    reqTime.setPrefWidth(80);
    reqStatus.setPrefWidth(80);
    comments.setPrefWidth(80);
    reqPriority.setPrefWidth(80);

    reqType.setStyle("-fx-alignment: center ;");
    reqID.setStyle("-fx-alignment: center ;");
    startLoc.setStyle("-fx-alignment: center ;");
    endLoc.setStyle("-fx-alignment: center ;");
    employeeReq.setStyle("-fx-alignment: center ;");
    employeeAss.setStyle("-fx-alignment: center ;");
    reqTime.setStyle("-fx-alignment: center ;");
    reqStatus.setStyle("-fx-alignment: center ;");
    comments.setStyle("-fx-alignment: center ;");
    reqPriority.setStyle("-fx-alignment: center ;");

    reqType.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getSrType().toString()));
    reqID.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getRequestID()));
    startLoc.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getStartLocation()));
    endLoc.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getEndLocation()));
    employeeReq.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getEmployeeRequested()));
    employeeAss.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getEmployeeAssigned()));
    reqTime.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getRequestTime()));
    reqStatus.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getRequestStatus()));
    comments.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getComments()));
    reqPriority.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().sr.getRequestPriority()));
    //    ServiceRequestDAO serviceRequestBase = new ServiceRequestDerbyImpl(new FoodDeliverySR());

    List<?> srList = ServiceRequestDerbyImpl.getAllServiceRequestList();
    ObservableList<RecursiveObj> requests = FXCollections.observableArrayList();
    for (Object sr : srList) {
      RecursiveObj recursiveSR = new RecursiveObj();
      recursiveSR.sr = (SR) sr;
      requests.add(recursiveSR);
    }

    // Sets up the table and puts the equipment data under the columns
    final TreeItem<RecursiveObj> root =
        new RecursiveTreeItem<RecursiveObj>(requests, RecursiveTreeObject::getChildren);

    table
        .getColumns()
        .setAll(
            reqType,
            reqID,
            startLoc,
            endLoc,
            employeeReq,
            employeeAss,
            reqTime,
            reqStatus,
            reqPriority,
            comments);
    table.setRoot(root);
  }

  public void initializeEquipmentTable() {
    // Create all columns in the tracker table
    JFXTreeTableColumn<RecursiveObj, String> id = new JFXTreeTableColumn<>("ID");
    JFXTreeTableColumn<RecursiveObj, String> type = new JFXTreeTableColumn<>("Type");
    JFXTreeTableColumn<RecursiveObj, String> clean = new JFXTreeTableColumn<>("Is Clean?");
    JFXTreeTableColumn<RecursiveObj, String> location = new JFXTreeTableColumn<>("Location");
    JFXTreeTableColumn<RecursiveObj, String> available = new JFXTreeTableColumn<>("Is Available?");
    id.setPrefWidth(112);
    type.setPrefWidth(110);
    clean.setPrefWidth(110);
    location.setPrefWidth(115);
    available.setPrefWidth(110);
    id.setStyle("-fx-alignment: center ;");
    type.setStyle("-fx-alignment: center ;");
    clean.setStyle("-fx-alignment: center ;");
    location.setStyle("-fx-alignment: center ;");
    available.setStyle("-fx-alignment: center ;");
    id.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().equip.getEquipmentID()));
    type.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().equip.getEquipmentType()));
    clean.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(
                param.getValue().getValue().equip.getIsClean() ? "Yes" : "No"));
    location.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().equip.getCurrentLocation()));
    available.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(
                param.getValue().getValue().equip.getIsAvailable() ? "Yes" : "No"));

    // Grab equipment from database
    EquipmentDAO database = new EquipmentDerbyImpl();
    List<Equipment> equipFromDatabase = database.getMedicalEquipmentList();
    ObservableList<RecursiveObj> equipment = FXCollections.observableArrayList();
    for (Equipment item : equipFromDatabase) {
      RecursiveObj recursiveEquipment = new RecursiveObj();
      recursiveEquipment.equip = item;
      equipment.add(recursiveEquipment);
    }

    // Sets up the table and puts the equipment data under the columns
    final TreeItem<RecursiveObj> root =
        new RecursiveTreeItem<RecursiveObj>(equipment, RecursiveTreeObject::getChildren);
    table.getColumns().setAll(id, type, clean, location, available);
    table.setRoot(root);
  }

  boolean isSelected() {
    return false;
  }

  public void initializeEmployeeTable() {
    // Create all columns in the tracker table
    JFXTreeTableColumn<RecursiveObj, String> employeeID = new JFXTreeTableColumn<>("ID");
    JFXTreeTableColumn<RecursiveObj, String> employeeType = new JFXTreeTableColumn<>("Type");
    JFXTreeTableColumn<RecursiveObj, String> firstName = new JFXTreeTableColumn<>("First Name");
    JFXTreeTableColumn<RecursiveObj, String> lastName = new JFXTreeTableColumn<>("Last Name");
    JFXTreeTableColumn<RecursiveObj, String> email = new JFXTreeTableColumn<>("Email");
    JFXTreeTableColumn<RecursiveObj, String> phoneNum = new JFXTreeTableColumn<>("Phone Number");
    JFXTreeTableColumn<RecursiveObj, String> address = new JFXTreeTableColumn<>("Address");
    JFXTreeTableColumn<RecursiveObj, String> startDate = new JFXTreeTableColumn<>("Start Date");

    employeeID.setPrefWidth(80);
    employeeType.setPrefWidth(80);
    firstName.setPrefWidth(80);
    lastName.setPrefWidth(80);
    email.setPrefWidth(80);
    phoneNum.setPrefWidth(80);
    address.setPrefWidth(80);
    startDate.setPrefWidth(80);

    // TODO CSS Sheet
    employeeID.setStyle("-fx-alignment: center ;");
    employeeType.setStyle("-fx-alignment: center ;");
    firstName.setStyle("-fx-alignment: center ;");
    lastName.setStyle("-fx-alignment: center ;");
    email.setStyle("-fx-alignment: center ;");
    phoneNum.setStyle("-fx-alignment: center ;");
    address.setStyle("-fx-alignment: center ;");
    startDate.setStyle("-fx-alignment: center ;");

    employeeID.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().employee.getEmployeeID()));
    employeeType.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().employee.getEmployeeType()));
    firstName.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().employee.getFirstName()));
    // the following code has to be implemented when a location's medical equipment is known.
    // eahc is currently filled with filler Yes values, which represent that atm every room
    // can store every equipment. In the future, this will not be true and must be updated
    lastName.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().employee.getLastName()));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreXRAY()));
    email.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().employee.getEmail()));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreBed()));
    phoneNum.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().employee.getPhoneNum()));
    // new SimpleStringProperty(param.getValue().getValue().getCanStorePump()));
    address.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().employee.getAddress()));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreRecliner()));
    startDate.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(
                param.getValue().getValue().employee.getStartDate().toString()));

    // Grab location / equipment from database, these are dummies
    EmployeeDAO employeeBase = new EmployeeDerbyImpl();
    List<Employee> employeeFromDatabase = employeeBase.getEmployeeList();
    ObservableList<RecursiveObj> employees = FXCollections.observableArrayList();
    for (Employee anEmployee : employeeFromDatabase) {
      RecursiveObj recursiveEmployee = new RecursiveObj();
      recursiveEmployee.employee = anEmployee;
      employees.add(recursiveEmployee);
    }

    // Sets up the table and puts the location data under the columns
    final TreeItem<RecursiveObj> root =
        new RecursiveTreeItem<>(employees, RecursiveTreeObject::getChildren);
    table
        .getColumns()
        .setAll(employeeID, employeeType, firstName, lastName, email, phoneNum, address, startDate);
    table.setRoot(root);
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }
}
