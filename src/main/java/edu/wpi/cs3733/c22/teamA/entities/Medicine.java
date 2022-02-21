package edu.wpi.cs3733.c22.teamA.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.List;

public class Medicine extends RecursiveTreeObject<Medicine> {

  private String medicineID; // ID of this medicine (Primary Key of table)
  private String genericName; // Long fancy pharmacy name(similar to active Ingredient) (ex. ibuprofen)
  private String brandName; // Name given to this drug by its manufacturer (ex. Advil)
  private String medicineClass; // Type of drug (pharmacy term used to make sure patient doesnt take conflicting drugs)
  private String uses; // What disease state this drug is used for (ex. Pain and fever)
  private String warnings; // "Do not take while (x,y,z)"
  private String sideEffects; // Listed sideeffects of the medicine (ex. Nausea)
  private String form; // Method of drug delivery (tablet, liquid, IV, etc.)

  private List<Float> dosageAmounts; // Different dosage amounts (in milligrams)

  public Medicine() {}

  public Medicine(
      String medicineID,
      String genericName,
      String brandName,
      String medicineClass,
      String uses,
      String warnings,
      String sideEffects,
      String form,
      List<Float> dosageAmounts) {
    this.medicineID = medicineID;
    this.genericName = genericName;
    this.brandName = brandName;
    this.medicineClass = medicineClass;
    this.uses = uses;
    this.warnings = warnings;
    this.sideEffects = sideEffects;
    this.form = form;
    this.dosageAmounts = dosageAmounts;
  }

  public String getMedicineID() {
    return this.medicineID;
  }

  public void setMedicineID(String medicineID) {
    this.medicineID = medicineID;
  }

  public String getGenericName() {
    return this.genericName;
  }

  public void setGenericName(String genericName) {
    this.genericName = genericName;
  }

  public String getBrandName() {
    return this.brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getMedicineClass() {
    return this.medicineClass;
  }

  public void setMedicineClass(String medicineClass) {
    this.medicineClass = medicineClass;
  }

  public String getUses() {
    return this.uses;
  }

  public void setUses(String uses) {
    this.uses = uses;
  }

  public String getWarnings() {
    return this.warnings;
  }

  public void setWarnings(String warnings) {
    this.warnings = warnings;
  }

  public String getSideEffects() {
    return this.sideEffects;
  }

  public void setSideEffects(String sideEffects) {
    this.sideEffects = sideEffects;
  }

  public String getForm() {
    return this.form;
  }

  public void setForm(String form) {
    this.form = form;
  }

  public List<Float> getDosageAmounts() {
    return this.dosageAmounts;
  }

  public void setDosageAmounts(List<Float> dosageAmounts) {
    this.dosageAmounts = dosageAmounts;
  }
}
