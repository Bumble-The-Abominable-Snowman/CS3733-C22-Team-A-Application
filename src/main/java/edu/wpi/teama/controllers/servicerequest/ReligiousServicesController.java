package edu.wpi.teama.controllers.servicerequest;

import edu.wpi.teama.Adb.employee.EmployeeDAO;
import edu.wpi.teama.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.teama.Adb.location.LocationDerbyImpl;
import edu.wpi.teama.controllers.SceneController;
import edu.wpi.teama.entities.Employee;
import edu.wpi.teama.entities.Location;
import edu.wpi.teama.entities.ReligiousServiceRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class ReligiousServicesController extends GenericServiceRequestsController {
  @FXML private TextArea specialNotes = new TextArea();
  @FXML private ChoiceBox religionChoiceBox = new ChoiceBox();
  @FXML private ComboBox toChoiceBox = new ComboBox();
  @FXML private ComboBox employeeChoiceBox = new ComboBox();
  @FXML private ChoiceBox denomChoiceBox = new ChoiceBox();

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
    specialNotes.setWrapText(true);

    religionChoiceBox.getItems().removeAll(religionChoiceBox.getItems());
    religionChoiceBox.getItems().addAll("Christian", "Non-Religious", "Other");
    religionChoiceBox.getSelectionModel().select("Type");
    religionChoiceBox
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldValue, newValue) -> {
              if (newValue.equals("Type")) {
                denomChoiceBox.getItems().clear();
                denomChoiceBox.setDisable(true);
              } else if (newValue.equals("Christian")) {
                denomChoiceBox.getItems().clear();
                denomChoiceBox.getItems().setAll(christianDenom);
                denomChoiceBox.getSelectionModel().select(christianDenom.get(0));
                denomChoiceBox.setDisable(false);
              } else if (newValue.equals("Non-Religious")) {
                denomChoiceBox.getItems().clear();
                denomChoiceBox.getItems().setAll(nonDenom);
                denomChoiceBox.getSelectionModel().select(nonDenom.get(0));
                denomChoiceBox.setDisable(false);
              } else if (newValue.equals("Other")) {
                denomChoiceBox.getItems().clear();
                denomChoiceBox.getItems().setAll(otherDenom);
                denomChoiceBox.getSelectionModel().select(otherDenom.get(0));
                denomChoiceBox.setDisable(false);
              }
            });
    denomChoiceBox.getItems().removeAll(denomChoiceBox.getItems());
    toChoiceBox.getItems().removeAll(toChoiceBox.getItems());
    toChoiceBox
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toChoiceBox.setVisibleRowCount(5);
    employeeChoiceBox.setVisibleRowCount(5);

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
  void submitRequest() throws IOException {
    ReligiousServiceRequest religiousServiceRequest = new ReligiousServiceRequest();
    if (!religionChoiceBox.getSelectionModel().getSelectedItem().equals("Type")
        && toChoiceBox.getSelectionModel().getSelectedItem() != null
        && employeeChoiceBox.getSelectionModel().getSelectedItem() != null) {
      // pass religious service request object
      this.returnToHomeScene();
    }
  }
}
