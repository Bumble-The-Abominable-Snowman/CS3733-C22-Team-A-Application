package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryServiceRequest;
import java.util.List;

public interface FoodDeliveryServiceRequestDAO {

  public List<FoodDeliveryServiceRequest> getNodeList();

  public void deleteRequest(String requestID);

  public void enterRequest(
      String requestID,
      String mainDish,
      String sideDish,
      String beverage,
      String dessert,
      String roomNum,
      String employee,
      String comments);

  public void updateRequest(String ID, String field, Object change);

  public FoodDeliveryServiceRequest getRequest(String ID);
}
