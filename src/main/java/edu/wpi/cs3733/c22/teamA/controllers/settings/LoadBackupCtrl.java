package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class LoadBackupCtrl {
  @FXML private JFXButton homeButton;
  @FXML private JFXButton backButton;
  @FXML private JFXButton loadFromBackupButton;
  @FXML private JFXComboBox<String> TypeCSV;
  @FXML private Text selectedFileText;
  @FXML private ListView<String> fileList;
  @FXML private String lastSelectedFile;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  public void initialize() {
    this.refreshFiles();

    double homeTextSize = homeButton.getFont().getSize();
    double backTextSize = backButton.getFont().getSize();
    double loadFromBackupTextSize = loadFromBackupButton.getFont().getSize();
    // double TypeCSVTextSize = TypeCSV.getFont().getSize();
    double selectedFileTextSize = selectedFileText.getFont().getSize();
    // double fileListTextSize = fileList.getFont().getSize();
    // double lastSelectedFileTextSize = lastSelectedFile.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              homeButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * homeTextSize) + "pt;");
              backButton.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * backTextSize) + "pt;");
              loadFromBackupButton.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * loadFromBackupTextSize)
                      + "pt;");
              selectedFileText.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * selectedFileTextSize)
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

  public void returnToSettingsScene(ActionEvent actionEvent) throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SETTINGS);
  }

  @FXML
  private void goToHomeScene() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
  }

  @FXML
  public void refreshFiles() {
    File f = new File("src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/");

    ObservableList<String> items = FXCollections.observableArrayList();

    for (String pathname : Objects.requireNonNull(f.list())) {
      if (pathname.toLowerCase().endsWith(".csv")) {
        items.add(pathname);
      }
    }

    Collections.sort(items);
    fileList.setItems(items);
  }

  public void loadFromBackup()
      throws IOException, ParseException, InvocationTargetException, IllegalAccessException,
          SQLException {
    //    String filepath = "edu/wpi/cs3733/c22/teamA/db/CSVs/" + lastSelectedFile;

    try {
      if (!TypeCSV.getValue().equals("CSV Type") && lastSelectedFile.length() > 4) {

        switch (TypeCSV.getSelectionModel().getSelectedItem().toString()) {
          case "TowerLocations":
            LocationDerbyImpl.inputFromCSV("Location", lastSelectedFile);
            break;
          case "Employee":
            EmployeeDerbyImpl.inputFromCSV("Employee", lastSelectedFile);
            break;
          case "MedicalEquipment":
            EquipmentDerbyImpl.inputFromCSV("MedicalEquipment", lastSelectedFile);
            break;
          case "MedicalEquipmentServiceRequest":
            ServiceRequestDerbyImpl<EquipmentSR> equipmentSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new EquipmentSR());
            equipmentSRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "FloralDeliveryServiceRequest":
            ServiceRequestDerbyImpl<FloralDeliverySR> floralDeliverySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new FloralDeliverySR());
            floralDeliverySRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "FoodDeliveryServiceRequest":
            ServiceRequestDerbyImpl<FoodDeliverySR> foodDeliverySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new FoodDeliverySR());
            foodDeliverySRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "GiftDeliveryServiceRequest":
            ServiceRequestDerbyImpl<GiftDeliverySR> giftDeliverySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new GiftDeliverySR());
            giftDeliverySRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "LanguageServiceRequest":
            ServiceRequestDerbyImpl<LanguageSR> languageSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new LanguageSR());
            languageSRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "LaundryServiceRequest":
            ServiceRequestDerbyImpl<LaundrySR> laundrySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new LaundrySR());
            laundrySRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "MaintenanceServiceRequest":
            ServiceRequestDerbyImpl<MaintenanceSR> maintenanceSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new MaintenanceSR());
            maintenanceSRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "MedicineDeliveryServiceRequest":
            ServiceRequestDerbyImpl<MedicineDeliverySR> medicineDeliverySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new MedicineDeliverySR());
            medicineDeliverySRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "ReligiousServiceRequest":
            ServiceRequestDerbyImpl<ReligiousSR> religiousSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new ReligiousSR());
            religiousSRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "SanitationServiceRequest":
            ServiceRequestDerbyImpl<SanitationSR> sanitationSRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new SanitationSR());
            sanitationSRServiceRequestDerby.populateFromCSV(lastSelectedFile);
            break;
          case "SecurityServiceRequest":
            ServiceRequestDerbyImpl<SecuritySR> securitySRServiceRequestDerby =
                new ServiceRequestDerbyImpl<>(new SecuritySR());
            securitySRServiceRequestDerby.populateFromCSV(lastSelectedFile);
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

    //    this.loadFromBackup(actionEvent);

  }
}
