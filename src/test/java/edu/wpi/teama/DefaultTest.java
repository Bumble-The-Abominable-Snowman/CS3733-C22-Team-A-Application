/*-------------------------*/
/* DO NOT DELETE THIS TEST */
/*-------------------------*/

package edu.wpi.teama;

import edu.wpi.teama.Adb.Adb;
import edu.wpi.teama.Adb.Employee.Employee;
import edu.wpi.teama.Adb.Employee.EmployeeDAO;
import edu.wpi.teama.Adb.Employee.EmployeeDerbyImpl;
import edu.wpi.teama.Adb.Location.Location;
import edu.wpi.teama.Adb.Location.LocationDAO;
import edu.wpi.teama.Adb.Location.LocationDerbyImpl;
import java.sql.Connection;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

public class DefaultTest {

  @Test
  public void test() throws ParseException {

    Connection connection = null;
    Adb.initialConnection();
    Adb.inputFromCSV("TowerLocations");

    // Test on Location table
    LocationDAO Location = new LocationDerbyImpl();

    Location.enterLocationNode("nyxdai0209", 1, 1, "B1", "Tower", "Dept", "nyx dai", "nd");
    Location l = Location.getLocationNode("FDEPT00101");
    System.out.println(
        l.getNodeID()
            + " "
            + l.getXCoord()
            + " "
            + l.getYCoord()
            + " "
            + l.getFloor()
            + " "
            + l.getBuilding()
            + " "
            + l.getNodeType()
            + " "
            + l.getLongName()
            + " "
            + l.getShortName());
    Location.updateLocation("nyxdai0209", "xcoord", "2");
    Location.updateLocation("nyxdai0209", "ycoord", "2");
    Location.deleteLocationNode("nyxdai0209");

    // Test on Employee table still bugged
    EmployeeDAO Employee = new EmployeeDerbyImpl();
    Employee.enterEmployee(
        "001",
        "Admin",
        "Yanbo",
        "Dai",
        "ydai2@wpi.edu",
        "0000000000",
        "100 institute Rd",
        "2022-02-02");
  }
}
