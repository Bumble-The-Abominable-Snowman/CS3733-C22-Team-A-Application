package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.App;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class HelpCtrl extends MasterCtrl {
  @FXML private ImageView bumble;
  @FXML private JFXButton bumbleButton;

  @FXML
  private void initialize() {

    double bumbleTextSize = bumbleButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              bumbleButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * bumbleTextSize)
                      + "pt;");
            });
  }
}
