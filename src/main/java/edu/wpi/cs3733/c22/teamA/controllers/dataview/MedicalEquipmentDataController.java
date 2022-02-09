package edu.wpi.cs3733.c22.teamA.controllers.dataview;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.MedicalEquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.MedicalEquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.MedicalEquipment;
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

public class MedicalEquipmentDataController implements Initializable {
  @FXML JFXButton backButton;
  @FXML JFXTreeTableView<MedicalEquipment> equipmentTable;

  private final SceneController sceneController = Aapp.sceneController;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    // Create all columns in the tracker table
    JFXTreeTableColumn<MedicalEquipment, String> id = new JFXTreeTableColumn<>("ID");
    JFXTreeTableColumn<MedicalEquipment, String> type = new JFXTreeTableColumn<>("Type");
    JFXTreeTableColumn<MedicalEquipment, String> clean = new JFXTreeTableColumn<>("Is Clean?");
    JFXTreeTableColumn<MedicalEquipment, String> location = new JFXTreeTableColumn<>("Location");
    JFXTreeTableColumn<MedicalEquipment, String> available =
        new JFXTreeTableColumn<>("Is Available?");
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
        (TreeTableColumn.CellDataFeatures<MedicalEquipment, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEquipmentID()));
    type.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipment, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getEquipmentType()));
    clean.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipment, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getIsClean() ? "Yes" : "No"));
    location.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipment, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getCurrentLocation()));
    available.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<MedicalEquipment, String> param) ->
            new SimpleStringProperty(param.getValue().getValue().getIsAvailable() ? "Yes" : "No"));

    // Grab equipment from database
    MedicalEquipmentDAO database = new MedicalEquipmentDerbyImpl();
    List<MedicalEquipment> equipFromDatabase = database.getMedicalEquipmentList();
    ObservableList<MedicalEquipment> equipment = FXCollections.observableArrayList();
    for (MedicalEquipment item : equipFromDatabase) {
      equipment.add(item);
    }

    // Sets up the table and puts the equipment data under the columns
    final TreeItem<MedicalEquipment> root =
        new RecursiveTreeItem<>(equipment, RecursiveTreeObject::getChildren);
    equipmentTable.getColumns().setAll(id, type, clean, location, available);
    equipmentTable.setRoot(root);
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
  }
}
