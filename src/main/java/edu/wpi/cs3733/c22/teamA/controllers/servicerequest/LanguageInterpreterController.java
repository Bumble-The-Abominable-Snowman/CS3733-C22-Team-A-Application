package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.requests.LanguageInterpreterRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class LanguageInterpreterController extends GenericServiceRequestsController {
  @FXML private JFXButton backButton;
  @FXML private JFXButton returnHomeButton;
  @FXML private JFXButton clearButton;
  @FXML private JFXButton submitButton;
  @FXML private JFXComboBox<String> languageChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() throws ParseException {
    sceneID = SceneController.SCENES.LANGUAGE_INTERPRETER_SERVICE_REQUEST_SCENE;

    backButton.setBackground(
        new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));

    commentsBox.setWrapText(true);

    languageChoice.getItems().removeAll(languageChoice.getItems());
    languageChoice
        .getItems()
        .addAll(
            "American Sign Language",
            "Arabic",
            "French",
            "German",
            "Italian",
            "Japanese",
            "Korean",
            "Mandarin",
            "Russian",
            "Spanish");
    languageChoice.getSelectionModel().select("Language");
    languageChoice.setVisibleRowCount(5);

    toLocationChoice.getItems().removeAll(toLocationChoice.getItems());
    toLocationChoice
        .getItems()
        .addAll(
            new LocationDerbyImpl()
                .getNodeList().stream().map(Location::getShortName).collect(Collectors.toList()));
    toLocationChoice.setVisibleRowCount(5);

    employeeChoice.getItems().removeAll(employeeChoice.getItems());
    EmployeeDAO EmployeeDAO = new EmployeeDerbyImpl();
    String input = "2022-02-03";
    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = originalFormat.parse(input);
    EmployeeDAO.enterEmployee(
        "323",
        "Admin",
        "Lucy",
        "Bernard",
        "lcbernard@wpi.edu",
        "0000000000",
        "100 institute Rd",
        date);
    employeeChoice
        .getItems()
        .addAll(
            EmployeeDAO.getEmployeeList().stream()
                .map(Employee::getFirstName)
                .collect(Collectors.toList()));
    employeeChoice.getSelectionModel().select("Employee");
    employeeChoice.setVisibleRowCount(5);
  }

  @FXML
  private void createLanguageRequest(LanguageInterpreterRequest langRequest) {
    langRequest.setLanguage(languageChoice.getValue());
    langRequest.setToLocation(toLocationChoice.getValue());
    langRequest.setRequestedEmployee(employeeChoice.getValue());
    langRequest.setToLocation(commentsBox.getText());
  }

  @FXML
  void submitRequest() throws IOException {
    LanguageInterpreterRequest langRequest = new LanguageInterpreterRequest();
    if (!languageChoice.getSelectionModel().getSelectedItem().equals("Language")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && !employeeChoice.getSelectionModel().getSelectedItem().equals("Employee")) {
      this.createLanguageRequest(langRequest);
      this.returnToHomeScene();
    }
  }
}
