package edu.wpi.cs3733.c22.teamA.Adb.servicerequest.fooddeliveryservicerequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryServiceRequest;
import java.util.List;
import java.sql.*;

public interface FoodDeliveryServiceRequestDAO {

  FoodDeliveryServiceRequest getFoodDeliveryRequest(String ID);

  void updateFoodDeliveryRequest(String ID, String field, Object change);

  void enterFoodDeliveryRequest(FoodDeliveryServiceRequest fdsr);

  void enterFoodDeliveryRequest(
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

  void deleteFoodDeliveryRequest(String id);

  List<FoodDeliveryServiceRequest> getFoodDeliveryRequestList();

}
