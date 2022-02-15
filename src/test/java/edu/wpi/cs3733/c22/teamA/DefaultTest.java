/// *-------------------------*/
/// * DO NOT DELETE THIS TEST */
/// *-------------------------*/

package edu.wpi.cs3733.c22.teamA;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.ParseException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DefaultTest {

  @Test
  public void test()
      throws ParseException, IOException, InvocationTargetException, IllegalAccessException {
    Connection connection = null;
    Adb.initialConnection("EmbeddedDriver");

    LocationDerbyImpl.inputFromCSV(
        "Location", "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/TowerLocations.csv");
    EmployeeDerbyImpl.inputFromCSV(
        "Employee", "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/Employee.CSV");

    System.out.println("---------------");
    ServiceRequestDerbyImpl<EquipmentSR> equipmentSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new EquipmentSR());

    equipmentSRServiceRequestDerby.populateFromCSV(
        "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipmentServiceRequest.CSV");

    // get sr request by id
    EquipmentSR sr = equipmentSRServiceRequestDerby.getRequest("REQ1256");

    // update the existing sr
    sr.setComments("new employee!");
    sr.setEmployeeAssigned("004");
    equipmentSRServiceRequestDerby.updateServiceRequest(sr);

    // create a new sr from the previous sr
    sr.setRequestID("REQ123456789");
    sr.setComments("new employee2!");
    sr.setEmployeeAssigned("003");
    sr.setRequestStatus("CANCELED");
    sr.setEquipmentID("EQ787898");
    equipmentSRServiceRequestDerby.enterServiceRequest(sr);

    // print out the list
    System.out.println("EquipmentSR List:");
    List<EquipmentSR> equipmentSRS = equipmentSRServiceRequestDerby.getServiceRequestList();
    for (EquipmentSR srItem : equipmentSRS) {
      System.out.println(srItem);
    }

    System.out.println("---------------");
    ServiceRequestDerbyImpl<ReligiousSR> religiousSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new ReligiousSR());

    religiousSRServiceRequestDerby.populateFromCSV(
        "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/ReligiousServiceRequest.csv");

    // get all the SRs
    System.out.println("---------------");
    ServiceRequestDerbyImpl<SR> serviceRequestDAO = new ServiceRequestDerbyImpl<>(new SR());

    // print out the list
    System.out.println("SR List:");
    List<?> srList = serviceRequestDAO.getAllServiceRequestList();
    for (Object srItem : srList) {
      System.out.println(srItem);
    }
  }

  @Test
  public void testOnAdbConnection() {

    // test switch between EmbeddedDriver and ClientDriver
    // Client server setup CMD line:
    // java -jar %DERBY_HOME%\lib\derbyrun.jar server start
    Adb.initialConnection("EmbeddedDriver");
  }

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
  //    //
  //
  // **************************************************************************************************
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
  //    //
  //
  // **************************************************************************************************
  //
  //    // Test on Medical Equipment
  //    Adb.inputFromCSV("MedicalEquipment", "edu/wpi/teama/db/MedicalEquipment.csv");
  //    MedicalEquipmentDAO equipment = new MedicalEquipmentDerbyImpl();
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

  //
  // ************************************************************************************************** */

  // Test on MedicalEquipmentServiceRequest
  //    MedicalEquipmentServiceRequestDAO mesr = new MedicalEquipmentServiceRequestDerbyImpl();
  //    System.out.println("Testing enter");
  //
  //    Adb.inputFromCSV(
  //        "MedicalEquipmentServiceRequest",
  // "edu/wpi/teama/db/MedicalEquipmentServiceRequest.csv");
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
  //
  //
  // WriteCSV.writeMedicalEquipmentServiceRequestCSV(mesr.getMedicalEquipmentServiceRequestList());
  //  }

  //  @Test
  //  public void testReligious() throws IOException {
  //    Adb.initialConnection();
  //
  //    ReligiousServiceRequestDerbyImpl.inputFromCSV(
  //        "ReligiousServiceRequest",
  // "edu/wpi/cs3733/c22/teamA/db/CSVs/ReligiousServiceRequest.csv");
  //
  //    ReligiousServiceRequestDAO derby = new ReligiousServiceRequestDerbyImpl();
  //    ReligiousSR rsr =
  //        new ReligiousSR(
  //            "rel123",
  //            "start",
  //            "end",
  //            "emp1",
  //            "emp2",
  //            "2020-01-01 12:45:00",
  //            "In Progress",
  //            "High Priority",
  //            "no additional comments",
  //            "Christain");
  //
  //    ReligiousSR rsr1 =
  //        new ReligiousSR(
  //            "rel124",
  //            "start",
  //            "end",
  //            "emp1",
  //            "emp2",
  //            "2020-01-01 12:45:00",
  //            "In Progress",
  //            "High Priority",
  //            "no additional comments",
  //            "Judiasm");
  //
  //    System.out.println("Testing enter");
  //    derby.enterReligiousServiceRequest(rsr);
  //    derby.enterReligiousServiceRequest(rsr1);
  //    System.out.println("Testing get");
  //    ReligiousSR rsr2 = derby.getReligiousServiceRequest("rel123");
  //    System.out.println("Got RequestID: " + rsr2.getRequestID());
  //    System.out.println("Testing update: updating religion to 'Judiasm'");
  //    derby.updateReligiousServiceRequest("rel123", "religion", "Judiasm");
  //    System.out.println("Testing getList");
  //    List<ReligiousSR> list = derby.getReligiousServiceRequestList();
  //    System.out.println("First element religion: " + list.get(0).getReligion());
  //    System.out.println("testing delete");
  //    derby.deleteReligiousServiceRequest("rel123");
  //    derby.deleteReligiousServiceRequest("rel124");
  //    ReligiousServiceRequestDerbyImpl.writeReligiousServiceRequestCSV(
  //        derby.getReligiousServiceRequestList(),
  //        "edu/wpi/cs3733/c22/teamA/db/CSVs/ReligiousServiceRequest.csv");
  //  }
  //
  //  @Test
  //  public void testSanitation() throws IOException {
  //    Adb.initialConnection();
  //
  //    SanitationServiceRequestDerbyImpl.inputFromCSV(
  //        "SanitationServiceRequest",
  //        "edu/wpi/cs3733/c22/teamA/db/CSVs/SanitationServiceRequest.csv");
  //    SanitationServiceRequestDerbyImpl derby = new SanitationServiceRequestDerbyImpl();
  //    SanitationSR ssr =
  //        new SanitationSR(
  //            "san123",
  //            "start",
  //            "end",
  //            "emp1",
  //            "emp2",
  //            "2020-01-01 12:45:00",
  //            "In Progress",
  //            "High Priority",
  //            "no additional comments",
  //            "floor");
  //    System.out.println("Testing enter");
  //    derby.enterSanitationServiceRequest(ssr);
  //    System.out.println("Testing get");
  //    SanitationSR rsr2 = derby.getSanitationServiceRequest("san123");
  //    System.out.println("Got RequestID: " + rsr2.getRequestID());
  //    System.out.println("Testing update: updating religion to 'super'");
  //    derby.updateSanitationServiceRequest("san123", "sanitationType", "super");
  //    SanitationSR rsr3 = derby.getSanitationServiceRequest("san123");
  //    System.out.println("Got santype: " + rsr3.getSanitationType());
  //    System.out.println("Testing getList");
  //    List<SanitationSR> list = derby.getSanitationServiceRequestList();
  //    System.out.println("First element santype: " + list.get(0).getSanitationType());
  //    System.out.println("testing delete");
  //    derby.deleteSanitationServiceRequest("san123");
  //    SanitationServiceRequestDerbyImpl.exportToCSV(
  //        "SanitationServiceRequest",
  //        "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/SanitationServiceRequest.csv");
  //  }
  //
  //  @Test
  //  public void testLaundry() throws IOException {
  //    Adb.initialConnection();
  //    LaundryServiceRequestDerbyImpl derby = new LaundryServiceRequestDerbyImpl();
  //
  //    LaundryServiceRequestDerbyImpl.inputFromCSV(
  //        "LaundryServiceRequest", "edu/wpi/cs3733/c22/teamA/db/CSVs/LaundryServiceRequest.csv");
  //    LaundrySR lsr =
  //        new LaundrySR(
  //            "lan123",
  //            "start",
  //            "end",
  //            "emp1",
  //            "emp2",
  //            "2020-01-01 12:45:00",
  //            "In Progress",
  //            "High Priority",
  //            "no additional comments",
  //            "Normal Wash");
  //    System.out.println("Testing enter");
  //    derby.enterLaundryServiceRequest(lsr);
  //    System.out.println("Testing get");
  //    LaundrySR rsr2 = derby.getLaundryServiceRequest("lan123");
  //    System.out.println("Got RequestID: " + rsr2.getRequestID());
  //    System.out.println("Testing update: updating washMode to 'Super Wash'");
  //    derby.updateLaundryServiceRequest("lan123", "washMode", "Super Wash");
  //    LaundrySR rsr3 = derby.getLaundryServiceRequest("lan123");
  //    System.out.println("Got washMode: " + rsr3.getWashMode());
  //    System.out.println("Testing getList");
  //    List<LaundrySR> list = derby.getLaundryServiceRequestList();
  //    System.out.println("First element washMode: " + list.get(0).getWashMode());
  //    System.out.println("testing delete");
  //    // derby.deleteLaundryServiceRequest("lan123");
  //    LaundryServiceRequestDerbyImpl.exportToCSV(
  //        "LaundryServiceRequest",
  //        "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/LaundryServiceRequest.csv");
  //  }
  //
  //  @Test
  //  public void testLanguage() throws IOException {
  //    Adb.initialConnection();
  //
  //    LanguageServiceRequestDerbyImpl.inputFromCSV(
  //        "LanguageServiceRequest",
  // "edu/wpi/cs3733/c22/teamA/db/CSVs/LanguageServiceRequest.csv");
  //
  //    LanguageServiceRequestDerbyImpl derby = new LanguageServiceRequestDerbyImpl();
  //    LanguageSR lsr =
  //        new LanguageSR(
  //            "lan124",
  //            "start",
  //            "end",
  //            "emp1",
  //            "emp2",
  //            "2020-01-01 12:45:00",
  //            "In Progress",
  //            "High Priority",
  //            "no additional comments",
  //            "Normal Wash");
  //    System.out.println("Testing enter");
  //    derby.enterLanguageServiceRequest(lsr);
  //    System.out.println("Testing get");
  //    LanguageSR rsr2 = derby.getLanguageServiceRequest("lan123");
  //    System.out.println("Got RequestID: " + rsr2.getRequestID());
  //    System.out.println("Testing update: updating washMode to 'Super Wash'");
  //    derby.updateLanguageServiceRequest("lan123", "language", "Super Wash");
  //    LanguageSR rsr3 = derby.getLanguageServiceRequest("lan123");
  //    System.out.println("Got washMode: " + rsr3.getLanguage());
  //    System.out.println("Testing getList");
  //    List<LanguageSR> list = derby.getLanguageServiceRequestList();
  //    System.out.println("First element language: " + list.get(0).getLanguage());
  //    System.out.println("testing delete");
  //    derby.deleteLanguageServiceRequest("lan123");
  //    LanguageServiceRequestDerbyImpl.exportToCSV(
  //        "LanguageServiceRequest",
  //        "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/LanguageServiceRequest.csv");
  //  }
  //
  //  @Test
  //  public void testFoodDelivery() throws IOException {
  //
  //    Adb.initialConnection();
  //
  //    FoodDeliveryServiceRequestDerbyImpl.inputFromCSV(
  //        "FoodDeliveryServiceRequest",
  //        "edu/wpi/cs3733/c22/teamA/db/CSVs/FoodDeliveryServiceRequest.csv");
  //
  //    FoodDeliveryServiceRequestDerbyImpl derby = new FoodDeliveryServiceRequestDerbyImpl();
  //    FoodDeliverySR fdsr =
  //        new FoodDeliverySR(
  //            "fod123",
  //            "start",
  //            "end",
  //            "emp1",
  //            "emp2",
  //            "2020-01-01 12:45:00",
  //            "In Progress",
  //            "High Priority",
  //            "no additional comments",
  //            "Steak",
  //            "Fries",
  //            "Soda",
  //            "Cake");
  //    System.out.println("Testing enter");
  //    derby.enterFoodDeliveryRequest(fdsr);
  //    System.out.println("Testing get");
  //    FoodDeliverySR rsr2 = derby.getFoodDeliveryRequest("fod123");
  //    System.out.println("Got RequestID: " + rsr2.getRequestID());
  //    System.out.println("Testing update: updating dessert to 'cookie'");
  //    derby.updateFoodDeliveryRequest("fod123", "dessert", "cookie");
  //    FoodDeliverySR rsr3 = derby.getFoodDeliveryRequest("fod123");
  //    System.out.println("Got dessert: " + rsr3.getDessert());
  //    System.out.println("Testing getList");
  //    List<FoodDeliverySR> list = derby.getFoodDeliveryRequestList();
  //    System.out.println("First element dessert: " + list.get(0).getDessert());
  //    System.out.println("testing delete");
  //    derby.deleteFoodDeliveryRequest("fod123");
  //    FoodDeliveryServiceRequestDerbyImpl.exportToCSV(
  //        "FoodDeliveryServiceRequest",
  //        "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/FoodDeliveryServiceRequest.csv");
  //  }
  //
  //  @Test
  //  public void testMedicalEquipment() {
  //    Adb.initialConnection();
  //    EquipmentServiceRequestDerbyImpl derby = new EquipmentServiceRequestDerbyImpl();
  //    EquipmentSR mesr =
  //        new EquipmentSR(
  //            "meq123",
  //            "start",
  //            "end",
  //            "emp1",
  //            "emp2",
  //            "2020-01-01 12:45:00",
  //            SR.Status.BLANK,
  //            "High Priority",
  //            "no additional comments",
  //            "bed1");
  //    System.out.println("Testing enter");
  //    derby.enterMedicalEquipmentServiceRequest(mesr);
  //    System.out.println("Testing get");
  //    EquipmentSR rsr2 = derby.getMedicalEquipmentServiceRequest("meq123");
  //    System.out.println("Got RequestID: " + rsr2.getRequestID());
  //    System.out.println("Testing update: updating equipmentID to 'xray1'");
  //    derby.updateMedicalEquipmentServiceRequest("meq123", "equipmentID", "xray1");
  //    EquipmentSR rsr3 = derby.getMedicalEquipmentServiceRequest("meq123");
  //    System.out.println("Got washMode: " + rsr3.getEquipmentID());
  //    System.out.println("Testing getList");
  //    List<EquipmentSR> list = derby.getMedicalEquipmentServiceRequestList();
  //    System.out.println("First element language: " + list.get(0).getEquipmentID());
  //    System.out.println("testing delete");
  //    derby.deleteMedicalEquipment("meq123");
  //  }
}
