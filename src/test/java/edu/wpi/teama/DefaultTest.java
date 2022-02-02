/*-------------------------*/
/* DO NOT DELETE THIS TEST */
/*-------------------------*/

package edu.wpi.teama;

import edu.wpi.teama.Adb.Adb;
import edu.wpi.teama.Adb.Location.Location;
import edu.wpi.teama.Adb.Location.LocationDAO;
import edu.wpi.teama.Adb.Location.LocationDerbyImpl;
import java.sql.Connection;
import org.junit.jupiter.api.Test;

public class DefaultTest {

  @Test
  public void test() {

    Connection connection = null;
    Adb.initialConnection();
    Adb.inputFromCSV("TowerLocations");

    LocationDAO Location = new LocationDerbyImpl();

    Location.enterLocationNode("nyxdai0209", 1, 1, "B1", "Tower", "Dept", "nyx dai", "nd");
    Location l = Location.getLocationNode("nyxdai0209");
    Location.updateLocation("nyxdai0209", "xcoord", "2");
    Location.updateLocation("nyxdai0209", "ycoord", "2");
    Location.deleteLocationNode("nyxdai0209");
  }
}
