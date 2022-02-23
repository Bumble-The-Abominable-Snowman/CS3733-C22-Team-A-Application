package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import java.util.List;

public interface MedicineDAO {

  public Medicine getMedicine(String ID);

  /**
   * Create a new instance of Medicine in the Database
   *
   * @param med A medicine object
   */
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

  /**
   * Enter a new dosage amount for a specific medicine
   *
   * @param ID The ID of the medicine you are adding a new dosage for
   * @param dosage A new dosage amount you are adding
   */
  public void enterMedicineDosage(String ID, Float dosage);

  /**
   * Delete a complete copy of medicine (The medicine and all its dosage amounts
   *
   * @param ID The ID of the medicine you want to delete
   */
  public void deleteMedicine(String ID);

  /**
   * Delete a specific dosage for a medicine
   *
   * @param ID The specifc ID
   * @param dosage The specifc Dosage
   */
  public void deleteMedicineDosage(String ID, Float dosage);

  /**
   * Get a list of dosages for a specific medicine
   *
   * @param ID
   * @return
   */
  public List<Float> getDosages(String ID);

  public List<Medicine> getMedicineList();
}
