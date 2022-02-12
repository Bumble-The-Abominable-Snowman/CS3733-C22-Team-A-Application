package edu.wpi.cs3733.c22.teamA.controllers.dataview;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Location;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class LocationDataController implements Initializable {
  @FXML JFXButton backButton;
  @FXML JFXTreeTableView<Location> locationTable;
  boolean fillerYes = true;

  private final SceneSwitcher sceneSwitcher = Aapp.sceneSwitcher;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

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
    // the following code has to be implemented when a location's medical equipment is known.
    // eahc is currently filled with filler Yes values, which represent that atm every room
    // can store every equipment. In the future, this will not be true and must be updated
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
    LocationDAO locationBase = new LocationDerbyImpl();
    List<Location> locationFromDatabase = locationBase.getNodeList();
    ObservableList<Location> locations = FXCollections.observableArrayList();
    for (Location currLoc : locationFromDatabase) {
      locations.add(currLoc);
    }

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
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME_SCENE);
  }
}
