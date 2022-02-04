package edu.wpi.teama;

import edu.wpi.teama.Adb.Adb;
import java.sql.Connection;

public class Main {

  public static void main(String[] args) {

    Connection connection = null;
    Adb.initialConnection();
    Adb.inputFromCSV("TowerLocations", "edu/wpi/teama/db/TowerLocations.csv");
    Adb.inputFromCSV("Employee", "edu/wpi/teama/db/Employee.csv");

    Aapp.launch(Aapp.class, args);
  }
}
