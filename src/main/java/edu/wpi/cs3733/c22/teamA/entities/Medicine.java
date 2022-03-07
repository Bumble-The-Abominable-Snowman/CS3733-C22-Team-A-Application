package edu.wpi.cs3733.c22.teamA.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;

public class Medicine extends RecursiveTreeObject<Medicine> {

  private String medicineID; // ID of this medicine (Primary Key of table)
  private String
      genericName; // Long fancy pharmacy name(similar to active Ingredient) (ex. ibuprofen)
  private String brandName; // Name given to this drug by its manufacturer (ex. Advil)
  private String medicineClass; // Type of drug (pharmacy term used to make sure patient doesnt take
  // conflicting drugs)
  private String uses; // What disease state this drug is used for (ex. Pain and fever)
  private String warnings; // "Do not take while (x,y,z)"
  private String sideEffects; // Listed sideeffects of the medicine (ex. Nausea)
  private String form; // Method of drug delivery (tablet, liquid, IV, etc.)

  protected HashMap<String, Object> fields = new HashMap<>();
  protected HashMap<String, String> fields_string = new HashMap<>();


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
    this.setField("medicine_id", medicineID);
    this.setField("generic_name", genericName);
    this.setField("brand_name", brandName);
    this.setField("medicine_class", medicineClass);
    this.setField("uses", uses);
    this.setField("warnings", warnings);
    this.setField("side_effects", sideEffects);
    this.setField("form", form);
    this.setField("dosage_amount", dosageAmounts);
  }

  public List<String> getListForm(){
    return List.of(
            this.fields_string.get("medicine_id"),
            this.fields_string.get("generic_name"),
            this.fields_string.get("brand_name"),
            this.fields_string.get("medicine_class"),
            this.fields_string.get("uses"),
            this.fields_string.get("warnings"),
            this.fields_string.get("side_effects"),
            this.fields_string.get("form"),
            this.fields_string.get("dosage_amount")
    );
  }

  public HashMap<String, String> getStringFields() {return this.fields_string;}

  public HashMap<String, Object> getFields() {
    return fields;
  }

  public void setField(String key, Object value) {
    if (Objects.equals(key, "dosage_amount"))
    {
      this.fields.put(key, null);
      this.fields_string.put(key, "");
      for (Float d: ((List<Float>) value)) {
        this.fields_string.put(key, this.fields_string.get(key) + d + " ");
      }
      this.fields.put(key, value);
    }
    else
    {
      this.fields.put(key, value);
      this.fields_string.put(key, String.valueOf(value));
    }

  }

  public void setFieldByString(String key, String value) throws ParseException {
    if (Objects.equals(key, "dosage_amount")) {
      try
      {
        ArrayList<Float> temp = new ArrayList<Float>();
        this.fields_string.put(key, "");
        for (String d: value.split(" ")) {
          this.fields_string.put(key, this.fields_string.get(key) + d + " ");

          DecimalFormatSymbols symbols = new DecimalFormatSymbols();
          symbols.setDecimalSeparator('.');
          DecimalFormat decimalFormat = new DecimalFormat("#.000");
          decimalFormat.setDecimalFormatSymbols(symbols);

          this.fields.put(key, temp.add(decimalFormat.parse(value).floatValue()));
          System.out.println(d);
          System.out.println(decimalFormat.parse(value).floatValue());
        }
      } catch (ParseException e)
      {
        System.out.println("parse exception");
        this.fields_string.put(key, "");
        this.fields.put(key, null);
      }

    }else {
      this.fields.put(key, value);
      this.fields_string.put(key, value);
    }
  }

}
