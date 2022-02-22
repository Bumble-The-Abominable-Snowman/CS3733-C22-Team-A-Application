package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class SideView {

  private List<JFXButton> buttons = new ArrayList<>();
  private GesturePaneManager gestureManager;
  private AnchorPane anchorPane;
  private ImageView mapImageView;

  public SideView(
      AnchorPane anchorPane, GesturePaneManager gestureManager, ImageView mapImageView) {
    this.anchorPane = anchorPane;
    this.gestureManager = gestureManager;
    this.mapImageView = mapImageView;
  }

  public void init() {
    int initialY = 1028;
    for (int i = 0; i < 5; i++) {
      double buttonX = 107 + mapImageView.getLayoutX();
      double buttonY = initialY + i * 52 + mapImageView.getLayoutY();
      JFXButton button = new JFXButton();
      button.setLayoutX(buttonX);
      button.setLayoutY(buttonY);
      button.setScaleY(button.getScaleY() + .3);
      buttons.add(button);
    }
    anchorPane.getChildren().addAll(buttons);
  }
  /*
  private List<Button> sideView = new ArrayList<>();

  public SideView() {}

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

   */
}
