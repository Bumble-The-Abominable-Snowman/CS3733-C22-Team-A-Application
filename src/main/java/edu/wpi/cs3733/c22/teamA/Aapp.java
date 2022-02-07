package edu.wpi.cs3733.c22.teamA;

import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Aapp extends Application {

  private static Stage guiStage;
  public static SceneController sceneController;

  public static ConnectionFactory factory = new ConnectionFactory();
  public static Connection connection;

  public static Stage getStage() {
    return guiStage;
  }

  @Override
  public void init() throws IOException {
    log.info("Starting Up");

    Aapp.factory.setHost("198.199.83.208");
    Aapp.factory.setPort(5672); // 5672 for regular connections, 5671 for connections that use TLS
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    guiStage = primaryStage;
    sceneController = new SceneController();
    sceneController.switchScene(SceneController.SCENES.LOG_IN_SCENE);
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
