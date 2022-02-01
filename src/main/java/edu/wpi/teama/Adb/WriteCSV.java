package edu.wpi.teama.Adb;

import edu.wpi.teama.Adb.Employee.Employee;
import edu.wpi.teama.Adb.Location.Location;
import edu.wpi.teama.Adb.MedicalEquipment.MedicalEquipment;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class WriteCSV {

  //Write CSV for location table
  static void writeLocationCSV(List<Location> List) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get("TowerLocations.CSV"));

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

  //Write CSV for Employee table
  static void writeEmployeeCSV(List<Employee> List) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get("Employee.CSV"));

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

  //Write CSV for MedicalEquipment table
  static void writeMedicalEquipmentCSV(List<MedicalEquipment> List) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get("MedicalEquipment.CSV"));

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

  //Write CSV for MedicalEquipmentServiceRequest table
  static void writeMedicalEquipmentServiceRequestCSV(List<MedicalEquipmentServiceRequest> List) throws IOException {

    // create a writer
    BufferedWriter writer = Files.newBufferedWriter(Paths.get("MedicalEquipmentServiceRequest.CSV"));

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
