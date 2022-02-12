package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest.FoodDeliveryServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.languageservicerequest.LanguageServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.laundryservicerequest.LaundryServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.religiousservicerequest.ReligiousServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.sanitationservicerequest.SanitationServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SaveBackupCtrl {
  @FXML private JFXButton backButton;
  @FXML private JFXButton homeButton;
  @FXML private JFXButton exportToBackupButton;
  @FXML public TextField filename;
  @FXML public Text exportFileText;
  @FXML private JFXComboBox<String> TypeCSV;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  public void initialize() {
    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    exportToBackupButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(20), Insets.EMPTY)));
    homeButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    TypeCSV.getItems().removeAll(TypeCSV.getItems());
    TypeCSV.getItems()
        .addAll(
            "TowerLocations",
            "Employee",
            "MedicalEquipment",
            "LanguageServiceRequest",
            "MedicalEquipmentServiceRequest",
            "FoodDeliveryServiceRequest",
            "LaundryServiceRequest",
            "ReligiousServiceRequest",
            "SanitationServiceRequest");
    TypeCSV.setValue("CSV Type");
  }

  public void returnToSettingsScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SETTINGS_SCENE);
  }

  @FXML
  private void goToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  public void exportToBackup() throws IOException {
    String input = filename.getCharacters().toString();
    System.out.println(input.length());
    if (!TypeCSV.getSelectionModel().getSelectedItem().equals("CSV Type") && input.length() > 0) {
      System.out.println(input);

      String filepath;
      if (input.endsWith(".csv")) {
        filepath = "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/" + input;
      } else {
        filepath = "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/" + input + ".csv";
      }

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
        case "LanguageServiceRequest":
          LanguageServiceRequestDerbyImpl.exportToCSV("LanguageServiceRequest", filepath);
          break;
        case "MedicalEquipmentServiceRequest":
          LanguageServiceRequestDerbyImpl.exportToCSV("MedicalEquipmentServiceRequest", filepath);
          break;
        case "FoodDeliveryServiceRequest":
          FoodDeliveryServiceRequestDerbyImpl.exportToCSV("FoodDeliveryServiceRequest", filepath);
          break;
        case "LaundryServiceRequest":
          LaundryServiceRequestDerbyImpl.exportToCSV("LaundryServiceRequest", filepath);
          break;
        case "ReligiousServiceRequest":
          ReligiousServiceRequestDerbyImpl.exportToCSV("ReligiousServiceRequest", filepath);
          break;
        case "SanitationServiceRequest":
          SanitationServiceRequestDerbyImpl.exportToCSV("SanitationServiceRequest", filepath);
          break;
      }
      //      Adb.exportToCSV(TypeCSV.getValue(), filepath);

      exportFileText.setText(filepath);
      exportFileText.setFill(Color.GREEN);
    } else {
      exportFileText.setText("Failed!");
      exportFileText.setFill(Color.RED);
    }
  }
}
