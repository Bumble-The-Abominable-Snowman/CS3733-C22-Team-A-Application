package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDAO;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.fxml.FXML;
//import teamA_API.entities.Employee;
import teamA_API.Main;
import teamA_API.exceptions.ServiceException;

public class SelectServiceRequestCtrl extends MasterCtrl {

  @FXML private JFXButton equipmentDeliveryButton;
  @FXML private JFXButton religiousButton;
  @FXML private JFXButton sanitationButton;
  @FXML private JFXButton laundryButton;
  @FXML private JFXButton foodDeliveryButton;
  @FXML private JFXButton languageButton;
  @FXML private JFXButton floralDeliveryButton;
  @FXML private JFXButton medicineDeliveryButton;
  @FXML private JFXButton securityButton;
  @FXML private JFXButton consultationButton;
  @FXML private JFXButton maintenanceButton;
  @FXML private JFXButton giftDeliveryButton;
  @FXML private JFXButton loadAPIButton;
  @FXML private JFXButton saveAPIButton;

  double stageWidth;
  double equipmentDeliveryTextSize;
  double religiousRequestTextSize;
  double sanitationServicesTextSize;
  double laundryServicesTextSize;
  double foodDeliveryTextSize;
  double languageServicesTextSize;
  double floralServicesTextSize;
  double medicineDeliveryTextSize;
  double securityTextSize;
  double consultationTextSize;
  double maintenanceTextSize;
  double giftDeliveryTextSize;
  double loadAPIButtonTextSize;
  double saveAPIButtonTextSize;

  @FXML
  private void initialize() {

    configure();

    equipmentDeliveryTextSize = equipmentDeliveryButton.getFont().getSize();
    religiousRequestTextSize = religiousButton.getFont().getSize();
    sanitationServicesTextSize = sanitationButton.getFont().getSize();
    laundryServicesTextSize = laundryButton.getFont().getSize();
    foodDeliveryTextSize = foodDeliveryButton.getFont().getSize();
    languageServicesTextSize = languageButton.getFont().getSize();
    floralServicesTextSize = floralDeliveryButton.getFont().getSize();
    medicineDeliveryTextSize = medicineDeliveryButton.getFont().getSize();
    securityTextSize = securityButton.getFont().getSize();
    consultationTextSize = consultationButton.getFont().getSize();
    maintenanceTextSize = maintenanceButton.getFont().getSize();
    giftDeliveryTextSize = giftDeliveryButton.getFont().getSize();
    loadAPIButtonTextSize = loadAPIButton.getFont().getSize();
    saveAPIButtonTextSize = saveAPIButton.getFont().getSize();

    updateSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateSize();
            });
  }

  @FXML
  private void goToEquipmentDeliverySR() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.EQUIPMENT_DELIVERY_SR);
  }

  @FXML
  private void goToReligiousSR() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.RELIGIOUS_SR);
  }

  @FXML
  private void goToSanitationSR() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SANITATION_SR);
  }

  @FXML
  private void goToLaundrySR() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LAUNDRY_SR);
  }

  @FXML
  private void goToSecuritySR() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.SECURITY_SR);
  }

  @FXML
  private void goToFoodDeliverySR() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.FOOD_DELIVERY_SR);
  }

  @FXML
  private void goToMaintenanceSR() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.MAINTENANCE_SR);
  }

  @FXML
  private void goToConsultationSR() throws IOException {
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.CONSULTATION_SR);
  }

  @FXML
  private void goToGiftDeliverySR() throws IOException {

    sceneSwitcher.switchScene(SceneSwitcher.SCENES.GIFT_DELIVERY_SR);
  }

  @FXML
  private void goToLanguageSR() throws IOException {

    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LANGUAGE_SR);
  }

  @FXML
  private void goToFloralDeliverySR() throws IOException {

    sceneSwitcher.switchScene(SceneSwitcher.SCENES.FLORAL_DELIVERY_SR);
  }

  @FXML
  private void goToMedicineDeliverySR() throws IOException {

    sceneSwitcher.switchScene(SceneSwitcher.SCENES.MEDICINE_DELIVERY_SR);
  }

  @FXML
  private void loadAPI() throws ServiceException, IOException {
    Main.run(500, 200, 960, 600, "", "N/A", "N/A");
  }

  @FXML
  private void saveAPI() throws IllegalAccessException, SQLException, InvocationTargetException {
    List<teamA_API.entities.SR> list = Main.getRequestList();
    ServiceRequestDerbyImpl data = new ServiceRequestDerbyImpl(SR.SRType.SANITATION);
    for (teamA_API.entities.SR req : list) {
      SR sr = new SR(SR.SRType.SANITATION);
      sr.setFieldByString("request_id", req.getStringFields().get("request_id"));
      sr.setFieldByString("start_location", req.getStringFields().get("start_location"));
      sr.setFieldByString("end_location", req.getStringFields().get("end_location"));

      teamA_API.entities.Employee employeeRequestedAPI = (teamA_API.entities.Employee) req.getFields().get("employee_requested");
      Employee employeeRequested = new Employee(
              employeeRequestedAPI.getEmployeeID(),
              employeeRequestedAPI.getEmployeeType(),
              employeeRequestedAPI.getFirstName(),
              employeeRequestedAPI.getLastName(),
              employeeRequestedAPI.getEmail(),
              employeeRequestedAPI.getPhoneNum(),
              employeeRequestedAPI.getAddress(),
              employeeRequestedAPI.startDate);
      teamA_API.entities.Employee employeeAssignedAPI = (teamA_API.entities.Employee) req.getFields().get("employee_assigned");
      Employee employeeAssigned = new Employee(
              employeeAssignedAPI.getEmployeeID(),
              employeeAssignedAPI.getEmployeeType(),
              employeeAssignedAPI.getFirstName(),
              employeeAssignedAPI.getLastName(),
              employeeAssignedAPI.getEmail(),
              employeeAssignedAPI.getPhoneNum(),
              employeeAssignedAPI.getAddress(),
              employeeAssignedAPI.startDate);

      EmployeeDerbyImpl employeeDerby = new EmployeeDerbyImpl();
      boolean doesEmployeeRequestedNotExist = false;
      boolean doesEmployeeAssignedNotExist = false;
      for (Employee e: employeeDerby.getEmployeeList()) {
        if (e.getEmployeeID().equals(employeeRequested.getEmployeeID()))
        {
          doesEmployeeRequestedNotExist = true;
        }
        if (e.getEmployeeID().equals(employeeAssigned.getEmployeeID()))
        {
          doesEmployeeAssignedNotExist = true;
        }
      }
      if (!doesEmployeeRequestedNotExist)
      {
        employeeDerby.enterEmployee(employeeRequestedAPI.getEmployeeID(),
                employeeRequestedAPI.getEmployeeType(),
                employeeRequestedAPI.getFirstName(),
                employeeRequestedAPI.getLastName(),
                employeeRequestedAPI.getEmail(),
                employeeRequestedAPI.getPhoneNum(),
                employeeRequestedAPI.getAddress(),
                employeeRequestedAPI.startDate);

      }
      if (!doesEmployeeAssignedNotExist)
      {
        employeeDerby.enterEmployee(employeeAssigned.getEmployeeID(),
                employeeAssigned.getEmployeeType(),
                employeeAssigned.getFirstName(),
                employeeAssigned.getLastName(),
                employeeAssigned.getEmail(),
                employeeAssigned.getPhoneNum(),
                employeeAssigned.getAddress(),
                employeeAssigned.startDate);
      }

      sr.setField("employee_requested", employeeRequested);
      sr.setField("employee_assigned", employeeAssigned);

      sr.setFieldByString("comments", req.getStringFields().get("comments"));
      sr.setFieldByString("sanitation_type", req.getStringFields().get("sanitation_type"));

      System.out.println(sr);
      data.enterServiceRequest(sr);
    }
  }

  @FXML
  private void updateSize() {

    stageWidth = App.getStage().getWidth();

    equipmentDeliveryButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * equipmentDeliveryTextSize) + "pt;");
    religiousButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * religiousRequestTextSize) + "pt;");
    sanitationButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * sanitationServicesTextSize) + "pt;");
    laundryButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * laundryServicesTextSize) + "pt;");
    foodDeliveryButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * foodDeliveryTextSize) + "pt;");
    languageButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * languageServicesTextSize) + "pt;");
    floralDeliveryButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * floralServicesTextSize) + "pt;");
    medicineDeliveryButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * medicineDeliveryTextSize) + "pt;");
    securityButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * securityTextSize) + "pt;");
    consultationButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * consultationTextSize) + "pt;");
    maintenanceButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * maintenanceTextSize) + "pt;");
    giftDeliveryButton.setStyle(
        "-fx-font-size: " + ((stageWidth / 1000) * giftDeliveryTextSize) + "pt;");
    loadAPIButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * loadAPIButtonTextSize) + "pt;");
    saveAPIButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * saveAPIButtonTextSize) + "pt;");
  }
}
