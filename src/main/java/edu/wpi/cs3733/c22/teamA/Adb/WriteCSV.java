package edu.wpi.cs3733.c22.teamA.Adb;

import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.MedicalEquipment;
import edu.wpi.cs3733.c22.teamA.entities.requests.MedicalEquipmentServiceRequest;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WriteCSV {

  // Write CSV for location table
  public static void writeLocationCSV(List<Location> List, String csvFilePath) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "getNodeID, xCord, yCord, getFloor(),getBuilding, getNodeType, getLongName, getShortName");
    writer.newLine();

    // write location data
    for (Location thisLocation : List) {

      String xCord = String.valueOf(thisLocation.getXCoord());
      String yCord = String.valueOf(thisLocation.getYCoord());
      writer.write(
          String.join(
              ",",
              thisLocation.getNodeID(),
              xCord,
              yCord,
              thisLocation.getFloor(),
              thisLocation.getBuilding(),
              thisLocation.getNodeType(),
              thisLocation.getLongName(),
              thisLocation.getShortName()));
      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // Write CSV for Employee table
  public static void writeEmployeeCSV(List<Employee> List, String csvFilePath) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "getEmployeeID, getEmployeeType, getFirstName, getLastName, getEmail, getPhoneNum, getAddress, startDate");
    writer.newLine();

    // write location data
    for (Employee thisEmployee : List) {

      String startDate = String.valueOf(thisEmployee.getStartDate());
      writer.write(
          String.join(
              ",",
              thisEmployee.getEmployeeID(),
              thisEmployee.getEmployeeType(),
              thisEmployee.getFirstName(),
              thisEmployee.getLastName(),
              thisEmployee.getEmail(),
              thisEmployee.getPhoneNum(),
              thisEmployee.getAddress(),
              startDate));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // Write CSV for MedicalEquipment table
  public static void writeMedicalEquipmentCSV(List<MedicalEquipment> List, String csvFilePath)
      throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write("getEquipmentID, getEquipmentType, isClean, getCurrentLocation, isAvailable");
    writer.newLine();

    // write location data
    for (MedicalEquipment thisME : List) {

      String isClean = String.valueOf(thisME.getIsClean());
      String isAvailable = String.valueOf(thisME.getIsAvailable());
      writer.write(
          String.join(
              ",",
              thisME.getEquipmentID(),
              thisME.getEquipmentType(),
              isClean,
              thisME.getCurrentLocation(),
              isAvailable));

      writer.newLine();
    }
    writer.close(); // close the writer
  }

  // Write CSV for MedicalEquipmentServiceRequest table
  public static void writeMedicalEquipmentServiceRequestCSV(
      List<MedicalEquipmentServiceRequest> List, String csvFilePath) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

    writer.write(
        "RequestID, getStartLocation, getEndLocation, getEmployeeRequested, getEmployeeAssigned, requestTime, getRequestStatus, getEquipmentID, getRequestType");
    writer.newLine();

    // write location data
    for (MedicalEquipmentServiceRequest thisMESR : List) {

      String requestTime = String.valueOf(thisMESR.getRequestTime());
      writer.write(
          String.join(
              ",",
              thisMESR.getRequestID(),
              thisMESR.getStartLocation(),
              thisMESR.getEndLocation(),
              thisMESR.getEmployeeRequested(),
              thisMESR.getEmployeeAssigned(),
              requestTime,
              thisMESR.getRequestStatus(),
              thisMESR.getEquipmentID(),
              thisMESR.getRequestType()));

      writer.newLine();
    }
    writer.close(); // close the writer
  }
}
