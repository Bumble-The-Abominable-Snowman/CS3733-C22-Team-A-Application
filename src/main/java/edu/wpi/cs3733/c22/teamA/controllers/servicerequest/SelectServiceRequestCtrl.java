package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeWrapperImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestWrapperImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
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

  @FXML private ImageView equipmentDeliveryIcon;
  @FXML private ImageView religiousIcon;
  @FXML private ImageView sanitationIcon;
  @FXML private ImageView laundryIcon;
  @FXML private ImageView foodDeliveryIcon;
  @FXML private ImageView languageIcon;
  @FXML private ImageView floralDeliveryIcon;
  @FXML private ImageView medicineDeliveryIcon;
  @FXML private ImageView securityIcon;
  @FXML private ImageView consultationIcon;
  @FXML private ImageView maintenanceIcon;
  @FXML private ImageView giftDeliveryIcon;

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
    handleSRIconPulses();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateSize();
            });

    equipmentDeliveryButton.setDisable(true);
    religiousButton.setDisable(true);
    sanitationButton.setDisable(true);
    laundryButton.setDisable(true);
    foodDeliveryButton.setDisable(true);
    languageButton.setDisable(true);
    floralDeliveryButton.setDisable(true);
    medicineDeliveryButton.setDisable(true);
    securityButton.setDisable(true);
    consultationButton.setDisable(true);
    maintenanceButton.setDisable(true);
    giftDeliveryButton.setDisable(true);

    for (String scope: App.authUser.getPermissions()) {
      System.out.printf("permission for %s\n", scope);
      switch (scope) {
        case "write:db-sr-equipment-delivery":
          equipmentDeliveryButton.setDisable(false);
          break;
        case "write:db-sr-religious":
          religiousButton.setDisable(false);
          break;
        case "write:db-sr-sanitation":
          sanitationButton.setDisable(false);
          break;
        case "write:db-sr-laundry":
          laundryButton.setDisable(false);
          break;
        case "write:db-sr-food-delivery":
          foodDeliveryButton.setDisable(false);
          break;
        case "write:db-sr-language":
          languageButton.setDisable(false);
          break;
        case "write:db-sr-floral-delivery":
          floralDeliveryButton.setDisable(false);
          break;
        case "write:db-sr-medicine-delivery":
          medicineDeliveryButton.setDisable(false);
          break;
        case "write:db-sr-security":
          securityButton.setDisable(false);
          break;
        case "write:db-sr-consultation":
          consultationButton.setDisable(false);
          break;
        case "write:db-sr-maintenance":
          maintenanceButton.setDisable(false);
          break;
        case "write:db-sr-gift-delivery":
          giftDeliveryButton.setDisable(false);
          break;
      }
    }
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
    Main.run(500, 200, 960, 600, App.class.getResource("css/styleSheet.css").toExternalForm(), "");
  }

  @FXML
  private void saveAPI() throws IllegalAccessException, SQLException, InvocationTargetException, ParseException, IOException {
    List<teamA_API.entities.SR> list = Main.getRequestList();
    ServiceRequestWrapperImpl data = new ServiceRequestWrapperImpl(SR.SRType.SANITATION);
    for (teamA_API.entities.SR req : list) {
      SR sr = new SR(SR.SRType.SANITATION);
      sr.setFieldByString("request_id", req.getFields_string().get("request_id"));
      sr.setFieldByString("start_location", "NA");
      sr.setFieldByString("end_location", req.getFields_string().get("end_location"));

      teamA_API.entities.Employee employeeAssignedAPI = (teamA_API.entities.Employee) Main.getEmployee(req.getFields_string().get("employee_assigned"));
      Employee employeeRequested = new Employee(
              employeeAssignedAPI.getEmployeeID(),
              employeeAssignedAPI.getEmployeeType(),
              employeeAssignedAPI.getFirstName(),
              employeeAssignedAPI.getLastName(),
              employeeAssignedAPI.getEmail(),
              employeeAssignedAPI.getPhoneNum(),
              employeeAssignedAPI.getAddress(),
              new SimpleDateFormat("yyyy-MM-dd").parse(employeeAssignedAPI.getStartDate()));

      EmployeeWrapperImpl employeeWrapper = new EmployeeWrapperImpl();
      boolean doesEmployeeAssignedNotExist = false;
      for (Employee e: employeeWrapper.getEmployeeList()) {
        if (e.getStringFields().get("employee_id").equals(employeeRequested.getStringFields().get("employee_id")))
        {
          doesEmployeeAssignedNotExist = true;
        }
      }
      if (!doesEmployeeAssignedNotExist)
      {
        employeeWrapper.enterEmployee(employeeAssignedAPI.getEmployeeID(),
                employeeAssignedAPI.getEmployeeType(),
                employeeAssignedAPI.getFirstName(),
                employeeAssignedAPI.getLastName(),
                employeeAssignedAPI.getEmail(),
                employeeAssignedAPI.getPhoneNum(),
                employeeAssignedAPI.getAddress(),
                new SimpleDateFormat("yyyy-MM-dd").parse(employeeAssignedAPI.getStartDate()));

      }

      sr.setField("employee_assigned", employeeRequested);
      sr.setField("employee_requested", employeeWrapper.getEmployee("001"));

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

  @FXML
  private void handleSRIconPulses() {

    equipmentDeliveryButton.setOnMouseEntered(homeEnter -> {equipmentDeliveryIcon.setFitHeight(40);});
    equipmentDeliveryButton.setOnMouseExited(homeExit -> {equipmentDeliveryIcon.setFitHeight(26);});

    religiousButton.setOnMouseEntered(selectSREnter -> {religiousIcon.setFitHeight(40);});
    religiousButton.setOnMouseExited(selectSRExit -> {religiousIcon.setFitHeight(26);});

    sanitationButton.setOnMouseEntered(mapEnter -> {sanitationIcon.setFitHeight(40);});
    sanitationButton.setOnMouseExited(mapExit -> {sanitationIcon.setFitHeight(26);});

    laundryButton.setOnMouseEntered(mouseEnter -> {laundryIcon.setFitHeight(40);});
    laundryButton.setOnMouseExited(mouseExit -> {laundryIcon.setFitHeight(26);});

    foodDeliveryButton.setOnMouseEntered(mouseEnter -> {foodDeliveryIcon.setFitHeight(40);});
    foodDeliveryButton.setOnMouseExited(mouseExit -> {foodDeliveryIcon.setFitHeight(26);});

    languageButton.setOnMouseEntered(mouseEnter -> {languageIcon.setFitHeight(40);});
    languageButton.setOnMouseExited(mouseExit -> {languageIcon.setFitHeight(26);});

    floralDeliveryButton.setOnMouseEntered(mouseEnter -> {floralDeliveryIcon.setFitHeight(40);});
    floralDeliveryButton.setOnMouseExited(mouseExit -> {floralDeliveryIcon.setFitHeight(26);});

    medicineDeliveryButton.setOnMouseEntered(mouseEnter -> {medicineDeliveryIcon.setFitHeight(40);});
    medicineDeliveryButton.setOnMouseExited(mouseExit -> {medicineDeliveryIcon.setFitHeight(26);});

    securityButton.setOnMouseEntered(mouseEnter -> {securityIcon.setFitHeight(40);});
    securityButton.setOnMouseExited(mouseExit -> {securityIcon.setFitHeight(26);});

    consultationButton.setOnMouseEntered(mouseEnter -> {consultationIcon.setFitHeight(40);});
    consultationButton.setOnMouseExited(mouseExit -> {consultationIcon.setFitHeight(26);});

    maintenanceButton.setOnMouseEntered(mouseEnter -> {maintenanceIcon.setFitHeight(40);});
    maintenanceButton.setOnMouseExited(mouseExit -> {maintenanceIcon.setFitHeight(26);});

    giftDeliveryButton.setOnMouseEntered(mouseEnter -> {giftDeliveryIcon.setFitHeight(40);});
    giftDeliveryButton.setOnMouseExited(mouseExit -> {giftDeliveryIcon.setFitHeight(26);});

  }

}
