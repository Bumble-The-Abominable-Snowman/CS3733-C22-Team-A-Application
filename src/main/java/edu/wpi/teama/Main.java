package edu.wpi.teama;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) { Aapp.launch(Aapp.class, args); }

  // This is the main function of the db repo. This needs to be integrated into the rest of the
  // code. For now, the aapp.launch is blocking, so can't add it to the main main function.
  public static void main_db(String[] args) throws Exception {
    System.out.println("calling CSV");
    List<Location> locList = ReadCSV.readCSV();

    Connection connection = null;
    Adb.initialConnection(locList);
    // App.launch(App.class, args);

    boolean exit = false;

    while (exit == false) {
      // launch 1-6 selections
      System.out.println(
          "\nPlease enter number 1-6 for: \n1- Node Information \n2- Update Node Coordinates \n3- Enter Node \n4- Delete  Node \n5- Save Locations to CSV file \n6- Exit Program");

      Scanner input = new Scanner(System.in); // New input
      String selection = input.next();

      // processing selection 1-6
      switch (selection) {
        case "1":
          System.out.println("\nExecuting: 1- Node Information");
          System.out.println("Please enter nodeID: ");
          Scanner case1 = new Scanner(System.in); // input nodeID
          String nodeID1 = case1.next();

          Adb.getNode(nodeID1);
          System.out.println("Action complete");
          break;

        case "2":
          System.out.println("\nExecuting: 2- Update Node Coordinates");

          System.out.println("Please enter nodeID: ");
          Scanner casen2 = new Scanner(System.in);
          String nodeID2 = casen2.next();

          System.out.println("Please enter xcoord: ");
          Scanner casex2 = new Scanner(System.in);
          int xcoord2 = casex2.nextInt();

          System.out.println("Please enter ycoord: ");
          Scanner casey2 = new Scanner(System.in);
          int ycoord2 = casey2.nextInt();

          Adb.updateCoordinates(nodeID2, xcoord2, ycoord2);

          System.out.println("Action complete");
          break;

        case "3":
          System.out.println("\nExecuting: 3- Enter Node");

          System.out.println("Please enter nodeID: ");
          Scanner casen3 = new Scanner(System.in);
          String nodeID3 = casen3.next();

          System.out.println("Please enter xcoord: ");
          Scanner casex3 = new Scanner(System.in);
          int xcoord3 = casex3.nextInt();

          System.out.println("Please enter ycoord: ");
          Scanner casey3 = new Scanner(System.in);
          int ycoord3 = casey3.nextInt();

          System.out.println("Please enter floor: ");
          Scanner casef3 = new Scanner(System.in);
          String floor3 = casef3.next();

          System.out.println("Please enter building: ");
          Scanner caseb3 = new Scanner(System.in);
          String building3 = caseb3.next();

          System.out.println("Please enter nodeType: ");
          Scanner casent3 = new Scanner(System.in);
          String nodeType3 = casent3.next();

          System.out.println("Please enter longName: ");
          Scanner casel3 = new Scanner(System.in);
          String longName3 = casel3.nextLine();

          System.out.println("Please enter shortName: ");
          Scanner cases3 = new Scanner(System.in);
          String shortName3 = cases3.nextLine();

          Adb.enterNode(
              nodeID3, xcoord3, ycoord3, floor3, building3, nodeType3, longName3, shortName3);

          System.out.println("Action complete");
          break;

        case "4":
          System.out.println("\nExecuting: 4- Delete  Node");
          System.out.println("Please enter in this format(nodeID): ");

          Scanner case4 = new Scanner(System.in);
          String nodeID4 = case4.next();

          Adb.deleteNode(nodeID4);

          System.out.println("Action complete");
          break;

        case "5":
          System.out.println("\nExecuting: 5- Save Locations to CSV file");
          List<Location> locationList = Adb.getNodeList();
          WriteCSV.writeCSV(locationList);
          System.out.println("Action complete");
          break;

        case "6":
          System.out.println("\nExecuting: 6- Exit Program");
          exit = true;
          break;

        default:
          System.out.println("\nInput not supported. Try again");
          break;
      }
    }

    return;
  }
}
