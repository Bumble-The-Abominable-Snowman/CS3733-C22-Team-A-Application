package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.controllers.MasterCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import edu.wpi.cs3733.c22.teamA.entities.map.GesturePaneManager;
import edu.wpi.cs3733.c22.teamA.entities.map.LocationMarker;
import edu.wpi.cs3733.c22.teamA.entities.map.MarkerManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import net.kurobako.gesturefx.GesturePane;

public abstract class SRCtrl extends MasterCtrl {
  @FXML Button submitButton;
  @FXML Button clearButton;
  @FXML GesturePane mapView;

  private double stageWidth;

  List<Employee> employeeList = new ArrayList<>();
  List<Location> locationList = new ArrayList<>();

  private GesturePaneManager gesturePaneManager;
  private MarkerManager markerManager;
  private LocationDAO locationDAO;
  private ImageView mapImageView;
  private String[] floorNames = {"3", "2", "1", "L1", "L2"};

  SceneSwitcher.SCENES sceneID;

  private final SceneSwitcher sceneSwitcher = App.sceneSwitcher;

  @FXML
  void initialize() throws ParseException {
    double submitTextSize = submitButton.getFont().getSize();
    double clearTextSize = clearButton.getFont().getSize();

    App.getStage()
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              stageWidth = App.getStage().getWidth();
              submitButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * submitTextSize) + "pt;");
              clearButton.setStyle("-fx-font-size: " + ((stageWidth / 1000) * clearTextSize) + "pt;");
            });

    mapImageView = new ImageView();
    AnchorPane anchorPane = new AnchorPane();
    gesturePaneManager = new GesturePaneManager(mapView, anchorPane, mapImageView);
    gesturePaneManager.setMapFloor("Floor 1");
    gesturePaneManager.initGesture();

    locationDAO = new LocationDerbyImpl();
    markerManager = new MarkerManager(locationDAO, anchorPane);
    List<JFXButton> buttons = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      double buttonY = i * 41 + mapImageView.getLayoutY();
      JFXButton button = new JFXButton();
      button.setLayoutX(mapImageView.getLayoutX());
      button.setLayoutY(buttonY);
      button.setPrefHeight(40);
      button.setPrefWidth(90);
      int finalI = i;
      button.setOnMousePressed(e -> {markerManager.initFloor(floorNames[finalI], (int)mapImageView.getLayoutX(), (int)mapImageView.getLayoutY()); if(floorNames[finalI].equals("L1") || floorNames[finalI].equals("L2")) gesturePaneManager.setMapFloor(floorNames[finalI]); else gesturePaneManager.setMapFloor("Floor " + floorNames[finalI]);});
      button.setText("Floor " + floorNames[finalI]);
      buttons.add(button);
    }
    anchorPane.getChildren().addAll(buttons);
    markerManager.initFloor("1", (int)mapImageView.getLayoutX(), (int)mapImageView.getLayoutY());
  }

  @FXML
  private void clearSubmission() throws IOException {
    sceneSwitcher.switchScene(sceneID);
  }

  @FXML
  abstract void submitRequest()
      throws IOException, TimeoutException, SQLException, InvocationTargetException,
          IllegalAccessException;

  @FXML
  protected void populateEmployeeAndLocationList() {
    this.employeeList = new EmployeeDerbyImpl().getEmployeeList();
    this.locationList = new LocationDerbyImpl().getNodeList();
  }

  @FXML
  protected void populateEmployeeComboBox(JFXComboBox<String> employeeChoice) {
    employeeChoice.getItems().removeAll(employeeChoice.getItems());
    employeeChoice
        .getItems()
        .addAll(this.employeeList.stream().map(Employee::getFullName).collect(Collectors.toList()));
    employeeChoice.setVisibleRowCount(5);
  }

  @FXML
  protected void populateLocationComboBox(JFXComboBox<String> locationChoice) {
    locationChoice.getItems().removeAll(locationChoice.getItems());
    locationChoice
        .getItems()
        .addAll(
            this.locationList.stream().map(Location::getShortName).collect(Collectors.toList()));
    locationChoice.setVisibleRowCount(5);
  }

  public String getUniqueNumbers(){
    int round = 0;
    double rand = Math.random()*1000;
    if(rand < 1.0) round = (int) Math.ceil(rand);
    else if(rand > 999.0) round = (int) Math.floor(rand);
    else round = (int) Math.floor(rand);

    String str ="";
    if(round >= 100) str = str + round;
    else if(round >= 10 && round <= 100) str = str + "0" + round;
    else if(round >= 1 && round <= 9 ) str = str + "00" + round;
    else if(round == 0) str = "000";
    System.out.println("rand = " + rand +" round = " + round + " str = " + str);

    return str;
  }

  public MarkerManager getMarkerManager() {
    return markerManager;
  }
}