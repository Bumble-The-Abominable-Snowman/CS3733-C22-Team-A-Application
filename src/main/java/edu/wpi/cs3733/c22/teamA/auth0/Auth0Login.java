package edu.wpi.cs3733.c22.teamA.auth0;

import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.auth0.AuthUser;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jooq.lambda.Unchecked;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Auth0Login {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 550;

    public static final String DOMAIN = "dev-x7bjt62i.us.auth0.com";
    public static final String AUTH_URI = "https://cs3733c22teama.ddns.net/api/login";
    public static final String REDIRECT_URI = "https://cs3733c22teama.ddns.net/api/verification?token=";

    public static CompletableFuture<AuthUser> login() {
        final CompletableFuture<AuthUser> future = new CompletableFuture<>();

        Platform.runLater(() -> {
            final WebView webView = new WebView();
            final WebEngine engine = webView.getEngine();

            final Scene scene = new Scene(webView, WINDOW_WIDTH, WINDOW_HEIGHT);
            App.getStage().setScene(scene);
            App.getStage().show();

            engine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {

                    String url = engine.getLocation();
                    if (url.startsWith(REDIRECT_URI)) {
                        String token = url.substring(REDIRECT_URI.length());
                        App.authUser = new AuthUser(token);
                        future.complete(App.authUser);
                    }
                }
            });

            // API's auth page
            engine.load(AUTH_URI);
        });

        return future.whenComplete((user, ex) -> {if (ex != null) {Unchecked.throwChecked(ex);}});
    }

}
