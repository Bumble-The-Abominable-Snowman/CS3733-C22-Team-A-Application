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
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class LoadBackupCtrl extends MasterCtrl {

  @FXML private JFXButton loadBackupButton;
  @FXML private JFXButton loadFromSystemButton;
  @FXML private JFXComboBox<String> TypeCSV;
  @FXML private Text selectedFileText;
  @FXML private Text statusText;
  @FXML private Text selectedLabel;
  @FXML private Text insertLabel;
  @FXML private String lastSelectedFile;
  private double stageWidth;
  private double loadBackupTextSize;
  private double loadFromSystemTextSize;
  private double selectedFileTextSize;
  private double statusTextSize;
  private double selectedLabelTextSize;
  private double insertLabelTextSize;

  @FXML
  public void initialize() {

    configure();

    loadBackupTextSize = loadBackupButton.getFont().getSize();
    loadFromSystemTextSize = loadFromSystemButton.getFont().getSize();
    selectedFileTextSize = selectedFileText.getFont().getSize();
    statusTextSize = statusText.getFont().getSize();
    selectedLabelTextSize = selectedLabel.getFont().getSize();
    insertLabelTextSize = insertLabel.getFont().getSize();

    App.getStage()
            .widthProperty()
            .addListener(
                    (obs, oldVal, newVal) -> {
                      updateSize();
                    });

    TypeCSV.getItems().removeAll(TypeCSV.getItems());
    TypeCSV.getItems()
            .addAll(
                    "TowerLocations",
                    "Employee",
                    "MedicalEquipment",
                    "Medicine",
                    "MedicineDosage",
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

  public void loadBackup() {
    try {
      if (!TypeCSV.getValue().equals("CSV Type") && lastSelectedFile.length() > 4) {

        switch (TypeCSV.getSelectionModel().getSelectedItem().toString()) {
          case "TowerLocations":
            LocationDerbyImpl.inputFromCSV(lastSelectedFile);
            break;
          case "Employee":
            EmployeeDerbyImpl.inputFromCSV( lastSelectedFile);
            break;
          case "MedicalEquipment":
            EquipmentDerbyImpl.inputFromCSV("MedicalEquipment", lastSelectedFile);
            break;
          case "Medicine":
            MedicineDerbyImpl.importMedicineFromCSV(lastSelectedFile);
            break;
          case "MedicineDosage":
            MedicineDerbyImpl.importDosagesFromCSV(lastSelectedFile);
            break;
          case "MedicalEquipmentServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyEq = new ServiceRequestDerbyImpl(SR.SRType.EQUIPMENT);
            serviceRequestDerbyEq.populateFromCSV(lastSelectedFile);
            break;
          case "FloralDeliveryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyFloral = new ServiceRequestDerbyImpl(SR.SRType.FLORAL_DELIVERY);
            serviceRequestDerbyFloral.populateFromCSV(lastSelectedFile);
            break;
          case "FoodDeliveryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyFood = new ServiceRequestDerbyImpl(SR.SRType.FOOD_DELIVERY);
            serviceRequestDerbyFood.populateFromCSV(lastSelectedFile);
            break;
          case "GiftDeliveryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyGift = new ServiceRequestDerbyImpl(SR.SRType.GIFT_DELIVERY);
            serviceRequestDerbyGift.populateFromCSV(lastSelectedFile);
            break;
          case "LanguageServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyLanguage = new ServiceRequestDerbyImpl(SR.SRType.LANGUAGE);
            serviceRequestDerbyLanguage.populateFromCSV(lastSelectedFile);
            break;
          case "LaundryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyLaundry = new ServiceRequestDerbyImpl(SR.SRType.LAUNDRY);
            serviceRequestDerbyLaundry.populateFromCSV(lastSelectedFile);
            break;
          case "MaintenanceServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyMaintenance = new ServiceRequestDerbyImpl(SR.SRType.MAINTENANCE);
            serviceRequestDerbyMaintenance.populateFromCSV(lastSelectedFile);
            break;
          case "MedicineDeliveryServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyMedicine = new ServiceRequestDerbyImpl(SR.SRType.MEDICINE_DELIVERY);
            serviceRequestDerbyMedicine.populateFromCSV(lastSelectedFile);
            break;
          case "ReligiousServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbyReligious = new ServiceRequestDerbyImpl(SR.SRType.RELIGIOUS);
            serviceRequestDerbyReligious.populateFromCSV(lastSelectedFile);
            break;
          case "SanitationServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbySanitation = new ServiceRequestDerbyImpl(SR.SRType.SANITATION);
            serviceRequestDerbySanitation.populateFromCSV(lastSelectedFile);
            break;
          case "SecurityServiceRequest":
            ServiceRequestDerbyImpl serviceRequestDerbySecurity = new ServiceRequestDerbyImpl(SR.SRType.SECURITY);
            serviceRequestDerbySecurity.populateFromCSV(lastSelectedFile);
            break;
        }
        statusText.setText("Success!");
        statusText.setFill(Color.LAWNGREEN);
      } else {
        throw new Exception("No csv file is selected!");
      }
    } catch (Exception e) {
      e.printStackTrace();
      statusText.setText("Failed!");
      statusText.setFill(Color.RED);
    }
  }

  @FXML
  public void loadFromSystem() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select the CSV file");
    fileChooser
            .getExtensionFilters()
            .addAll(new FileChooser.ExtensionFilter("CSV Backup Files", "*.csv", "*.CSV"));
    File selectedFile = fileChooser.showOpenDialog(App.getStage());

    lastSelectedFile = selectedFile.getAbsolutePath();

    String[] arrOfStr = lastSelectedFile.split("/");
    selectedFileText.setText(arrOfStr[arrOfStr.length - 1]);
    selectedFileText.setFill(Color.WHITE);
  }

  @FXML
  private void updateSize() {

    stageWidth = App.getStage().getWidth();

    loadBackupButton.setStyle(
            "-fx-font-size: " + ((stageWidth / 1000) * loadBackupTextSize) + "pt;");
    loadFromSystemButton.setStyle(
            "-fx-font-size: " + ((stageWidth / 1000) * loadFromSystemTextSize) + "pt;");
    selectedFileText.setStyle(
            "-fx-font-size: " + ((stageWidth / 1000) * selectedFileTextSize) + "pt;");
    statusText.setStyle(
            "-fx-font-size: " + ((stageWidth / 1000) * statusTextSize) + "pt;");
    selectedLabel.setStyle(
            "-fx-font-size: " + ((stageWidth / 1000) * selectedLabelTextSize) + "pt;");
    insertLabel.setStyle(
            "-fx-font-size: " + ((stageWidth / 1000) * insertLabelTextSize) + "pt;");
  }


}