package edu.wpi.cs3733.c22.teamA.controllers;

import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import javafx.fxml.FXML;

public class AboutCtrl extends MasterCtrl {

  @FXML
  private void help() throws IOException {

    this.onSceneSwitch();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }
}
