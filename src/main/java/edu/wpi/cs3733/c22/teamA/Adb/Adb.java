package edu.wpi.cs3733.c22.teamA.Adb;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.ParseException;

public class Adb {

  public static String pathToDBA = "";

  public static boolean usingEmbedded = true;

  public static void initialConnection(String arg) {

    boolean isInitialized = false;
    // Connection to database driver
    System.out.println("----- Apache Derby Connection Testing -----");
    switch (arg) {
      case "EmbeddedDriver":
        usingEmbedded = true;

        pathToDBA = "src/main/resources/edu/wpi/cs3733/c22/teamA/db/HospitalDBA";

        try {
          Class.forName("org.apache.derby.jdbc." + arg);
          System.out.println("Apache Derby embedded driver registered!\n");
          break;

        } catch (ClassNotFoundException e) {
          System.out.println("Apache Derby Not Found");
          e.printStackTrace();
          return;
        }

      case "ClientDriver":
        usingEmbedded = false;
        pathToDBA = "//198.199.83.208:1527/HospitalDBA";

        try {
          Class.forName("org.apache.derby.jdbc." + arg);
          System.out.println("Apache Derby client driver registered!\n");
          break;

        } catch (ClassNotFoundException e) {
          System.out.println("Apache Derby Not Found");
//          e.printStackTrace();
          return;
        }
    }

    try {

      // Check if database exist. If not then create one.
      try {
        Connection connection =
            DriverManager.getConnection(
                String.format(
                    "jdbc:derby:%s;user=Admin;password=admin",
                    pathToDBA)); // Modify the database name from TowerLocation to Adb
        // for better
        // recognition.
        isInitialized = true;
        System.out.println("DB already exist");

      } catch (SQLException e) {
        Connection c =
            DriverManager.getConnection(String.format("jdbc:derby:%s;create=true", pathToDBA));
        turnOnBuiltInUsers(c);
        c.close();
        System.out.println("DB initialized");
        System.out.println("Closed connection");

        isInitialized = false;
      }

    } catch (SQLException e) {
      System.out.println("Connection failed");
//      e.printStackTrace();
      return;
    }

    /*    try {
         Connection connection = DriverManager.getConnection("jdbc:derby:HospitalDBA;");
         Statement drop = connection.createStatement();
         drop.execute("DROP TABLE MedicalEquipmentServiceRequest");

       } catch (SQLException e) {

       }
    */

    // Check if tables exist
    // Check Locations table.
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE TowerLocations(node_id varchar(25), "
              + "xcoord int, "
              + "ycoord int, "
              + "floor varchar(25), "
              + "building varchar(25), "
              + "node_type varchar(25), "
              + "long_name varchar(100), "
              + "short_name varchar(50), "
              + "PRIMARY KEY (node_id))");

    } catch (SQLException e) {
      System.out.println("Table TowerLocations already exist");
//      e.printStackTrace();
    }

    // Check Employee table.
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE Employee(employee_id varchar(25), "
              + "employee_type varchar(25), "
              + "first_name varchar(25), "
              + "last_name varchar(25), "
              + "email varchar(25), "
              + "phone_num varchar(25), "
              + "address varchar(25), "
              + "start_date date, "
              + "PRIMARY KEY (employee_id))");

    } catch (SQLException e) {
      System.out.println("Table Employee already exist");
//      e.printStackTrace();
    }

    // Check MedicalEquipment table.
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE MedicalEquipment(equipment_id varchar(25), "
              + "equipment_type varchar(25), "
              + "is_clean varchar(25), "
              + "current_location varchar(25), "
              + "is_available varchar(25), "
              + "PRIMARY KEY (equipment_id),"
              + "FOREIGN KEY (current_location) REFERENCES TowerLocations(node_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipment already exist");
    }

    // Check ServiceRequestDerbyImpl table.
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE ServiceRequest(request_id varchar(25), "
              + "start_location varchar(25), "
              + "end_location varchar(25), "
              + "employee_requested varchar(25), "
              + "employee_assigned varchar(25), "
              + "request_time timestamp, "
              + "request_status varchar(25), "
              + "request_priority varchar(25), "
              + "comments varchar(255), "
              + "PRIMARY KEY (request_id),"
              + "FOREIGN KEY (start_location) REFERENCES TowerLocations(node_id) ON DELETE CASCADE,"
              + "FOREIGN KEY (end_location) REFERENCES TowerLocations(node_id) ON DELETE CASCADE,"
              + "FOREIGN KEY (employee_requested) REFERENCES Employee(employee_id) ON DELETE CASCADE,"
              + "FOREIGN KEY (employee_assigned) REFERENCES Employee(employee_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table ServiceRequestDerbyImpl already exist");
    }

    // Check MedicalEquipmentServiceRequest table.
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE MedicalEquipmentServiceRequest(request_id varchar(25), "
              + "equipment_id varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table MedicalEquipmentServiceRequest already exist");
    }

    // Check Food Delivery Table
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE FoodDeliveryServiceRequest(request_id varchar(25), "
              + "main_dish varchar(50), "
              + "side_dish varchar(50), "
              + "beverage varchar(50), "
              + "dessert varchar(50), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table FoodDeliveryServiceRequest already exist");
    }

    // Check Language  table.
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE LanguageServiceRequest(request_id varchar(25), "
              + "language varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table languageservicerequest already exist");
    }

    //   Check Laundry  table.
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE LaundryServiceRequest(request_id varchar(25), "
              + "wash_mode varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table laundryservicerequest already exist");
    }

    //  Check Religious  table.
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE ReligiousServiceRequest(request_id varchar(25), "
              + "religion varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table ReligiousServiceRequest already exist");
    }

    // Check Sanitation  table.
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE SanitationServiceRequest(request_id varchar(25), "
              + "sanitation_type varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table SanitationServiceRequest already exist");
    }

    // check FloralDeliveryServiceRequest
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE FloralDeliveryServiceRequest(request_id varchar(25), "
              + "flower varchar(25), "
              + "bouquet_type varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table Floral Delivery Service already exist");
    }

    // check GiftDeliveryServiceRequest
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE GiftDeliveryServiceRequest(request_id varchar(25), "
              + "gift_description varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table GiftDeliveryServiceRequest already exist");
    }

    // check MaintenanceServiceRequest
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE MaintenanceServiceRequest(request_id varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table MaintenanceServiceRequest already exist");
    }

    // check MedicineDeliveryServiceRequest
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE MedicineDeliveryServiceRequest(request_id varchar(25), "
              + "medicine_choice varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table MedicineDeliveryServiceRequest already exist");
    }

    // check SecurityServiceRequest
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE SecurityServiceRequest(request_id varchar(25), "
              + "PRIMARY KEY (request_id), "
              + "FOREIGN KEY (request_id) REFERENCES ServiceRequest(request_id) ON DELETE CASCADE)");

    } catch (SQLException e) {
      System.out.println("Table SecurityServiceRequest already exist");
    }

    // Initialize the database and input data
    //    if (!isInitialized) {
    //      LocationDerbyImpl.inputFromCSV(
    //          "TowerLocations", "edu/wpi/cs3733/c22/teamA/db/CSVs/TowerLocations.csv");
    //      EmployeeDerbyImpl.inputFromCSV("Employee",
    // "edu/wpi/cs3733/c22/teamA/db/CSVs/Employee.csv");
    //      EquipmentDerbyImpl.inputFromCSV(
    //          "MedicalEquipment", "edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipment.csv");
    //      EquipmentServiceRequestDerbyImpl.inputFromCSV(
    //          "MedicalEquipmentServiceRequest",
    //          "edu/wpi/cs3733/c22/teamA/db/CSVs/MedicalEquipmentServiceRequest.csv");
    //      ReligiousServiceRequestDerbyImpl.inputFromCSV(
    //          "ReligiousServiceRequest",
    //          "edu/wpi/cs3733/c22/teamA/db/CSVs/ReligiousServiceRequest.csv");
    //      SanitationServiceRequestDerbyImpl.inputFromCSV(
    //          "SanitationServiceRequest",
    //          "edu/wpi/cs3733/c22/teamA/db/CSVs/SanitationServiceRequest.csv");
    //      LaundryServiceRequestDerbyImpl.inputFromCSV(
    //          "LaundryServiceRequest",
    // "edu/wpi/cs3733/c22/teamA/db/CSVs/LaundryServiceRequest.csv");
    //      LanguageServiceRequestDerbyImpl.inputFromCSV(
    //          "LanguageServiceRequest",
    // "edu/wpi/cs3733/c22/teamA/db/CSVs/LanguageServiceRequest.csv");
    //      FoodDeliveryServiceRequestDerbyImpl.inputFromCSV(
    //          "FoodDeliveryServiceRequest",
    //          "edu/wpi/cs3733/c22/teamA/db/CSVs/FoodDeliveryServiceRequest.csv");
    //    }
  }

  public static void turnOnBuiltInUsers(Connection conn) throws SQLException {

    String setProperty = "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(";
    String getProperty = "VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY(";
    String requireAuth = "'derby.connection.requireAuthentication'";
    String defaultConnMode = "'derby.database.defaultConnectionMode'";
    String fullAccessUsers = "'derby.database.fullAccessUsers'";
    String readOnlyAccessUsers = "'derby.database.readOnlyAccessUsers'";
    String provider = "'derby.authentication.provider'";
    String propertiesOnly = "'derby.database.propertiesOnly'";

    System.out.println("Turning on authentication.");
    Statement s = conn.createStatement();

    // Set and confirm requireAuthentication
    s.executeUpdate(setProperty + requireAuth + ", 'true')");
    ResultSet rs = s.executeQuery(getProperty + requireAuth + ")");
    rs.next();
    System.out.println("Value of requireAuthentication is " + rs.getString(1));

    // Set authentication scheme to Derby builtin
    s.executeUpdate(setProperty + provider + ", 'BUILTIN')");

    // Create some sample users
    s.executeUpdate(setProperty + "'derby.user.Admin', 'admin')");
    s.executeUpdate(setProperty + "'derby.user.Guest', 'guest')");

    // Define noAccess as default connection mode
    s.executeUpdate(setProperty + defaultConnMode + ", 'noAccess')");

    // Confirm default connection mode
    rs = s.executeQuery(getProperty + defaultConnMode + ")");
    rs.next();
    System.out.println("Value of defaultConnectionMode is " + rs.getString(1));

    // Define read-write user
    s.executeUpdate(setProperty + fullAccessUsers + ", 'Admin')");

    // Define read-only user
    s.executeUpdate(setProperty + readOnlyAccessUsers + ", 'Guest')");

    // Confirm full-access users
    rs = s.executeQuery(getProperty + fullAccessUsers + ")");
    rs.next();
    System.out.println("Value of fullAccessUsers is " + rs.getString(1));

    // Confirm read-only users
    rs = s.executeQuery(getProperty + readOnlyAccessUsers + ")");
    rs.next();
    System.out.println("Value of readOnlyAccessUsers is " + rs.getString(1));

    // We would set the following property to TRUE only when we were
    // ready to deploy. Setting it to FALSE means that we can always
    // override using system properties if we accidentally paint
    // ourselves into a corner.
    s.executeUpdate(setProperty + propertiesOnly + ", 'false')");
    s.close();
  }

  public static void exportAllToCSV(String folderName)
      throws IOException, SQLException, ParseException, InvocationTargetException,
          IllegalAccessException {
    String csvBasePath = "src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/";
    String dirPath = csvBasePath + folderName;
    File newFolder = new File(dirPath);

    // Creating the directory
    boolean b = newFolder.mkdirs();
    if (b) {
      System.out.println("Directory created successfully");
    } else {
      System.out.println("Sorry couldnt create specified directory");
    }

    EmployeeDerbyImpl.exportToCSV("", dirPath + "/Employee.csv");

    LocationDerbyImpl.exportToCSV("", dirPath + "/TowerLocations.csv");

    EquipmentDerbyImpl.exportToCSV("", dirPath + "/MedicalEquipment.csv");

    // Service Requests
//    ServiceRequestDerbyImpl<EquipmentSR> equipmentSRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new EquipmentSR());
//    equipmentSRServiceRequestDerby.exportToCSV(dirPath + "/MedicalEquipmentServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<FloralDeliverySR> floralDeliverySRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new FloralDeliverySR());
//    floralDeliverySRServiceRequestDerby.exportToCSV(dirPath + "/FloralDeliveryServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<FoodDeliverySR> foodDeliverySRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new FoodDeliverySR());
//    foodDeliverySRServiceRequestDerby.exportToCSV(dirPath + "/FoodDeliveryServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<GiftDeliverySR> giftDeliverySRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new GiftDeliverySR());
//    giftDeliverySRServiceRequestDerby.exportToCSV(dirPath + "/GiftDeliveryServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<LanguageSR> languageSRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new LanguageSR());
//    languageSRServiceRequestDerby.exportToCSV(dirPath + "/LanguageServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<LaundrySR> laundrySRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new LaundrySR());
//    laundrySRServiceRequestDerby.exportToCSV(dirPath + "/LaundryServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<MaintenanceSR> maintenanceSRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new MaintenanceSR());
//    maintenanceSRServiceRequestDerby.exportToCSV(dirPath + "/MaintenanceServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<MedicineDeliverySR> medicineDeliverySRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new MedicineDeliverySR());
//    medicineDeliverySRServiceRequestDerby.exportToCSV(
//        dirPath + "/MedicineDeliveryServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<ReligiousSR> religiousSRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new ReligiousSR());
//    religiousSRServiceRequestDerby.exportToCSV(dirPath + "/ReligiousServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<SanitationSR> sanitationSRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new SanitationSR());
//    sanitationSRServiceRequestDerby.exportToCSV(dirPath + "/SanitationServiceRequest.csv");
//
//    ServiceRequestDerbyImpl<SecuritySR> securitySRServiceRequestDerby =
//        new ServiceRequestDerbyImpl<>(new SecuritySR());
//    securitySRServiceRequestDerby.exportToCSV(dirPath + "/SecurityServiceRequest.csv");
  }
}
