package edu.wpi.teama.controllers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MessagingDemoController {

  @FXML private Label messageLabel;
  @FXML private TextField specialNotes;
  @FXML private Button homeButton = new Button();
  @FXML private Button submitButton;
  @FXML private Button backButton;

  private String incomingMessage;

  ConnectionFactory factory = new ConnectionFactory();
  private static final String QUEUE_NAME = "demo";

  @FXML
  public void initialize() throws IOException, TimeoutException {
    factory.setUsername("yidikut");
    factory.setPassword("anan");
    factory.setHost("198.199.83.208");

    Connection connection = factory.newConnection("app:audit component:event-consumer");
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Task task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            while (true) {
              Platform.runLater(
                  new Runnable() {
                    @Override
                    public void run() {
                      messageLabel.setText("incomingMessage: " + incomingMessage);
                    }
                  });

              TimeUnit.MILLISECONDS.sleep(100);
            }
          }
        };
    new Thread(task).start();

    DeliverCallback deliverCallback =
        (consumerTag, delivery) -> {
          incomingMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
          System.out.println(" [x] Received '" + incomingMessage + "'");
        };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
  }

  @FXML
  private void returnToHomeScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource("views/home.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) homeButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Home");
    window.show();
  }

  @FXML
  private void returnToSelectServiceScene() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = Aapp.class.getResource("views/selectServiceRequest.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    Stage window = (Stage) backButton.getScene().getWindow();
    window.setScene(new Scene(root));
    window.setTitle("Select Service Request");
    window.show();
  }

  @FXML
  private void submitRequest() throws IOException, TimeoutException {
    System.out.print("\nNew request, got some work to do bud!\n");
    System.out.printf(
        "Added this note : \n[NOTE START]\n%s\n[NOTE END]\n", specialNotes.getCharacters());

    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      String message = specialNotes.getCharacters().toString();
      channel.basicPublish(
          "",
          QUEUE_NAME,
          null,
          specialNotes.getCharacters().toString().getBytes(StandardCharsets.UTF_8));
    }
  }
}
