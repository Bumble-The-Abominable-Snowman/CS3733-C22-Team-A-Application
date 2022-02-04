package edu.wpi.teama.controllers.SettingsControllers;

import edu.wpi.teama.Aapp;
import edu.wpi.teama.Adb.Adb;
import edu.wpi.teama.controllers.SceneController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ExportToBackupController {
  @FXML public Button refreshButton;
  @FXML public TextField filename;
  @FXML public Text exportFileText;
  @FXML private ChoiceBox<String> TypeCSV;
  @FXML private ListView<String> fileList;

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  public void initialize() {
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

  public void exportToBackup(ActionEvent actionEvent) throws IOException {
    String input = filename.getCharacters().toString();

    if (!TypeCSV.getValue().equals("CSV Type") && input.length() > 0) {
      System.out.println(input);

      String filepath;
      if (input.endsWith(".csv")) {
        filepath = "edu/wpi/teama/db/" + input;
      } else {
        filepath = "edu/wpi/teama/db/" + input + ".csv";
      }
      Adb.exportToCSV(TypeCSV.getValue(), filepath);

      exportFileText.setText(filepath);
      exportFileText.setFill(Color.GREEN);
    } else {
      exportFileText.setText("Failed!");
      exportFileText.setFill(Color.RED);
    }
  }
}
