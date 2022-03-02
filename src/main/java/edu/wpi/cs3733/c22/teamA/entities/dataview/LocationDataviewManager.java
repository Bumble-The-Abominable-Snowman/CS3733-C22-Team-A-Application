package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.DataViewCtrl;
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

public class LocationDataviewManager {
	JFXTreeTableView<RecursiveObj> table;
	DataViewCtrl dataViewCtrl;
	boolean fillerYes = true;

	String[] locationColumnNames = {
			"ID",
			"Room Name",
			"Floor Number",
			"Store XRAY?",
			"Store Beds?",
			"Store I. Pumps?",
			"Store Recliners?"
	};

	private List<Location> locList = new ArrayList<>();

	public LocationDataviewManager(DataViewCtrl dataViewCtrl){
		this.dataViewCtrl = dataViewCtrl;
	}

	public void delete() throws SQLException {
		try {
			LocationDAO locationDAO = new LocationDerbyImpl();
			locationDAO.deleteLocationNode(
					table.getSelectionModel().getSelectedItem().getValue().loc.getNodeID());
			dataViewCtrl.titleLabel.setText("Locations");
			initializeLocationTable();
		}
		catch (NullPointerException aE){

		}
	}

	public void initializeLocationTable() {
		table = dataViewCtrl.getTable();
		dataViewCtrl.getSelectEmployeeBox().setVisible(false);
		List<JFXTreeTableColumn<RecursiveObj, String>> locationColumns = new ArrayList<>();

		for (String columnName : locationColumnNames) {
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
		locList = locationBase.getNodeList();
		ObservableList<RecursiveObj> locations = FXCollections.observableArrayList();
		for (Location currLoc : locList) {
			RecursiveObj recursiveLoc = new RecursiveObj();
			recursiveLoc.loc = currLoc;
			if (!recursiveLoc.loc.getShortName().equals("N/A"))
			locations.add(recursiveLoc);
		}
		// Sets up the table and puts the location data under the columns
		final TreeItem<RecursiveObj> root =
				new RecursiveTreeItem<>(locations, RecursiveTreeObject::getChildren);

		table.getColumns().setAll(locationColumns);
		table.setRoot(root);

		dataViewCtrl.setupViewDetailsAndModify();
	}

	public void modifyPopup(JFXComboBox<String> field, TextArea value, JFXButton updateButton){
		Location loc = locList.get(table.getSelectionModel().getSelectedIndex());
		Method[] methods = loc.getClass().getMethods();
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
									this.initializeLocationTable();
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
