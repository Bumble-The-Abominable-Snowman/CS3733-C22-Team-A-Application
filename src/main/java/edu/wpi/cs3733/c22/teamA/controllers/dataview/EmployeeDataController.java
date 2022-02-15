package edu.wpi.cs3733.c22.teamA.controllers.dataview;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class EmployeeDataController implements Initializable {
  @FXML private Button backButton;
  @FXML private JFXTreeTableView<Employee> employeeTable;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    // Create all columns in the tracker table
    JFXTreeTableColumn<Employee, String> employeeID = new JFXTreeTableColumn<>("ID");
    JFXTreeTableColumn<Employee, String> employeeType = new JFXTreeTableColumn<>("Type");
    JFXTreeTableColumn<Employee, String> firstName = new JFXTreeTableColumn<>("First Name");
    JFXTreeTableColumn<Employee, String> lastName = new JFXTreeTableColumn<>("Last Name");
    JFXTreeTableColumn<Employee, String> email = new JFXTreeTableColumn<>("Email");
    JFXTreeTableColumn<Employee, String> phoneNum = new JFXTreeTableColumn<>("Phone Number");
    JFXTreeTableColumn<Employee, String> address = new JFXTreeTableColumn<>("Address");
    JFXTreeTableColumn<Employee, String> startDate = new JFXTreeTableColumn<>("Start Date");

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
        (TreeTableColumn.CellDataFeatures<Employee, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEmployeeID()));
    employeeType.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Employee, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEmployeeType()));
    firstName.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Employee, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getFirstName()));
    // the following code has to be implemented when a location's medical equipment is known.
    // eahc is currently filled with filler Yes values, which represent that atm every room
    // can store every equipment. In the future, this will not be true and must be updated
    lastName.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Employee, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getLastName()));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreXRAY()));
    email.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Employee, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEmail()));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreBed()));
    phoneNum.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Employee, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getPhoneNum()));
    // new SimpleStringProperty(param.getValue().getValue().getCanStorePump()));
    address.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Employee, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getAddress()));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreRecliner()));
    startDate.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Employee, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getStartDate().toString()));

    // Grab location / equipment from database, these are dummies
    EmployeeDAO employeeBase = new EmployeeDerbyImpl();
    List<Employee> employeeFromDatabase = employeeBase.getEmployeeList();
    ObservableList<Employee> employees = FXCollections.observableArrayList();
    employees.addAll(employeeFromDatabase);

    // Sets up the table and puts the location data under the columns
    final TreeItem<Employee> root =
        new RecursiveTreeItem<>(employees, RecursiveTreeObject::getChildren);
    employeeTable
        .getColumns()
        .setAll(employeeID, employeeType, firstName, lastName, email, phoneNum, address, startDate);
    employeeTable.setRoot(root);
  }

  @FXML
  private void goToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }
}
