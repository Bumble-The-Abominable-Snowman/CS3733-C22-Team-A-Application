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

  public static void initialConnection(String arg) {

    boolean isInitialized = false;
    // Connection to database driver
    System.out.println("----- Apache Derby Connection Testing -----");
    switch (arg) {
      case "EmbeddedDriver":
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
        pathToDBA = "//192.168.50.89:1527/HospitalDBA";

        try {
          Class.forName("org.apache.derby.jdbc." + arg);
          System.out.println("Apache Derby client driver registered!\n");
          break;

        } catch (ClassNotFoundException e) {
          System.out.println("Apache Derby Not Found");
          e.printStackTrace();
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
      e.printStackTrace();
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
          "CREATE TABLE TowerLocations(nodeID varchar(25), "
              + "xcoord int, "
              + "ycoord int, "
              + "floor varchar(25), "
              + "building varchar(25), "
              + "nodeType varchar(25), "
              + "longName varchar(100), "
              + "shortName varchar(50), "
              + "PRIMARY KEY (nodeID))");

    } catch (SQLException e) {
      System.out.println("Table TowerLocations already exist");
    }

    // Check Employee table.
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE Employee(employeeID varchar(25), "
              + "employeeType varchar(25), "
              + "firstName varchar(25), "
              + "lastName varchar(25), "
              + "email varchar(25), "
              + "phoneNum varchar(25), "
              + "address varchar(25), "
              + "startDate date, "
              + "PRIMARY KEY (employeeID))");

    } catch (SQLException e) {
      System.out.println("Table Employee already exist");
    }

    // Check MedicalEquipment table.
    try {

      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", pathToDBA));
      Statement addTable = connection.createStatement();

      addTable.execute(
          "CREATE TABLE MedicalEquipment(equipmentID varchar(25), "
              + "equipmentType varchar(25), "
              + "isClean varchar(25), "
              + "currentLocation varchar(25), "
              + "isAvailable varchar(25), "
              + "PRIMARY KEY (equipmentID),"
              + "FOREIGN KEY (currentLocation) REFERENCES TowerLocations(nodeID) ON DELETE CASCADE)");

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
          "CREATE TABLE ServiceRequest(requestID varchar(25), "
              + "startLocation varchar(25), "
              + "endLocation varchar(25), "
              + "employeeRequested varchar(25), "
              + "employeeAssigned varchar(25), "
              + "requestTime timestamp, "
              + "requestStatus varchar(25), "
              + "requestPriority varchar(25), "
              + "comments varchar(255), "
              + "PRIMARY KEY (requestID),"
              + "FOREIGN KEY (startLocation) REFERENCES TowerLocations(nodeID) ON DELETE CASCADE,"
              + "FOREIGN KEY (endLocation) REFERENCES TowerLocations(nodeID) ON DELETE CASCADE,"
              + "FOREIGN KEY (employeeRequested) REFERENCES Employee(employeeID) ON DELETE CASCADE,"
              + "FOREIGN KEY (employeeAssigned) REFERENCES Employee(employeeID) ON DELETE CASCADE)");

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
          "CREATE TABLE MedicalEquipmentServiceRequest(requestID varchar(25), "
              + "equipmentID varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE FoodDeliveryServiceRequest(requestID varchar(25), "
              + "mainDish varchar(50), "
              + "sideDish varchar(50), "
              + "beverage varchar(50), "
              + "dessert varchar(50), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE LanguageServiceRequest(requestID varchar(25), "
              + "language varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE LaundryServiceRequest(requestID varchar(25), "
              + "washMode varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE ReligiousServiceRequest(requestID varchar(25), "
              + "religion varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE SanitationServiceRequest(requestID varchar(25), "
              + "sanitationType varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE FloralDeliveryServiceRequest(requestID varchar(25), "
              + "flower varchar(25), "
              + "bouquetType varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE GiftDeliveryServiceRequest(requestID varchar(25), "
              + "giftDescription varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE MaintenanceServiceRequest(requestID varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE MedicineDeliveryServiceRequest(requestID varchar(25), "
              + "medicineChoice varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
          "CREATE TABLE SecurityServiceRequest(requestID varchar(25), "
              + "PRIMARY KEY (requestID), "
              + "FOREIGN KEY (requestID) REFERENCES ServiceRequest(requestID) ON DELETE CASCADE)");

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
    ServiceRequestDerbyImpl<EquipmentSR> equipmentSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new EquipmentSR());
    equipmentSRServiceRequestDerby.exportToCSV(dirPath + "/MedicalEquipmentServiceRequest.csv");

    ServiceRequestDerbyImpl<FloralDeliverySR> floralDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new FloralDeliverySR());
    floralDeliverySRServiceRequestDerby.exportToCSV(dirPath + "/FloralDeliveryServiceRequest.csv");

    ServiceRequestDerbyImpl<FoodDeliverySR> foodDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new FoodDeliverySR());
    foodDeliverySRServiceRequestDerby.exportToCSV(dirPath + "/FoodDeliveryServiceRequest.csv");

    ServiceRequestDerbyImpl<GiftDeliverySR> giftDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new GiftDeliverySR());
    giftDeliverySRServiceRequestDerby.exportToCSV(dirPath + "/GiftDeliveryServiceRequest.csv");

    ServiceRequestDerbyImpl<LanguageSR> languageSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new LanguageSR());
    languageSRServiceRequestDerby.exportToCSV(dirPath + "/LanguageServiceRequest.csv");

    ServiceRequestDerbyImpl<LaundrySR> laundrySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new LaundrySR());
    laundrySRServiceRequestDerby.exportToCSV(dirPath + "/LaundryServiceRequest.csv");

    ServiceRequestDerbyImpl<MaintenanceSR> maintenanceSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new MaintenanceSR());
    maintenanceSRServiceRequestDerby.exportToCSV(dirPath + "/MaintenanceServiceRequest.csv");

    ServiceRequestDerbyImpl<MedicineDeliverySR> medicineDeliverySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new MedicineDeliverySR());
    medicineDeliverySRServiceRequestDerby.exportToCSV(
        dirPath + "/MedicineDeliveryServiceRequest.csv");

    ServiceRequestDerbyImpl<ReligiousSR> religiousSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new ReligiousSR());
    religiousSRServiceRequestDerby.exportToCSV(dirPath + "/ReligiousServiceRequest.csv");

    ServiceRequestDerbyImpl<SanitationSR> sanitationSRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new SanitationSR());
    sanitationSRServiceRequestDerby.exportToCSV(dirPath + "/SanitationServiceRequest.csv");

    ServiceRequestDerbyImpl<SecuritySR> securitySRServiceRequestDerby =
        new ServiceRequestDerbyImpl<>(new SecuritySR());
    securitySRServiceRequestDerby.exportToCSV(dirPath + "/SecurityServiceRequest.csv");
  }
}
