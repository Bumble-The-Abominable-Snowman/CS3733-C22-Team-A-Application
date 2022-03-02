package edu.wpi.cs3733.c22.teamA.controllers;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.io.IOException;

public class AboutCtrl extends MasterCtrl {

    @FXML
    private void initialize() {

        configure();

    }

    @FXML
    private void help() throws IOException {

        if (helpState != 0) {
            nextButton.setVisible(false);
            helpText.setVisible(false);
            drawer.setEffect(null);
            helpButton.setEffect(null);
            helpState = 0;
        }
        else {
            borderGlow.setColor(Color.GOLD);
            borderGlow.setOffsetX(0f);
            borderGlow.setOffsetY(0f);
            borderGlow.setHeight(45);
            nextButton.setVisible(true);
            helpText.setVisible(true);
            helpText.setText("Select a menu option to use the application.  This menu is present on every page and is the primary navigation tool you will use.");
            drawer.setEffect(borderGlow);
            helpState = 1;
        }

    }

    @FXML
    private void next() throws IOException {

        if (helpState == 1) {
            drawer.setEffect(null);
            helpText.setText("You can always click the help button to exit help at any time");
            helpButton.setEffect(borderGlow);
            helpState = 2;
        }

    }

}

