package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.controllers.DataViewCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDataviewManager {
	JFXTreeTableView<RecursiveObj> table;
	DataViewCtrl dataViewCtrl;
	private List<Employee> empList = new ArrayList<>();

	String[] employeeColumnNames = {
			"ID", "Type", "First Name", "Last Name", "Email", "Phone Number", "Address", "Start Date"
	};

	private Popup popup;

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
		popup = new Popup();
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

    public void addData() {
		popup.hide();

		// cancel button
		JFXButton cancelUpdateButton = new JFXButton();
		cancelUpdateButton.setText("X");

		cancelUpdateButton.setOnAction(
				event -> {
					popup.hide();
				});

		Label empIDLabel = new Label("Employee ID");
		Label typeLabel = new Label("Employee Type");
		Label firstNameLabel = new Label("First Name");
		Label lastNameLabel = new Label("Last Name");
		Label emailLabel = new Label("Email");
		Label phoneNumLabel = new Label("Phone Number");
		Label addressLabel = new Label("Address");
		Label startDate = new Label("Start Date");

		empIDLabel.setPadding(new Insets(10, 10, 10, 5));
		typeLabel.setPadding(new Insets(10, 10, 10, 5));
		firstNameLabel.setPadding(new Insets(10, 10, 10, 5));
		lastNameLabel.setPadding(new Insets(10, 10, 10, 5));
		emailLabel.setPadding(new Insets(10, 10, 10, 5));
		phoneNumLabel.setPadding(new Insets(10, 10, 10, 5));
		addressLabel.setPadding(new Insets(10, 10, 10, 5));

		cancelUpdateButton.setStyle("-fx-alignment: center ;");
		cancelUpdateButton.setAlignment(Pos.CENTER);

		// value text area
		TextArea employeeIDText = new TextArea();
		employeeIDText.setPromptText("Enter Employee ID:");
		employeeIDText.setMinSize(50, 30);
		employeeIDText.setMaxSize(150, 30);

		ObservableList<String> typeOptions = FXCollections.observableArrayList("admin",
				"aide",
				"courier",
				"custodian",
				"doctor",
				"food",
				"language",
				"laundry",
				"maintenance",
				"nurse",
				"religious",
				"security",
				"staff");
		JFXComboBox type = new JFXComboBox(typeOptions);

		TextArea firstNameText = new TextArea();
		firstNameText.setPromptText("Enter First Name:");
		firstNameText.setMinSize(50, 30);
		firstNameText.setMaxSize(150, 30);

		TextArea lastNameText = new TextArea();
		lastNameText.setPromptText("Enter Last Name:");
		lastNameText.setMinSize(50, 30);
		lastNameText.setMaxSize(150, 30);

		TextArea emailText = new TextArea();
		emailText.setPromptText("Enter Email:");
		emailText.setMinSize(50, 30);
		emailText.setMaxSize(150, 30);

		TextArea phoneNumberText = new TextArea();
		phoneNumberText.setPromptText("Enter Phone Number:");
		phoneNumberText.setMinSize(50, 30);
		phoneNumberText.setMaxSize(150, 30);

		TextArea addressText = new TextArea();
		addressText.setPromptText("Enter Address:");
		addressText.setMinSize(50, 30);
		addressText.setMaxSize(150, 30);

		JFXButton updateButton = new JFXButton();
		updateButton.setText("Update");

		updateButton.setOnAction(
				e -> {
					if (employeeIDText.getText().length() > 0
							&& firstNameText.getText().length() > 2
							&& lastNameText.getText().length() > 2
							&& emailText.getText().length() > 2
							&& phoneNumberText.getText().length() > 2
							&& addressText.getText().length() > 2) {
						System.out.println("submit emp!");
						updateButton.setTextFill(Color.GREEN);
						try {
							Employee employee = new Employee(employeeIDText.getText(),
									type.getSelectionModel().getSelectedItem().toString(),
									firstNameText.getText(),
									lastNameText.getText(),
									emailText.getText(),
									phoneNumberText.getText(),
									addressText.getText(),
									new Date());
							(new EmployeeWrapperImpl()).enterEmployee(employee);
							popup.hide();
							this.initializeEmployeeTable();
						} catch (IOException | ParseException ex) {
							ex.printStackTrace();
						}
					} else {
						updateButton.setTextFill(Color.RED);
					}
				});

		// add it to the scene
		GridPane content = new GridPane();
		content.setRowIndex(empIDLabel, 0);
		content.setColumnIndex(empIDLabel, 0);
		content.setRowIndex(typeLabel, 1);
		content.setColumnIndex(typeLabel, 0);
		content.setRowIndex(firstNameLabel, 2);
		content.setColumnIndex(firstNameLabel, 0);
		content.setRowIndex(lastNameLabel, 3);
		content.setColumnIndex(lastNameLabel, 0);
		content.setRowIndex(emailLabel, 4);
		content.setColumnIndex(emailLabel, 0);
		content.setRowIndex(phoneNumLabel, 5);
		content.setColumnIndex(phoneNumLabel, 0);
		content.setRowIndex(addressLabel, 6);
		content.setColumnIndex(addressLabel, 0);
		content.setRowIndex(cancelUpdateButton, 7);
		content.setColumnIndex(cancelUpdateButton, 0);

		content.setRowIndex(employeeIDText, 0);
		content.setColumnIndex(employeeIDText, 1);
		content.setRowIndex(type, 1);
		content.setColumnIndex(type, 1);
		content.setRowIndex(firstNameText, 2);
		content.setColumnIndex(firstNameText, 1);
		content.setRowIndex(lastNameText, 3);
		content.setColumnIndex(lastNameText, 1);
		content.setRowIndex(emailText, 4);
		content.setColumnIndex(emailText, 1);
		content.setRowIndex(phoneNumberText, 5);
		content.setColumnIndex(phoneNumberText, 1);
		content.setRowIndex(addressText, 6);
		content.setColumnIndex(addressText, 1);
		content.setRowIndex(updateButton, 7);
		content.setColumnIndex(updateButton, 1);

		content
				.getChildren()
				.addAll(
						empIDLabel,
						employeeIDText,
						typeLabel,
						type,
						firstNameLabel,
						firstNameText,
						lastNameLabel,
						lastNameText,
						emailLabel,
						emailText,
						phoneNumLabel,
						phoneNumberText,
						addressLabel,
						addressText,
						updateButton,
						cancelUpdateButton);

		content.setPadding(new Insets(10, 5, 10, 5));
		content.setBackground(
				new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
		content.setEffect(new DropShadow());

		popup.getContent().add(content);

		popup.show(App.getStage());


	}
}
