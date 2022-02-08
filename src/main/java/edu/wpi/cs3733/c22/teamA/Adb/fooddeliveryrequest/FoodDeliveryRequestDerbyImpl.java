package edu.wpi.cs3733.c22.teamA.Adb.fooddeliveryrequest;

import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.requests.FoodDeliveryRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDeliveryRequestDerbyImpl implements FoodDeliveryRequestDAO {

    List<FoodDeliveryRequest> foodDeliveryRequest;

    public void FoodDeliveryRequestDerbyImpl(){
        try {

            foodDeliveryRequest = new ArrayList<FoodDeliveryRequest>();
            Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
            Statement getNodeList = connection.createStatement();
            ResultSet rset = getNodeList.executeQuery("SELECT * FROM FoodDeliveryRequest");

            while (rset.next()) {
                String requestID = rset.getString("requestID");
                String mainDish = rset.getString("mainDish");
                String sideDish = rset.getString("sideDish");
                String beverage = rset.getString("beverage");
                String dessert = rset.getString("dessert");
                String roomNum = rset.getString("roomNum");
                String employee = rset.getString("employee");
                String comments = rset.getString("comments");

                FoodDeliveryRequest f = new FoodDeliveryRequest(requestID, mainDish, sideDish, beverage, dessert, roomNum, employee, comments);
                foodDeliveryRequest.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }

    public List<FoodDeliveryRequest> getNodeList() {
        return foodDeliveryRequest;
    }

    public void deleteRequest(String requestID) {

        String tableName = "FoodDeliveryRequest";
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

        String tableName = "FoodDeliveryRequest";
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

        String tableName = "FoodDeliveryRequest";
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

    public FoodDeliveryRequest getRequest(String ID) {

        String tableName = "FoodDeliveryRequest";
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
            FoodDeliveryRequest f = new FoodDeliveryRequest();
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

                f = new FoodDeliveryRequest(requestID, mainDish, sideDish, beverage, dessert, roomNum, employee, comments);
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
