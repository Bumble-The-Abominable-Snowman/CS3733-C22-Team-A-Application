package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestWrapperImpl;
import edu.wpi.cs3733.c22.teamA.controllers.DataViewCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SRDataviewManager {

	private List<SR> srList = new ArrayList<>();

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

	JFXTreeTableView<RecursiveObj> table;
	DataViewCtrl dataViewCtrl;

	public SRDataviewManager(DataViewCtrl dataViewCtrl){
		this.dataViewCtrl = dataViewCtrl;
	}

	public void delete() throws SQLException, InvocationTargetException, IllegalAccessException {
		try{
			dataViewCtrl.titleLabel.setText("Service Requests");
			SR sr = table.getSelectionModel().getSelectedItem().getValue().sr;
			ServiceRequestWrapperImpl serviceRequestWrapper = new ServiceRequestWrapperImpl((SR.SRType) sr.getFields().get("sr_type"));
			serviceRequestWrapper.deleteServiceRequest(table.getSelectionModel().getSelectedItem().getValue().sr);
			initializeRequestsTable();
		} catch (NullPointerException aE){

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public void initializeRequestsTable()
			throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
		table = dataViewCtrl.getTable();
		EmployeeDAO employeeDAO = new EmployeeWrapperImpl();
		dataViewCtrl.getSelectEmployeeBox().getItems().addAll(employeeDAO.getEmployeeList().stream().map(Employee::getFullName).collect(Collectors.toList()));
		dataViewCtrl.getSelectEmployeeBox().getItems().add("All");
		dataViewCtrl.getSelectEmployeeBox().setVisible(true);
		List<JFXTreeTableColumn<RecursiveObj, String>> srColumns = new ArrayList<>();

		for (String columnName : srColumnNames) {
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
								new SimpleStringProperty(((Location)param.getValue().getValue().sr.getFields().get("start_location")).getStringFields().get("short_name")));
		srColumns
				.get(3)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(((Location)param.getValue().getValue().sr.getFields().get("end_location")).getStringFields().get("short_name")));
		srColumns
				.get(4)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(((Employee)param.getValue().getValue().sr.getFields().get("employee_requested")).getFullName()));
		srColumns
				.get(5)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(((Employee)param.getValue().getValue().sr.getFields().get("employee_assigned")).getFullName()));
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

		srList = ServiceRequestWrapperImpl.getAllServiceRequestList();
		ObservableList<RecursiveObj> requests = FXCollections.observableArrayList();
		for (SR sr : srList) {
			RecursiveObj recursiveSR = new RecursiveObj();
			recursiveSR.sr = sr;
			requests.add(recursiveSR);
		}

		// Sets up the table and puts the equipment data under the columns
		final TreeItem<RecursiveObj> root =
				new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);
		System.out.println(table == null);
		System.out.println(table.getColumns() == null);
		table.getColumns().setAll(srColumns);
		table.setRoot(root);

		dataViewCtrl.setupViewDetailsAndModify();
	}

	public void filterSRs(String newValue) throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
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
								new SimpleStringProperty(((Location)param.getValue().getValue().sr.getFields().get("start_location")).getStringFields().get("short_name")));
		srColumns
				.get(3)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(((Location)param.getValue().getValue().sr.getFields().get("end_location")).getStringFields().get("short_name")));
		srColumns
				.get(4)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(((Employee)param.getValue().getValue().sr.getFields().get("employee_requested")).getFullName()));
		srColumns
				.get(5)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(((Employee)param.getValue().getValue().sr.getFields().get("employee_assigned")).getFullName()));srColumns
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

		this.srList = ServiceRequestWrapperImpl.getAllServiceRequestList();
		ObservableList<RecursiveObj> requests = FXCollections.observableArrayList();
		for (SR sr : this.srList) {
			RecursiveObj recursiveSR = new RecursiveObj();
			recursiveSR.sr = sr;
			if(newValue.equals(((Employee)sr.getFields().get("employee_requested")).getFullName()) || newValue.equals(((Employee)sr.getFields().get("employee_assigned")).getFullName()) || newValue.equals("All")) {
				requests.add(recursiveSR);
			}
		}
		// Sets up the table and puts the equipment data under the columns
		final TreeItem<RecursiveObj> root =
				new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);

		table.getColumns().setAll(srColumns);
		table.setRoot(root);

		dataViewCtrl.setupViewDetailsAndModify();
	}

	public StringBuilder details(StringBuilder detailLabel){
		detailLabel = new StringBuilder();

		SR sr = srList.get(table.getSelectionModel().getSelectedIndex());
		for (String key : sr.getStringFields().keySet()) {
			if (!(key.equals("request_id")
					|| key.equals("start_location")
					|| key.equals("end_location")
					|| key.equals("employee_requested")
					|| key.equals("employee_assigned")
					|| key.equals("request_time")
					|| key.equals("request_status")
					|| key.equals("request_priority")
					|| key.equals("comments")
					|| key.equals("sr_type")))
			{
				detailLabel.append(
						String.format("%s: %s, ", key, sr.getStringFields().get(key)));
			}
		}


		if (detailLabel.length() > 1) {
			detailLabel.delete(detailLabel.length() - 2, detailLabel.length());
		}
		if (detailLabel.length() == 0) {
			detailLabel = new StringBuilder("No further details  ");
		}

		return detailLabel;
	}

	public void modifyPopup(JFXComboBox<String> field, TextArea value, JFXButton updateButton){
		SR sr = srList.get(table.getSelectionModel().getSelectedIndex());

		for (String key: sr.getStringFields().keySet()) {
			if ((key.equals("sr_type")) || (key.equals("request_time") || (key.equals("request_id"))))
			{
			}
			else
			{
				field.getItems().add(key);
			}
		}

		field.setOnAction(
				e -> {
					if (field.getSelectionModel().getSelectedIndex() > -1) {
						value.setText(sr.getStringFields().get(field.getSelectionModel().getSelectedItem()));
					}
				});

		updateButton.setOnAction(
				e -> {
					if (field.getSelectionModel().getSelectedIndex() > -1
							&& value.getText().length() > 0) {

						SR.SRType sr_type = SR.SRType.valueOf(sr.getStringFields().get("sr_type"));
						ServiceRequestWrapperImpl serviceRequestWrapper = new ServiceRequestWrapperImpl(sr_type);
						try {
							sr.setFieldByString(field.getSelectionModel().getSelectedItem(), value.getText());
							serviceRequestWrapper.updateServiceRequest(sr);
							updateButton.setTextFill(Color.GREEN);
							this.initializeRequestsTable();
						} catch (SQLException | IllegalAccessException | InvocationTargetException | IOException | ParseException ex) {
							ex.printStackTrace();
							updateButton.setTextFill(Color.RED);
						}
					}
				});
	}
}
