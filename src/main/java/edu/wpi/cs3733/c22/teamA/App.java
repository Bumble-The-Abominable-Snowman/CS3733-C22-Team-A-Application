package edu.wpi.cs3733.c22.teamA;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  private static Stage guiStage;
  public static SceneSwitcher sceneSwitcher;

  public static Stage getStage() {

    return guiStage;
  }

  @Override
  public void init() {

    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    guiStage = primaryStage;
    sceneSwitcher = new SceneSwitcher();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
//    guiStage.setMaximized(true);
//    guiStage.setMinHeight(600);
//    guiStage.setMinWidth(960);
//    double screenWidth = Screen.getPrimary().getBounds().getWidth();
//    double screenHeight = Screen.getPrimary().getBounds().getHeight();
//    double aspectRatio = (screenWidth / screenHeight);
//    guiStage.setMaxWidth(screenWidth);
//    guiStage.setMaxHeight(screenHeight);
//    guiStage.minHeightProperty().bind(guiStage.widthProperty().divide(aspectRatio));
//    guiStage.maxHeightProperty().bind(guiStage.widthProperty().divide(aspectRatio));
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
