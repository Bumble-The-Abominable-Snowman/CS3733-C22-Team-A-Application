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
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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
  private String lastSelectedFile;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  public void initialize() {
    this.refreshFiles();

    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    loadFromBackupButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(20), Insets.EMPTY)));
    homeButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

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
            "LanguageServiceRequest",
            "MedicalEquipmentServiceRequest",
            "FoodDeliveryServiceRequest",
            "LaundryServiceRequest",
            "ReligiousServiceRequest",
            "SanitationServiceRequest");
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
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SETTINGS_SCENE);
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

  public void loadFromBackup() {
    //    String filepath = "edu/wpi/cs3733/c22/teamA/db/CSVs/" + lastSelectedFile;

    if (!TypeCSV.getValue().equals("CSV Type") && lastSelectedFile.length() > 4) {
      //      Adb.inputFromCSV(TypeCSV.getValue(), filepath);

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
        case "LanguageServiceRequest":
          LanguageServiceRequestDerbyImpl.inputFromCSV("LanguageServiceRequest", lastSelectedFile);
          break;
        case "MedicalEquipmentServiceRequest":
          LanguageServiceRequestDerbyImpl.inputFromCSV(
              "MedicalEquipmentServiceRequest", lastSelectedFile);
          break;
        case "FoodDeliveryServiceRequest":
          FoodDeliveryServiceRequestDerbyImpl.inputFromCSV(
              "FoodDeliveryServiceRequest", lastSelectedFile);
          break;
        case "LaundryServiceRequest":
          LaundryServiceRequestDerbyImpl.inputFromCSV("LaundryServiceRequest", lastSelectedFile);
          break;
        case "ReligiousServiceRequest":
          ReligiousServiceRequestDerbyImpl.inputFromCSV(
              "ReligiousServiceRequest", lastSelectedFile);
          break;
        case "SanitationServiceRequest":
          SanitationServiceRequestDerbyImpl.inputFromCSV(
              "SanitationServiceRequest", lastSelectedFile);
          break;
      }

      selectedFileText.setText("Success!");
      selectedFileText.setFill(Color.GREEN);
    } else {
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
