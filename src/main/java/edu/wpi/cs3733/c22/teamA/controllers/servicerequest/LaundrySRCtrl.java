package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.LaundrySR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class LaundrySRCtrl extends SRCtrl {
  @FXML private Label locationLabel;
  @FXML private JFXComboBox<String> washMode;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  LaundrySRCtrl() {
    super();
  }

  @FXML
  public void initialize() {
    sceneID = SceneSwitcher.SCENES.LAUNDRY_SR;

    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    homeButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    clearButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    submitButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    commentsBox.setWrapText(true);

    washMode.getItems().removeAll(washMode.getItems());
    washMode.getItems().addAll("Colors", "Whites", "Perm. press", "Save the trees!");
    washMode.setValue("Wash Mode");
  }

  @FXML
  void submitRequest() throws IOException {
    System.out.print("\nNew request, got some work to do bud!\n");
    System.out.printf("Selected wash mode is : %s\n", washMode.getValue());
    System.out.printf("Added this note : \n[NOTE START]\n%s\n[NOTE END]\n", commentsBox.getText());
    if (!washMode.getValue().equals("Wash Mode")) {
      LaundrySR laundrySR =
          new LaundrySR(
              "PlaceHolderID",
              "N/A",
              toLocationChoice.getSelectionModel().getSelectedItem(),
              App.factory.getUsername(),
              employeeChoice.getSelectionModel().getSelectedItem(),
              new Timestamp((new Date()).getTime()).toString(),
              SR.Status.BLANK,
              "Laundry Service",
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
              washMode.getValue());
      ServiceRequestDerbyImpl<LaundrySR> serviceRequestDAO =
          new ServiceRequestDerbyImpl<>(new LaundrySR());
      serviceRequestDAO.enterServiceRequest(laundrySR);
      this.goToHomeScene();
      // send request to database
    }
  }

  @FXML
  public void chooseFloor(ActionEvent actionEvent) {
    locationLabel.setText(((Button) actionEvent.getSource()).getText());
    locationLabel.setAlignment(Pos.CENTER);
  }
}
