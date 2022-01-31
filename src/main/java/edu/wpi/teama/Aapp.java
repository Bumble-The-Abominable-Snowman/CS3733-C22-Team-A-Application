package edu.wpi.teama;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Aapp extends Application {

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    primaryStage.setTitle("Home");
    System.out.println(getClass());
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = getClass().getResource("views/home.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();

    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
