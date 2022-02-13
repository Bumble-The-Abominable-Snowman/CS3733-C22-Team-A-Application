package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.LanguageSR;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class LanguageSRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> languageChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;

  @FXML
  public void initialize() throws ParseException {
    sceneID = SceneSwitcher.SCENES.LANGUAGE_SR;

    backButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    homeButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    clearButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));
    submitButton.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, new CornerRadii(0), Insets.EMPTY)));

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
  void submitRequest() throws IOException {
    if (!languageChoice.getSelectionModel().getSelectedItem().equals("Language")
        && toLocationChoice.getSelectionModel().getSelectedItem() != null
        && !employeeChoice.getSelectionModel().getSelectedItem().equals("Employee")) {
      LanguageSR languageSR =
          new LanguageSR(
              "PlaceHolderID",
              "N/A",
              toLocationChoice.getSelectionModel().getSelectedItem(),
              App.factory.getUsername(),
              employeeChoice.getSelectionModel().getSelectedItem(),
              new Timestamp((new Date()).getTime()).toString(),
              SR.Status.BLANK,
              "Language Interpreter",
              commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
              languageChoice.getValue());

      ServiceRequestDerbyImpl<LanguageSR> serviceRequestDAO =
          new ServiceRequestDerbyImpl<>(new LanguageSR());
      serviceRequestDAO.enterServiceRequest(languageSR);
      this.goToHomeScene();
    }
  }
}
