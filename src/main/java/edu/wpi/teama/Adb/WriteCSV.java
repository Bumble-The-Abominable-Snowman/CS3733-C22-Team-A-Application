package edu.wpi.teama.Adb;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class WriteCSV {

  static void writeCSV(List<Location> locList) throws IOException {

    // create a writer
    Scanner chooseName = new Scanner(System.in); // New input
    System.out.println("Enter file name and extension");
    String fileName = chooseName.next();
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));

    // write location data
    for (Location thisLocation : locList) {

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
}
