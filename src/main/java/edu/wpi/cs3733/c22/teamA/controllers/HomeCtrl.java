package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeWrapperImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Employee;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class HomeCtrl extends MasterCtrl {


    public Label greetingLabel;
    @FXML private Label homeTitle;
  @FXML private ImageView frame1;
  @FXML private ImageView frame2;
  @FXML private ImageView frame3;
  @FXML private ImageView frame4;
  @FXML private ImageView frame5;
  @FXML private JFXButton newSRHelpButton;
  @FXML private JFXButton mapHelpButton;
  @FXML private JFXButton viewDataHelpButton;
  @FXML private JFXButton settingsHelpButton;
  @FXML private Label newSRText;
  @FXML private Label mapText;
  @FXML private Label dataViewText;
  @FXML private Label settingsText;

  @FXML
  private void initialize() throws IOException, ParseException, TimeoutException {

      configure();
      drawer.open();
      drawer.toFront();
      menuBox.toFront();


      greetingLabel.setText("Welcome, " + App.authUser.getEmployeeName());
      if (App.DB_CHOICE.equals("nosql"))
      {
        String EXCHANGE_NAME = "push_api";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("cs3733c22teama.ddns.net");
        factory.setUsername("a");
        factory.setPassword("a");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "anonymous.info");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [x] Received '" +
                  delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");

          if (String.valueOf(SceneSwitcher.fxmlval.get(SceneSwitcher.fxmlval.size() - 1)).equals("DATA_VIEW"))
          {
            Platform.runLater(() -> {
              try {
                App.sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
          }
        };
        System.out.println("starting the rabbitmq daemon...");
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
      }

//    EmployeeDAO employeeBase = new EmployeeWrapperImpl();
//    List<Employee> empList = employeeBase.getEmployeeList();

    double newSRHelpTextSize = newSRHelpButton.getFont().getSize();
    double mapHelpTextSize = mapHelpButton.getFont().getSize();
    double viewDataHelpTextSize = viewDataHelpButton.getFont().getSize();
    double settingsHelpTextSize = settingsHelpButton.getFont().getSize();
    double newSRTextSize = newSRText.getFont().getSize();
    double mapTextSize = mapText.getFont().getSize();
    double dataViewTextSize = dataViewText.getFont().getSize();
    double settingsTextSize = settingsText.getFont().getSize();

    App.getStage()
            .widthProperty()
            .addListener(
                    (obs, oldVal, newVal) -> {
                      newSRHelpButton.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * newSRHelpTextSize)
                                      + "pt;");
                      mapHelpButton.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * mapHelpTextSize)
                                      + "pt;");
                      viewDataHelpButton.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * viewDataHelpTextSize)
                                      + "pt;");
                      settingsHelpButton.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * settingsHelpTextSize)
                                      + "pt;");
                      newSRText.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * newSRTextSize)
                                      + "pt;");
                      mapText.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * mapTextSize)
                                      + "pt;");
                      dataViewText.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * dataViewTextSize)
                                      + "pt;");
                      settingsText.setStyle(
                              "-fx-font-size: "
                                      + ((App.getStage().getWidth() / 1000) * settingsTextSize)
                                      + "pt;");
                    });

  }


  @FXML
  private void activateBumble() {
    boolean bumbleStop = false;
    int counter = 0;
    double dur = 0;
    giveHelp();
    helpButton.setVisible(false);
    bumbleXButton.setVisible(true);

    while(!bumbleStop) {
      counter++;
      dur = dur + 100;
      PauseTransition pt = new PauseTransition(Duration.millis(dur));
      frame1.setVisible(true);
      pt.setOnFinished(e -> frame1.setVisible(false));
      pt.play();

      PauseTransition pt1 = new PauseTransition(Duration.millis(dur));
      pt1.setOnFinished(e -> frame2.setVisible(true));
      pt1.play();

      dur = dur + 100;
      PauseTransition pt2 = new PauseTransition(Duration.millis(dur));
      pt2.setOnFinished(e -> frame2.setVisible(false));
      pt2.play();

      PauseTransition pt3 = new PauseTransition(Duration.millis(dur));
      pt3.setOnFinished(e -> frame3.setVisible(true));
      pt3.play();

      dur = dur + 100;
      PauseTransition pt4 = new PauseTransition(Duration.millis(dur));
      pt4.setOnFinished(e -> frame3.setVisible(false));
      pt4.play();

      PauseTransition pt5 = new PauseTransition(Duration.millis(dur));
      pt5.setOnFinished(e -> frame4.setVisible(true));
      pt5.play();

      dur = dur + 100;
      PauseTransition pt6 = new PauseTransition(Duration.millis(dur));
      pt6.setOnFinished(e -> frame4.setVisible(false));
      pt6.play();

      PauseTransition pt7 = new PauseTransition(Duration.millis(dur));
      pt7.setOnFinished(e -> frame5.setVisible(true));
      pt7.play();

      dur = dur + 100;
      PauseTransition pt8 = new PauseTransition(Duration.millis(dur));
      pt8.setOnFinished(e -> frame5.setVisible(false));
      pt8.play();

      PauseTransition pt9 = new PauseTransition(Duration.millis(dur));
      pt9.setOnFinished(e -> frame4.setVisible(true));
      pt9.play();

      dur = dur + 100;
      PauseTransition pt10 = new PauseTransition(Duration.millis(dur));
      pt10.setOnFinished(e -> frame4.setVisible(false));
      pt10.play();

      PauseTransition pt11 = new PauseTransition(Duration.millis(dur));
      pt11.setOnFinished(e -> frame3.setVisible(true));
      pt11.play();

      dur = dur + 100;
      PauseTransition pt12 = new PauseTransition(Duration.millis(dur));
      pt12.setOnFinished(e -> frame3.setVisible(false));
      pt12.play();

      PauseTransition pt13 = new PauseTransition(Duration.millis(dur));
      pt13.setOnFinished(e -> frame2.setVisible(true));
      pt13.play();

      dur = dur + 100;
      PauseTransition pt14 = new PauseTransition(Duration.millis(dur));
      pt14.setOnFinished(e -> frame2.setVisible(false));
      pt14.play();

      PauseTransition pt15 = new PauseTransition(Duration.millis(dur));
      pt15.setOnFinished(e -> frame1.setVisible(true));
      pt15.play();
      if (counter == 5000){
        bumbleStop = true;
      }
    }
  }

  @FXML private void giveHelp(){
    bubbleText.setVisible(true);
    newSRHelpButton.setVisible(true);
    mapHelpButton.setVisible(true);
    viewDataHelpButton.setVisible(true);
    settingsHelpButton.setVisible(true);
  }

  @FXML private void newSRHelp(){
    bubbleText.setVisible(false);
    newSRText.setVisible(true);
    mapText.setVisible(false);
    dataViewText.setVisible(false);
    settingsText.setVisible(false);

    borderGlow.setColor(Color.GOLD);
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    borderGlow.setHeight(45);
    selectSRButton.setEffect(borderGlow);

    transparentGlow.setColor(Color.TRANSPARENT);
    mapButton.setEffect(transparentGlow);
    viewEmployeesButton.setEffect(transparentGlow);
    viewLocationsButton.setEffect(transparentGlow);
    viewSRButton.setEffect(transparentGlow);
    viewMedicineButton.setEffect(transparentGlow);
    viewEquipmentButton.setEffect(transparentGlow);
    settingsButton.setEffect(transparentGlow);
  }

  @FXML private void mapHelp(){
    bubbleText.setVisible(false);
    newSRText.setVisible(false);
    mapText.setVisible(true);
    dataViewText.setVisible(false);
    settingsText.setVisible(false);

    borderGlow.setColor(Color.GOLD);
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    borderGlow.setHeight(45);
    mapButton.setEffect(borderGlow);

    transparentGlow.setColor(Color.TRANSPARENT);
    selectSRButton.setEffect(transparentGlow);
    viewEmployeesButton.setEffect(transparentGlow);
    viewLocationsButton.setEffect(transparentGlow);
    viewSRButton.setEffect(transparentGlow);
    viewMedicineButton.setEffect(transparentGlow);
    viewEquipmentButton.setEffect(transparentGlow);
    settingsButton.setEffect(transparentGlow);
  }

  @FXML private void dataViewHelp(){
    bubbleText.setVisible(false);
    newSRText.setVisible(false);
    mapText.setVisible(false);
    dataViewText.setVisible(true);
    settingsText.setVisible(false);

    borderGlow.setColor(Color.GOLD);
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    borderGlow.setHeight(45);
    viewEmployeesButton.setEffect(borderGlow);
    viewLocationsButton.setEffect(borderGlow);
    viewSRButton.setEffect(borderGlow);
    viewMedicineButton.setEffect(borderGlow);
    viewEquipmentButton.setEffect(borderGlow);

    transparentGlow.setColor(Color.TRANSPARENT);
    selectSRButton.setEffect(transparentGlow);
    mapButton.setEffect(transparentGlow);
    settingsButton.setEffect(transparentGlow);
  }

  @FXML private void settingsHelp(){
    bubbleText.setVisible(false);
    newSRText.setVisible(false);
    mapText.setVisible(false);
    dataViewText.setVisible(false);
    settingsText.setVisible(true);

    borderGlow.setColor(Color.GOLD);
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    borderGlow.setHeight(45);
    settingsButton.setEffect(borderGlow);

    transparentGlow.setColor(Color.TRANSPARENT);
    mapButton.setEffect(transparentGlow);
    selectSRButton.setEffect(transparentGlow);
    viewEmployeesButton.setEffect(transparentGlow);
    viewLocationsButton.setEffect(transparentGlow);
    viewSRButton.setEffect(transparentGlow);
    viewMedicineButton.setEffect(transparentGlow);
    viewEquipmentButton.setEffect(transparentGlow);
  }

}
