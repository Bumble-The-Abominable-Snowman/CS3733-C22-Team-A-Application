package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.requests.LaundryServiceRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;

public class LaundryServiceRequestController extends GenericServiceRequestsController {

  @FXML private Label locationLabel;
  @FXML private TextArea commentsBox;
  @FXML private JFXComboBox<String> toLocationChoice;
  @FXML private JFXComboBox<String> washMode;
  @FXML private JFXComboBox<String> employeeChoice;
  @FXML private JFXButton returnHomeButton;
  @FXML private JFXButton submitButton;
  @FXML private JFXButton backButton;
  @FXML private JFXButton clearButton;

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() {
    sceneID = SceneController.SCENES.LAUNDRY_SERVICE_REQUEST_SCENE;

    washMode.getItems().removeAll(washMode.getItems());
    washMode.getItems().addAll("Colors", "Whites", "Perm. press", "Save the trees!");
    washMode.setValue("Wash Mode");
  }

  @FXML
  void submitRequest() {
    System.out.print("\nNew request, got some work to do bud!\n");
    System.out.printf("Selected wash mode is : %s\n", washMode.getValue());
    System.out.printf("Added this note : \n[NOTE START]\n%s\n[NOTE END]\n", commentsBox.getText());
    if (!washMode.getValue().equals("Wash Mode")) {
      LaundryServiceRequest laundryServiceRequest = new LaundryServiceRequest();
      laundryServiceRequest.setWashMode(washMode.getValue());
      laundryServiceRequest.setSpecialInstructions(commentsBox.getText());

      // send request to database
    }
  }

  @FXML
  public void chooseFloor(ActionEvent actionEvent) {
    locationLabel.setText(((Button) actionEvent.getSource()).getText());
    locationLabel.setAlignment(Pos.CENTER);
  }
}
