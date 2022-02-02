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
import edu.wpi.teama.Adb.WriteCSV;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class DefaultTest {

  @Test
  public void test() throws ParseException, IOException {

    Connection connection = null;
    Adb.initialConnection();
    Adb.inputFromCSV("TowerLocations");

    // Test on Location table (Fixed)
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
    WriteCSV.writeLocationCSV(Location.getNodeList());

    // Test on Employee table (Fixed)
    EmployeeDAO Employee = new EmployeeDerbyImpl();

    String input = "2022-02-01";
    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = originalFormat.parse(input);
    System.out.println(date.toString());

    Employee.enterEmployee(
        "001", "Admin", "Yanbo", "Dai", "ydai2@wpi.edu", "0000000000", "100 institute Rd", date);

    edu.wpi.teama.Adb.Employee.Employee e = Employee.getEmployee("001");
    System.out.println(
        e.getEmployeeID()
            + " "
            + e.getEmployeeType()
            + " "
            + e.getFirstName()
            + " "
            + e.getLastName()
            + " "
            + e.getEmail()
            + " "
            + e.getPhoneNum()
            + " "
            + e.getAddress()
            + " "
            + e.getStartDate());
    Employee.updateEmployee("001", "phoneNum", "0000000001");
    Employee.deleteEmployee("001");
  }
}
