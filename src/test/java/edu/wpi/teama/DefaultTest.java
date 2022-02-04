/*-------------------------*/
/* DO NOT DELETE THIS TEST */
/*-------------------------*/

package edu.wpi.teama;

import edu.wpi.teama.Adb.Adb;
import edu.wpi.teama.Adb.Employee.EmployeeDAO;
import edu.wpi.teama.Adb.Employee.EmployeeDerbyImpl;
import edu.wpi.teama.Adb.Location.LocationDAO;
import edu.wpi.teama.Adb.Location.LocationDerbyImpl;
import edu.wpi.teama.Adb.MedicalEquipment.MedicalEquipmentDAO;
import edu.wpi.teama.Adb.MedicalEquipment.MedicalEquipmentImpl;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequestDAO;
import edu.wpi.teama.Adb.MedicalEquipmentServiceRequest.MedicalEquipmentServiceRequestImpl;
import edu.wpi.teama.Adb.WriteCSV;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

public class DefaultTest {

  @Test
  public void test() throws ParseException, IOException {

    Connection connection = null;
    Adb.initialConnection();
    Adb.inputFromCSV("TowerLocations", "edu/wpi/teama/db/TowerLocations.csv");

    // Test on Location table (Fixed)
//    LocationDAO Location = new LocationDerbyImpl();

    /* Location.enterLocationNode("nyxdai0209", 1, 1, "B1", "Tower", "Dept", "nyx dai", "nd");
    edu.wpi.teama.entities.Location l = Location.getLocationNode("FDEPT00101");
    System.out.println(
        ((edu.wpi.teama.entities.Location) l).getNodeID()
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
    Location.deleteLocationNode("nyxdai0209");*/
//    WriteCSV.writeLocationCSV(Location.getNodeList());
//
//    // **************************************************************************************************
//
//    // Test on Employee table (Fixed)
//    EmployeeDAO Employee = new EmployeeDerbyImpl();
//
//    Adb.inputFromCSV("Employee", "edu/wpi/teama/db/Employee.csv");
    /*String input = "2022-02-01";
    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = originalFormat.parse(input);
    System.out.println(date.toString());

    Employee.enterEmployee(
        "001", "Admin", "Yanbo", "Dai", "ydai2@wpi.edu", "0000000000", "100 institute Rd", date);

    Employee.enterEmployee(
        "002", "Admin", "Yanbo", "Dai", "ydai2@wpi.edu", "0000000001", "100 institute Rd", date);

    edu.wpi.teama.entities.Employee e = Employee.getEmployee("001");
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
    // Employee.deleteEmployee("001");*/
//    WriteCSV.writeEmployeeCSV(Employee.getEmployeeList());
//
//    // **************************************************************************************************
//
//    // Test on Medical Equipment
//    Adb.inputFromCSV("MedicalEquipment", "edu/wpi/teama/db/MedicalEquipment.csv");
//    MedicalEquipmentDAO equipment = new MedicalEquipmentImpl();
//    /*    System.out.println("Testing enter");
//    equipment.enterMedicalEquipment("EQ1235", "Bed", true, "FDEPT00101", true);
//    equipment.enterMedicalEquipment("EQ5679", "Xray machine", false, "FDEPT00201", true);
//    System.out.println("Testing get");
//    System.out.println(equipment.getMedicalEquipment("EQ1234"));
//    System.out.println("Testing getList");
//    System.out.println(equipment.getMedicalEquipmentList());
//    System.out.println("Testing udpate");
//    equipment.updateMedicalEquipment("EQ1234", "isClean", "false");
//    System.out.println("testing delete");
//    equipment.deleteMedicalEquipment("EQ1234");
//    System.out.println("deletion Successful");
//    equipment.deleteMedicalEquipment("EQ5678");
//    System.out.println("deletion Successful");
//    System.out.println(equipment.getMedicalEquipmentList());*/
//    WriteCSV.writeMedicalEquipmentCSV(equipment.getMedicalEquipmentList());

    // ************************************************************************************************** */

    // Test on MedicalEquipmentServiceRequest
//    MedicalEquipmentServiceRequestDAO mesr = new MedicalEquipmentServiceRequestImpl();
//    System.out.println("Testing enter");
//
//    Adb.inputFromCSV(
//        "MedicalEquipmentServiceRequest", "edu/wpi/teama/db/MedicalEquipmentServiceRequest.csv");
//    mesr.enterMedicalEquipmentServiceRequest(
//        "REQ1256",
//        "FDEPT00101",
//        "FDEPT00201",
//        "EMP1",
//        "EMP2",
//        Timestamp.valueOf("2018-09-01 09:01:15"),
//        "In Progress",
//        "EQ1234",
//        "Low Priority");
//    mesr.enterMedicalEquipmentServiceRequest(
//        "REQ12325",
//        "FDEPT00101",
//        "FDEPT00201",
//        "EMP1",
//        "EMP2",
//        Timestamp.valueOf("2018-09-01 09:01:15"),
//        "In Progress",
//        "EQ1234",
//        "Low Priority");
//    System.out.println("Testing get");
//    System.out.println(mesr.getMedicalEquipmentServiceRequest("REQ123"));
//    System.out.println("Testing udpate");
//    mesr.updateMedicalEquipmentServiceRequest("REQ123", "StartLocation", "FDEPT00301");
//    System.out.println("Testing getList");
//    System.out.println(mesr.getMedicalEquipmentServiceRequestList());
//    System.out.println("testing delete");
//    mesr.deleteMedicalEquipment("REQ123");
//    mesr.deleteMedicalEquipment("REQ124");
//    WriteCSV.writeMedicalEquipmentServiceRequestCSV(mesr.getMedicalEquipmentServiceRequestList());
  }
}
