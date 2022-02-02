package edu.wpi.teama.controllers;

import edu.wpi.teama.Aapp;
import edu.wpi.teama.Adb.Employee.Employee;
import edu.wpi.teama.Adb.Employee.EmployeeDAO;
import edu.wpi.teama.Adb.Employee.EmployeeDerbyImpl;
import edu.wpi.teama.Adb.Location.Location;
import edu.wpi.teama.Adb.Location.LocationDerbyImpl;
import edu.wpi.teama.entities.MedicalEquipmentServiceRequest;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MedicalEquipmentDeliveryController {
  @FXML private TextArea specialNotes;
  @FXML private ChoiceBox employeeChoiceBox;
  @FXML private ComboBox toChoiceBox;
  @FXML private Button homeButton;
  @FXML private ChoiceBox typeChoiceBox;
  @FXML private ChoiceBox fromChoiceBox;
  @FXML private Button submitButton;
  @FXML private Button clearButton;
  @FXML private Label locationLabel;

  @FXML private Button backButton;

  private FXMLLoader loader = new FXMLLoader();
  private MedicalEquipmentServiceRequest medicalEquipmentServiceRequest;
  private List<String> bedLocations = new ArrayList<>();
  private List<String> xrayLocations = new ArrayList<>();
  private List<String> infusionPumpLocations = new ArrayList<>();
  private List<String> reclinerLocations = new ArrayList<>();

  public MedicalEquipmentDeliveryController() {
    medicalEquipmentServiceRequest = new MedicalEquipmentServiceRequest();
    bedLocations.add("Nearest Location");
    bedLocations.add("OR Bed Park");
    bedLocations.add("Nearest Hallway");

    xrayLocations.add("Nearest Location");
    xrayLocations.add("Near In-patient Unit");

    infusionPumpLocations.add("Clean Storage Area");

    reclinerLocations.add("Nearest from Hallways");
    reclinerLocations.add("West Plaza 1st Floor");
  }

  @FXML
  private void initialize() throws ParseException {
    specialNotes.setWrapText(true);

    typeChoiceBox.getItems().removeAll(typeChoiceBox.getItems());
    typeChoiceBox.getItems().addAll("Type", "Bed", "XRAY", "Infusion Pump", "Patient Recliner");
    typeChoiceBox.getSelectionModel().select("Type");
    typeChoiceBox
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue.equals("Type")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.setDisable(true);
              } else if (newValue.equals("Bed")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.getItems().setAll(bedLocations);
                fromChoiceBox.getSelectionModel().select(bedLocations.get(0));
                fromChoiceBox.setDisable(false);
              } else if (newValue.equals("XRAY")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.getItems().setAll(xrayLocations);
                fromChoiceBox.getSelectionModel().select(xrayLocations.get(0));
                fromChoiceBox.setDisable(false);
              } else if (newValue.equals("Infusion Pump")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.getItems().setAll(infusionPumpLocations);
                fromChoiceBox.getSelectionModel().select(infusionPumpLocations.get(0));
                fromChoiceBox.setDisable(false);
              } else if (newValue.equals("Patient Recliner")) {
                fromChoiceBox.getItems().clear();
                fromChoiceBox.getItems().setAll(reclinerLocations);
                fromChoiceBox.getSelectionModel().select(reclinerLocations.get(0));
                fromChoiceBox.setDisable(false);
              }
            });

    fromChoiceBox.getItems().removeAll(fromChoiceBox.getItems());
    toChoiceBox.getItems().removeAll(toChoiceBox.getItems());
    toChoiceBox
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toChoiceBox.setVisibleRowCount(5);

    EmployeeDAO EmployeeDAO = new EmployeeDerbyImpl();
    String input = "2022-02-01";
    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = originalFormat.parse(input);
    EmployeeDAO.enterEmployee(
        "001", "Admin", "Yanbo", "Dai", "ydai2@wpi.edu", "0000000000", "100 institute Rd", date);

    employeeChoiceBox
        .getItems()
        .addAll(
            EmployeeDAO.getEmployeeList().stream()
                .map(Employee::getFirstName)
                .collect(Collectors.toList()));
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/home.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) homeButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Home");
    window.show();
  }

  @FXML
  private void returnToSelectServiceScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource("views/selectServiceRequest.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) backButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Select Service Request");
    window.show();
  }

  @FXML
  public void chooseFloor(ActionEvent actionEvent) {
    locationLabel.setText(((Button) actionEvent.getSource()).getText());
    locationLabel.setAlignment(Pos.CENTER);
  }

  @FXML
  private void clearSubmission() throws IOException {
    URL xmlUrl = Aapp.class.getResource("views/medicalEquipmentDelivery.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) clearButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Medical Equipment Delivery");
    window.show();
  }
}
