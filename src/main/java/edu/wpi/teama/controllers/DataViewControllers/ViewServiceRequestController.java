package edu.wpi.teama.controllers.DataViewControllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teama.Aapp;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequestDAO;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequestImpl;
import edu.wpi.teama.controllers.SceneController;
import edu.wpi.teama.entities.requests.MedicalEquipmentServiceRequest;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

public class ViewServiceRequestController implements Initializable {
  @FXML Button backButton;
  @FXML JFXTreeTableView<MedicalEquipmentServiceRequest> requestsTable;

  private final SceneController sceneController = Aapp.sceneController;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Create all columns in the tracker table
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> reqID =
        new JFXTreeTableColumn<>("ID");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> startLoc =
        new JFXTreeTableColumn<>("Start Location");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> endLoc =
        new JFXTreeTableColumn<>("End Location");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> employeeReq =
        new JFXTreeTableColumn<>("Requested By");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> employeeAss =
        new JFXTreeTableColumn<>("Employee Assigned");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> reqTime =
        new JFXTreeTableColumn<>("Request Time");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> reqStatus =
        new JFXTreeTableColumn<>("Status");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> equipmentID =
        new JFXTreeTableColumn<>("Equipment ID");
    JFXTreeTableColumn<MedicalEquipmentServiceRequest, String> reqType =
        new JFXTreeTableColumn<>("Type");
    reqID.setPrefWidth(80);
    startLoc.setPrefWidth(80);
    endLoc.setPrefWidth(80);
    employeeReq.setPrefWidth(80);
    employeeAss.setPrefWidth(80);
    reqTime.setPrefWidth(80);
    reqStatus.setPrefWidth(80);
    equipmentID.setPrefWidth(80);
    reqType.setPrefWidth(80);
    reqID.setStyle("-fx-alignment: center ;");
    startLoc.setStyle("-fx-alignment: center ;");
    endLoc.setStyle("-fx-alignment: center ;");
    employeeReq.setStyle("-fx-alignment: center ;");
    employeeAss.setStyle("-fx-alignment: center ;");
    reqTime.setStyle("-fx-alignment: center ;");
    reqStatus.setStyle("-fx-alignment: center ;");
    equipmentID.setStyle("-fx-alignment: center ;");
    reqType.setStyle("-fx-alignment: center ;");
    reqID.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getRequestID()));
    startLoc.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getStartLocation()));
    endLoc.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEndLocation()));
    employeeReq.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEmployeeRequested()));
    employeeAss.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEmployeeAssigned()));
    reqTime.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getRequestTime()));
    reqStatus.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getRequestStatus()));
    equipmentID.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEquipmentID()));
    reqType.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipmentServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getRequestType()));
    MedicalEquipmentServiceRequestDAO serviceRequestBase = new MedicalEquipmentServiceRequestImpl();
    List<MedicalEquipmentServiceRequest> employeeFromDatabase =
        serviceRequestBase.getMedicalEquipmentServiceRequestList();
    ObservableList<MedicalEquipmentServiceRequest> requests = FXCollections.observableArrayList();
    for (MedicalEquipmentServiceRequest currLoc : employeeFromDatabase) {
      requests.add(currLoc);
    }

    // Sets up the table and puts the equipment data under the columns
    final TreeItem<MedicalEquipmentServiceRequest> root =
        new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);
    requestsTable
        .getColumns()
        .setAll(
            reqID,
            startLoc,
            endLoc,
            employeeReq,
            employeeAss,
            reqTime,
            reqStatus,
            equipmentID,
            reqType);
    requestsTable.setRoot(root);
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
  }
}
