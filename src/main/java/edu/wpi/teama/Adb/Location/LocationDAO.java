package edu.wpi.teama.Adb.Location;

import java.util.List;

public interface LocationDAO {
    public Location getLocationNode(String ID);
    public void updateLocation(String ID, String field, String change);
    public void enterLocationNode(
            String nodeID,
            int xcoord,
            int ycoord,
            String floor,
            String building,
            String nodeType,
            String longName,
            String shortName);

    public void deleteLocationNode(String nodeID);
    public List<Location> getNodeList();


}
