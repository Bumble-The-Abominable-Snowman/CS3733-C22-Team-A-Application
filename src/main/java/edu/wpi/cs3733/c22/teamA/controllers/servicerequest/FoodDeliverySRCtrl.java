package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.FoodDeliverySR;
//import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import edu.wpi.cs3733.c22.teamA.entities.servicerequests.AutoCompleteBox;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FoodDeliverySRCtrl extends SRCtrl {
  @FXML private JFXComboBox<String> mainChoice;
  @FXML private JFXComboBox<String> sideChoice;
  @FXML private JFXComboBox<String> dessertChoice;
  @FXML private JFXComboBox<String> drinkChoice;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private TextArea commentsBox;
  @FXML private Label foodLabel;
  @FXML private Label mapLabel;
  @FXML private Label locationLabel;
  @FXML private Label orderLabel;
  @FXML private Label employeeLabel;

  @FXML
  protected void initialize() throws ParseException {
    super.initialize();
    sceneID = SceneSwitcher.SCENES.FOOD_DELIVERY_SR;

    // double mainChoiceTextSize = mainChoice.getFont().getSize();
    // double sideChoiceTextSize = sideChoice.getFont().getSize();
    // double dessertChoiceTextSize = dessertChoice.getFont().getSize();
    // double drinkChoiceTextSize = drinkChoice.getFont().getSize();
    // double toLocationChoiceTextSize = toLocationChoice.getFont().getSize();
    // double employeeChoiceTextSize = employeeChoice.getFont().getSize();
    double commentsTextSize = commentsBox.getFont().getSize();
    double foodTextSize = foodLabel.getFont().getSize();
    double mapTextSize = mapLabel.getFont().getSize();
    double locationTextSize = locationLabel.getFont().getSize();
    double orderTextSize = orderLabel.getFont().getSize();
    double employeeTextSize = employeeLabel.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              commentsBox.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * commentsTextSize)
                      + "pt;");
              foodLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * foodTextSize) + "pt;");
              mapLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * mapTextSize) + "pt;");
              locationLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * locationTextSize)
                      + "pt;");
              orderLabel.setStyle(
                  "-fx-font-size: " + ((App.getStage().getWidth() / 1000) * orderTextSize) + "pt;");
              employeeLabel.setStyle(
                  "-fx-font-size: "
                      + ((App.getStage().getWidth() / 1000) * employeeTextSize)
                      + "pt;");
            });

    commentsBox.setWrapText(true);

    mainChoice.getItems().addAll("Turkey Sandwich", "Grilled Cheese Sandwich", "Fried Chicken");
    drinkChoice.getItems().addAll("Water", "Juice", "Milk");
    sideChoice.getItems().addAll("French Fries", "Apple", "Biscuit");
    dessertChoice.getItems().addAll("Cookie", "Brownie", "Cinnamon Roll");
    toLocationChoice.getSelectionModel().select(0);
    mainChoice.getSelectionModel().selectedItemProperty();
    drinkChoice.getSelectionModel().selectedItemProperty();
    sideChoice.getSelectionModel().selectedItemProperty();
    dessertChoice.getSelectionModel().selectedItemProperty();
    toLocationChoice.getSelectionModel().selectedItemProperty();

    new AutoCompleteBox(mainChoice);
    new AutoCompleteBox(drinkChoice);
    new AutoCompleteBox(sideChoice);
    new AutoCompleteBox(dessertChoice);

    this.populateEmployeeAndLocationList();
    this.populateEmployeeComboBox(this.employeeChoice);
    this.populateLocationComboBox(this.toLocationChoice);
    new AutoCompleteBox(toLocationChoice);
    new AutoCompleteBox(employeeChoice);
  }

  @FXML
  void submitRequest()
      throws IOException, SQLException, InvocationTargetException, IllegalAccessException {

    int employeeIndex = this.employeeChoice.getSelectionModel().getSelectedIndex();
    if(employeeIndex == -1){
      return;
    }
    Employee employeeSelected = this.employeeList.get(employeeIndex);

    int locationIndex = this.toLocationChoice.getSelectionModel().getSelectedIndex();
    if(locationIndex == -1){
      return;
    }
    Location toLocationSelected = this.locationList.get(locationIndex);

    SR sr = new SR("FoodDeliverySRID",
            (new LocationDerbyImpl()).getLocationNode("N/A"),
            toLocationSelected,
            (new EmployeeDerbyImpl()).getEmployee("001"),
            employeeSelected,
            new Timestamp((new Date()).getTime()),
            SR.Status.BLANK,
            SR.Priority.REGULAR,
            commentsBox.getText().equals("") ? "N/A" : commentsBox.getText(),
            SR.SRType.FOOD_DELIVERY);

    sr.setFieldByString("main_dish", this.mainChoice.getValue());
    sr.setFieldByString("side_dish", this.sideChoice.getValue());
    sr.setFieldByString("beverage", this.drinkChoice.getValue());
    sr.setFieldByString("dessert", this.dessertChoice.getValue());

    ServiceRequestDerbyImpl serviceRequestDerby = new ServiceRequestDerbyImpl(SR.SRType.FOOD_DELIVERY);
    serviceRequestDerby.enterServiceRequest(sr);

//    FoodDeliverySR foodDeliverySR =
//        new FoodDeliverySR(
//            "FoodDeliverySRID",
//            "N/A",
//            toLocationSelected.getNodeID(),
//            "001",
//            employeeSelected.getEmployeeID(),
//            new Timestamp((new Date()).getTime()),
//            SR.Status.BLANK,
//            SR.Priority.REGULAR,
//            commentsBox.getText().equals("") ? "N/A" : commentsBox.getText());
//
//    foodDeliverySR.setDessert(this.dessertChoice.getValue());
//    foodDeliverySR.setBeverage(this.drinkChoice.getValue());
//    foodDeliverySR.setMainDish(this.mainChoice.getValue());
//    foodDeliverySR.setSideDish(this.sideChoice.getValue());
//
//    ServiceRequestDerbyImpl<FoodDeliverySR> serviceRequestDAO =
//        new ServiceRequestDerbyImpl<>(new FoodDeliverySR());
//    serviceRequestDAO.enterServiceRequest(foodDeliverySR);
  }
}
