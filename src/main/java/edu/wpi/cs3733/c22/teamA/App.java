package edu.wpi.cs3733.c22.teamA;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.auth0.json.mgmt.users.User;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.auth0.UserInfo;
import edu.wpi.cs3733.c22.teamA.auth0.rest.login.Auth0Login;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class App extends Application {

  private static Stage guiStage;
  public static SceneSwitcher sceneSwitcher;
  public static UserInfo user = null;

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
    try
    {
      Auth0Login.login().thenApply(userInfo -> {
        userInfo.ifPresent(info -> user = info);

        try {
          Adb.initialConnection("EmbeddedDriver");
          sceneSwitcher.switchScene(SceneSwitcher.SCENES.HOME);
        } catch (IOException | SQLException e) {
          e.printStackTrace();
        }
        return userInfo;
      });
//      Auth0Login.login();
//      CompletableFuture<Optional<UserInfo>> optionalCompletableFuture = Auth0Login.login();
//      optionalCompletableFuture.get().ifPresent(userInfo -> user = userInfo);

    } catch (Exception e) {
      System.out.println("LOGIN ERROR: COULDN'T GET USER!");
    }
//    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
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
