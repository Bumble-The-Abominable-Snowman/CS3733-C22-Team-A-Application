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

	// Sets up the side view
	public void showSideView() {
		String[] floorNames = {"Floor 3", "Floor 2", "Floor 1", "L1", "L2"};
		int initialY = 1025;
		for (int i = 0; i < 5; i++) {
			double buttonX = 116 + mapImageView.getLayoutX();
			double buttonY = initialY + i * 50 + mapImageView.getLayoutY();
			Button button = newButton(buttonX, buttonY, 420, 45);
			sideView.add(button);
			button.setShape(equipmentMarkerShape);
			String floor = floorNames[i];
			int srCount = 0;
			int dEquipCount = 0;
			int cEquipCount = 0;
			for (Location location : locations) {
				if (location.getFloor().equals(floor.replace("Floor ", ""))) {
					for (int j = 0; j < serviceRequests.size(); j++) {
						if (serviceRequests.get(j).getEndLocation().equals(location.getNodeID())) {
							srCount++;
						}
					}
					for (int j = 0; j < equipments.size(); j++) {
						if (equipments.get(j).getCurrentLocation().equals(location.getNodeID())) {
							if (!equipments.get(j).getIsClean()) dEquipCount++;
							else cEquipCount++;
						}
					}
				}
			}
			button.setText(
					"Reqs: " + srCount + " | Clean Equip: " + cEquipCount + " | Dirty Equip: " + dEquipCount);
			button.setOnMousePressed(mouseEvent -> floorSelectionComboBox.setValue(floor));
			miniAnchorPane.getChildren().add(button);
		}
	}

}
