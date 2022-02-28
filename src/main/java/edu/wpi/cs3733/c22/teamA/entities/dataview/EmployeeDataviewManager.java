package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.DataViewCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EmployeeDataviewManager {
	JFXTreeTableView<RecursiveObj> table;
	DataViewCtrl dataViewCtrl;
	private List<Employee> empList = new ArrayList<>();

	String[] employeeColumnNames = {
			"ID", "Type", "First Name", "Last Name", "Email", "Phone Number", "Address", "Start Date"
	};

	public EmployeeDataviewManager(DataViewCtrl dataViewCtrl){
		this.dataViewCtrl = dataViewCtrl;
	}

	public void delete() throws SQLException {
		EmployeeDAO employeeDAO = new EmployeeDerbyImpl();
		employeeDAO.deleteEmployee(
				table.getSelectionModel().getSelectedItem().getValue().employee.getEmployeeID());
		dataViewCtrl.titleLabel.setText("Employees");
		initializeEmployeeTable();
	}

	public void initializeEmployeeTable() {
		table = dataViewCtrl.getTable();

		EmployeeDAO employeeDAO = new EmployeeDerbyImpl();
		dataViewCtrl.getSelectEmployeeBox().getItems().addAll(employeeDAO.getEmployeeList().stream().map(Employee::getFullName).collect(Collectors.toList()));
		dataViewCtrl.getSelectEmployeeBox().getItems().addAll("All");
		dataViewCtrl.getSelectEmployeeBox().setVisible(true);

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

		dataViewCtrl.setupViewDetailsAndModify();
	}

	public void filterEmployees(String newValue){

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
			if(anEmployee.getFullName().equals(newValue) || newValue.equals("All")) {
				RecursiveObj recursiveEmployee = new RecursiveObj();
				recursiveEmployee.employee = anEmployee;
				employees.add(recursiveEmployee);
			}
		}

		final TreeItem<RecursiveObj> root =
				new RecursiveTreeItem<>(employees, RecursiveTreeObject::getChildren);
		table.getColumns().setAll(employeeColumns);
		table.setRoot(root);

		dataViewCtrl.setupViewDetailsAndModify();
	}

	public void modifyPopup(JFXComboBox<String> field, TextArea value, JFXButton updateButton, SRDataviewManager srDataviewManager){
		Employee emp = empList.get(table.getSelectionModel().getSelectedIndex());
		Method[] methods = emp.getClass().getMethods();
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
										srDataviewManager.initializeRequestsTable();
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
	}
}
