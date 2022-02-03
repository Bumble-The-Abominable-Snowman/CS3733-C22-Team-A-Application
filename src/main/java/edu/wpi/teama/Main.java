package edu.wpi.teama;

import edu.wpi.teama.Adb.Adb;
import java.sql.Connection;

public class Main {

  public static void main(String[] args) {

    Connection connection = null;
    Adb.initialConnection();
    Adb.inputFromCSV("TowerLocations");
    Adb.inputFromCSV("MedicalEquipmentServiceRequest");
    Adb.inputFromCSV("Employee");

    Aapp.launch(Aapp.class, args);
  }
}
