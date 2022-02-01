package edu.wpi.teama;

import java.sql.Connection;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    Aapp.launch(Aapp.class, args);
  }

  // This is the main function of the db repo. This needs to be integrated into the rest of the
  // code. For now, the aapp.launch is blocking, so can't add it to the main main function.
  public static void main_db(String[] args) throws Exception {
    System.out.println("calling CSV");
    List<Location> locList = ReadCSV.readCSV();

    Connection connection = null;
    Adb.initialConnection(locList);
    // App.launch(App.class, args);

    return;
  }
}
