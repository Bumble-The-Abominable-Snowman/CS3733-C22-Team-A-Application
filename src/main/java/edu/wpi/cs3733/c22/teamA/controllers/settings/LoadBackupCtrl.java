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
  @FXML private JFXButton loadAllCSVButton;
  @FXML private JFXComboBox<String> TypeCSV;
  @FXML private Text selectedFileText;
  @FXML private ListView<String> fileList;
  @FXML private String lastSelectedFile;
  private double stageWidth;
  private double loadBackupTextSize;
  private double selectedFileTextSize;
  @FXML private ImageView bumbleHead;
  @FXML private Label bubbleText;

  @FXML
  public void initialize() {

    configure();

    this.refreshFiles();

    double bubbleTextSize = bubbleText.getFont().getSize();
    loadBackupTextSize = loadBackupButton.getFont().getSize();
    selectedFileTextSize = selectedFileText.getFont().getSize();

    App.getStage()
            .widthProperty()
            .addListener(
                    (obs, oldVal, newVal) -> {
                      updateSize();
                      bubbleText.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubbleTextSize)
                                      + "pt;");
                    });


    fileList.setOnMouseClicked(
            new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                  String currentItemSelected = fileList.getSelectionModel().getSelectedItem();
                  lastSelectedFile =
                          "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/" + currentItemSelected;
                  String[] arrOfStr = lastSelectedFile.split("/");
                  selectedFileText.setText(arrOfStr[arrOfStr.length - 1]);
                  selectedFileText.setFill(Color.BLACK);
                }
              }
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

    TypeCSV.setOnMouseClicked(
            new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                  //                  String currentItemSelected =
                  // fileList.getSelectionModel().getSelectedItem();
                  //                  lastSelectedFile = "edu/wpi/cs3733/c22/teamA/db/CSVs/" +
                  // currentItemSelected;
                  String[] arrOfStr = lastSelectedFile.split("/");
                  selectedFileText.setText(arrOfStr[arrOfStr.length - 1]);
                  selectedFileText.setFill(Color.BLACK);
                }
              }
            });
  }

  @FXML
  public void refreshFiles() {
    File f = new File("src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/");
    //    File f = new File("");
    // File f = new File(String.valueOf(App.class.getResourceAsStream("db/CSVs/")));
    // File f = new File(String.valueOf(App.class.getResource("db/CSVs/")));
    ObservableList<String> items = FXCollections.observableArrayList();
    try {
      for (String pathname : Objects.requireNonNull(f.list())) {
        if (pathname.toLowerCase().endsWith(".csv")) {
          items.add(pathname);
        }
      }
    } catch (Exception e) {
      items.add("Please add files manually");
    }

    Collections.sort(items);
    fileList.setItems(items);
  }

  public void loadBackup()
          throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException {

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
        selectedFileText.setText("Success!");
        selectedFileText.setFill(Color.GREEN);
      } else {
        throw new Exception("No csv file is selected!");
      }
    } catch (Exception e) {
      e.printStackTrace();
      selectedFileText.setText("Failed!");
      selectedFileText.setFill(Color.RED);
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
    System.out.println(selectedFile.getAbsolutePath());

    lastSelectedFile = selectedFile.getAbsolutePath();

    String[] arrOfStr = lastSelectedFile.split("/");
    selectedFileText.setText(arrOfStr[arrOfStr.length - 1]);
    selectedFileText.setFill(Color.BLACK);
  }

  @FXML
  public void loadAllCSV() {

    // Add load all CSV code here

  }

  @FXML
  private void updateSize() {

    stageWidth = App.getStage().getWidth();

    loadBackupButton.setStyle(
            "-fx-font-size: " + ((stageWidth / 1000) * loadBackupTextSize) + "pt;");
    selectedFileText.setStyle(
            "-fx-font-size: " + ((stageWidth / 1000) * selectedFileTextSize) + "pt;");
  }


}