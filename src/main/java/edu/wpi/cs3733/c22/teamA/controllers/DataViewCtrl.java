package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.dataview.*;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.util.Duration;

public class DataViewCtrl extends MasterCtrl {

  @FXML private JFXTreeTableView<RecursiveObj> table;
  @FXML private VBox inputVBox;
  @FXML private JFXComboBox selectEmployeeBox;
  @FXML private JFXButton addButton;

  @FXML private ImageView bumbleBlinkHead;
  @FXML private JFXButton previousButton;
  @FXML private JFXButton nextButton;
  @FXML private JFXButton previous1Button;
  @FXML private JFXButton next1Button;
  @FXML private JFXButton previous2Button;
  @FXML private JFXButton next2Button;
  @FXML private JFXButton previous3Button;
  @FXML private JFXButton next3Button;
  @FXML private Label bubble1Text;
  @FXML private Label bubble2Text;
  @FXML private Label bubble3Text;
  @FXML private Label bubble4Text;

  private StringBuilder detailLabel = new StringBuilder("No further details  ");
  public static AtomicReference<Popup> detailsPopup = new AtomicReference<>(new Popup());
  public static AtomicReference<Popup> modifyPopup = new AtomicReference<>(new Popup());
  public static AtomicReference<Popup> addPopup = new AtomicReference<>(new Popup());

  AtomicReference<Point2D> point = new AtomicReference<>(new Point2D(0., 0.));

  ContextMenu rightClickMenu = new ContextMenu();
  MenuItem viewDetails = new MenuItem("View Details");
  MenuItem modify = new MenuItem("Modify");

  Popup p = new Popup();

  SRDataviewManager srDataviewManager;
  EmployeeDataviewManager employeeDataviewManager;
  EquipmentDataviewManager equipmentDataviewManager;
  LocationDataviewManager locationDataviewManager;
  MedicineDataviewManager medicineDataviewManager;
  DVSelectionManager dvSelectionManager;

  public DataViewCtrl(){
    table = new JFXTreeTableView<>();
    srDataviewManager = new SRDataviewManager(this);
    employeeDataviewManager = new EmployeeDataviewManager(this);
    equipmentDataviewManager = new EquipmentDataviewManager(this);
    locationDataviewManager = new LocationDataviewManager(this);
    medicineDataviewManager = new MedicineDataviewManager(this);
  }

  @FXML
  public void delete() throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
    //System.out.println(table.getSelectionModel().getSelectedItem().getValue().sr);

    if (HomeCtrl.sceneFlag == 1) {
      srDataviewManager.delete();
    } else if (HomeCtrl.sceneFlag == 2) {
      locationDataviewManager.delete();
    } else if (HomeCtrl.sceneFlag == 3) {
      equipmentDataviewManager.delete();
    } else if (HomeCtrl.sceneFlag == 4) {
      employeeDataviewManager.delete();
    } else if (HomeCtrl.sceneFlag == 5) {
      medicineDataviewManager.delete();
    } else {
      // wait what how did you get here
    }
  }

 /* @FXML
  public void save() {

    if (HomeCtrl.sceneFlag == 3) {

      DataViewCtrl.addPopup.get().hide();

      // cancel button
      JFXButton cancelUpdateButton = new JFXButton();
      cancelUpdateButton.setText("X");

      cancelUpdateButton.setOnAction(
          event -> {
            DataViewCtrl.addPopup.get().hide();
          });

      Label equipmentIDLabel = new Label("equipmentID");
      Label equipmentTypeLabel = new Label("equipmentType");
      Label isCleanLabel = new Label("isClean");
      Label currentLocationLabel = new Label("currentLocation");
      Label isAvailableLabel = new Label("isAvailable");

      equipmentIDLabel.setPadding(new Insets(10, 10, 10, 5));
      equipmentTypeLabel.setPadding(new Insets(10, 10, 10, 5));
      isCleanLabel.setPadding(new Insets(10, 10, 10, 5));
      currentLocationLabel.setPadding(new Insets(10, 10, 10, 5));
      isAvailableLabel.setPadding(new Insets(10, 10, 10, 5));
      cancelUpdateButton.setStyle("-fx-alignment: center ;");
      cancelUpdateButton.setAlignment(Pos.CENTER);

      // value text area
      TextArea equipmentIDText = new TextArea();
      equipmentIDText.setPromptText("Enter equipmentID:");
      equipmentIDText.setMinSize(50, 30);
      equipmentIDText.setMaxSize(150, 30);

      TextArea equipmentTypeText = new TextArea();
      equipmentTypeText.setPromptText("Enter equipmentType:");
      equipmentTypeText.setMinSize(50, 30);
      equipmentTypeText.setMaxSize(150, 30);

      JFXCheckBox isCleanCheckBox = new JFXCheckBox();

      TextArea currentLocationText = new TextArea();
      currentLocationText.setPromptText("Enter currentLocation ID:");
      currentLocationText.setMinSize(50, 30);
      currentLocationText.setMaxSize(150, 30);

      JFXCheckBox isAvailableCheckBox = new JFXCheckBox();

      JFXButton updateButton = new JFXButton();
      updateButton.setText("Update");

      updateButton.setOnAction(
          e -> {
            if (equipmentIDText.getText().length() > 2
                && equipmentTypeText.getText().length() > 2
                && currentLocationText.getText().length() > 2) {
              EquipmentWrapperImpl equipmentWrapper = new EquipmentWrapperImpl();
              LocationDAO locationDAO = new LocationWrapperImpl();
              List<Location> aList = locationDAO.getNodeList();
              Location theL = new Location();
              for (Location aL: aList){
                if(currentLocationText.getText().equals(aL.getStringFields().get("node_id"))){
                  theL = aL;
                  System.out.println(theL.getStringFields().get("node_id"));
                  break;
                }
              }
              if(theL.getStringFields().get("node_id") == null){
                System.out.println("Location does not exist");
                return;
              }
              if(!(theL.getStringFields().get("node_type").equals("STOR")) && !(theL.getStringFields().get("node_type").equals("PATI"))){
                System.out.println("THIS EQUIPMENT CANNOT BE STORED HERE");
                return;
              }
              equipmentWrapper.enterMedicalEquipment(
                  equipmentIDText.getText(),
                  equipmentTypeText.getText(),
                  isCleanCheckBox.isSelected(),
                  currentLocationText.getText(),
                  isAvailableCheckBox.isSelected());
              updateButton.setTextFill(Color.GREEN);
              try {
                srDataviewManager.initializeRequestsTable();
              } catch (SQLException | InvocationTargetException | IllegalAccessException ex) {
                ex.printStackTrace();
              }
            } else {
              updateButton.setTextFill(Color.RED);
            }
          });

      // add it to the scene
      var content = new GridPane();
      content.setRowIndex(equipmentIDLabel, 0);
      content.setColumnIndex(equipmentIDLabel, 0);
      content.setRowIndex(equipmentTypeLabel, 1);
      content.setColumnIndex(equipmentTypeLabel, 0);
      content.setRowIndex(isCleanLabel, 2);
      content.setColumnIndex(isCleanLabel, 0);
      content.setRowIndex(currentLocationLabel, 3);
      content.setColumnIndex(currentLocationLabel, 0);
      content.setRowIndex(isAvailableLabel, 4);
      content.setColumnIndex(isAvailableLabel, 0);
      content.setRowIndex(cancelUpdateButton, 5);
      content.setColumnIndex(cancelUpdateButton, 0);

      content.setRowIndex(equipmentIDText, 0);
      content.setColumnIndex(equipmentIDText, 1);
      content.setRowIndex(equipmentTypeText, 1);
      content.setColumnIndex(equipmentTypeText, 1);
      content.setRowIndex(isCleanCheckBox, 2);
      content.setColumnIndex(isCleanCheckBox, 1);
      content.setRowIndex(currentLocationText, 3);
      content.setColumnIndex(currentLocationText, 1);
      content.setRowIndex(isAvailableCheckBox, 4);
      content.setColumnIndex(isAvailableCheckBox, 1);
      content.setRowIndex(updateButton, 5);
      content.setColumnIndex(updateButton, 1);

      content
          .getChildren()
          .addAll(
              equipmentIDLabel,
              equipmentTypeLabel,
              isCleanLabel,
              currentLocationLabel,
              isAvailableLabel,
              cancelUpdateButton,
              equipmentIDText,
              equipmentTypeText,
              isCleanCheckBox,
              currentLocationText,
              isAvailableCheckBox,
              updateButton);

      content.setPadding(new Insets(10, 5, 10, 5));
      content.setBackground(
          new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
      content.setEffect(new DropShadow());

      p = new Popup();
      p.getContent().add(content);

      DataViewCtrl.addPopup.set(p);
      DataViewCtrl.addPopup.get().show(App.getStage());
    }
  }*/

  @FXML
  public void initialize() throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException {

    configure();
/*
    double previousTextSize = previousButton.getFont().getSize();
    double nextTextSize = nextButton.getFont().getSize();
    double previous1TextSize = previous1Button.getFont().getSize();
    double next1TextSize = next1Button.getFont().getSize();
    double previous2TextSize = previous2Button.getFont().getSize();
    double next2TextSize = next2Button.getFont().getSize();
    double previous3TextSize = previous3Button.getFont().getSize();
    double next3TextSize = next3Button.getFont().getSize();
    double bubble1TextSize = bubble1Text.getFont().getSize();
    double bubble2TextSize = bubble2Text.getFont().getSize();
    double bubble3TextSize = bubble3Text.getFont().getSize();
    double bubble4TextSize = bubble4Text.getFont().getSize(); */

    selectEmployeeBox
            .getSelectionModel()
            .selectedItemProperty()
            .addListener(
                    (obs, oldValue, newValue) -> {
                      if (HomeCtrl.sceneFlag == 4) {
                        try {
                          employeeDataviewManager.filterEmployees(newValue.toString());
                        } catch (IOException | ParseException e) {
                          e.printStackTrace();
                        }
                      } else if(HomeCtrl.sceneFlag == 1){
                        try {
                          srDataviewManager.filterSRs(newValue.toString());
                        } catch (SQLException | IllegalAccessException | InvocationTargetException | IOException e) {
                          e.printStackTrace();
                        } catch (ParseException e) {
                          e.printStackTrace();
                        }
                      } /*
                      previousButton.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previousTextSize)
                                      + "pt;");
                      nextButton.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * nextTextSize)
                                      + "pt;");
                      previous1Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previous1TextSize)
                                      + "pt;");
                      next1Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * next1TextSize)
                                      + "pt;");
                      previous2Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previous2TextSize)
                                      + "pt;");
                      next2Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * next2TextSize)
                                      + "pt;");
                      previous3Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * previous3TextSize)
                                      + "pt;");
                      next3Button.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * next3TextSize)
                                      + "pt;");
                      bubble1Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble1TextSize)
                                      + "pt;");
                      bubble2Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble2TextSize)
                                      + "pt;");
                      bubble3Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble3TextSize)
                                      + "pt;");
                      bubble4Text.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * bubble4TextSize)
                                      + "pt;");

               */     });
    if (HomeCtrl.sceneFlag == 1) {
      titleLabel.setText("Service Requests");
      srDataviewManager.initializeRequestsTable();
    } else if (HomeCtrl.sceneFlag == 2) {
      titleLabel.setText("Locations");
      locationDataviewManager.initializeLocationTable();
    } else if (HomeCtrl.sceneFlag == 3) {
      titleLabel.setText("Equipment");
      equipmentDataviewManager.initializeEquipmentTable();
    } else if (HomeCtrl.sceneFlag == 4) {
      titleLabel.setText("Employees");
      employeeDataviewManager.initializeEmployeeTable();
    } else if(HomeCtrl.sceneFlag == 5) {
      titleLabel.setText("MEDICINE");
      medicineDataviewManager.initializeMedicineTable();
    }
    else {
      // wait what how did you get here
    }
    dvSelectionManager = new DVSelectionManager(inputVBox, this);
  }

  @FXML
  private void createDetailsPopup() throws InvocationTargetException, IllegalAccessException {
    DataViewCtrl.detailsPopup.get().hide();

    detailLabel = new StringBuilder("Nothing selected  ");

    if (HomeCtrl.sceneFlag == 1 && table.getSelectionModel().getSelectedIndex() > -1) {
      detailLabel = srDataviewManager.details(detailLabel);
    }

    if (HomeCtrl.sceneFlag != 1) {
      this.detailLabel = new StringBuilder("No further details  ");
    }

    var content = new StackPane(new Label(this.detailLabel.toString()));
    content.setPadding(new Insets(10, 5, 10, 5));
    content.setBackground(
        new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
    content.setEffect(new DropShadow());

    var p = new Popup();
    p.getContent().add(content);

    DataViewCtrl.detailsPopup.set(p);
  }

  @FXML
  private void createModifyPopup() throws InvocationTargetException, IllegalAccessException {
    DataViewCtrl.modifyPopup.get().hide();

    // cancel button
    JFXButton cancelUpdateButton = new JFXButton();
    cancelUpdateButton.setText("X");

    cancelUpdateButton.setOnAction(
        event -> {
          DataViewCtrl.modifyPopup.get().hide();
        });

    // combobox field
    JFXComboBox<String> field = new JFXComboBox<>();
    field.setValue("Select Field");
    field.setMinSize(50, 30);
    field.setMaxSize(150, 30);

    // value text area
    TextArea value = new TextArea();
    value.setPromptText("Enter value:");
    value.setMinSize(50, 30);
    value.setMaxSize(150, 30);

    JFXButton updateButton = new JFXButton();
    updateButton.setText("Update");

    // fill out the combobox
    switch (HomeCtrl.sceneFlag) {
      case 1:
        srDataviewManager.modifyPopup(field, value, updateButton);
        break;
      case 2:
        locationDataviewManager.modifyPopup(field, value, updateButton);
        break;
      case 3:
        equipmentDataviewManager.modifyPopup(field, value, updateButton);
        break;
      case 4:
        employeeDataviewManager.modifyPopup(field, value, updateButton);
        break;
      case 5:
        medicineDataviewManager.modifyPopup(field, value, updateButton);
        break;
    }

    // add it to the scene
    var content = new GridPane();
    content.setRowIndex(cancelUpdateButton, 0);
    content.setColumnIndex(cancelUpdateButton, 0);
    content.setRowIndex(field, 0);
    content.setColumnIndex(field, 1);
    content.setRowIndex(value, 0);
    content.setColumnIndex(value, 2);
    content.setRowIndex(updateButton, 0);
    content.setColumnIndex(updateButton, 3);
    content.getChildren().addAll(cancelUpdateButton, field, value, updateButton);

    content.setPadding(new Insets(10, 5, 10, 5));
    content.setBackground(
        new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), null)));
    content.setEffect(new DropShadow());

    var p = new Popup();
    p.getContent().add(content);

    DataViewCtrl.modifyPopup.set(p);
  }

  public void setupViewDetailsAndModify() {

    this.rightClickMenu.getItems().addAll(this.viewDetails);
    this.rightClickMenu.getItems().addAll(this.modify);

    this.viewDetails.setOnAction(
        e -> {
          this.rightClickMenu.hide();
          try {
            this.createDetailsPopup();
          } catch (InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
          }
          DataViewCtrl.detailsPopup
              .get()
              .show(App.getStage(), this.point.get().getX(), this.point.get().getY());
        });
    this.modify.setOnAction(
        e -> {
          this.rightClickMenu.hide();
          try {
            this.createModifyPopup();
          } catch (InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
          }
          DataViewCtrl.modifyPopup
              .get()
              .show(App.getStage(), this.point.get().getX(), this.point.get().getY());
        });

    table.setOnMouseClicked(
        e -> {
          if (e.getButton() == MouseButton.PRIMARY) {
            DataViewCtrl.detailsPopup.get().hide();
          }
          if (e.getButton() == MouseButton.SECONDARY) {
            this.point.set(new Point2D(e.getScreenX(), e.getScreenY()));
            this.rightClickMenu.show(this.table, e.getScreenX(), e.getScreenY());
          }
        });
    table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
      dvSelectionManager.select(newValue.getValue(), HomeCtrl.sceneFlag);
    });
  }

  @FXML
  void edit() {}

  @FXML
  void clear() {}

  @FXML
  void addData() {}

  protected void onSceneSwitch() {
    DataViewCtrl.detailsPopup.get().hide();
    DataViewCtrl.modifyPopup.get().hide();
    p.hide();
  }

  public JFXComboBox getSelectEmployeeBox() {
    return selectEmployeeBox;
  }

  public JFXTreeTableView<RecursiveObj> getTable() {
    return table;
  }

  public void activateBumble(){
    helpButton.setVisible(false);
    bumbleXButton.setVisible(true);
    bumbleHead.setVisible(true);
    bubbleText.setVisible(true);
    nextButton.setVisible(true);
  }

  public void terminateBumble(){
    helpButton.setVisible(true);
    bumbleXButton.setVisible(false);
    bumbleHead.setVisible(false);
    bubbleText.setVisible(false);
    bubble1Text.setVisible(false);
    bubble2Text.setVisible(false);
    bubble3Text.setVisible(false);
    bubble4Text.setVisible(false);
    previousButton.setVisible(false);
    nextButton.setVisible(false);
    previous1Button.setVisible(false);
    next1Button.setVisible(false);
    previous2Button.setVisible(false);
    next2Button.setVisible(false);
    previous3Button.setVisible(false);
    next3Button.setVisible(false);
  }

  public void next(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previousButton.setVisible(true);
    nextButton.setVisible(false);
    next1Button.setVisible(true);
    bubbleText.setVisible(false);
    bubble1Text.setVisible(true);
  }

  public void previous(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previousButton.setVisible(false);
    nextButton.setVisible(true);
    next1Button.setVisible(false);
    bubbleText.setVisible(true);
    bubble1Text.setVisible(false);
  }

  public void next1(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(true);
    next1Button.setVisible(false);
    next2Button.setVisible(true);
    bubble1Text.setVisible(false);
    bubble2Text.setVisible(true);
  }

  public void previous1() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(false);
    next1Button.setVisible(true);
    next2Button.setVisible(false);
    bubble1Text.setVisible(true);
    bubble2Text.setVisible(false);
  }

  public void next2(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(false);
    previous2Button.setVisible(true);
    next2Button.setVisible(false);
    next3Button.setVisible(true);
    bubble2Text.setVisible(false);
    bubble3Text.setVisible(true);
  }

  public void previous2() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous1Button.setVisible(true);
    previous2Button.setVisible(false);
    next2Button.setVisible(true);
    next3Button.setVisible(false);
    bubble2Text.setVisible(true);
    bubble3Text.setVisible(false);
  }

  public void next3(){
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous2Button.setVisible(false);
    previous3Button.setVisible(true);
    next3Button.setVisible(false);
    bubble3Text.setVisible(false);
    bubble4Text.setVisible(true);
  }

  public void previous3() {
    PauseTransition pt = new PauseTransition(Duration.millis(100));
    bumbleBlinkHead.setVisible(true);
    pt.setOnFinished(e -> bumbleBlinkHead.setVisible(false));
    pt.play();
    previous2Button.setVisible(true);
    previous3Button.setVisible(false);
    next3Button.setVisible(true);
    bubble3Text.setVisible(true);
    bubble4Text.setVisible(false);
  }
}
