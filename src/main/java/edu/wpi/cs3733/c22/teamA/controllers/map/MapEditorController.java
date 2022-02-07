package edu.wpi.cs3733.c22.teamA.controllers.map;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.shape.Polygon;

public class MapEditorController {

  @FXML JFXButton groundFloorButton = new JFXButton();
  @FXML JFXButton lowerFloorOneButton = new JFXButton();
  @FXML JFXButton lowerFloorTwoButton = new JFXButton();
  @FXML JFXButton floorOneButton = new JFXButton();
  @FXML JFXButton floorTwoButton = new JFXButton();
  @FXML JFXButton floorThreeButton = new JFXButton();
  @FXML JFXButton floorFourteenButton = new JFXButton();

  @FXML JFXButton newLocButton = new JFXButton();
  @FXML JFXButton viewTableButton = new JFXButton();

  @FXML JFXButton saveButton = new JFXButton();
  @FXML JFXButton clearButton = new JFXButton();

  @FXML JFXButton backButton = new JFXButton();
  @FXML JFXButton homeButton = new JFXButton();


  @FXML
  public void initialize() {
    Polygon polygon = new Polygon();
    polygon.getPoints().addAll(new Double[] {1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0});
  }

  public void clearSubmission(){

  }

  public void returnToHomeScene(){

  }
  public void returnToSelectServiceScene(){

  }
}
