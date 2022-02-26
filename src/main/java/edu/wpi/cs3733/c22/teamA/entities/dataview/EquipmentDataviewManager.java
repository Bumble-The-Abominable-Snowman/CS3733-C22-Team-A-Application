package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
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
		table = dataViewCtrl.getTable();
		EquipmentDAO equipmentDAO = new EquipmentDerbyImpl();
		equipmentDAO.deleteMedicalEquipment(
				table.getSelectionModel().getSelectedItem().getValue().equip.getEquipmentID());
		dataViewCtrl.titleLabel.setText("Equipment");
		initializeEquipmentTable();
	}

	public void initializeEquipmentTable() {
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

		dataViewCtrl.setupViewDetailsAndModify();
	}

	public void modifyPopup(JFXComboBox<String> field, TextArea value, JFXButton updateButton){
		// BIGGER MARKER
		Equipment eq = this.eqList.get(table.getSelectionModel().getSelectedIndex());
		Method[] methods = eq.getClass().getMethods();
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
									String aField = "";
									System.out.println(field.getValue());
									if(field.getValue().equals("EquipmentType")){
										aField = "equipment_type";
									} else if (field.getValue().equals("EquipmentID")){
										aField = "equipment_id";
									} else if (field.getValue().equals("CurrentLocation")){
										aField = "current_location";
									}
									if (aField == "current_location") {
										LocationDAO locationDAO = new LocationDerbyImpl();
										List<Location> aList = locationDAO.getNodeList();
										Location theL = new Location();
										for (Location aL : aList) {
											if (value.getText().equals(aL.getNodeID())) {
												theL = aL;
												break;
											}
										}
										if (theL.getNodeID() == null) {
											System.out.println("Location does not exist");
											updateButton.setTextFill(Color.RED);
											return;
										}
										if (!(theL.getNodeType().equals("STOR")) && !(theL.getNodeType().equals("PATI"))) {
											System.out.println("THIS EQUIPMENT CANNOT BE STORED HERE");
											updateButton.setTextFill(Color.RED);
											return;
										}
									}
									equipmentDerby.updateMedicalEquipment(
											eq.getEquipmentID(), aField, value.getText());
									updateButton.setTextFill(Color.GREEN);
									System.out.println("B4 initialize");
									this.initializeEquipmentTable();
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
