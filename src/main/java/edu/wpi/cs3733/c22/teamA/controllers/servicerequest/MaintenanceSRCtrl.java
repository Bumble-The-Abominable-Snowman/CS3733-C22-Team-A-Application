package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class MaintenanceSRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private TextArea typeOtherBox;

  @FXML
  private void initialize() {
    sceneID = SceneSwitcher.SCENES.SANITATION_SR;

    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    homeButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    clearButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    submitButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    commentsBox.setWrapText(true);
    typeOtherBox.setWrapText(true);

    // Put sanitation types in temporary type menu
    typeChoice.getItems().addAll("Decontaminate Area", "Floor Spill", "Other");
    typeChoice.getSelectionModel().select("Select Type");
    typeChoice
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue.equals("Other")) {
                typeOtherBox.setVisible(true);
              } else {
                typeOtherBox.setVisible(false);
              }
            });

    // Put locations in temporary location menu
    toLocationChoice.getSelectionModel().select("Select Location");
    toLocationChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toLocationChoice.setVisibleRowCount(5);
  }

  @FXML
  void submitRequest() {
    // Create request object
    //    MaintenanceSR maintenanceSR =
    //        new MaintenanceSR(
    //            "PlaceHolderID",
    //            "N/A",
    //            toLocationChoice.getSelectionModel().getSelectedItem(),
    //            App.factory.getUsername(),
    //            "employee",
    //            new Timestamp((new Date()).getTime()).toString(),
    //            SR.Status.BLANK,
    //            "Sanitation Services",
    //            commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
    //            typeChoice.getValue());
    //    ServiceRequestDerbyImpl<MaintenanceSR> serviceRequestDAO =
    //        new ServiceRequestDerbyImpl<>(new MaintenanceSR());
    //    serviceRequestDAO.enterServiceRequest(maintenanceSR);

    // Submit to database
  }
}
