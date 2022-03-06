package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.MedicineDosage;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface MedicineDAO {

  Medicine getMedicine(String ID) throws IOException, ParseException;

  void updateMedicine(String ID, String field, String change) throws IOException;

  void enterMedicine(Medicine med) throws IOException;

  void enterMedicine(
      String medicineID,
      String genericName,
      String brandName,
      String medicineClass,
      String uses,
      String warnings,
      String sideEffects,
      String form,
      List<Float> dosageAmount) throws IOException;


  void enterMedicineDosage(String ID, Float dosage) throws IOException;


  void deleteMedicine(String ID) throws IOException;


  void deleteMedicineDosage(String ID, Float dosage) throws IOException;


  List<Float> getSpecificDosages(String ID) throws IOException, ParseException;

  List<Medicine> getMedicineList() throws IOException, ParseException;

  List<MedicineDosage> getAllDosages() throws IOException, ParseException;


}
