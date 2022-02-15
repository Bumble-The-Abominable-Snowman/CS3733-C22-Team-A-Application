package edu.wpi.cs3733.c22.teamA;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Screen;
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
    guiStage.setMaximized(true);
    guiStage.setMinHeight(600);
    guiStage.setMinWidth(960);
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();
    double aspectRatio = (screenWidth / screenHeight);
    guiStage.setMaxWidth(screenWidth);
    guiStage.setMaxHeight(screenHeight);
    guiStage.minHeightProperty().bind(guiStage.widthProperty().divide(aspectRatio));
    guiStage.maxHeightProperty().bind(guiStage.widthProperty().divide(aspectRatio));
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
