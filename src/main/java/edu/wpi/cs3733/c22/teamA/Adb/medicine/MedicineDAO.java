package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.MedicineDosage;

import java.io.IOException;
import java.util.List;

public interface MedicineDAO {

  Medicine getMedicine(String ID) throws IOException;

  void updateMedicine(String ID, String field, String change);

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


  void enterMedicineDosage(String ID, Float dosage);


  void deleteMedicine(String ID) throws IOException;


  void deleteMedicineDosage(String ID, Float dosage);


  List<Float> getSpecificDosages(String ID);

  List<Medicine> getMedicineList() throws IOException;

  List<MedicineDosage> getAllDosages();


}
