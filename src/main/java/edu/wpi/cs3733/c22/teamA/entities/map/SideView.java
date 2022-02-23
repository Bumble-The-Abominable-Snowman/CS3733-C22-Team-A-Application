package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SideView {

  private List<JFXButton> buttons;
  private Label display;
  private MarkerManager markerManager;
  private AnchorPane anchorPane;
  private ImageView mapImageView;
  private String[] floorNames = {"3", "2", "1", "L1", "L2"};

  public SideView(AnchorPane anchorPane, ImageView mapImageView, MarkerManager markerManager) {
    this.anchorPane = anchorPane;
    this.mapImageView = mapImageView;
    this.markerManager = markerManager;
  }

  public void init() {
    // Creates side view buttons
    buttons = new ArrayList<>();
    int initialY = 1028;
    for (int i = 0; i < 5; i++) {
      double buttonY = initialY + i * 52 + mapImageView.getLayoutY();
      JFXButton button = new JFXButton();
      button.setLayoutX(107 + mapImageView.getLayoutX());
      button.setLayoutY(buttonY);
      button.setScaleY(button.getScaleY() + .3);
      int finalI = i;
      button.setOnMousePressed(e -> buttonClicked(floorNames[finalI]));
      buttons.add(button);
    }
    anchorPane.getChildren().addAll(buttons);

    // Creates side view display label
    display = new Label();
    display.setFont(new Font(25));
    display.setTextAlignment(TextAlignment.CENTER);
    display.setLayoutX(530 + mapImageView.getLayoutX());
    display.setLayoutY(25 + mapImageView.getLayoutY());
    anchorPane.getChildren().add(display);
  }

  public void buttonClicked(String floor) {
    markerManager.getFloorInfo(floor);
    String displayText = "Service Requests\n";
    List<SR> requests = markerManager.returnSRLocations();
    for (SR request : requests) {
      HashMap<String, String> data = request.getStringFields();
      displayText += data.get("request_id") + ", " + data.get("end_location") + "\n";
    }
    if (displayText.equals("Service Requests\n")) displayText += "None\n";
    displayText += "\n";
    List<Equipment> equipments = markerManager.returnEquipmentLocations();
    String clean = "";
    String dirty = "";
    for (Equipment equip : equipments) {
      if (equip.getIsClean())
        clean += equip.getEquipmentID() + ", " + equip.getCurrentLocation() + "\n";
      else dirty += equip.getEquipmentID() + ", " + equip.getCurrentLocation() + "\n";
    }
    if (clean.equals("")) clean += "None\n";
    if (dirty.equals("")) dirty += "None\n";
    displayText += "Clean Equipment\n" + clean + "\nDirty Equipment\n" + dirty;
    displayText = displayText.substring(0, displayText.length() - 1);
    display.setText(displayText);
  }

  public void toggleVisibility(boolean visible) {
    if (buttons != null) {
      for (JFXButton btn : buttons) {
        btn.setVisible(visible);
      }
    }
    if (display != null) display.setVisible(visible);
  }

  public void clearLabel() {
    if (display != null) display.setText("");
  }
}
