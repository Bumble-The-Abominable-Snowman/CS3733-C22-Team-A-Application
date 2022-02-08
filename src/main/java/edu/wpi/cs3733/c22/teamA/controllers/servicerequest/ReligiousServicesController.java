package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.requests.ReligiousServiceRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ReligiousServicesController extends GenericServiceRequestsController {
  @FXML private JFXButton backButton;
  @FXML private JFXButton returnHomeButton;
  @FXML private JFXButton clearButton;
  @FXML private JFXButton submitButton;
  @FXML private JFXComboBox<String> religionChoice;
  @FXML private JFXComboBox<String> denominationChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private List<String> christianDenom = new ArrayList<>();
  private List<String> nonDenom = new ArrayList<>();
  private List<String> otherDenom = new ArrayList<>();
  private List<String> initChoices = new ArrayList<>();

  private FXMLLoader loader = new FXMLLoader();

  public ReligiousServicesController() {
    super();

    initChoices.add("Christian");
    initChoices.add("Non-Religious");
    initChoices.add("Other");

    christianDenom.add("Catholic");
    christianDenom.add("Baptist");
    christianDenom.add("Unspec. Protestant");
    christianDenom.add("Episcopal");
    christianDenom.add("UCOC");
    christianDenom.add("Methodist");
    christianDenom.add("Other");

    nonDenom.add("Atheist");
    nonDenom.add("Agnostic");
    nonDenom.add("Other");

    otherDenom.add("Jewish");
    otherDenom.add("Buddhist");
    otherDenom.add("Muslim");
    otherDenom.add("Hindu");
    otherDenom.add("Other");
  }

  @FXML
  private void initialize() throws ParseException {
    sceneID = SceneController.SCENES.RELIGIOUS_SERVICE_REQUEST_SCENE;

    backButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
    returnHomeButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
    clearButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
    submitButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));

    commentsBox.setWrapText(true);

    religionChoice.getItems().removeAll(religionChoice.getItems());
    religionChoice.getItems().addAll("Christian", "Non-Religious", "Other");
    religionChoice.getSelectionModel().select("Type");
    religionChoice
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue.equals("Type")) {
                denominationChoice.getItems().clear();
                denominationChoice.setDisable(true);
              } else if (newValue.equals("Christian")) {
                denominationChoice.getItems().clear();
                denominationChoice.getItems().setAll(christianDenom);
                denominationChoice.getSelectionModel().select(christianDenom.get(0));
                denominationChoice.setDisable(false);
              } else if (newValue.equals("Non-Religious")) {
                denominationChoice.getItems().clear();
                denominationChoice.getItems().setAll(nonDenom);
                denominationChoice.getSelectionModel().select(nonDenom.get(0));
                denominationChoice.setDisable(false);
              } else if (newValue.equals("Other")) {
                denominationChoice.getItems().clear();
                denominationChoice.getItems().setAll(otherDenom);
                denominationChoice.getSelectionModel().select(otherDenom.get(0));
                denominationChoice.setDisable(false);
              }
            });
    denominationChoice.getItems().removeAll(denominationChoice.getItems());
    toLocationChoice.getItems().removeAll(toLocationChoice.getItems());
    toLocationChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toLocationChoice.setVisibleRowCount(5);
    employeeChoice.setVisibleRowCount(5);

    EmployeeDAO EmployeeDAO = new EmployeeDerbyImpl();
    String input = "2022-02-01";
    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = originalFormat.parse(input);
    EmployeeDAO.enterEmployee(
        "001", "Admin", "Yanbo", "Dai", "ydai2@wpi.edu", "0000000000", "100 institute Rd", date);

    employeeChoice
        .getItems()
        .addAll(
            EmployeeDAO.getEmployeeList().stream()
                .map(Employee::getFirstName)
                .collect(Collectors.toList()));
  }

  @FXML
  void submitRequest() throws IOException {
    ReligiousServiceRequest religiousServiceRequest = new ReligiousServiceRequest();
    if (!religionChoice.getSelectionModel().getSelectedItem().equals("Type")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && employeeChoice.getSelectionModel().getSelectedItem() != null) {
      // pass religious service request object
      this.returnToHomeScene();
    }
  }
}
