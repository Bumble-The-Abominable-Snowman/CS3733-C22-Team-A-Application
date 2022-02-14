package edu.wpi.cs3733.c22.teamA;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  private static Stage guiStage;
  public static SceneSwitcher sceneSwitcher;

  public static ConnectionFactory factory = new ConnectionFactory();
  public static Connection connection;

  public static Stage getStage() {
    return guiStage;
  }

  @Override
  public void init() {
    log.info("Starting Up");

    factory.setHost("198.199.83.208");
    factory.setPort(5672); // 5672 for regular connections, 5671 for connections that use TLS
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    guiStage = primaryStage;
    sceneSwitcher = new SceneSwitcher();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
