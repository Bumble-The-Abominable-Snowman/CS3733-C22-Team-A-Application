package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.*;
import java.io.IOException;
import java.rmi.server.ExportException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SaveBackupCtrl extends MasterCtrl {
  @FXML private JFXButton saveBackupButton;
  @FXML public TextField fileName;
  @FXML public Text exportFileText;
  @FXML private JFXComboBox<String> TypeCSV;
  @FXML private Text exportLabel;

  @FXML
  public void initialize() {

    configure();

    double saveBackupTextSize = saveBackupButton.getFont().getSize();
    double fileNameTextSize = fileName.getFont().getSize();
    double exportFileTextSize = exportFileText.getFont().getSize();
    double exportLabelTextSize = exportLabel.getFont().getSize();

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
              exportLabel.setStyle(
                      "-fx-font-size: "
                              + ((App.getStage().getWidth() / 1000) * exportLabelTextSize)
                              + "pt;");
            });

    TypeCSV.getItems().removeAll(TypeCSV.getItems());
    TypeCSV.getItems()
        .addAll(
            "TowerLocations",
            "Employee",
            "MedicalEquipment",
            "MedicalEquipmentServiceRequest",
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
}
