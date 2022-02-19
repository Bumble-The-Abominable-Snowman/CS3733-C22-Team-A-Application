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
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SaveBackupCtrl extends MasterCtrl {
  @FXML private JFXButton saveBackupButton;
  @FXML public TextField fileName;
  @FXML public Text exportFileText;
  @FXML private JFXComboBox<String> TypeCSV;

  @FXML
  public void initialize() {

    configure();

    double saveBackupTextSize = saveBackupButton.getFont().getSize();
    double fileNameTextSize = fileName.getFont().getSize();
    double exportFileTextSize = exportFileText.getFont().getSize();

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
            });

    TypeCSV.getItems().removeAll(TypeCSV.getItems());
    TypeCSV.getItems()
        .addAll(
            "TowerLocations",
            "Employee",
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

  public void saveBackup() throws IOException {
    String input = fileName.getCharacters().toString();
    System.out.println(input.length());
    if (!TypeCSV.getSelectionModel().getSelectedItem().equals("CSV Type") && input.length() > 0) {
      System.out.println(input);

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
            ServiceRequestDerbyImpl<EquipmentSR> equipmentSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new EquipmentSR());
            equipmentSRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "FloralDeliveryServiceRequest":
            ServiceRequestDerbyImpl<FloralDeliverySR> floralDeliverySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new FloralDeliverySR());
            floralDeliverySRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "FoodDeliveryServiceRequest":
            ServiceRequestDerbyImpl<FoodDeliverySR> foodDeliverySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new FoodDeliverySR());
            foodDeliverySRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "GiftDeliveryServiceRequest":
            ServiceRequestDerbyImpl<GiftDeliverySR> giftDeliverySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new GiftDeliverySR());
            giftDeliverySRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "LanguageServiceRequest":
            ServiceRequestDerbyImpl<LanguageSR> languageSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new LanguageSR());
            languageSRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "LaundryServiceRequest":
            ServiceRequestDerbyImpl<LaundrySR> laundrySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new LaundrySR());
            laundrySRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "MaintenanceServiceRequest":
            ServiceRequestDerbyImpl<MaintenanceSR> maintenanceSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new MaintenanceSR());
            maintenanceSRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "MedicineDeliveryServiceRequest":
            ServiceRequestDerbyImpl<MedicineDeliverySR> medicineDeliverySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new MedicineDeliverySR());
            medicineDeliverySRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "ReligiousServiceRequest":
            ServiceRequestDerbyImpl<ReligiousSR> religiousSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new ReligiousSR());
            religiousSRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "SanitationServiceRequest":
            ServiceRequestDerbyImpl<SanitationSR> sanitationSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new SanitationSR());
            sanitationSRServiceRequestDerby.exportToCSV(filepath);
            break;
          case "SecurityServiceRequest":
            ServiceRequestDerbyImpl<SecuritySR> securitySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new SecuritySR());
            securitySRServiceRequestDerby.exportToCSV(filepath);
            break;
        }
        exportFileText.setText(filepath);
        exportFileText.setFill(Color.GREEN);
      } catch (Exception e) {
        exportFileText.setText("Failed!");
        exportFileText.setFill(Color.RED);
      }
    }
  }
}
