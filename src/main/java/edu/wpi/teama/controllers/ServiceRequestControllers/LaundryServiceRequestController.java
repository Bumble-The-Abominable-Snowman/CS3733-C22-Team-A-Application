package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.controllers.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;

public class LaundryServiceRequestController extends GenericServiceRequestsController {
  @FXML private Button floor4Button;
  @FXML private Button floor3Button;
  @FXML private Button floor2Button;
  @FXML private Button floor1Button;

  @FXML private Label locationLabel;
  @FXML private TextField specialNotes;
  @FXML private ChoiceBox washMode = new ChoiceBox();
  @FXML private Button homeButton = new Button();
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private Button clearButton;

  private FXMLLoader loader = new FXMLLoader();

  @FXML
  public void initialize() {
    sceneID = SceneController.SCENES.LAUNDRY_SERVICE_REQUEST_SCENE;

    washMode.getItems().removeAll(washMode.getItems());
    washMode.getItems().addAll("Colors", "Whites", "Perm. press", "Save the trees!");
    washMode.setValue("Wash Mode");
  }

  @FXML
  private void submitRequest() {
    System.out.print("\nNew request, got some work to do bud!\n");
    System.out.printf("Selected wash mode is : %s\n", washMode.getValue());
    System.out.printf(
        "Added this note : \n[NOTE START]\n%s\n[NOTE END]\n", specialNotes.getCharacters());
    // send request to database
  }

  @FXML
  public void chooseFloor(ActionEvent actionEvent) {
    locationLabel.setText(((Button) actionEvent.getSource()).getText());
    locationLabel.setAlignment(Pos.CENTER);
  }
}