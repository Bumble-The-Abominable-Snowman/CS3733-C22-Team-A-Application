package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class SRCtrl extends MasterCtrl {
  @FXML Button submitButton;
  @FXML Button clearButton;

  // TODO: request employee, comments, location, start end location, priority, etc.

  SceneSwitcher.SCENES sceneID;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  private void initialize() {
    double submitTextSize = submitButton.getFont().getSize();
    double clearTextSize = clearButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              submitButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * submitTextSize)
                      + "pt;");
              clearButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * clearTextSize) + "pt;");
            });
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
