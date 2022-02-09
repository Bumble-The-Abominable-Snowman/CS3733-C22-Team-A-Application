package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

public class LoadFromBackupController {
  public JFXButton refreshButton;
  @FXML private JFXButton returnHomeButton;
  @FXML private JFXButton backButton;
  @FXML private JFXButton loadFromBackupButton;
  @FXML private JFXComboBox<String> TypeCSV;
  @FXML private Text selectedFileText;
  @FXML private ListView<String> fileList;
  private String lastSelectedFile;

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  public void initialize() {
    refreshFiles(null);

    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    loadFromBackupButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(20), Insets.EMPTY)));
    returnHomeButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

    fileList.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.getButton() == MouseButton.PRIMARY) {
              String currentItemSelected = fileList.getSelectionModel().getSelectedItem();
              lastSelectedFile = currentItemSelected;
              selectedFileText.setText(currentItemSelected);
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
  }

  public void returnToSettingsScene(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene(SceneController.SCENES.SETTINGS_SCENE);
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
  }

  @FXML
  public void refreshFiles(ActionEvent actionEvent) {
    File f = new File("src/main/resources/edu/wpi/cs3733/c22/teamA/db/");

    ObservableList<String> items = FXCollections.observableArrayList();

    for (String pathname : Objects.requireNonNull(f.list())) {
      if (pathname.toLowerCase().endsWith(".csv")) {
        items.add(pathname);
      }
    }

    Collections.sort(items);
    fileList.setItems(items);
  }

  public void loadFromBackup(ActionEvent actionEvent) throws IOException {
    String filepath = "edu/wpi/cs3733/c22/teamA/db/" + lastSelectedFile;

    if (!TypeCSV.getValue().equals("CSV Type") && lastSelectedFile.length() > 4) {
      //      Adb.inputFromCSV(TypeCSV.getValue(), filepath);

      switch (TypeCSV.getSelectionModel().getSelectedItem().toString()) {
        case "TowerLocations":
          LocationDerbyImpl.inputFromCSV("Location", filepath);
          break;
        case "Employee":
          EmployeeDerbyImpl.inputFromCSV("Employee", filepath);
          break;
        case "MedicalEquipment":
          MedicalEquipmentDerbyImpl.inputFromCSV("MedicalEquipment", filepath);
          break;
        case "LanguageServiceRequest":
          LanguageServiceRequestDerbyImpl.inputFromCSV("LanguageServiceRequest", filepath);
          break;
        case "MedicalEquipmentServiceRequest":
          LanguageServiceRequestDerbyImpl.inputFromCSV("MedicalEquipmentServiceRequest", filepath);
          break;
        case "FoodDeliveryServiceRequest":
          FoodDeliveryServiceRequestDerbyImpl.inputFromCSV("FoodDeliveryServiceRequest", filepath);
          break;
        case "LaundryServiceRequest":
          LaundryServiceRequestDerbyImpl.inputFromCSV("LaundryServiceRequest", filepath);
          break;
        case "ReligiousServiceRequest":
          ReligiousServiceRequestDerbyImpl.inputFromCSV("ReligiousServiceRequest", filepath);
          break;
        case "SanitationServiceRequest":
          SanitationServiceRequestDerbyImpl.inputFromCSV("SanitationServiceRequest", filepath);
          break;
      }

      selectedFileText.setText("Success!");
      selectedFileText.setFill(Color.GREEN);
    } else {
      selectedFileText.setText("Failed!");
      selectedFileText.setFill(Color.RED);
    }
  }
}
