package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import edu.wpi.teama.Adb.Location.Location;
import edu.wpi.teama.Adb.Location.LocationDerbyImpl;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LanguageInterpreterController {
  @FXML private Button returnHomeButton;
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private Button clearButton;
  @FXML private ComboBox<String> languageChoice;
  @FXML private ComboBox<String> toLocationChoice;
  @FXML private ComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  private void initialize() {
    commentsBox.setWrapText(true);

    languageChoice.getItems().removeAll(languageChoice.getItems());
    languageChoice
        .getItems()
        .addAll(
            "American Sign Language",
            "Arabic",
            "French",
            "German",
            "Italian",
            "Japanese",
            "Korean",
            "Mandarin",
            "Russian",
            "Spanish");
    languageChoice.getSelectionModel().select("Language");
    languageChoice.getSelectionModel().selectedItemProperty();
    languageChoice.setVisibleRowCount(5);

    toLocationChoice.getItems().removeAll(toLocationChoice.getItems());
    toLocationChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toLocationChoice.setVisibleRowCount(5);

    employeeChoice.getItems().removeAll(employeeChoice.getItems());
    employeeChoice.getSelectionModel().select("Employee");
    employeeChoice.setVisibleRowCount(5);
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource("views/home.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) returnHomeButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Home");
    window.show();
  }

  @FXML
  private void returnToSelectServiceScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource("views/selectServiceRequest.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) backButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Select Service Request");
    window.show();
  }

  @FXML
  private void clearSubmission() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/languageInterpreter.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) clearButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Language Services");
    window.show();
  }

  @FXML
  private LanguageInterpreterRequest createLanguageRequest() throws IOException {
    LanguageInterpreterRequest aRequest =
        new LanguageInterpreterRequest(
            languageChoice.getValue(),
            toLocationChoice.getValue(),
            employeeChoice.getValue(),
            commentsBox.getText());
    clearSubmission();
    return (aRequest);
  }
}
