package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.*;
import java.io.IOException;
import java.rmi.server.ExportException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SaveBackupCtrl extends MasterCtrl {
  @FXML private JFXButton saveBackupButton;
  @FXML public TextField fileName;
  @FXML public Text exportFileText;
  @FXML private JFXComboBox<String> TypeCSV;
  //@FXML private Text exportLabel;

  /*
  @FXML private JFXButton nextButton;
  @FXML private ImageView bumbleBlinkHead;
  @FXML private JFXButton previousButton;
  @FXML private JFXButton previous1Button;
  @FXML private JFXButton next1Button;
  @FXML private JFXButton previous2Button;
  @FXML private JFXButton next2Button;
  @FXML private Label bubbleText;
  @FXML private Label bubble1Text;
  @FXML private Label bubble2Text;
  @FXML private Label bubble3Text; */

  @FXML
  public void initialize() {

    configure();

    double saveBackupTextSize = saveBackupButton.getFont().getSize();
    double fileNameTextSize = fileName.getFont().getSize();
    double exportFileTextSize = exportFileText.getFont().getSize();
    //double exportLabelTextSize = exportLabel.getFont().getSize();

    /*
    double previousTextSize = previousButton.getFont().getSize();
      double nextTextSize = nextButton.getFont().getSize();
    double previous1TextSize = previous1Button.getFont().getSize();
    double next1TextSize = next1Button.getFont().getSize();
    double previous2TextSize = previous2Button.getFont().getSize();
    double next2TextSize = next2Button.getFont().getSize();
    double bubble1TextSize = bubble1Text.getFont().getSize();
    double bubble2TextSize = bubble2Text.getFont().getSize();
    double bubble3TextSize = bubble3Text.getFont().getSize(); */

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              saveBackupButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * saveBackupTextSize)
                      + "pt;");
              fileName.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * fileNameTextSize)
                      + "pt;");
              exportFileText.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * exportFileTextSize)
                      + "pt;");
            /*  exportLabel.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * exportLabelTextSize)
                              + "pt;");
              nextButton.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * nextTextSize)
                              + "pt;");
             previousButton.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * previousTextSize)
                              + "pt;");
              previous1Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * previous1TextSize)
                              + "pt;");
              next1Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * next1TextSize)
                              + "pt;");
              previous2Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * previous2TextSize)
                              + "pt;");
              next2Button.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * next2TextSize)
                              + "pt;");
              bubble1Text.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bubble1TextSize)
                              + "pt;");
              bubble2Text.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bubble2TextSize)
                              + "pt;");
              bubble3Text.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * bubble3TextSize)
                              + "pt;");
         */   });

    TypeCSV.getItems().removeAll(TypeCSV.getItems());
    TypeCSV.getItems()
        .addAll(
            "TowerLocations",
            "Employee",
            "MedicalEquipment",
            "MedicalEquipmentServiceRequest",
            "Medicine",
            "MedicineDosage",
            "FloralDeliveryServiceRequest",
            "FoodDeliveryServiceRequest",
            "GiftDeliveryServiceRequest",
            "LanguageServiceRequest",
            "LaundryServiceRequest",
            "MaintenanceServiceRequest",
            "MedicineDeliveryServiceRequest",
            "ReligiousServiceRequest",
            "SanitationServiceRequest",
            "SecurityServiceRequest");
    TypeCSV.setValue("CSV Type");
  }

  public void saveBackup() {
    String input = fileName.getCharacters().toString();
    if (!TypeCSV.getSelectionModel().getSelectedItem().equals("CSV Type") && input.length() > 0) {

      String filepath;
      if (input.endsWith(".csv")) {
        filepath = "" + input;
      } else {
        filepath = "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/" + input + ".csv";
      }

      try {
        switch (TypeCSV.getSelectionModel().getSelectedItem().toString()) {
          case "TowerLocations":
            LocationDerbyImpl.exportToCSV("Location", filepath);
            break;
          case "Employee":
            EmployeeDerbyImpl.exportToCSV("Employee", filepath);
            break;
          case "MedicalEquipment":
            EquipmentDerbyImpl.exportToCSV("MedicalEquipment", filepath);
            break;
          case "Medicine":
            MedicineDerbyImpl.exportMedicineToCSV(filepath);
            break;
          case "MedicineDosage":
            MedicineDerbyImpl.exportDosagesToCSV(filepath);
            break;
          case "MedicalEquipmentServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyEq = new ServiceRequestDerbyImpl(SR.SRType.EQUIPMENT);
            serviceRequestDerbyEq.exportToCSV(filepath);
            break;
          case "FloralDeliveryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyFloral = new ServiceRequestDerbyImpl(SR.SRType.FLORAL_DELIVERY);
            serviceRequestDerbyFloral.exportToCSV(filepath);
            break;
          case "FoodDeliveryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyFood = new ServiceRequestDerbyImpl(SR.SRType.FOOD_DELIVERY);
            serviceRequestDerbyFood.exportToCSV(filepath);
            break;
          case "GiftDeliveryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyGift = new ServiceRequestDerbyImpl(SR.SRType.GIFT_DELIVERY);
            serviceRequestDerbyGift.exportToCSV(filepath);
            break;
          case "LanguageServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyLanguage = new ServiceRequestDerbyImpl(SR.SRType.LANGUAGE);
            serviceRequestDerbyLanguage.exportToCSV(filepath);
            break;
          case "LaundryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyLaundry = new ServiceRequestDerbyImpl(SR.SRType.LAUNDRY);
            serviceRequestDerbyLaundry.exportToCSV(filepath);
            break;
          case "MaintenanceServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyMaintenance = new ServiceRequestDerbyImpl(SR.SRType.MAINTENANCE);
            serviceRequestDerbyMaintenance.exportToCSV(filepath);
            break;
          case "MedicineDeliveryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyMedicine = new ServiceRequestDerbyImpl(SR.SRType.MEDICINE_DELIVERY);
            serviceRequestDerbyMedicine.exportToCSV(filepath);
            break;
          case "ReligiousServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyReligious = new ServiceRequestDerbyImpl(SR.SRType.RELIGIOUS);
            serviceRequestDerbyReligious.exportToCSV(filepath);
            break;
          case "SanitationServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbySanitation = new ServiceRequestDerbyImpl(SR.SRType.SANITATION);
            serviceRequestDerbySanitation.exportToCSV(filepath);
            break;
          case "SecurityServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbySecurity = new ServiceRequestDerbyImpl(SR.SRType.SECURITY);
            serviceRequestDerbySecurity.exportToCSV(filepath);
            break;
        }
        exportFileText.setText(filepath);
        exportFileText.setFill(Color.LAWNGREEN);
      } catch (Exception e) {
        exportFileText.setText("Failed!");
        exportFileText.setFill(Color.RED);
        e.printStackTrace();
      }
    }
  }

/*
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
    bubble3Text.setVisible(false);
    previousButton.setVisible(false);
    nextButton.setVisible(false);
    previous1Button.setVisible(false);
    next1Button.setVisible(false);
    previous2Button.setVisible(false);
    next2Button.setVisible(false);
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
    next2Button.setVisible(true);
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
    next2Button.setVisible(false);
    bubble1Text.setVisible(true);
    bubble2Text.setVisible(false);
  }

  public void next2(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(false);
    previous2Button.setVisible(true);
    next2Button.setVisible(false);
    bubble2Text.setVisible(false);
    bubble3Text.setVisible(true);
  }

  public void previous2() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(true);
    previous2Button.setVisible(false);
    next2Button.setVisible(true);
    bubble2Text.setVisible(true);
    bubble3Text.setVisible(false);
  } */
}
