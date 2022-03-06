package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeWrapperImpl;
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
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

	public void delete() throws SQLException, IOException, ParseException {
		EmployeeDAO employeeDAO = new EmployeeWrapperImpl();
		employeeDAO.deleteEmployee(table.getSelectionModel().getSelectedItem().getValue().employee);
		dataViewCtrl.titleLabel.setText("Employees");
		initializeEmployeeTable();
	}

	public void initializeEmployeeTable() throws IOException, ParseException {
		table = dataViewCtrl.getTable();

		EmployeeDAO employeeDAO = new EmployeeWrapperImpl();
		dataViewCtrl.getSelectEmployeeBox().getItems().addAll(employeeDAO.getEmployeeList().stream().map(Employee::getFullName).collect(Collectors.toList()));
		dataViewCtrl.getSelectEmployeeBox().getItems().add("All");
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
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("employee_id")));
		employeeColumns
				.get(1)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("employee_type")));
		employeeColumns
				.get(2)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("first_name")));
		employeeColumns
				.get(3)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("last_name")));
		employeeColumns
				.get(4)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("email")));
		employeeColumns
				.get(5)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("phone_num")));
		employeeColumns
				.get(6)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("address")));
		employeeColumns
				.get(7)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(
										param.getValue().getValue().employee.getStringFields().get("start_date")));

		// Grab location / equipment from database, these are dummies
		EmployeeDAO employeeBase = new EmployeeWrapperImpl();
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

	public void filterEmployees(String newValue) throws IOException, ParseException {

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
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("employee_id")));
		employeeColumns
				.get(1)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("employee_type")));
		employeeColumns
				.get(2)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("first_name")));
		employeeColumns
				.get(3)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("last_name")));
		employeeColumns
				.get(4)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("email")));
		employeeColumns
				.get(5)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("phone_num")));
		employeeColumns
				.get(6)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().employee.getStringFields().get("address")));
		employeeColumns
				.get(7)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(
										param.getValue().getValue().employee.getStringFields().get("start_date").toString()));


		// Grab location / equipment from database, these are dummies
		EmployeeDAO employeeBase = new EmployeeWrapperImpl();
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

	public void modifyPopup(JFXComboBox<String> field, TextArea value, JFXButton updateButton){
		Employee emp = empList.get(table.getSelectionModel().getSelectedIndex());

		for (String key: emp.getStringFields().keySet()) {
			if ((key.equals("start_date") || (key.equals("employee_id")) || (key.equals("employee_type"))))
			{
			}
			else
			{
				field.getItems().add(key);
			}
		}

		field.setOnAction(
				e -> value.setText(emp.getStringFields().get(field.getSelectionModel().getSelectedItem())));

		updateButton.setOnAction(
				e -> {
					if (field.getSelectionModel().getSelectedIndex() > -1
							&& value.getText().length() > 0) {

						try {
							emp.setFieldByString(field.getSelectionModel().getSelectedItem(), value.getText());
							EmployeeDAO employeeDAO = new EmployeeWrapperImpl();
							employeeDAO.updateEmployee(emp);
							updateButton.setTextFill(Color.GREEN);
							this.initializeEmployeeTable();
						} catch (ParseException | IOException | SQLException ex) {
							ex.printStackTrace();
							updateButton.setTextFill(Color.RED);
						}
					}
				});
	}
}
