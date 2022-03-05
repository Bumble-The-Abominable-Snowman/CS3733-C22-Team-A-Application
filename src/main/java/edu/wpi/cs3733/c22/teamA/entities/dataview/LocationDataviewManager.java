package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentWrapperImpl;
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
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
			LocationDAO locationDAO = new LocationWrapperImpl();
			locationDAO.deleteLocationNode(
					table.getSelectionModel().getSelectedItem().getValue().loc.getStringFields().get("node_id"));
			dataViewCtrl.titleLabel.setText("Locations");
			initializeLocationTable();
		}
		catch (NullPointerException aE){

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	public void initializeLocationTable() throws IOException, ParseException {
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
								new SimpleStringProperty(param.getValue().getValue().loc.getStringFields().get("node_id")));
		locationColumns
				.get(1)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().loc.getStringFields().get("long_name")));
		locationColumns
				.get(2)
				.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
								new SimpleStringProperty(param.getValue().getValue().loc.getStringFields().get("floor")));
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
		LocationDAO locationBase = new LocationWrapperImpl();
		locList = locationBase.getNodeList();
		ObservableList<RecursiveObj> locations = FXCollections.observableArrayList();
		for (Location currLoc : locList) {
			RecursiveObj recursiveLoc = new RecursiveObj();
			recursiveLoc.loc = currLoc;
			if (!recursiveLoc.loc.getStringFields().get("short_name").equals("N/A"))
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

		field.getItems().addAll(loc.getStringFields().keySet());

		field.setOnAction( e -> value.setText(loc.getStringFields().get(field.getSelectionModel().getSelectedItem())));

		updateButton.setOnAction(
				e -> {
					if (field.getSelectionModel().getSelectedIndex() > -1
							&& value.getText().length() > 0) {

						LocationWrapperImpl locationWrapper = new LocationWrapperImpl();
						try {

							locationWrapper.updateLocation(loc);
							this.initializeLocationTable();
							updateButton.setTextFill(Color.GREEN);
						} catch (Exception ex) {
							ex.printStackTrace();
							updateButton.setTextFill(Color.RED);
						}

					}
				});
	}
}
