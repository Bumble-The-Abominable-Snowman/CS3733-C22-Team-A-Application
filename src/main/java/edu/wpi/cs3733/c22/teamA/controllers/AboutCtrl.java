package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.App;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AboutCtrl extends MasterCtrl {

    @FXML private ImageView bumbleBlinkHead;
    @FXML private Label bubble1Text;
    @FXML private Label bubble2Text;
    @FXML private JFXButton previousButton;
    @FXML private JFXButton nextButton;
    @FXML private JFXButton previous1Button;
    @FXML private JFXButton next1Button;

    @FXML
    private void initialize() {

        configure();

        double previousTextSize = previousButton.getFont().getSize();
        double nextTextSize = nextButton.getFont().getSize();
        double previous1TextSize = previous1Button.getFont().getSize();
        double next1TextSize = next1Button.getFont().getSize();
        double bubble1TextSize = bubble1Text.getFont().getSize();
        double bubble2TextSize = bubble2Text.getFont().getSize();

        App.getStage()
                .widthProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {
                            previousButton.setStyle(
                                    "-fx-font-size: "
                                            + ((App.getStage().getWidth() / 1000) * previousTextSize)
                                            + "pt;");
                            nextButton.setStyle(
                                    "-fx-font-size: "
                                            + ((App.getStage().getWidth() / 1000) * nextTextSize)
                                            + "pt;");
                            previous1Button.setStyle(
                                    "-fx-font-size: "
                                            + ((App.getStage().getWidth() / 1000) * previous1TextSize)
                                            + "pt;");
                            next1Button.setStyle(
                                    "-fx-font-size: "
                                            + ((App.getStage().getWidth() / 1000) * next1TextSize)
                                            + "pt;");
                            bubble1Text.setStyle(
                                    "-fx-font-size: "
                                            + ((App.getStage().getWidth() / 1000) * bubble1TextSize)
                                            + "pt;");
                            bubble2Text.setStyle(
                                    "-fx-font-size: "
                                            + ((App.getStage().getWidth() / 1000) * bubble2TextSize)
                                            + "pt;");
                        });

    }

    public void activateBumble(){
        helpButton.setVisible(false);
        bumbleXButton.setVisible(true);
        bumbleHead.setVisible(true);
        bubbleText.setVisible(true);
        nextButton.setVisible(true);
    }

    public void terminateBumble(){
        helpButton.setVisible(true);
        bumbleXButton.setVisible(false);
        bumbleHead.setVisible(false);
        bubbleText.setVisible(false);
        bubble1Text.setVisible(false);
        bubble2Text.setVisible(false);
        previousButton.setVisible(false);
        nextButton.setVisible(false);
        previous1Button.setVisible(false);
        next1Button.setVisible(false);
    }

    public void next(){
        PauseTransition pt = new PauseTransition(Duration.millis(100));
        bumbleBlinkHead.setVisible(true);
        pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
        pt.play();
        previousButton.setVisible(true);
        nextButton.setVisible(false);
        next1Button.setVisible(true);
        bubbleText.setVisible(false);
        bubble1Text.setVisible(true);
    }

    public void previous(){
        PauseTransition pt = new PauseTransition(Duration.millis(100));
        bumbleBlinkHead.setVisible(true);
        pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
        pt.play();
        previousButton.setVisible(false);
        nextButton.setVisible(true);
        next1Button.setVisible(false);
        bubbleText.setVisible(true);
        bubble1Text.setVisible(false);
    }

    public void next1(){
        PauseTransition pt = new PauseTransition(Duration.millis(100));
        bumbleBlinkHead.setVisible(true);
        pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
        pt.play();
        previous1Button.setVisible(true);
        next1Button.setVisible(false);
        bubble1Text.setVisible(false);
        bubble2Text.setVisible(true);
    }

    public void previous1() {
        PauseTransition pt = new PauseTransition(Duration.millis(100));
        bumbleBlinkHead.setVisible(true);
        pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
        pt.play();
        previous1Button.setVisible(false);
        next1Button.setVisible(true);
        bubble1Text.setVisible(true);
        bubble2Text.setVisible(false);
    }

}

