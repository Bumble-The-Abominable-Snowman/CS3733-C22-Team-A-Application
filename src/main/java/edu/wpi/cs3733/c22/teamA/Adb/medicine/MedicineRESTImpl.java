package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.MedicineDosage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;

public class MedicineRESTImpl implements MedicineDAO {

  private final String url = "https://cs3733c22teama.ddns.net/api/medicine";

  public MedicineRESTImpl() {}

  public Medicine getMedicine(String ID) throws IOException, ParseException {
    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "get");
    metadata.put("medicine_id", ID);
    HashMap<String, String> resp = Adb.getREST(url, metadata);
    Medicine medicine = new Medicine();
    for (String key: resp.keySet()) {
      medicine.setFieldByString(key, resp.get(key));
    }
    return medicine;
  }

  public void updateMedicine(Medicine m) throws IOException {
//    HashMap<String, String> metadata = new HashMap<>();
//    metadata.put("operation", "update");
//
//    HashMap<String, String> payload = new HashMap<>();
//    payload.put(field, change);
//    payload.put("medicine_id", ID);
//    Adb.postREST(url, metadata, payload);

    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "update");
    Adb.postREST(url, metadata, m.getStringFields());

  }

  public void enterMedicine(Medicine med) throws IOException {
    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "add");
    Adb.postREST(url, metadata, med.getStringFields());

  }

  /**
   * Enters a Medicine into Medicine Table and its dosages into the dosage table
   */
  public void enterMedicine(
      String medicineID,
      String genericName,
      String brandName,
      String medicineClass,
      String uses,
      String warnings,
      String sideEffects,
      String form,
      List<Float> dosageAmount) throws IOException {

    Medicine med = new Medicine(medicineID,
            genericName,
            brandName,
            medicineClass,
            uses,
            warnings,
            sideEffects,
            form,
            dosageAmount);
    enterMedicine(med);
  }

  public void enterMedicineDosage(String ID, Float dosage) throws IOException {

    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "enter_dosage");

    HashMap<String, String> payload = new HashMap<>();
    metadata.put("medicine_id", ID);
    payload.put("dosage", String.valueOf(dosage));
    Adb.postREST(url, metadata, payload);

  }

  /**
   * Delete a specific Dosage
   * @param ID The specifc ID
   * @param dosage The specifc Dosage
   */
  public void deleteMedicineDosage(String ID, Float dosage) throws IOException {

    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "delete_dosage");

    HashMap<String, String> payload = new HashMap<>();
    payload.put("dosage", String.valueOf(dosage));
    Adb.postREST(url, metadata, payload);

  }

  /**
   * Delete a specific medicine
   * @param ID The ID of the medicine you want to delete
   */
  public void deleteMedicine(String ID) throws IOException {
    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "delete");
    metadata.put("medicine_id", ID);
    Adb.postREST(url, metadata, new HashMap<>());

  }


  /****************** GET LIST FUNCTIONS *************************/
  /**
   * Get all the dosages for a specific dosage
   * @param ID
   * @return
   */
  public List<Float> getSpecificDosages(String ID) throws IOException, ParseException {
    Medicine medicine = getMedicine(ID);
    return (List<Float>) medicine.getFields().get("dosage_amount");
  }


  /**
   * Get a list of MedicineDosage entites to export to csv
   * @return
   */
  public List<MedicineDosage> getAllDosages() throws IOException, ParseException {
    List<MedicineDosage> arrayList = new ArrayList<>();
    List<HashMap<String, String>> mapArrayList = Adb.getAllREST(url);
    for (HashMap<String, String> map: mapArrayList) {

      for (String dosage_s: map.get("dosage_amount").split(" ")) {
        MedicineDosage d = new MedicineDosage();
        d.setMedicine_id(map.get("medicine_id"));

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.000");
        decimalFormat.setDecimalFormatSymbols(symbols);

        d.setDosage_amount(decimalFormat.parse(dosage_s).floatValue());
        arrayList.add(d);
      }

    }

    return arrayList;
  }


  // get all medicine
  public List<Medicine> getMedicineList() throws IOException, ParseException {
    List<Medicine> arrayList = new ArrayList<>();
    List<HashMap<String, String>> mapArrayList = Adb.getAllREST(url);
    for (HashMap<String, String> map: mapArrayList) {
      Medicine m = new Medicine();
      for (String key: map.keySet()) {
        m.setFieldByString(key, map.get(key));
      }
      arrayList.add(m);
    }

    return arrayList;

  }


  /********** IMPORT FUNCTIONS  *********************/




  /**
   * Import medicine from a csv file
   * Note: This function does not do dosages
   * @param csvFilePath
   */
  public void importMedicineFromCSV(String csvFilePath) throws IOException {
    // Delete all contents of both table

    File file = new File(csvFilePath);
    Scanner lineScanner = new Scanner(file);

    Scanner dataScanner;
    int dataIndex = 0;
    List<String> field_list = new ArrayList<>();

    dataScanner = new Scanner(lineScanner.nextLine());
    dataScanner.useDelimiter(",");
    while (dataScanner.hasNext()) {
      field_list.add(dataScanner.next().toLowerCase(Locale.ROOT).strip());
    }

    ArrayList<HashMap<String, String>> med_list = new ArrayList<>();
    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");

      HashMap<String, String> med_h = new HashMap<>();
      while (dataScanner.hasNext()) {
        String data = dataScanner.next().strip();

        String columnName = field_list.get(dataIndex);
        med_h.put(columnName, data);
        dataIndex++;
      }
      med_list.add(med_h);

      dataIndex = 0;
    }
    lineScanner.close();

    HashMap<String, String> metadata_map = new HashMap<>();
    metadata_map.put("operation", "populate");
    metadata_map.put("table-name", "medicine");

    Adb.populate_db(url, metadata_map, med_list);
  }

  /**
   * Import dosages from a csv file
   * NOTE: this function does not check or anything if foreign key stuff
   * @param csvFilePath
   */
  public void importDosagesFromCSV(String csvFilePath) throws IOException {

    File file = new File(csvFilePath);
    Scanner lineScanner = new Scanner(file);

//    ClassLoader classLoader = MedicineRESTImpl.class.getClassLoader();
//    InputStream is = classLoader.getResourceAsStream(medicineCSVFilePath);
//    Scanner lineScanner = new Scanner(is);


    Scanner dataScanner;
    int dataIndex = 0;
    List<String> field_list = new ArrayList<>();

    dataScanner = new Scanner(lineScanner.nextLine());
    dataScanner.useDelimiter(",");
    while (dataScanner.hasNext()) {
      field_list.add(dataScanner.next().toLowerCase(Locale.ROOT).strip());
    }

    ArrayList<HashMap<String, String>> med_list = new ArrayList<>();
    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");

      HashMap<String, String> med_h = new HashMap<>();
      while (dataScanner.hasNext()) {
        String data = dataScanner.next().strip();

        String columnName = field_list.get(dataIndex);
        med_h.put(columnName, data);
        dataIndex++;
      }
      med_list.add(med_h);

      dataIndex = 0;
    }
    lineScanner.close();

    HashMap<String, String> metadata_map = new HashMap<>();
    metadata_map.put("operation", "populate");
    metadata_map.put("table-name", "dosages");

    Adb.populate_db(url, metadata_map, med_list);
  }
}
