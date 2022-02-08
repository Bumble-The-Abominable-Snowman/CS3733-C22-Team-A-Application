package edu.wpi.cs3733.c22.teamA.Adb.fooddeliveryrequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryRequest;

import java.sql.*;
import java.util.List;

public interface FoodDeliveryRequestDAO {

    public List<FoodDeliveryRequest> getNodeList();

    public void deleteRequest(String requestID);

    public void enterRequest(
            String requestID, String mainDish, String sideDish, String beverage, String dessert, String roomNum, String employee, String comments);

    public void updateRequest(String ID, String field, Object change);

    public FoodDeliveryRequest getRequest(String ID);

}
