package edu.wpi.cs3733.c22.teamA.controllers.map;

import javafx.fxml.FXML;
import javafx.scene.shape.Polygon;

public class MapEditorController {

	@FXML
	public void initialize(){
		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(new Double[] { 1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0 });
	}

}
