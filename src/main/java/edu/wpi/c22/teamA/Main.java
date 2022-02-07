package edu.wpi.c22.teamA;

import edu.wpi.c22.teamA.Adb.Adb;

public class Main {

  public static void main(String[] args) {

    Adb.initialConnection();

    Aapp.launch(Aapp.class, args);
  }
}
