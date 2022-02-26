package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.MedicineDosage;

import java.util.List;

public interface MedicineDAO {

  public Medicine getMedicine(String ID);


  public void enterMedicine(Medicine med);

  public void enterMedicine(
      String medicineID,
      String genericName,
      String brandName,
      String medicineClass,
      String uses,
      String warnings,
      String sideEffects,
      String form,
      List<Float> dosageAmount);


  public void enterMedicineDosage(String ID, Float dosage);


  public void deleteMedicine(String ID);


  public void deleteMedicineDosage(String ID, Float dosage);


  public List<Float> getSpecificDosages(String ID);

  public List<Medicine> getMedicineList();

  public List<MedicineDosage> getAllDosages();


}
