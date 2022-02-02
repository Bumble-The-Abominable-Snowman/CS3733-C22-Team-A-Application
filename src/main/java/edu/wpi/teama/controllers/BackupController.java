package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BackupController {
  public Button refreshButton;
  @FXML private Text selectedFileText;
  @FXML private ListView<String> fileList;
  @FXML private Button homeButton;
  @FXML private Button backButton;
  @FXML private Button loadFromBackupButton;
  private FXMLLoader loader = new FXMLLoader();

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
    URL xmlUrl = Aapp.class.getResource("views/settings.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) homeButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Settings");
    window.show();
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource("views/home.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) homeButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Home");
    window.show();
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
