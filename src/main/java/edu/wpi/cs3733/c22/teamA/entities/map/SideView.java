package edu.wpi.cs3733.c22.teamA.entities.map;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class SideView {
	private List<Button> sideView = new ArrayList<>();

	public SideView(){

	}

	public void hideSideView() {
		for (Button b : sideView) {
			b.setVisible(false);
		}
	}


}
