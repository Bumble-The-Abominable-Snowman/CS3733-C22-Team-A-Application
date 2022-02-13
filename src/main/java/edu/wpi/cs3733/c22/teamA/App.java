package edu.wpi.cs3733.c22.teamA;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.EquipmentSR;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  private static Stage guiStage;
  public static SceneSwitcher sceneSwitcher;

  public static ConnectionFactory factory = new ConnectionFactory();
  public static Connection connection;

  public static Stage getStage() {
    return guiStage;
  }

  @Override
  public void init()
      throws IOException, ParseException, InvocationTargetException, IllegalAccessException {
    log.info("Starting Up");

    factory.setHost("198.199.83.208");
    factory.setPort(5672); // 5672 for regular connections, 5671 for connections that use TLS

    ServiceRequestDerbyImpl<EquipmentSR> serviceRequestDAO =
        new ServiceRequestDerbyImpl<>(new EquipmentSR());
    //    EquipmentSR sr = serviceRequestDAO.getRequest("REQ1256");
    //    System.out.println(sr);

    //    sr.setComments("new employee!");
    //    sr.setEmployeeAssigned("EMP1");
    //    serviceRequestDAO.enterServiceRequest(sr);
    //    EquipmentSR equipmentSR = new EquipmentSR();
    //    equipmentSR.setRequestID("REQ1256");
    //    serviceRequestDAO.deleteServiceRequest(equipmentSR);
    //    equipmentSR.setRequestID("REQ12325");
    //    serviceRequestDAO.deleteServiceRequest(equipmentSR);

    serviceRequestDAO.readCSV(
        "/Users/yasaridikut/IdeaProjects/CS3733-C22-Team-A-Application/src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipmentServiceRequest.CSV");

    List<EquipmentSR> srList = serviceRequestDAO.getServiceRequestList();
    System.out.println(srList.size());
    for (EquipmentSR srItem : srList) {
      System.out.println(srItem);
    }
    //        System.out.println(srList);

    //    serviceRequestDAO.enterServiceRequest(new EquipmentSR());

    //    serviceRequestDAO.updateServiceRequest(sr);

  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    guiStage = primaryStage;
    sceneSwitcher = new SceneSwitcher();
    sceneSwitcher.switchScene(SceneSwitcher.SCENES.LOGIN);
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
