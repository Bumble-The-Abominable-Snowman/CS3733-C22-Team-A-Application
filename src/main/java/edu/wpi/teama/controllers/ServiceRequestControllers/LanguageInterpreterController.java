package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.Adb.Location.Location;
import edu.wpi.teama.Adb.Location.LocationDerbyImpl;
import edu.wpi.teama.controllers.SceneController;
import edu.wpi.teama.entities.LanguageInterpreterRequest;
import java.io.IOException;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class LanguageInterpreterController extends GenericServiceRequestsController {
  @FXML private Button returnHomeButton;
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private Button clearButton;
  @FXML private ComboBox<String> employeeChoice;
  @FXML private ComboBox<String> toLocationChoice;
  @FXML private ComboBox<String> languageChoice;
  @FXML private TextArea commentsBox;

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() {
    sceneID = SceneController.SCENES.LANGUAGE_INTERPRETER_SERVICE_REQUEST_SCENE;

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
    languageChoice.setVisibleRowCount(5);

    toLocationChoice.getItems().removeAll(toLocationChoice.getItems());
    toLocationChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toLocationChoice.setVisibleRowCount(5);

    employeeChoice.getItems().removeAll(employeeChoice.getItems());
    employeeChoice.getItems().addAll("Placeholder");
    employeeChoice.getSelectionModel().select("Employee");
    employeeChoice.setVisibleRowCount(5);
  }

  @FXML
  private void createLanguageRequest(LanguageInterpreterRequest langRequest) {
    langRequest.setLanguage(languageChoice.getValue());
    langRequest.setToLocation(toLocationChoice.getValue());
    langRequest.setRequestedEmployee(employeeChoice.getValue());
    langRequest.setToLocation(commentsBox.getText());
  }

  @FXML
  void submitRequest() throws IOException {
    LanguageInterpreterRequest langRequest = new LanguageInterpreterRequest();
    if (!languageChoice.getSelectionModel().getSelectedItem().equals("Language")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && !employeeChoice.getSelectionModel().getSelectedItem().equals("Employee")) {
      this.createLanguageRequest(langRequest);
      this.returnToHomeScene();
    }
  }
}
