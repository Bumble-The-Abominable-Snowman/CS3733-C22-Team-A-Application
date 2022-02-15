package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javax.swing.*;

public abstract class MasterCtrl {

  @FXML public JFXHamburger hamburger;

  @FXML public JFXDrawer drawer;

  @FXML public JFXButton backButton;

  @FXML
  public void initialize(URL url, ResourceBundle rb) {

    HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamburger);

    GridPane menuBox = null;
    try {
      menuBox = FXMLLoader.load(getClass().getResource("Menu.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    drawer.setSidePane(menuBox);
    hamburger.setOnMouseClicked(
        mouseEvent -> {
          transition.setRate(transition.getRate() * -1);
          transition.play();

          if (drawer.isOpened()) drawer.close();
          else drawer.open();
        });
  }
}
