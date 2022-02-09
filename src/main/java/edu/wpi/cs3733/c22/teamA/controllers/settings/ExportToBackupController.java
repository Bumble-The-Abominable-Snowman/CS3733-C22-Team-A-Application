package edu.wpi.cs3733.c22.teamA.controllers.settings;

import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.MedicalEquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest.FoodDeliveryServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.languageservicerequest.LanguageServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.laundryservicerequest.LaundryServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.religiousservicerequest.ReligiousServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.sanitationservicerequest.SanitationServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ExportToBackupController {
  @FXML public Button refreshButton;
  @FXML public TextField filename;
  @FXML public Text exportFileText;
  @FXML private ChoiceBox<String> TypeCSV;
  @FXML private ListView<String> fileList;

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  public void initialize() {
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

  public void returnToSettingsScene(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene(SceneController.SCENES.SETTINGS_SCENE);
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
  }

  public void exportToBackup(ActionEvent actionEvent) throws IOException {
    String input = filename.getCharacters().toString();
    System.out.println(input.length());
    if (!TypeCSV.getSelectionModel().getSelectedItem().equals("CSV Type") && input.length() > 0) {
      System.out.println(input);

      String filepath;
      if (input.endsWith(".csv")) {
        filepath = "src/main/resources/edu/wpi/cs3733/c22/teamA/db/" + input;
      } else {
        filepath = "src/main/resources/edu/wpi/cs3733/c22/teamA/db/" + input + ".csv";
      }

      switch (TypeCSV.getSelectionModel().getSelectedItem().toString()) {
        case "TowerLocations":
          LocationDerbyImpl.exportToCSV("Location", filepath);
          break;
        case "Employee":
          EmployeeDerbyImpl.exportToCSV("Employee", filepath);
          break;
        case "MedicalEquipment":
          MedicalEquipmentDerbyImpl.exportToCSV("MedicalEquipment", filepath);
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
