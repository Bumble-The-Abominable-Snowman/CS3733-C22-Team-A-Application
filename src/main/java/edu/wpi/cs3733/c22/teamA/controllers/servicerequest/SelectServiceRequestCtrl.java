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
    Main.run(500, 200, 960, 600, "", "");
  }

  @FXML
  private void saveAPI() throws IllegalAccessException, SQLException, InvocationTargetException {
    List<teamA_API.entities.SR> list = Main.getRequestList();
    ServiceRequestDerbyImpl data = new ServiceRequestDerbyImpl(SR.SRType.SANITATION);
    for (teamA_API.entities.SR req : list) {
      SR sr = new SR(SR.SRType.SANITATION);
      sr.setFieldByString("request_id", req.getFields_string().get("request_id"));
      sr.setFieldByString("start_location", "N/A");
      sr.setFieldByString("end_location", req.getFields_string().get("end_location"));

      teamA_API.entities.Employee employeeAssignedAPI = (teamA_API.entities.Employee) Main.getEmployee(req.getFields_string().get("employee_assigned"));
      Employee employeeRequested = new Employee(
              employeeAssignedAPI.getStringFields().get("employee_id"),
              employeeAssignedAPI.getStringFields().get("employee_type"),
              employeeAssignedAPI.getStringFields().get("first_name"),
              employeeAssignedAPI.getStringFields().get("last_name"),
              employeeAssignedAPI.getStringFields().get("email"),
              employeeAssignedAPI.getStringFields().get("phone_num"),
              employeeAssignedAPI.getStringFields().get("address"),
              employeeAssignedAPI.thisEmployee.getStringFields().get("start_date"));

      EmployeeDerbyImpl employeeDerby = new EmployeeDerbyImpl();
      boolean doesEmployeeAssignedNotExist = false;
      for (Employee e: employeeDerby.getEmployeeList()) {
        if (e.getStringFields().get("employee_id").equals(employeeRequested.getStringFields().get("employee_id")))
        {
          doesEmployeeAssignedNotExist = true;
        }
      }
      if (!doesEmployeeAssignedNotExist)
      {
        employeeDerby.enterEmployee(employeeAssignedAPI.getStringFields().get("employee_id"),
                employeeAssignedAPI.getStringFields().get("employee_type"),
                employeeAssignedAPI.getStringFields().get("first_name"),
                employeeAssignedAPI.getStringFields().get("last_name"),
                employeeAssignedAPI.getStringFields().get("email"),
                employeeAssignedAPI.getStringFields().get("phone_num"),
                employeeAssignedAPI.getStringFields().get("address"),
                employeeAssignedAPI.thisEmployee.getStringFields().get("start_date"));

      }

      sr.setField("employee_assigned", employeeRequested);
      sr.setField("employee_requested", employeeDerby.getEmployee("001"));

      sr.setFieldByString("comments", req.getFields_string().get("comments"));
      sr.setFieldByString("sanitation_type", req.getFields_string().get("sanitation_type"));

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
