package edu.wpi.cs3733.c22.teamA.controllers;

import edu.wpi.cs3733.c22.teamA.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class AboutCtrl extends MasterCtrl {

    @FXML
    private void initialize() {

        configure();

    }

    @FXML
    private void activateBumble(){
        helpButton.setVisible(false);
        bumbleXButton.setVisible(true);
        bumbleHead.setVisible(true);
        bubbleText.setVisible(true);
    }

    @FXML
    private void terminateBumble(){
        helpButton.setVisible(true);
        bumbleXButton.setVisible(false);
        bumbleHead.setVisible(false);
        bubbleText.setVisible(false);
    }

}

