package edu.wpi.cs3733.c22.teamA.controllers;

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
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import lombok.SneakyThrows;

public class DataViewCtrl extends MasterCtrl {

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
    public Location locStart;
    public Location locEnd;
    public Employee employeeReq;
    public Employee employeeAss;
    public Location loc;
    public Equipment equip;
    public Employee employee;
  }

  @FXML JFXButton backButton;
  @FXML Label titleLabel;
  @FXML JFXTreeTableView<RecursiveObj> table;

  boolean fillerYes = true;

  private StringBuilder detailLabel = new StringBuilder("No further details  ");
  private List<Object> srList = new ArrayList<>();
  public static AtomicReference<Popup> popup = new AtomicReference<>(new Popup());

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  public void initialize() throws SQLException, InvocationTargetException, IllegalAccessException {
    double backTextSize = backButton.getFont().getSize();
    double titleTextSize = titleLabel.getFont().getSize();

    configure();

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
            new SimpleStringProperty(param.getValue().getValue().locStart.getShortName()));
    endLoc.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().locEnd.getShortName()));
    employeeReq.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().employeeReq.getFullName()));
    employeeAss.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().employeeAss.getFullName()));
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

    //    AtomicLong sinceOnTable = new AtomicLong(Clock.systemDefaultZone().millis());
    //    AtomicLong sinceNotOnTable = new AtomicLong(Clock.systemDefaultZone().millis());
    //    AtomicBoolean mouseOnTable = new AtomicBoolean(false);
    AtomicReference<Point2D> point = new AtomicReference<>(new Point2D(0., 0.));
    //    sceneSwitcher.currentScene.setOnMouseMoved(
    //        e -> {
    //          sinceNotOnTable.set(Clock.systemDefaultZone().millis());
    //        });
    //
    //    table.setOnMouseMoved(
    //        e -> {
    //          point.set(table.localToScreen(e.getX(), e.getY()));
    //          sinceOnTable.set(Clock.systemDefaultZone().millis());
    //        });
    //
    //    System.out.println(table.getBorder());
    //

    AtomicBoolean showPopUp = new AtomicBoolean(false);
    Task task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            final int[] waitTime = {100};
            while (true) {
              Platform.runLater(
                  new Runnable() {
                    @Override
                    public void run() {
                      if (showPopUp.get()) {
                        DataViewController.popup
                            .get()
                            .show(App.getStage(), point.get().getX(), point.get().getY());
                      } else {
                        DataViewController.popup.get().hide();
                      }
                    }
                  });

              TimeUnit.MILLISECONDS.sleep(waitTime[0]);
            }
          }
        };
    new Thread(task).start();

    ContextMenu rightClickMenu = new ContextMenu();
    MenuItem viewDetails = new MenuItem("View Details");
    MenuItem modify = new MenuItem("Modify");
    rightClickMenu.getItems().addAll(viewDetails);
    rightClickMenu.getItems().addAll(modify);

    viewDetails.setOnAction(
        e -> {
          rightClickMenu.hide();
          try {
            this.createNewPopup();
          } catch (InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
          }
          showPopUp.set(true);
        });
    modify.setOnAction(
        e -> {
          System.out.println("Modify baby");
          rightClickMenu.hide();
        });

    table.setOnMouseClicked(
        e -> {
          if (e.getButton() == MouseButton.PRIMARY) {
            showPopUp.set(false);
          }
          if (e.getButton() == MouseButton.SECONDARY) {
            point.set(new Point2D(e.getScreenX(), e.getScreenY()));
            rightClickMenu.show(table, e.getScreenX(), e.getScreenY());
            showPopUp.set(false);
          }
        });
  }

  private void createNewPopup() throws InvocationTargetException, IllegalAccessException {
    DataViewController.popup.get().hide();

    this.detailLabel = new StringBuilder("Nothing selected  ");

    if (table.getSelectionModel().getSelectedIndex() > -1) {
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

    var content = new StackPane(new Label(this.detailLabel.toString()));
    content.setPadding(new Insets(10, 5, 10, 5));
    content.setBackground(
        new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
    content.setEffect(new DropShadow());

    var p = new Popup();
    p.getContent().add(content);

    DataViewController.popup.set(p);
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
