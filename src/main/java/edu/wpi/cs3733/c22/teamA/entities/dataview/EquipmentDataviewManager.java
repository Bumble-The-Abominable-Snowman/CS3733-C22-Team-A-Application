package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentWrapperImpl;
import edu.wpi.cs3733.c22.teamA.controllers.DataViewCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EquipmentDataviewManager {
	JFXTreeTableView<RecursiveObj> table;
	DataViewCtrl dataViewCtrl;

	String[] equipmentColumnNames = {"ID", "Type", "Is Clean?", "Location", "Is Available?"};

	private List<Equipment> eqList = new ArrayList<>();


	public EquipmentDataviewManager(DataViewCtrl dataViewCtrl){
		this.dataViewCtrl = dataViewCtrl;
	}

	public void delete() throws SQLException {
		try {
			EquipmentDAO equipmentDAO = new EquipmentWrapperImpl();
			equipmentDAO.deleteMedicalEquipment(
					table.getSelectionModel().getSelectedItem().getValue().equip.getStringFields().get("equipment_id"));
			dataViewCtrl.titleLabel.setText("Equipment");
			initializeEquipmentTable();
		}
		catch (NullPointerException | IOException aE){

		}
	}

	public void initializeEquipmentTable() throws IOException {

		table = dataViewCtrl.getTable();
		dataViewCtrl.getSelectEmployeeBox().setVisible(false);

		List<JFXTreeTableColumn<RecursiveObj, String>> equipmentColumns = new ArrayList<>();

		for (String columnName : equipmentColumnNames) {
			JFXTreeTableColumn<RecursiveObj, String> column = new JFXTreeTableColumn<>(columnName);
			column.setPrefWidth(100);
			column.setStyle("-fx-alignment: center ;");

			equipmentColumns.add(column);
		}

		equipmentColumns
				.get(0)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().equip.getStringFields().get("equipment_id")));
		equipmentColumns
				.get(1)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().equip.getStringFields().get("equipment_type")));
		equipmentColumns
				.get(2)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(
										(Boolean)param.getValue().getValue().equip.getFields().get("is_clean") ? "Yes" : "No"));
		equipmentColumns
				.get(3)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().equip.getStringFields().get("current_location")));
		equipmentColumns
				.get(4)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(
										(Boolean)param.getValue().getValue().equip.getFields().get("is_available") ? "Yes" : "No"));

		// Grab equipment from database
		EquipmentDAO database = new EquipmentWrapperImpl();
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

		dataViewCtrl.setupViewDetailsAndModify();
	}

	public void modifyPopup(JFXComboBox<String> field, TextArea value, JFXButton updateButton){
		// BIGGER MARKER
		Equipment eq = this.eqList.get(table.getSelectionModel().getSelectedIndex());

		field.getItems().addAll(eq.getStringFields().keySet());

		field.setOnAction(
				e -> value.setText(eq.getStringFields().get(field.getSelectionModel().getSelectedItem())));

		updateButton.setOnAction(
				e -> {
					if (field.getSelectionModel().getSelectedIndex() > -1
							&& value.getText().length() > 0) {

						EquipmentWrapperImpl equipmentWrapper = new EquipmentWrapperImpl();
						try {
							String aField = "";
							if(field.getValue().equals("EquipmentType")){
								aField = "equipment_type";
							} else if (field.getValue().equals("EquipmentID")){
								aField = "equipment_id";
							} else if (field.getValue().equals("CurrentLocation")){
								aField = "current_location";
							}
							if (aField == "current_location") {
								LocationDAO locationDAO = new LocationWrapperImpl();
								List<Location> aList = locationDAO.getNodeList();
								Location theL = new Location();
								for (Location aL : aList) {
									if (value.getText().equals(aL.getStringFields().get("node_id"))) {
										theL = aL;
										break;
									}
								}
								if (theL.getStringFields().get("node_id") == null) {
									JOptionPane pane = new JOptionPane("Location does not exist", JOptionPane.ERROR_MESSAGE);
									JDialog dialog = pane.createDialog("Update failed");
									dialog.setVisible(true);
									updateButton.setTextFill(Color.RED);
									return;
								}
								if (eq.getFields().get("is_clean").equals("No")) {
									JOptionPane pane = new JOptionPane("Dirty equipment cannot be moved", JOptionPane.ERROR_MESSAGE);
									JDialog dialog = pane.createDialog("Update failed");
									dialog.setVisible(true);
									updateButton.setTextFill(Color.RED);
									return;
								}
								if (!(theL.getStringFields().get("node_type").equals("STOR")) && !(theL.getStringFields().get("node_type").equals("PATI"))) {
									JOptionPane pane = new JOptionPane("Equipment cannot be stored here", JOptionPane.ERROR_MESSAGE);
									JDialog dialog = pane.createDialog("Update failed");
									dialog.setVisible(true);
									updateButton.setTextFill(Color.RED);
									return;
								}
							}
							eq.setFieldByString(field.getSelectionModel().getSelectedItem(), value.getText());
							equipmentWrapper.updateMedicalEquipment(eq);
							updateButton.setTextFill(Color.GREEN);
							this.initializeEquipmentTable();
						} catch (Exception ex) {
							ex.printStackTrace();
							updateButton.setTextFill(Color.RED);
						}

					}
				});
	}
}
