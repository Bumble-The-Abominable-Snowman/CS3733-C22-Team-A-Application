package edu.wpi.teama.controllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teama.Aapp;
import edu.wpi.teama.Adb.Location.Location;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;

public class LocationDataController implements Initializable {
  @FXML Button backButton;
  @FXML JFXTreeTableView<Location> locationTable;
  boolean fillerYes = true;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Create all columns in the tracker table
    JFXTreeTableColumn<Location, String> id = new JFXTreeTableColumn<>("ID");
    JFXTreeTableColumn<Location, String> name = new JFXTreeTableColumn<>("Room Name");
    JFXTreeTableColumn<Location, String> floorNum = new JFXTreeTableColumn<>("Floor Number");
    JFXTreeTableColumn<Location, String> storeXRAY = new JFXTreeTableColumn<>("Store XRAY?");
    JFXTreeTableColumn<Location, String> storeBed = new JFXTreeTableColumn<>("Store Beds?");
    JFXTreeTableColumn<Location, String> storePump = new JFXTreeTableColumn<>("Store I. Pumps?");
    JFXTreeTableColumn<Location, String> storeRecliner =
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
        (TreeTableColumn.CellDataFeatures<Location, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getNodeID()));
    name.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Location, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getLongName()));
    floorNum.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Location, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getFloor()));
    // this code has to be implemented when a location's medical equipment is known
    // currently filled with filler Yes values, which represent that atm every room
    // can store every equipment. In the future, this will not be true.
    storeXRAY.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Location, String> param) ->
            new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreXRAY()));
    storeBed.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Location, String> param) ->
            new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreBed()));
    storePump.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Location, String> param) ->
            new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    // new SimpleStringProperty(param.getValue().getValue().getCanStorePump()));
    storeRecliner.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Location, String> param) ->
            new SimpleStringProperty(fillerYes ? "Yes" : "No"));
    // new SimpleStringProperty(param.getValue().getValue().getCanStoreRecliner()));

    // Grab location / equipment from database, these are dummies
    ObservableList<Location> locations = FXCollections.observableArrayList();
    locations.add(new Location("12", 0, 0, "Three", "Tower", "false", "Room 312", "false"));
    locations.add(new Location("24", 0, 0, "Two", "Tower", "false", "Room 2-A", "false"));

    // Sets up the table and puts the location data under the columns
    final TreeItem<Location> root =
        new RecursiveTreeItem<>(locations, RecursiveTreeObject::getChildren);
    locationTable
        .getColumns()
        .setAll(id, name, floorNum, storeXRAY, storeBed, storePump, storeRecliner);
    locationTable.setRoot(root);
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource("views/home.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) backButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Home");
    window.show();
  }
}
