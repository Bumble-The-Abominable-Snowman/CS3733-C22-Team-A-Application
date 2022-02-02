package edu.wpi.teama.controllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teama.Aapp;
import edu.wpi.teama.Adb.MedicalEquipment.MedicalEquipmentServiceRequest;
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

public class MedicalEquipmentDataController implements Initializable {
  @FXML Button backButton;
  @FXML JFXTreeTableView<MedicalEquipmentServiceRequest> equipmentTable;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Create all columns in the tracker table
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> id =
        new JFXTreeTableColumn<>("Request ID");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> type =
        new JFXTreeTableColumn<>("Start Location");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> clean =
        new JFXTreeTableColumn<>("End Location");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> location =
        new JFXTreeTableColumn<>("Employee Requests");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> available =
        new JFXTreeTableColumn<>("Employee Assigned");
    id.setPrefWidth(112);
    type.setPrefWidth(110);
    clean.setPrefWidth(110);
    location.setPrefWidth(115);
    available.setPrefWidth(110);
    id.setStyle("-fx-alignment: center ;");
    type.setStyle("-fx-alignment: center ;");
    clean.setStyle("-fx-alignment: center ;");
    location.setStyle("-fx-alignment: center ;");
    available.setStyle("-fx-alignment: center ;");
    id.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEquipmentID()));
    type.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEquipmentType()));
    clean.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getIsClean() ? "Yes" : "No"));
    location.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getCurrentLocation()));
    available.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getIsAvailable() ? "Yes" : "No"));

    // Grab equipment from database (uses example item currently)
    ObservableList<MedicalEquipmentServiceRequest> equipment = FXCollections.observableArrayList();
    equipment.add(new MedicalEquipmentServiceRequest("12", "XRAY", false, "Room 2-A", false));
    equipment.add(new MedicalEquipmentServiceRequest("14", "BED", true, "OR", true));

    // Sets up the table and puts the equipment data under the columns
    final TreeItem<MedicalEquipmentServiceRequest> root =
        new RecursiveTreeItem<>(equipment, RecursiveTreeObject::getChildren);
    equipmentTable.getColumns().setAll(id, type, clean, location, available);
    equipmentTable.setRoot(root);
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
