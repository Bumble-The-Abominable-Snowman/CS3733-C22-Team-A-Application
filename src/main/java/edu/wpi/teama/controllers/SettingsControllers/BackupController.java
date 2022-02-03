package edu.wpi.teama.controllers.SettingsControllers;

import edu.wpi.teama.Aapp;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class BackupController {
  public Button refreshButton;
  @FXML private Text selectedFileText;
  @FXML private ListView<String> fileList;
  @FXML private Button homeButton;
  @FXML private Button backButton;
  @FXML private Button loadFromBackupButton;
  private FXMLLoader loader = new FXMLLoader();

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  public void initialize() {
    refreshFiles(null);

    fileList.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
              String currentItemSelected = fileList.getSelectionModel().getSelectedItem();
              selectedFileText.setText(currentItemSelected);
            }
          }
        });
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
      if (pathname.endsWith(".csv")) {
        items.add(pathname);
      }
    }

    Collections.sort(items);
    fileList.setItems(items);
  }

  public void loadFromBackup(ActionEvent actionEvent) {
    // TODO: load csv file and populate the db given a filepath
    String filepath = "src/main/resources/edu/wpi/teama/db/" + selectedFileText.getText();
    System.out.println(filepath);
  }
}