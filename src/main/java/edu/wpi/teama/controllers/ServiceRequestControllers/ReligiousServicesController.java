package edu.wpi.teama.controllers.ServiceRequestControllers;

import edu.wpi.teama.controllers.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class ReligiousServicesController extends GenericServiceRequestsController {
  @FXML private TextArea specialNotes = new TextArea();
  @FXML private ChoiceBox religionChoiceBox = new ChoiceBox();
  @FXML private ChoiceBox toChoiceBox = new ChoiceBox();
  @FXML private ChoiceBox employeeChoiceBox = new ChoiceBox();
  @FXML private ChoiceBox fromChoiceBox = new ChoiceBox();
  @FXML private Label locationLabel = new Label();
  @FXML private Button homeButton = new Button();
  @FXML private Button backButton = new Button();
  @FXML private Button clearButton;
  @FXML private Button submitButton = new Button();


  private List<String> christianDenom = new ArrayList<>();
  private List<String> nonDenom = new ArrayList<>();
  private List<String> otherDenom = new ArrayList<>();

  private FXMLLoader loader = new FXMLLoader();

  public ReligiousServicesController() {
    super();

    christianDenom.add("Catholic");
    christianDenom.add("Baptist");
    christianDenom.add("Unspec. Protestant");
    christianDenom.add("Episcopal");
    christianDenom.add("UCOC");
    christianDenom.add("Methodist");
    christianDenom.add("Other");

    nonDenom.add("Atheist");
    nonDenom.add("Agnostic");
    nonDenom.add("Other");

    otherDenom.add("Jewish");
    otherDenom.add("Buddhist");
    otherDenom.add("Muslim");
    otherDenom.add("Hindu");
    otherDenom.add("Other");
  }

  private void initialize() {
    sceneID = SceneController.SCENES.RELIGIOUS_SERVICE_REQUEST_SCENE;
    specialNotes.setWrapText(true);


  }

  @FXML
  void submitRequest() {
    // send request to database
  }
}
