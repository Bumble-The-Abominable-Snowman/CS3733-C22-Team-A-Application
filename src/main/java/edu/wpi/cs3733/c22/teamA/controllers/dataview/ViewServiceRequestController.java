package edu.wpi.cs3733.c22.teamA.controllers.dataview;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.medicalequipmentservicerequest.EquipmentServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.medicalequipmentservicerequest.EquipmentServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;
import edu.wpi.cs3733.c22.teamA.entities.requests.ServiceRequest;
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

public class ViewServiceRequestController implements Initializable {

  @FXML JFXButton backButton;
  @FXML JFXTreeTableView<ServiceRequest> requestsTable;

  private final SceneSwitcher sceneSwitcher = Aapp.sceneSwitcher;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    // Create all columns in the tracker table
    JFXTreeTableColumn<ServiceRequest, String> reqID = new JFXTreeTableColumn<>("ID");
    JFXTreeTableColumn<ServiceRequest, String> startLoc =
        new JFXTreeTableColumn<>("Start Location");
    JFXTreeTableColumn<ServiceRequest, String> endLoc = new JFXTreeTableColumn<>("End Location");
    JFXTreeTableColumn<ServiceRequest, String> employeeReq =
        new JFXTreeTableColumn<>("Requested By");
    JFXTreeTableColumn<ServiceRequest, String> employeeAss =
        new JFXTreeTableColumn<>("Employee Assigned");
    JFXTreeTableColumn<ServiceRequest, String> reqTime = new JFXTreeTableColumn<>("Request Time");
    JFXTreeTableColumn<ServiceRequest, String> reqStatus = new JFXTreeTableColumn<>("Status");
    JFXTreeTableColumn<ServiceRequest, String> reqType = new JFXTreeTableColumn<>("Type");
    JFXTreeTableColumn<ServiceRequest, String> comments = new JFXTreeTableColumn<>("Comments");
    reqID.setPrefWidth(80);
    startLoc.setPrefWidth(80);
    endLoc.setPrefWidth(80);
    employeeReq.setPrefWidth(80);
    employeeAss.setPrefWidth(80);
    reqTime.setPrefWidth(80);
    reqStatus.setPrefWidth(80);
    comments.setPrefWidth(80);
    reqType.setPrefWidth(80);
    reqID.setStyle("-fx-alignment: center ;");
    startLoc.setStyle("-fx-alignment: center ;");
    endLoc.setStyle("-fx-alignment: center ;");
    employeeReq.setStyle("-fx-alignment: center ;");
    employeeAss.setStyle("-fx-alignment: center ;");
    reqTime.setStyle("-fx-alignment: center ;");
    reqStatus.setStyle("-fx-alignment: center ;");
    comments.setStyle("-fx-alignment: center ;");
    reqType.setStyle("-fx-alignment: center ;");
    reqID.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getRequestID()));
    startLoc.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getStartLocation()));
    endLoc.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEndLocation()));
    employeeReq.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEmployeeRequested()));
    employeeAss.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEmployeeAssigned()));
    reqTime.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getRequestTime()));
    reqStatus.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getRequestStatus()));
    comments.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getComments()));
    reqType.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<ServiceRequest, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getRequestType()));
    EquipmentServiceRequestDAO serviceRequestBase = new EquipmentServiceRequestDerbyImpl();
    List<MedicalEquipmentServiceRequest> employeeFromDatabase =
        serviceRequestBase.getMedicalEquipmentServiceRequestList();
    ObservableList<ServiceRequest> requests = FXCollections.observableArrayList();
    for (ServiceRequest currLoc : employeeFromDatabase) {
      requests.add(currLoc);
    }

    // Sets up the table and puts the equipment data under the columns
    final TreeItem<ServiceRequest> root =
        new RecursiveTreeItem<ServiceRequest>(requests, RecursiveTreeObject::getChildren);
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
            reqType,
            comments);
    requestsTable.setRoot(root);
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME_SCENE);
  }
}
