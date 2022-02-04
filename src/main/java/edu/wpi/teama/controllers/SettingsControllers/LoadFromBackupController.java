package edu.wpi.teama.controllers.SettingsControllers;

import edu.wpi.teama.Aapp;
import edu.wpi.teama.Adb.Adb;
import edu.wpi.teama.controllers.SceneController;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoadFromBackupController {
  public Button refreshButton;
  @FXML private ChoiceBox<String> TypeCSV;
  @FXML private Text selectedFileText;
  @FXML private ListView<String> fileList;
  private String lastSelectedFile;

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  public void initialize() {
    refreshFiles(null);

    fileList.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
              String currentItemSelected = fileList.getSelectionModel().getSelectedItem();
              lastSelectedFile = currentItemSelected;
              selectedFileText.setText(currentItemSelected);
              selectedFileText.setFill(Color.BLACK);
            }
          }
        });

    TypeCSV.getItems().removeAll(TypeCSV.getItems());
    TypeCSV.getItems()
        .addAll("TowerLocations", "Employee", "MedicalEquipment", "MedicalEquipmentServiceRequest");
    TypeCSV.setValue("CSV Type");
  }

  public void returnToSettingsScene(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene(SceneController.SCENES.SETTINGS_SCENE);
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
  }

  @FXML
  public void refreshFiles(ActionEvent actionEvent) {
    File f = new File("src/main/resources/edu/wpi/teama/db");

    ObservableList<String> items = FXCollections.observableArrayList();

    for (String pathname : Objects.requireNonNull(f.list())) {
      if (pathname.toLowerCase().endsWith(".csv")) {
        items.add(pathname);
      }
    }

    Collections.sort(items);
    fileList.setItems(items);
  }

  public void loadFromBackup(ActionEvent actionEvent) {
    // TODO: load csv file and populate the db given a filepath
    String filepath = "edu/wpi/teama/db/" + lastSelectedFile;

    if (!TypeCSV.getValue().equals("CSV Type") && lastSelectedFile.length() > 4) {
      Adb.inputFromCSV(TypeCSV.getValue(), filepath);

      selectedFileText.setText("Success!");
      selectedFileText.setFill(Color.GREEN);
    } else {
      selectedFileText.setText("Failed!");
      selectedFileText.setFill(Color.RED);
    }
  }
}
