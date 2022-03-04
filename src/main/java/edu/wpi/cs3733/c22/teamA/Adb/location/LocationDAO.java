package edu.wpi.cs3733.c22.teamA.Adb.location;

import edu.wpi.cs3733.c22.teamA.entities.Location;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface LocationDAO {

  public List<Location> getNodeList() throws IOException, ParseException;

  public void deleteLocationNode(String ID) throws IOException;

  public void enterLocationNode(Location location) throws IOException;

  public void enterLocationNode(
      String ID,
      int xcoord,
      int ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) throws IOException;

  public void updateLocation(Location location) throws SQLException, IOException;

  public Location getLocationNode(String ID) throws IOException, ParseException;
}
