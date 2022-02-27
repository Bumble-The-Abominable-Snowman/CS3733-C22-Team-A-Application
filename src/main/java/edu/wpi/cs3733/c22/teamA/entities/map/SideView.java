package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SideView {

  private List<JFXButton> buttons;
  private List<JFXTextArea> texts;
  private Label label;
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
    int initialY = 1024;
    for (int i = 0; i < 5; i++) {
      double buttonY = initialY + i * 51 + mapImageView.getLayoutY();
      JFXButton button = new JFXButton();
      button.setLayoutX(121 + mapImageView.getLayoutX());
      button.setLayoutY(buttonY);
      button.setPrefHeight(51);
      button.setPrefWidth(415);
      int finalI = i;
      button.setOnMousePressed(e -> buttonClicked(floorNames[finalI]));
      markerManager.getFloorInfo(floorNames[finalI]);
      int dirty = 0;
      int clean = 0;
      List<Equipment> equipments = markerManager.returnEquipmentLocations();
      for (Equipment equip : equipments) {
        if (equip.getIsClean())
          dirty++;
        else
          clean++;
      }
      button.setText("Requests: " + markerManager.returnSRLocations().size() + " | Clean Equip: " + clean + " | Dirty Equip: " + dirty);
      buttons.add(button);
    }
    anchorPane.getChildren().addAll(buttons);

    // Creates side view display label
    label = new Label();
    label.setFont(new Font(25));
    label.setTextAlignment(TextAlignment.CENTER);
    label.setLayoutX(575 + mapImageView.getLayoutX());
    label.setLayoutY(25 + mapImageView.getLayoutY());
    anchorPane.getChildren().add(label);

    // Create side view text areas
    texts = new ArrayList<>();
    initialY = 65;
    for (int i = 0; i < 3; i++) {
      JFXTextArea box = new JFXTextArea();
      box.setLayoutX(525 + mapImageView.getLayoutX());
      box.setLayoutY(initialY + 325 * i + mapImageView.getLayoutY());
      box.setPrefWidth(300);
      box.setPrefHeight(250);
      box.setEditable(false);
      box.setVisible(false);
      texts.add(box);
      anchorPane.getChildren().add(box);
    }
  }

  public void buttonClicked(String floor) {
    label.setText("Service Requests\n\n\n\n\n\n\n\n\nClean Equipment\n\n\n\n\n\n\n\n\nDirty Equipment");
    markerManager.getFloorInfo(floor);
    for (int i = 0; i < 3; i++) {
      String displayText = "";
      if (i == 0) {
        List<SR> requests = markerManager.returnSRLocations();
        for (SR request : requests) {
          HashMap<String, String> data = request.getStringFields();
          displayText += data.get("request_id") + ", " + data.get("end_location") + "\n";
        }
      }
      else {
        List<Equipment> equipments = markerManager.returnEquipmentLocations();
        for (Equipment equip : equipments) {
          if ((i == 1 && equip.getIsClean()) || (i == 2 && !equip.getIsClean()))
            displayText += equip.getEquipmentID() + ", " + equip.getCurrentLocation() + "\n";
        }
      }
      if (displayText.equals("")) displayText += "None\n";
      displayText = displayText.substring(0, displayText.length() - 1);
      texts.get(i).setText(displayText);
      texts.get(i).setVisible(true);
    }
  }

  public void toggleVisibility(boolean visible) {
    if (buttons != null) {
      for (JFXButton btn : buttons) {
        btn.setVisible(visible);
      }
    }
    if (label != null) label.setVisible(visible);
    if (texts != null) {
      for (JFXTextArea box : texts) {
        box.setVisible(false);
      }
    }
  }

  public void clearLabel() {
    if (label != null) label.setText("");
  }
}
