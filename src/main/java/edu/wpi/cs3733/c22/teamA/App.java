package edu.wpi.cs3733.c22.teamA;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Clock;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.jwt.JWT;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.auth0.UserInfo;
import edu.wpi.cs3733.c22.teamA.auth0.rest.login.Auth0Login;
import edu.wpi.cs3733.c22.teamA.auth0.rest.login.Auth0PKCEFlow;
import edu.wpi.cs3733.c22.teamA.auth0.rest.login.JWTUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

@Slf4j
public class App extends Application {

  private static Stage guiStage;
  public static SceneSwitcher sceneSwitcher;
  public static UserInfo user = null;

  private boolean db_setup = false;
  private Semaphore semaphoreHomeScene = new Semaphore(1);

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
      this.handleLogin();
    } catch (Exception e) {
      System.out.println("LOGIN ERROR!");
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

  private void handleLogin()
  {
    Auth0Login.login().thenApply(userInfo -> {
      userInfo.ifPresent(info -> user = info);
      try {
        semaphoreHomeScene.acquire();
        this.setupEmbeddedDB(user);
        this.handleLoginSwitch();

        System.out.println((new ObjectMapper()).writeValueAsString(user));
        JWTUtils.verifyToken(user.getJwtToken()).map(jwt -> {
          for (String key: jwt.getClaims().keySet()) {
            System.out.printf("openid Key: %s\tValue: %s\n", key,jwt.getClaim(key).asString());
          }
          return 1;
        });




        AuthAPI authAPI = new AuthAPI(Auth0PKCEFlow.DOMAIN, Auth0PKCEFlow.CLIENT_ID, Auth0PKCEFlow.CLIENT_SECRET);
        AuthRequest authRequest = authAPI.requestToken("https://" + Auth0PKCEFlow.DOMAIN + "/api/v2/");
        TokenHolder holder = authRequest.execute();
        ManagementAPI mgmt = new ManagementAPI(Auth0PKCEFlow.DOMAIN, holder.getAccessToken());

        JWTUtils.verifyToken(user.getJwtToken()).map(jwt -> {

          UserFilter filter = new UserFilter();
          System.out.println(jwt.getClaim("sub").asString());
          Request<User> request = mgmt.users().get(jwt.getClaim("sub").asString(), filter);
          try {
            User response = request.execute();
            for (String key: response.getAppMetadata().keySet()) {
              System.out.printf("metadata Key: %s\tValue: %s\n", key, response.getAppMetadata().get(key));
            }
          } catch (APIException exception) {
            // api error
          } catch (Auth0Exception exception) {
            // request error
          }
          return 1;
        });

      } catch (Exception e) {
        e.printStackTrace();
      }
      return userInfo;
    });

  }

  private void setupEmbeddedDB(UserInfo user)
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
