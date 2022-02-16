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

  @FXML
  public void deleteSelected()
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

  @FXML
  public void addData() {}

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

  @FXML Label titleLabel;
  @FXML JFXTreeTableView<RecursiveObj> table;

  @FXML JFXButton addDataButton;
  @FXML JFXButton deleteSelectedButton;

  boolean fillerYes = true;

  private StringBuilder detailLabel = new StringBuilder("No further details  ");
  private List<Object> srList = new ArrayList<>();
  private List<Location> locList = new ArrayList<>();
  private List<Equipment> eqList = new ArrayList<>();
  private List<Employee> empList = new ArrayList<>();
  public static AtomicReference<Popup> detailsPopup = new AtomicReference<>(new Popup());
  public static AtomicReference<Popup> modifyPopup = new AtomicReference<>(new Popup());

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

  public void initialize() throws SQLException, InvocationTargetException, IllegalAccessException {

    double titleTextSize = titleLabel.getFont().getSize();

    configure();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
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
                new SimpleStringProperty(param.getValue().getValue().sr.getSrType().toString()));
    srColumns
        .get(1)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getRequestID()));
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
                new SimpleStringProperty(param.getValue().getValue().sr.getRequestTime()));
    srColumns
        .get(7)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getRequestStatus()));
    srColumns
        .get(8)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getComments()));
    srColumns
        .get(9)
        .setCellValueFactory(
            (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                new SimpleStringProperty(param.getValue().getValue().sr.getRequestPriority()));

    this.srList = ServiceRequestDerbyImpl.getAllServiceRequestList();
    ObservableList<RecursiveObj> requests = FXCollections.observableArrayList();
    for (Object sr : this.srList) {
      RecursiveObj recursiveSR = new RecursiveObj();
      recursiveSR.sr = (SR) sr;
      recursiveSR.locStart = new LocationDerbyImpl().getLocationNode(((SR) sr).getStartLocation());
      recursiveSR.locEnd = new LocationDerbyImpl().getLocationNode(((SR) sr).getEndLocation());
      recursiveSR.employeeReq =
          new EmployeeDerbyImpl().getEmployee(((SR) sr).getEmployeeRequested());
      recursiveSR.employeeAss =
          new EmployeeDerbyImpl().getEmployee(((SR) sr).getEmployeeAssigned());
      requests.add(recursiveSR);
    }

    // Sets up the table and puts the equipment data under the columns
    final TreeItem<RecursiveObj> root =
        new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);

    table.getColumns().setAll(srColumns);
    table.setRoot(root);

    this.setupViewDetailsAndModify();
  }

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

  private void createDetailsPopup() throws InvocationTargetException, IllegalAccessException {
    DataViewCtrl.detailsPopup.get().hide();

    this.detailLabel = new StringBuilder("Nothing selected  ");

    if (HomeCtrl.sceneFlag == 1 && table.getSelectionModel().getSelectedIndex() > -1) {
      this.detailLabel = new StringBuilder("");

      Object sr = this.srList.get(table.getSelectionModel().getSelectedIndex());
      System.out.println(sr);
      Method[] methods = sr.getClass().getMethods();
      for (Method method : methods) {
        boolean is_the_method_exclusive = method.getDeclaringClass().equals(sr.getClass());
        boolean starts_with_get = method.getName().split("^get")[0].equals("");

        if (starts_with_get && is_the_method_exclusive) {
          try {
            this.detailLabel.append(
                String.format("%s: %s, ", method.getName().substring(3), method.invoke(sr)));
          } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
          }
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
        Object sr = this.srList.get(table.getSelectionModel().getSelectedIndex());
        methods = sr.getClass().getMethods();
        for (Method method : methods) {
          boolean is_the_method_of_super = method.getDeclaringClass().equals(SR.class);
          boolean is_the_method_exclusive = method.getDeclaringClass().equals(sr.getClass());
          boolean starts_with_set = method.getName().split("^set")[0].equals("");
          boolean is_not_sr_type = !method.getName().toLowerCase(Locale.ROOT).contains("srtype");

          if (is_not_sr_type
              && starts_with_set
              && (is_the_method_of_super || is_the_method_exclusive)) {
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
                      value.setText((String) method.invoke(sr));
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

                    ServiceRequestDerbyImpl<Object> objectServiceRequestDerby =
                        new ServiceRequestDerbyImpl<>(sr);
                    try {
                      method.invoke(sr, value.getText());
                      objectServiceRequestDerby.updateServiceRequest((SR) sr);
                      updateButton.setTextFill(Color.GREEN);
                    } catch (SQLException | InvocationTargetException | IllegalAccessException ex) {
                      ex.printStackTrace();
                      updateButton.setTextFill(Color.RED);
                    }
                  }
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

  protected void onSceneSwitch() {
    DataViewCtrl.detailsPopup.get().hide();
    DataViewCtrl.modifyPopup.get().hide();
  }
}
