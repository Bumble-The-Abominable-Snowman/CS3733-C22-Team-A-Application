package edu.wpi.teama.controllers;

import com.rabbitmq.client.AuthenticationFailureException;
import edu.wpi.teama.Aapp;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LogInController {
  @FXML private Text welcomeBox;
  @FXML private TextField usernameBox;
  @FXML private PasswordField passwordBox;
  @FXML private Button loginButton;
  @FXML private Button exitButton;

  private String incomingMessage;

  private final SceneController sceneController = Aapp.sceneController;

  @FXML
  private void logIn(ActionEvent actionEvent) throws IOException, TimeoutException {
    Aapp.factory.setUsername(usernameBox.getText());
    Aapp.factory.setPassword(passwordBox.getText());

    try {
      Aapp.connection = Aapp.factory.newConnection("app:audit component:event-consumer");

      welcomeBox.setText("Success!");
      welcomeBox.setFill(Color.GREEN);

      sceneController.switchScene(SceneController.SCENES.HOME_SCENE);
    } catch (AuthenticationFailureException exception) {

      welcomeBox.setText("Unsuccesful login!");
      welcomeBox.setFill(Color.RED);

      usernameBox.setText("");
      passwordBox.setText("");
    }
  }

  @FXML
  private void exitApp(ActionEvent actionEvent) {
    System.exit(0);
  }

  @FXML
  public void initialize() throws IOException, TimeoutException {

    //    Task task =
    //            new Task<Void>() {
    //              @Override
    //              protected Void call() throws Exception {
    //                while (true) {
    //                  Platform.runLater(
    //                          new Runnable() {
    //                            @Override
    //                            public void run() {
    //                              messageLabel.setText("incomingMessage: " + incomingMessage);
    //                            }
    //                          });
    //
    //                  TimeUnit.MILLISECONDS.sleep(100);
    //                }
    //              }
    //            };
    //    new Thread(task).start();
    //
    //    DeliverCallback deliverCallback =
    //            (consumerTag, delivery) -> {
    //              incomingMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
    //              System.out.println(" [x] Received '" + incomingMessage + "'");
    //            };
    //    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
  }

  //  @FXML
  //  private void submitRequest() throws IOException, TimeoutException {
  //    System.out.print("\nNew request, got some work to do bud!\n");
  //    System.out.printf(
  //            "Added this note : \n[NOTE START]\n%s\n[NOTE END]\n", specialNotes.getCharacters());
  //
  //    try (Connection connection = factory.newConnection();
  //         Channel channel = connection.createChannel()) {
  //      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
  //      String message = specialNotes.getCharacters().toString();
  //      channel.basicPublish(
  //              "",
  //              QUEUE_NAME,
  //              null,
  //              specialNotes.getCharacters().toString().getBytes(StandardCharsets.UTF_8));
  //    }
  //  }
}
