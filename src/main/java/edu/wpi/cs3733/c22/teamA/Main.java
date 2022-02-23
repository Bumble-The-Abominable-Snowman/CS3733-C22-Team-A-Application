package edu.wpi.cs3733.c22.teamA;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;

public class Main {

  public static void main(String[] args) {

    Adb.initialConnection("EmbeddedDriver");
    App.launch(App.class, args);
  }
}
