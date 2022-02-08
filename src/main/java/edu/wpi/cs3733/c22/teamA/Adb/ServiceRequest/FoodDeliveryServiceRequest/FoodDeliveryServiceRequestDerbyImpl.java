package edu.wpi.cs3733.c22.teamA.Adb.ServiceRequest.FoodDeliveryServiceRequest;

import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryServiceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDeliveryServiceRequestDerbyImpl implements FoodDeliveryServiceRequestDAO {

    List<FoodDeliveryServiceRequest> FoodDeliveryServiceRequest;

    public void FoodDeliveryRequestDerbyImpl(){
        try {

            FoodDeliveryServiceRequest = new ArrayList<FoodDeliveryServiceRequest>();
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
            Statement getNodeList = connection.createStatement();
            ResultSet rset = getNodeList.executeQuery("SELECT * FROM FoodDeliveryServiceRequest");

            while (rset.next()) {
                String requestID = rset.getString("requestID");
                String startLocation = rset.getString("startLocation");
                String endLocation = rset.getString("endLocation");
                String employeeRequested = rset.getString("employeeRequested");
                String employeeAssigned = rset.getString("employeeAssigned");
                String requestTime = rset.getString("requestTime");
                String requestStatus = rset.getString("requestStatus");
                String requestType = rset.getString("requestType");
                String comments = rset.getString("comments");

                FoodDeliveryServiceRequest f = new FoodDeliveryServiceRequest(requestID, startLocation, endLocation, employeeRequested, employeeAssigned, requestTime, requestStatus, requestType, comments);
                FoodDeliveryServiceRequest.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }

    public List<FoodDeliveryServiceRequest> getNodeList() {
        return foodDeliveryServiceRequest;
    }

    public void deleteRequest(String requestID) {

        String tableName = "FoodDeliveryServiceRequest";
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
            Statement deleteNode = connection.createStatement();

            String str =
                    String.format(
                            "DELETE FROM " + tableName + " WHERE requestID ='%s'", requestID); // delete the selected node

            deleteNode.execute(str);

        } catch (SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
            return;
        }
    }

    public void enterRequest(
            String requestID, String mainDish, String sideDish, String beverage, String dessert, String roomNum, String employee, String comments) {

        String tableName = "FoodDeliveryServiceRequest";
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
            Statement enterNode = connection.createStatement();

            String str =
                    String.format(
                            "INSERT INTO "
                                    + tableName
                                    + "(requestID, mainDish, sideDish, beverage, dessert, roomNum, employee, comments)"
                                    + "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                            requestID, mainDish, sideDish, beverage, dessert, roomNum, employee, comments); // insert values from input.

            enterNode.execute(str);

        } catch (SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }

    public void updateRequest(String ID, String field, Object change) {

        String tableName = "FoodDeliveryServiceRequest";
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
            Statement updateCoords = connection.createStatement();

            String str =
                    String.format(
                            "update " + tableName + " set " + field + " = '%s' where requestID = '%s'", change, ID);
            updateCoords.execute(str);

        } catch (SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }

    public FoodDeliveryServiceRequest getRequest(String ID) {

        String tableName = "FoodDeliveryServiceRequest";
        try {
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
            Statement getNode = connection.createStatement();
            String str =
                    String.format(
                            "select * from " + tableName + " Where requestID = '%s'",
                            ID); // get node from table location

            getNode.execute(str);

            // Commented code to print out the selected node.
            ResultSet rset = getNode.getResultSet();
            FoodDeliveryServiceRequest f = new FoodDeliveryServiceRequest();
            // process results
            if (rset.next()) {
                String requestID = rset.getString("requestID");
                String mainDish = rset.getString("mainDish");
                String sideDish = rset.getString("sideDish");
                String beverage = rset.getString("beverage");
                String dessert = rset.getString("dessert");
                String roomNum = rset.getString("roomNum");
                String employee = rset.getString("employee");
                String comments = rset.getString("comments");

                f = new FoodDeliveryServiceRequest(requestID, mainDish, sideDish, beverage, dessert, roomNum, employee, comments);
            }

            // Return the location object
            return f;

        } catch (SQLException e) {
            System.out.println("Failed to get node");
            e.printStackTrace();
            return null;
        }
    }

}
