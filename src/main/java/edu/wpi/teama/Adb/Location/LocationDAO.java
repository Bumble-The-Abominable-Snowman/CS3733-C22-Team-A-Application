package edu.wpi.teama.Adb.Location;

import java.util.List;

public interface LocationDAO {
    public static Location getLocationNode(String ID) {
        return null;
    }

    public static void updateLocation(String ID, String field, String change) {

    }

    public static void enterLocationNode(
            String nodeID,
            int xcoord,
            int ycoord,
            String floor,
            String building,
            String nodeType,
            String longName,
            String shortName) {

    }

    public static void deleteLocationNode(String nodeID) {

    }

    public static List<Location> getNodeList() {
        return null;
    }


}
