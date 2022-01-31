package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LanguageInterpreterController {
  @FXML private Button floor1Button;
  @FXML private Button floor2Button;
  @FXML private Button floor3Button;
  @FXML private Button floor4Button;
  @FXML private Button returnHomeButton;
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private ChoiceBox<String> displayLang;
  @FXML private ChoiceBox<String> langInterpreter;
  @FXML private ChoiceBox<String> langInterpreter1;
  @FXML private TextField roomNum;
  @FXML private TextField commentsBox;

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
}
