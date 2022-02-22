package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.App;
import java.net.URL;
import javafx.animation.Interpolator;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.kurobako.gesturefx.AffineEvent;
import net.kurobako.gesturefx.GesturePane;

public class GesturePaneManager {

  private GesturePane gesturePane;
  private AnchorPane anchorPane;
  private ImageView mapImageView;
  private String currentFloor;
  private Dimension2D transformed;

  public GesturePaneManager(
      GesturePane gesturePane, AnchorPane anchorPane, ImageView mapImageView) {
    this.gesturePane = gesturePane;
    this.anchorPane = anchorPane;
    this.mapImageView = mapImageView;
    transformed = new Dimension2D(1000, 1000); // TODO Fix initial dimension
  }

  public void initGesture() {
    anchorPane.getChildren().add(mapImageView);
    anchorPane.setLayoutX(0);
    anchorPane.setLayoutY(0);
    //    anchorPane.setPrefHeight(gesturePane.getHeight());
    //    anchorPane.setPrefWidth(gesturePane.getWidth());
    mapImageView.setStyle(
        " -fx-border-color: black;\n"
            + "    -fx-border-style: solid;\n"
            + "    -fx-border-width: 5;");
    mapImageView.setPreserveRatio(true);
    // mapImageView.setLayoutX(layoutStart);
    mapImageView.setLayoutY(0);
    gesturePane.setMinScale(0.75f);
    gesturePane.reset();
    gesturePane.setContent(anchorPane);
    gesturePane.addEventFilter(
        AffineEvent.CHANGED,
        event -> {
          transformed = event.getTransformedDimension();
        });
    gesturePane.setOnMouseClicked(
        e -> {
          if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            Point2D pivotOnTarget =
                gesturePane
                    .targetPointAt(new Point2D(e.getX(), e.getY()))
                    .orElse(gesturePane.targetPointAtViewportCentre());
            // increment of scale makes more sense exponentially instead of linearly
            gesturePane
                .animate(Duration.millis(200))
                .interpolateWith(Interpolator.EASE_BOTH)
                .zoomBy(gesturePane.getCurrentScale(), pivotOnTarget);
          }
        });
  }

  public void reset() {
    gesturePane.reset();
  }

  public void setMapFloor(String floor) {
    URL url;
    switch (floor) {
      case "Choose Floor:":
        currentFloor = "";
        url = App.class.getResource("images/Side View.png");
        break;
      case "Floor 1":
        currentFloor = "1";
        url = App.class.getResource("images/1st Floor.png");
        break;
      case "Floor 2":
        currentFloor = "2";
        url = App.class.getResource("images/2nd Floor.png");
        break;
      case "Floor 3":
        currentFloor = "3";
        url = App.class.getResource("images/3rd Floor.png");
        break;
      case "L1":
        currentFloor = "L1";
        url = App.class.getResource("images/LL1.png");
        break;
      case "L2":
        currentFloor = "L2";
        url = App.class.getResource("images/LL2.png");
        break;
      default:
        currentFloor = "";
        url = App.class.getResource("images/Side View.png");
        break;
    }

    Image image = new Image(String.valueOf(url));
    mapImageView.setImage(image);
  }

  public String getCurrentFloor() {
    return currentFloor;
  }

  public Dimension2D getTransformed() {
    return transformed;
  }

  public ImageView getMapImageView() {
    return mapImageView;
  }

  public AnchorPane getAnchorPane() {
    return anchorPane;
  }
}
