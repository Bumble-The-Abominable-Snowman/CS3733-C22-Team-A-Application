package edu.wpi.cs3733.c22.teamA;

import java.io.IOException;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.auth0.AuthUser;
import edu.wpi.cs3733.c22.teamA.auth0.Auth0Login;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

@Slf4j
public class App extends Application {

  private static Stage guiStage;
  public static SceneSwitcher sceneSwitcher;

  public static AuthUser authUser = new AuthUser();

  private boolean db_setup = false;
  private final Semaphore semaphoreHomeScene = new Semaphore(1);

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
    try {
      //this.handleLogin();
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
    } catch (Exception e) {
      System.out.println("LOGIN ERROR!");
    }
  }

  private void handleLogin()
  {
    Auth0Login.login().thenApply(u -> {
      try {
        authUser = u;

        semaphoreHomeScene.acquire();
        this.setupEmbeddedDB(authUser);
        this.handleLoginSwitch();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return u;
    });

  }

  private void setupEmbeddedDB(AuthUser user)
  {
    Task<Integer> task = new Task<>() {
      @Override protected Integer call() throws Exception {
        Adb.username = "admin";
        Adb.password = "admin";
        Adb.initialConnection("EmbeddedDriver");

        semaphoreHomeScene.release();
        db_setup = true;
        return 1;
      }
    };

    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }

  private void handleLoginSwitch()
  {
    Task<Integer> task = new Task<>() {
      @Override protected Integer call() throws Exception {
        Thread.sleep(1000);
        semaphoreHomeScene.acquire();
        Platform.runLater(() -> {
          try {
            sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
            guiStage.setMaximized(true);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
        db_setup = true;
        return 1;
      }
    };

    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }
    @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
