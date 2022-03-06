package edu.wpi.cs3733.c22.teamA.controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestWrapperImpl;
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

import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

public class LoadBackupCtrl extends MasterCtrl {

  @FXML private JFXButton loadBackupButton;
  @FXML private JFXButton loadFromSystemButton;
  @FXML private JFXComboBox<String> TypeCSV;
  @FXML private Text selectedFileText;
  @FXML private Text statusText;
  @FXML private Text selectedLabel;
  @FXML private Text insertLabel;
  @FXML private String lastSelectedFile;
  private double loadBackupTextSize;
  private double loadFromSystemTextSize;
  private double selectedFileTextSize;
  private double statusTextSize;
  private double selectedLabelTextSize;
  private double insertLabelTextSize;

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
  @FXML private Label bubble3Text;

  @FXML
  public void initialize() {

    configure();

    loadBackupTextSize = loadBackupButton.getFont().getSize();
    loadFromSystemTextSize = loadFromSystemButton.getFont().getSize();
    selectedFileTextSize = selectedFileText.getFont().getSize();
    statusTextSize = statusText.getFont().getSize();
    selectedLabelTextSize = selectedLabel.getFont().getSize();
    insertLabelTextSize = insertLabel.getFont().getSize();


    double previousTextSize = previousButton.getFont().getSize();
       double nextTextSize = nextButton.getFont().getSize();
    double previous1TextSize = previous1Button.getFont().getSize();
    double next1TextSize = next1Button.getFont().getSize();
    double previous2TextSize = previous2Button.getFont().getSize();
    double next2TextSize = next2Button.getFont().getSize();
    double bubble1TextSize = bubble1Text.getFont().getSize();
    double bubble2TextSize = bubble2Text.getFont().getSize();
    double bubble3TextSize = bubble3Text.getFont().getSize();

    App.getStage()
            .widthProperty()
            .addListener(
                    (obs, oldVal, newVal) -> {
                      updateSize(); /*
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
         */       });

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
            (new LocationWrapperImpl()).inputFromCSVfile(lastSelectedFile);
            break;
          case "Employee":
            (new EmployeeWrapperImpl()).inputFromCSVfile( lastSelectedFile);
            break;
          case "MedicalEquipment":
            (new EquipmentWrapperImpl()).inputFromCSVfile(lastSelectedFile);
            break;
          case "Medicine":
            (new MedicineWrapperImpl()).importMedicineFromCSV(lastSelectedFile);
            break;
          case "MedicineDosage":
            (new MedicineWrapperImpl()).importDosagesFromCSV(lastSelectedFile);
            break;
          case "MedicalEquipmentServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.EQUIPMENT)).populateFromCSV(lastSelectedFile);
            break;
          case "FloralDeliveryServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.FLORAL_DELIVERY)).populateFromCSV(lastSelectedFile);
            break;
          case "FoodDeliveryServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.FOOD_DELIVERY)).populateFromCSV(lastSelectedFile);
            break;
          case "GiftDeliveryServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.GIFT_DELIVERY)).populateFromCSV(lastSelectedFile);
            break;
          case "LanguageServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.LANGUAGE)).populateFromCSV(lastSelectedFile);
            break;
          case "LaundryServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.LAUNDRY)).populateFromCSV(lastSelectedFile);
            break;
          case "MaintenanceServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.MAINTENANCE)).populateFromCSV(lastSelectedFile);
            break;
          case "MedicineDeliveryServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.MEDICINE_DELIVERY)).populateFromCSV(lastSelectedFile);
            break;
          case "ReligiousServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.RELIGIOUS)).populateFromCSV(lastSelectedFile);
            break;
          case "SanitationServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.SANITATION)).populateFromCSV(lastSelectedFile);
            break;
          case "SecurityServiceRequest":
            (new ServiceRequestWrapperImpl(SR.SRType.SECURITY)).populateFromCSV(lastSelectedFile);
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
    transparentGlow.setColor(Color.TRANSPARENT);
    loadFromSystemButton.setEffect(transparentGlow);
    TypeCSV.setEffect(transparentGlow);
    loadBackupButton.setEffect(transparentGlow);
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

    borderGlow.setColor(Color.GOLD);
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    borderGlow.setHeight(45);
    loadFromSystemButton.setEffect(borderGlow);

    transparentGlow.setColor(Color.TRANSPARENT);
    TypeCSV.setEffect(transparentGlow);
    loadBackupButton.setEffect(transparentGlow);
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

    transparentGlow.setColor(Color.TRANSPARENT);
    loadFromSystemButton.setEffect(transparentGlow);
    TypeCSV.setEffect(transparentGlow);
    loadBackupButton.setEffect(transparentGlow);
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

    borderGlow.setColor(Color.GOLD);
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    borderGlow.setHeight(45);
    TypeCSV.setEffect(borderGlow);

    transparentGlow.setColor(Color.TRANSPARENT);
    loadFromSystemButton.setEffect(transparentGlow);
    loadBackupButton.setEffect(transparentGlow);
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

    borderGlow.setColor(Color.GOLD);
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    borderGlow.setHeight(45);
    loadFromSystemButton.setEffect(borderGlow);

    transparentGlow.setColor(Color.TRANSPARENT);
    TypeCSV.setEffect(transparentGlow);
    loadBackupButton.setEffect(transparentGlow);
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

    borderGlow.setColor(Color.GOLD);
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    borderGlow.setHeight(45);
    loadBackupButton.setEffect(borderGlow);

    transparentGlow.setColor(Color.TRANSPARENT);
    loadFromSystemButton.setEffect(transparentGlow);
    TypeCSV.setEffect(transparentGlow);
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

    borderGlow.setColor(Color.GOLD);
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    borderGlow.setHeight(45);
    TypeCSV.setEffect(borderGlow);

    transparentGlow.setColor(Color.TRANSPARENT);
    loadFromSystemButton.setEffect(transparentGlow);
    loadBackupButton.setEffect(transparentGlow);
  }


}