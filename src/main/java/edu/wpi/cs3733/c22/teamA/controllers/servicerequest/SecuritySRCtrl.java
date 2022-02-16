package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class SecuritySRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> typeChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private TextArea typeOtherBox;

  @FXML
  private void initialize() {

    sceneID = SceneSwitcher.SCENES.SANITATION_SR;

    // double typeChoiceTextSize = typeChoice.getFont().getSize();
    // double toLocationChoiceTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();
    double typeOtherTextSize = typeOtherBox.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              commentsBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * commentsTextSize)
                      + "pt;");
              typeOtherBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * typeOtherTextSize)
                      + "pt;");
            });

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
    //    SecuritySR securitySR =
    //        new SecuritySR(
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
    //    ServiceRequestDerbyImpl<SecuritySR> serviceRequestDAO =
    //        new ServiceRequestDerbyImpl<>(new SecuritySR());
    //    serviceRequestDAO.enterServiceRequest(securitySR);

    // Submit to database
  }
}
