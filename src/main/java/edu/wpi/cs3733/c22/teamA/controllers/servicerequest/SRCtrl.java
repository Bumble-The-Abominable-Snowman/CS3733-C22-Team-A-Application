package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class SRCtrl {
  @FXML Button homeButton;
  @FXML Button submitButton;
  @FXML Button backButton;
  @FXML Button clearButton;

  // TODO: request employee, comments, location, start end location, priority, etc.

  SceneSwitcher.SCENES sceneID;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  private void initialize() {
    double homeTextSize = homeButton.getFont().getSize();
    double submitTextSize = submitButton.getFont().getSize();
    double backTextSize = backButton.getFont().getSize();
    double clearTextSize = clearButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              homeButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * homeTextSize) + "pt;");
              submitButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * submitTextSize)
                      + "pt;");
              backButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * backTextSize) + "pt;");
              clearButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * clearTextSize) + "pt;");
            });
  }

  @FXML
  void goToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  @FXML
  private void goToSelectServiceScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SELECT_SERVICE_REQUEST);
  }

  @FXML
  private void clearSubmission() throws IOException {
    sceneSwitcher.switchScene(sceneID);
  }

  @FXML
  abstract void submitRequest()
      throws IOException, TimeoutException, SQLException, InvocationTargetException,
          IllegalAccessException;
}
