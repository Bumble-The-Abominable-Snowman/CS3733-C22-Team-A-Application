package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryServiceRequest;
import java.sql.*;
import java.util.List;

public interface FoodDeliveryServiceRequestDAO {

  FoodDeliveryServiceRequest getFoodDeliveryServiceRequest(String ID);

  void updateFoodDeliveryServiceRequest(String ID, String field, Object change);

  void enterFoodDeliveryServiceRequest(FoodDeliveryServiceRequest fdsr);

  void enterFoodDeliveryServiceRequest(
      String requestID,
      String startLocation,
      String endLocation,
      String employeeRequested,
      String employeeAssigned,
      Timestamp requestTime,
      String requestStatus,
      String requestType,
      String comments,
      String mainDish,
      String sideDish,
      String beverage,
      String dessert);

  void deleteFoodDeliveryServiceRequest(String id);

  List<FoodDeliveryServiceRequest> getFoodDeliveryServiceRequestList();
}
