package edu.wpi.cs3733.c22.teamA.controllers.servicerequest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import edu.wpi.cs3733.c22.teamA.Aapp;
import edu.wpi.cs3733.c22.teamA.controllers.SceneController;
import edu.wpi.cs3733.c22.teamA.entities.requests.LaundryServiceRequest;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import org.apache.commons.lang3.SerializationUtils;

public class LaundryServiceRequestController extends GenericServiceRequestsController {
  @FXML private Button floor4Button;
  @FXML private Button floor3Button;
  @FXML private Button floor2Button;
  @FXML private Button floor1Button;

  @FXML private Label locationLabel;
  @FXML private TextField specialNotes;
  @FXML private ChoiceBox<String> washMode = new ChoiceBox<>();
  @FXML private Button homeButton = new Button();
  @FXML private Button submitButton;
  @FXML private Button backButton;
  @FXML private Button clearButton;

  @FXML
  public void initialize() {
    sceneID = SceneController.SCENES.LAUNDRY_SERVICE_REQUEST_SCENE;

    washMode.getItems().removeAll(washMode.getItems());
    washMode.getItems().addAll("Colors", "Whites", "Perm. press", "Save the trees!");
    washMode.setValue("Wash Mode");
  }

  @FXML
  void submitRequest() throws IOException, TimeoutException {
    System.out.print("\nNew request, got some work to do bud!\n");
    System.out.printf("Selected wash mode is : %s\n", washMode.getValue());
    System.out.printf(
        "Added this note : \n[NOTE START]\n%s\n[NOTE END]\n", specialNotes.getCharacters());
    if (!washMode.getValue().equals("Wash Mode")) {
      LaundryServiceRequest laundryServiceRequest = new LaundryServiceRequest();
      laundryServiceRequest.setWashMode(washMode.getValue());
      laundryServiceRequest.setSpecialInstructions(specialNotes.getCharacters().toString());

      // serialize the entity
      byte[] data = SerializationUtils.serialize(laundryServiceRequest);

      // send request to database
      try (Connection connection = Aapp.factory.newConnection();
          Channel channel = connection.createChannel()) {
        channel.exchangeDeclare("service_requests/laundry", "topic");

        channel.basicPublish("service_requests/laundry", "com.cs377.c22.teamA", null, data);
      }
    }
  }

  @FXML
  public void chooseFloor(ActionEvent actionEvent) {
    locationLabel.setText(((Button) actionEvent.getSource()).getText());
    locationLabel.setAlignment(Pos.CENTER);
  }
}
