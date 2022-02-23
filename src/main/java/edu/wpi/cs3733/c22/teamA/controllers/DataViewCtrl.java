package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

public class DataViewCtrl extends MasterCtrl {

  @FXML private VBox inputVBox;
  @FXML private JFXComboBox selectEmployeeBox;
  @FXML private JFXButton saveButton;
  @FXML private JFXButton editButton;
  @FXML private JFXButton clearButton;
  @FXML private JFXButton deleteButton;

  @FXML
  public void delete() throws SQLException, InvocationTargetException, IllegalAccessException {
    System.out.println(table.getSelectionModel().getSelectedItem().getValue().sr);

    if (HomeCtrl.sceneFlag == 1) {
      titleLabel.setText("Service Requests");
      SR sr = table.getSelectionModel().getSelectedItem().getValue().sr;
      ServiceRequestDerbyImpl serviceRequestDerby = new ServiceRequestDerbyImpl((SR.SRType) sr.getFields().get("sr_type"));
      serviceRequestDerby.deleteServiceRequest(table.getSelectionModel().getSelectedItem().getValue().sr);
      initializeRequestsTable();
    } else if (HomeCtrl.sceneFlag == 2) {
      LocationDAO locationDAO = new LocationDerbyImpl();
      locationDAO.deleteLocationNode(
          table.getSelectionModel().getSelectedItem().getValue().loc.getNodeID());
      titleLabel.setText("Locations");
      initializeLocationTable();
    } else if (HomeCtrl.sceneFlag == 3) {
      EquipmentDAO equipmentDAO = new EquipmentDerbyImpl();
      equipmentDAO.deleteMedicalEquipment(
          table.getSelectionModel().getSelectedItem().getValue().equip.getEquipmentID());
      titleLabel.setText("Equipment");
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

  @FXML
  public void save() {

    if (HomeCtrl.sceneFlag == 3) {

      DataViewCtrl.addPopup.get().hide();

      // cancel button
      JFXButton cancelUpdateButton = new JFXButton();
      cancelUpdateButton.setText("X");

      cancelUpdateButton.setOnAction(
          event -> {
            DataViewCtrl.addPopup.get().hide();
          });

      Label equipmentIDLabel = new Label("equipmentID");
      Label equipmentTypeLabel = new Label("equipmentType");
      Label isCleanLabel = new Label("isClean");
      Label currentLocationLabel = new Label("currentLocation");
      Label isAvailableLabel = new Label("isAvailable");

      equipmentIDLabel.setPadding(new Insets(10, 10, 10, 5));
      equipmentTypeLabel.setPadding(new Insets(10, 10, 10, 5));
      isCleanLabel.setPadding(new Insets(10, 10, 10, 5));
      currentLocationLabel.setPadding(new Insets(10, 10, 10, 5));
      isAvailableLabel.setPadding(new Insets(10, 10, 10, 5));
      cancelUpdateButton.setStyle("-fx-alignment: center ;");
      cancelUpdateButton.setAlignment(Pos.CENTER);

      // value text area
      TextArea equipmentIDText = new TextArea();
      equipmentIDText.setPromptText("Enter equipmentID:");
      equipmentIDText.setMinSize(50, 30);
      equipmentIDText.setMaxSize(150, 30);

      TextArea equipmentTypeText = new TextArea();
      equipmentTypeText.setPromptText("Enter equipmentType:");
      equipmentTypeText.setMinSize(50, 30);
      equipmentTypeText.setMaxSize(150, 30);

      JFXCheckBox isCleanCheckBox = new JFXCheckBox();

      TextArea currentLocationText = new TextArea();
      currentLocationText.setPromptText("Enter currentLocation:");
      currentLocationText.setMinSize(50, 30);
      currentLocationText.setMaxSize(150, 30);

      JFXCheckBox isAvailableCheckBox = new JFXCheckBox();

      JFXButton updateButton = new JFXButton();
      updateButton.setText("Update");

      updateButton.setOnAction(
          e -> {
            if (equipmentIDText.getText().length() > 2
                && equipmentTypeText.getText().length() > 2
                && currentLocationText.getText().length() > 2) {
              EquipmentDerbyImpl equipmentDerby = new EquipmentDerbyImpl();
              equipmentDerby.enterMedicalEquipment(
                  equipmentIDText.getText(),
                  equipmentTypeText.getText(),
                  isCleanCheckBox.isSelected(),
                  currentLocationText.getText(),
                  isAvailableCheckBox.isSelected());
              updateButton.setTextFill(Color.GREEN);
              try {
                this.initializeRequestsTable();
              } catch (SQLException | InvocationTargetException | IllegalAccessException ex) {
                ex.printStackTrace();
              }
            } else {
              updateButton.setTextFill(Color.RED);
            }
          });

      // add it to the scene
      var content = new GridPane();
      content.setRowIndex(equipmentIDLabel, 0);
      content.setColumnIndex(equipmentIDLabel, 0);
      content.setRowIndex(equipmentTypeLabel, 1);
      content.setColumnIndex(equipmentTypeLabel, 0);
      content.setRowIndex(isCleanLabel, 2);
      content.setColumnIndex(isCleanLabel, 0);
      content.setRowIndex(currentLocationLabel, 3);
      content.setColumnIndex(currentLocationLabel, 0);
      content.setRowIndex(isAvailableLabel, 4);
      content.setColumnIndex(isAvailableLabel, 0);
      content.setRowIndex(cancelUpdateButton, 5);
      content.setColumnIndex(cancelUpdateButton, 0);

      content.setRowIndex(equipmentIDText, 0);
      content.setColumnIndex(equipmentIDText, 1);
      content.setRowIndex(equipmentTypeText, 1);
      content.setColumnIndex(equipmentTypeText, 1);
      content.setRowIndex(isCleanCheckBox, 2);
      content.setColumnIndex(isCleanCheckBox, 1);
      content.setRowIndex(currentLocationText, 3);
      content.setColumnIndex(currentLocationText, 1);
      content.setRowIndex(isAvailableCheckBox, 4);
      content.setColumnIndex(isAvailableCheckBox, 1);
      content.setRowIndex(updateButton, 5);
      content.setColumnIndex(updateButton, 1);

      content
          .getChildren()
          .addAll(
              equipmentIDLabel,
              equipmentTypeLabel,
              isCleanLabel,
              currentLocationLabel,
              isAvailableLabel,
              cancelUpdateButton,
              equipmentIDText,
              equipmentTypeText,
              isCleanCheckBox,
              currentLocationText,
              isAvailableCheckBox,
              updateButton);

      content.setPadding(new Insets(10, 5, 10, 5));
      content.setBackground(
          new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
      content.setEffect(new DropShadow());

      var p = new Popup();
      p.getContent().add(content);

      DataViewCtrl.addPopup.set(p);
      DataViewCtrl.addPopup.get().show(App.getStage());
    }
  }

  static class RecursiveObj extends RecursiveTreeObject<RecursiveObj> {

    public SR sr;
    public Location locStart;
    public Location locEnd;
    public Employee employeeReq;
    public Employee employeeAss;
    public Location loc;
    public Equipment equip;
    public Employee employee;
  }

  @FXML JFXTreeTableView<RecursiveObj> table;

  boolean fillerYes = true;

  private StringBuilder detailLabel = new StringBuilder("No further details  ");
  private List<SR> srList = new ArrayList<>();
  private List<Location> locList = new ArrayList<>();
  private List<Equipment> eqList = new ArrayList<>();
  private List<Employee> empList = new ArrayList<>();
  public static AtomicReference<Popup> detailsPopup = new AtomicReference<>(new Popup());
  public static AtomicReference<Popup> modifyPopup = new AtomicReference<>(new Popup());
  public static AtomicReference<Popup> addPopup = new AtomicReference<>(new Popup());

  String[] locationColumnNames = {
    "ID",
    "Room Name",
    "Floor Number",
    "Store XRAY?",
    "Store Beds?",
    "Store I. Pumps?",
    "Store Recliners?"
  };

  String[] srColumnNames = {
    "Type",
    "ID",
    "Start Location",
    "End Location",
    "Requested By",
    "Employee Assigned",
    "Request Time",
    "Status",
    "Priority",
    "Comments"
  };

  String[] equipmentColumnNames = {"ID", "Type", "Is Clean?", "Location", "Is Available?"};

  String[] employeeColumnNames = {
    "ID", "Type", "First Name", "Last Name", "Email", "Phone Number", "Address", "Start Date"
  };

  AtomicReference<Point2D> point = new AtomicReference<>(new Point2D(0., 0.));

  ContextMenu rightClickMenu = new ContextMenu();
  MenuItem viewDetails = new MenuItem("View Details");
  MenuItem modify = new MenuItem("Modify");

  @FXML
  public void initialize() throws SQLException, InvocationTargetException, IllegalAccessException {

    configure();

    if (HomeCtrl.sceneFlag == 1) {
      titleLabel.setText("Service Requests");
      initializeRequestsTable();
    } else if (HomeCtrl.sceneFlag == 2) {
      titleLabel.setText("Locations");
      initializeLocationTable();
    } else if (HomeCtrl.sceneFlag == 3) {
      titleLabel.setText("Equipment");
      initializeEquipmentTable();
    } else if (HomeCtrl.sceneFlag == 4) {
      titleLabel.setText("Employees");
      initializeEmployeeTable();
    } else {
      // wait what how did you get here
    }
  }

  @FXML
  public void initializeLocationTable() {

    List<JFXTreeTableColumn<RecursiveObj, String>> locationColumns = new ArrayList<>();

    for (String columnName : this.locationColumnNames) {
      JFXTreeTableColumn<RecursiveObj, String> column = new JFXTreeTableColumn<>(columnName);
      column.setPrefWidth(80);
      column.setStyle("-fx-alignment: center ;");

      locationColumns.add(column);
    }

    locationColumns
        .get(0)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().loc.getNodeID()));
    locationColumns
        .get(1)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().loc.getLongName()));
    locationColumns
        .get(2)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().loc.getFloor()));
    locationColumns
        .get(3)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    locationColumns
        .get(4)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    locationColumns
        .get(5)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    locationColumns
        .get(6)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(fillerYes ? "Yes" : "No"));

    // Grab location / equipment from database, these are dummies
    LocationDAO locationBase = new LocationDerbyImpl();
    this.locList = locationBase.getNodeList();
    ObservableList<RecursiveObj> locations = FXCollections.observableArrayList();
    for (Location currLoc : this.locList) {
      RecursiveObj recursiveLoc = new RecursiveObj();
      recursiveLoc.loc = currLoc;
      locations.add(recursiveLoc);
    }
    // Sets up the table and puts the location data under the columns
    final TreeItem<RecursiveObj> root =
        new RecursiveTreeItem<>(locations, RecursiveTreeObject::getChildren);

    table.getColumns().setAll(locationColumns);
    this.table.setRoot(root);

    this.setupViewDetailsAndModify();
  }

  @FXML
  public void initializeRequestsTable()
      throws SQLException, InvocationTargetException, IllegalAccessException {

    List<JFXTreeTableColumn<RecursiveObj, String>> srColumns = new ArrayList<>();

    for (String columnName : this.srColumnNames) {
      JFXTreeTableColumn<RecursiveObj, String> column = new JFXTreeTableColumn<>(columnName);
      column.setPrefWidth(80);
      column.setStyle("-fx-alignment: center ;");

      srColumns.add(column);
    }

    srColumns
        .get(0)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getFields().get("sr_type").toString()));
    srColumns
        .get(1)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getFields().get("request_id").toString()));
    srColumns
        .get(2)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().locStart.getShortName()));
    srColumns
        .get(3)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().locEnd.getShortName()));
    srColumns
        .get(4)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().employeeReq.getFullName()));
    srColumns
        .get(5)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().employeeAss.getFullName()));
    srColumns
        .get(6)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getFields().get("request_time").toString()));
    srColumns
        .get(7)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getFields().get("request_status").toString()));
    srColumns
        .get(8)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getFields().get("request_priority").toString()));
    srColumns
        .get(9)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getFields().get("comments").toString()));

    this.srList = ServiceRequestDerbyImpl.getAllServiceRequestList();
    ObservableList<RecursiveObj> requests = FXCollections.observableArrayList();
    for (SR sr : this.srList) {
      RecursiveObj recursiveSR = new RecursiveObj();
      recursiveSR.sr = sr;
      recursiveSR.locStart = (Location) sr.getFields().get("start_location");
      recursiveSR.locEnd = (Location) sr.getFields().get("end_location");
      recursiveSR.employeeReq = (Employee) sr.getFields().get("employee_requested");
      recursiveSR.employeeAss = (Employee) sr.getFields().get("employee_assigned");
      requests.add(recursiveSR);
    }

    // Sets up the table and puts the equipment data under the columns
    final TreeItem<RecursiveObj> root =
        new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);

    table.getColumns().setAll(srColumns);
    table.setRoot(root);

    this.setupViewDetailsAndModify();
  }

  @FXML
  public void initializeEquipmentTable() {

    List<JFXTreeTableColumn<RecursiveObj, String>> equipmentColumns = new ArrayList<>();

    for (String columnName : this.equipmentColumnNames) {
      JFXTreeTableColumn<RecursiveObj, String> column = new JFXTreeTableColumn<>(columnName);
      column.setPrefWidth(100);
      column.setStyle("-fx-alignment: center ;");

      equipmentColumns.add(column);
    }

    equipmentColumns
        .get(0)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().equip.getEquipmentID()));
    equipmentColumns
        .get(1)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().equip.getEquipmentType()));
    equipmentColumns
        .get(2)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(
                    param.getValue().getValue().equip.getIsClean() ? "Yes" : "No"));
    equipmentColumns
        .get(3)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().equip.getCurrentLocation()));
    equipmentColumns
        .get(4)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(
                    param.getValue().getValue().equip.getIsAvailable() ? "Yes" : "No"));

    // Grab equipment from database
    EquipmentDAO database = new EquipmentDerbyImpl();
    this.eqList = database.getMedicalEquipmentList();
    ObservableList<RecursiveObj> equipment = FXCollections.observableArrayList();
    for (Equipment item : this.eqList) {
      RecursiveObj recursiveEquipment = new RecursiveObj();
      recursiveEquipment.equip = item;
      equipment.add(recursiveEquipment);
    }

    // Sets up the table and puts the equipment data under the columns
    final TreeItem<RecursiveObj> root =
        new RecursiveTreeItem<>(equipment, RecursiveTreeObject::getChildren);
    table.getColumns().setAll(equipmentColumns);
    table.setRoot(root);

    this.setupViewDetailsAndModify();
  }

  @FXML
  public void initializeEmployeeTable() {

    List<JFXTreeTableColumn<RecursiveObj, String>> employeeColumns = new ArrayList<>();

    for (String columnName : this.employeeColumnNames) {
      JFXTreeTableColumn<RecursiveObj, String> column = new JFXTreeTableColumn<>(columnName);
      column.setPrefWidth(80);
      column.setStyle("-fx-alignment: center ;");

      employeeColumns.add(column);
    }

    employeeColumns
        .get(0)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().employee.getEmployeeID()));
    employeeColumns
        .get(1)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().employee.getEmployeeType()));
    employeeColumns
        .get(2)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().employee.getFirstName()));
    employeeColumns
        .get(3)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().employee.getLastName()));
    employeeColumns
        .get(4)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().employee.getEmail()));
    employeeColumns
        .get(5)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().employee.getPhoneNum()));
    employeeColumns
        .get(6)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().employee.getAddress()));
    employeeColumns
        .get(7)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(
                    param.getValue().getValue().employee.getStartDate().toString()));

    // Grab location / equipment from database, these are dummies
    EmployeeDAO employeeBase = new EmployeeDerbyImpl();
    this.empList = employeeBase.getEmployeeList();
    ObservableList<RecursiveObj> employees = FXCollections.observableArrayList();
    for (Employee anEmployee : this.empList) {
      RecursiveObj recursiveEmployee = new RecursiveObj();
      recursiveEmployee.employee = anEmployee;
      employees.add(recursiveEmployee);
    }

    // Sets up the table and puts the location data under the columns
    final TreeItem<RecursiveObj> root =
        new RecursiveTreeItem<>(employees, RecursiveTreeObject::getChildren);
    table.getColumns().setAll(employeeColumns);
    table.setRoot(root);

    this.setupViewDetailsAndModify();
  }

  @FXML
  private void createDetailsPopup() throws InvocationTargetException, IllegalAccessException {
    DataViewCtrl.detailsPopup.get().hide();

    this.detailLabel = new StringBuilder("Nothing selected  ");

    if (HomeCtrl.sceneFlag == 1 && table.getSelectionModel().getSelectedIndex() > -1) {
      this.detailLabel = new StringBuilder();

      SR sr = this.srList.get(table.getSelectionModel().getSelectedIndex());

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
                || Objects.equals(key, "sr_type")))
        {
          this.detailLabel.append(
                  String.format("%s: %s, ", key, sr.getStringFields().get(key)));
        }
      }


      if (this.detailLabel.length() > 1) {
        this.detailLabel.delete(this.detailLabel.length() - 2, this.detailLabel.length());
      }
      if (this.detailLabel.length() == 0) {
        this.detailLabel = new StringBuilder("No further details  ");
      }
    }

    if (HomeCtrl.sceneFlag != 1) {
      this.detailLabel = new StringBuilder("No further details  ");
    }

    var content = new StackPane(new Label(this.detailLabel.toString()));
    content.setPadding(new Insets(10, 5, 10, 5));
    content.setBackground(
        new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
    content.setEffect(new DropShadow());

    var p = new Popup();
    p.getContent().add(content);

    DataViewCtrl.detailsPopup.set(p);
  }

  @FXML
  private void createModifyPopup() throws InvocationTargetException, IllegalAccessException {
    DataViewCtrl.modifyPopup.get().hide();

    // cancel button
    JFXButton cancelUpdateButton = new JFXButton();
    cancelUpdateButton.setText("X");

    cancelUpdateButton.setOnAction(
        event -> {
          DataViewCtrl.modifyPopup.get().hide();
        });

    // combobox field
    JFXComboBox<String> field = new JFXComboBox<>();
    field.setValue("Select Field");
    field.setMinSize(50, 30);
    field.setMaxSize(150, 30);

    // value text area
    TextArea value = new TextArea();
    value.setPromptText("Enter value:");
    value.setMinSize(50, 30);
    value.setMaxSize(150, 30);

    JFXButton updateButton = new JFXButton();
    updateButton.setText("Update");

    Method[] methods;

    // fill out the combobox
    switch (HomeCtrl.sceneFlag) {
      case 1:
        SR sr = this.srList.get(table.getSelectionModel().getSelectedIndex());

        field.getItems().addAll(sr.getStringFields().keySet());

        field.setOnAction(
            e -> {
              if (field.getSelectionModel().getSelectedIndex() > -1) {
                value.setText(sr.getStringFields().get(field.getSelectionModel().getSelectedItem()));



//                for (Method method : methods) {
//                  boolean starts_with_get = method.getName().split("^get")[0].equals("");
//                  boolean contains_name =
//                      method
//                          .getName()
//                          .toLowerCase(Locale.ROOT)
//                          .contains(
//                              field.getSelectionModel().getSelectedItem().toLowerCase(Locale.ROOT));
//                  if (starts_with_get && contains_name) {
//                    try {
//                      value.setText((String) method.invoke(sr));
//                    } catch (IllegalAccessException | InvocationTargetException ex) {
//                      ex.printStackTrace();
//                    }
//                  }
//                }
              }
            });

        updateButton.setOnAction(
            e -> {
              if (field.getSelectionModel().getSelectedIndex() > -1
                  && value.getText().length() > 0) {

                ServiceRequestDerbyImpl serviceRequestDerby = new ServiceRequestDerbyImpl((SR.SRType) sr.getFields().get("sr_type"));
                try {
                  sr.setFieldByString(field.getSelectionModel().getSelectedItem(), value.getText());
                  serviceRequestDerby.updateServiceRequest(sr);

                  updateButton.setTextFill(Color.GREEN);
                } catch (SQLException | IllegalAccessException | InvocationTargetException ex) {
                  ex.printStackTrace();
                  updateButton.setTextFill(Color.RED);
                }
              }
            });
        break;
      case 2:
        Location loc = this.locList.get(table.getSelectionModel().getSelectedIndex());
        methods = loc.getClass().getMethods();
        for (Method method : methods) {
          boolean is_the_method_of_loc = method.getDeclaringClass().equals(loc.getClass());
          boolean starts_with_set = method.getName().split("^set")[0].equals("");
          boolean return_string =
              Arrays.toString(method.getParameterTypes())
                  .toLowerCase(Locale.ROOT)
                  .contains("string");

          if (is_the_method_of_loc && starts_with_set && return_string) {
            field.getItems().addAll(method.getName().substring(3));
          }
        }

        field.setOnAction(
            e -> {
              if (field.getSelectionModel().getSelectedIndex() > -1) {
                for (Method method : methods) {
                  boolean starts_with_get = method.getName().split("^get")[0].equals("");
                  boolean contains_name =
                      method
                          .getName()
                          .toLowerCase(Locale.ROOT)
                          .contains(
                              field.getSelectionModel().getSelectedItem().toLowerCase(Locale.ROOT));
                  if (starts_with_get && contains_name) {
                    try {
                      value.setText((String) method.invoke(loc));
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                      ex.printStackTrace();
                    }
                  }
                }
              }
            });

        updateButton.setOnAction(
            e -> {
              if (field.getSelectionModel().getSelectedIndex() > -1
                  && value.getText().length() > 0) {
                for (Method method : methods) {
                  boolean starts_with_set = method.getName().split("^set")[0].equals("");
                  boolean contains_name =
                      method
                          .getName()
                          .toLowerCase(Locale.ROOT)
                          .contains(
                              field.getSelectionModel().getSelectedItem().toLowerCase(Locale.ROOT));
                  if (starts_with_set && contains_name) {

                    LocationDerbyImpl locationDerby = new LocationDerbyImpl();
                    try {
                      locationDerby.updateLocation(
                          loc.getNodeID(), field.getValue(), value.getText());
                      updateButton.setTextFill(Color.GREEN);
                      try {
                        this.initializeRequestsTable();
                      } catch (SQLException | InvocationTargetException | IllegalAccessException ex) {
                        ex.printStackTrace();
                      }
                    } catch (Exception ex) {
                      ex.printStackTrace();
                      updateButton.setTextFill(Color.RED);
                    }
                  }
                }
              }
            });

        break;
      case 3:
        Equipment eq = this.eqList.get(table.getSelectionModel().getSelectedIndex());
        methods = eq.getClass().getMethods();
        for (Method method : methods) {
          boolean is_the_method_of_eq = method.getDeclaringClass().equals(eq.getClass());
          boolean starts_with_set = method.getName().split("^set")[0].equals("");
          boolean return_string =
              Arrays.toString(method.getParameterTypes())
                  .toLowerCase(Locale.ROOT)
                  .contains("string");

          if (is_the_method_of_eq && starts_with_set && return_string) {
            field.getItems().addAll(method.getName().substring(3));
          }
        }

        field.setOnAction(
            e -> {
              if (field.getSelectionModel().getSelectedIndex() > -1) {
                for (Method method : methods) {
                  boolean starts_with_get = method.getName().split("^get")[0].equals("");
                  boolean contains_name =
                      method
                          .getName()
                          .toLowerCase(Locale.ROOT)
                          .contains(
                              field.getSelectionModel().getSelectedItem().toLowerCase(Locale.ROOT));
                  if (starts_with_get && contains_name) {
                    try {
                      value.setText((String) method.invoke(eq));
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                      ex.printStackTrace();
                    }
                  }
                }
              }
            });

        updateButton.setOnAction(
            e -> {
              if (field.getSelectionModel().getSelectedIndex() > -1
                  && value.getText().length() > 0) {
                for (Method method : methods) {
                  boolean starts_with_set = method.getName().split("^set")[0].equals("");
                  boolean contains_name =
                      method
                          .getName()
                          .toLowerCase(Locale.ROOT)
                          .contains(
                              field.getSelectionModel().getSelectedItem().toLowerCase(Locale.ROOT));
                  if (starts_with_set && contains_name) {

                    EquipmentDerbyImpl equipmentDerby = new EquipmentDerbyImpl();
                    try {
                      equipmentDerby.updateMedicalEquipment(
                          eq.getEquipmentID(), field.getValue(), value.getText());
                      updateButton.setTextFill(Color.GREEN);
                      try {
                        this.initializeRequestsTable();
                      } catch (SQLException | InvocationTargetException | IllegalAccessException ex) {
                        ex.printStackTrace();
                      }
                    } catch (Exception ex) {
                      ex.printStackTrace();
                      updateButton.setTextFill(Color.RED);
                    }
                  }
                }
              }
            });

        break;
      case 4:
        Employee emp = this.empList.get(table.getSelectionModel().getSelectedIndex());
        methods = emp.getClass().getMethods();
        for (Method method : methods) {
          boolean is_the_method_of_emp = method.getDeclaringClass().equals(emp.getClass());
          boolean starts_with_set = method.getName().split("^set")[0].equals("");
          boolean return_string =
              Arrays.toString(method.getParameterTypes())
                  .toLowerCase(Locale.ROOT)
                  .contains("string");

          if (is_the_method_of_emp && starts_with_set && return_string) {
            field.getItems().addAll(method.getName().substring(3));
          }
        }

        field.setOnAction(
            e -> {
              if (field.getSelectionModel().getSelectedIndex() > -1) {
                for (Method method : methods) {
                  boolean starts_with_get = method.getName().split("^get")[0].equals("");
                  boolean contains_name =
                      method
                          .getName()
                          .toLowerCase(Locale.ROOT)
                          .contains(
                              field.getSelectionModel().getSelectedItem().toLowerCase(Locale.ROOT));
                  if (starts_with_get && contains_name) {
                    try {
                      value.setText((String) method.invoke(emp));
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                      ex.printStackTrace();
                    }
                  }
                }
              }
            });

        updateButton.setOnAction(
            e -> {
              if (field.getSelectionModel().getSelectedIndex() > -1
                  && value.getText().length() > 0) {
                for (Method method : methods) {
                  boolean starts_with_set = method.getName().split("^set")[0].equals("");
                  boolean contains_name =
                      method
                          .getName()
                          .toLowerCase(Locale.ROOT)
                          .contains(
                              field.getSelectionModel().getSelectedItem().toLowerCase(Locale.ROOT));
                  if (starts_with_set && contains_name) {

                    EmployeeDerbyImpl employeeDerby = new EmployeeDerbyImpl();
                    try {
                      employeeDerby.updateEmployee(
                          emp.getEmployeeID(), field.getValue(), value.getText());
                      updateButton.setTextFill(Color.GREEN);
                      try {
                        this.initializeRequestsTable();
                      } catch (SQLException | InvocationTargetException | IllegalAccessException ex) {
                        ex.printStackTrace();
                      }
                    } catch (Exception ex) {
                      ex.printStackTrace();
                      updateButton.setTextFill(Color.RED);
                    }
                  }
                }
              }
            });

        break;
    }

    // add it to the scene
    var content = new GridPane();
    content.setRowIndex(cancelUpdateButton, 0);
    content.setColumnIndex(cancelUpdateButton, 0);
    content.setRowIndex(field, 0);
    content.setColumnIndex(field, 1);
    content.setRowIndex(value, 0);
    content.setColumnIndex(value, 2);
    content.setRowIndex(updateButton, 0);
    content.setColumnIndex(updateButton, 3);
    content.getChildren().addAll(cancelUpdateButton, field, value, updateButton);

    content.setPadding(new Insets(10, 5, 10, 5));
    content.setBackground(
        new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
    content.setEffect(new DropShadow());

    var p = new Popup();
    p.getContent().add(content);

    DataViewCtrl.modifyPopup.set(p);
  }

  @FXML
  private void setupViewDetailsAndModify() {

    this.rightClickMenu.getItems().addAll(this.viewDetails);
    this.rightClickMenu.getItems().addAll(this.modify);

    this.viewDetails.setOnAction(
        e -> {
          this.rightClickMenu.hide();
          try {
            this.createDetailsPopup();
          } catch (InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
          }
          DataViewCtrl.detailsPopup
              .get()
              .show(App.getStage(), this.point.get().getX(), this.point.get().getY());
        });
    this.modify.setOnAction(
        e -> {
          this.rightClickMenu.hide();
          try {
            this.createModifyPopup();
          } catch (InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
          }
          DataViewCtrl.modifyPopup
              .get()
              .show(App.getStage(), this.point.get().getX(), this.point.get().getY());
        });

    this.table.setOnMouseClicked(
        e -> {
          if (e.getButton() == MouseButton.PRIMARY) {
            DataViewCtrl.detailsPopup.get().hide();
          }
          if (e.getButton() == MouseButton.SECONDARY) {
            this.point.set(new Point2D(e.getScreenX(), e.getScreenY()));
            this.rightClickMenu.show(this.table, e.getScreenX(), e.getScreenY());
          }
        });
  }

  @FXML
  void edit() {}

  @FXML
  void clear() {}

  @FXML
  private void employeeFilter() throws IOException {}

  protected void onSceneSwitch() {
    DataViewCtrl.detailsPopup.get().hide();
    DataViewCtrl.modifyPopup.get().hide();
  }
}
