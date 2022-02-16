package edu.wpi.cs3733.c22.teamA.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeCtrl extends MasterCtrl {

  @FXML public Label homeTitle;

  @FXML
  private void initialize() {

    configure();
  }
}
