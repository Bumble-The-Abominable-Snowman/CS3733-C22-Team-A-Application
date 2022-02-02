package edu.wpi.teama.Adb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadCSV {

  public static List<Location> readCSV() throws IOException {
    // System.out.println("beginning to read csv");

    Scanner lineScanner =
        new Scanner(ReadCSV.class.getClassLoader().getResourceAsStream("TowerLocations.csv"));
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;
    List<Location> locList = new ArrayList<>();
    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      Location thisLocation = new Location();

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) thisLocation.setNodeID(data);
        else if (dataIndex == 1) {
          intData = Integer.parseInt(data);
          thisLocation.setXCoord(intData);
        } else if (dataIndex == 2) {
          intData = Integer.parseInt(data);
          thisLocation.setYCoord(intData);
        } else if (dataIndex == 3) thisLocation.setFloor(data);
        else if (dataIndex == 4) thisLocation.setBuilding(data);
        else if (dataIndex == 5) thisLocation.setNodeType(data);
        else if (dataIndex == 6) thisLocation.setLongName(data);
        else if (dataIndex == 7) thisLocation.setShortName(data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      locList.add(thisLocation);
      // System.out.println(thisLocation);

    }

    lineIndex++;
    lineScanner.close();
    return locList;
  }
}
