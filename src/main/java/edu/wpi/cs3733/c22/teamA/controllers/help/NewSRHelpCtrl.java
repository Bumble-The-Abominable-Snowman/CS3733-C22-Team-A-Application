package edu.wpi.cs3733.c22.teamA.controllers.help;

import edu.wpi.cs3733.c22.teamA.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NewSRHelpCtrl {
    @FXML private Label titleLabel;
    @FXML private Label paragraphBodyLabel;

    @FXML private void initialize(){
        double titleTextSize = titleLabel.getFont().getSize();
        double paragraphBodyTextSize = paragraphBodyLabel.getFont().getSize();

        App.getStage()
                .widthProperty()
                .addListener(
                        (obs, oldVal, newVal) -> {
                            titleLabel.setStyle(
                                    "-fx-font-size: "
                                            + ((App.getStage().getWidth() / 1000) * titleTextSize)
                                            + "pt;");
                            paragraphBodyLabel.setStyle(
                                    "-fx-font-size: "
                                            + ((App.getStage().getWidth() / 1000) * paragraphBodyTextSize)
                                            + "pt;");
                        });
    }
}
