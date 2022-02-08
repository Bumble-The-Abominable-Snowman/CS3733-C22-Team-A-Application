package edu.wpi.cs3733.c22.teamA.Adb.location;

import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.io.IOException;
import java.util.List;

public interface LocationDAO {

  public List<Location> getNodeList();

  public void deleteLocationNode(String ID);

  public void enterLocationNode(
      String ID,
      int xcoord,
      int ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName);

  public void updateLocation(String ID, String field, String change);

  public Location getLocationNode(String ID);

  public void writeLocationCSV(List<Location> List, String csvFilePath) throws IOException;
}
