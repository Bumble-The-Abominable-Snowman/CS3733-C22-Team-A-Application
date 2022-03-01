package edu.wpi.cs3733.c22.teamA.controllers;

import edu.wpi.cs3733.c22.teamA.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class AboutCtrl extends MasterCtrl {

    @FXML private ImageView bumbleHead;
    @FXML private Label bubbleText;

    @FXML
    private void initialize() {

        configure();
        double bubbleTextSize = bubbleText.getFont().getSize();

        App.getStage()
                .widthProperty()
                .addListener(
                        (obs, oldVal, newVal) -> {
                            bubbleText.setStyle(
                                    "-fx-font-size: "
                                            + ((App.getStage().getWidth() / 1000) * bubbleTextSize)
                                            + "pt;");
                        });

    }

}

