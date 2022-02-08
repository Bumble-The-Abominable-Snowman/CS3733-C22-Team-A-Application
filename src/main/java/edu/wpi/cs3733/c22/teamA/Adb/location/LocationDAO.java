package edu.wpi.teama.Adb.Location;

import edu.wpi.teama.entities.Location;
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
}
