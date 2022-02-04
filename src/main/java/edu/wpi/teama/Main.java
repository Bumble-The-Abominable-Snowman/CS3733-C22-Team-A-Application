package edu.wpi.teama;

import edu.wpi.teama.Adb.Adb;

public class Main {

  public static void main(String[] args) {

    Adb.initialConnection();

    Aapp.launch(Aapp.class, args);
  }
}
