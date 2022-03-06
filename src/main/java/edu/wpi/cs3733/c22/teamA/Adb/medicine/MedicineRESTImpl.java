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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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

  public void updateMedicine(String ID, String field, String change) throws IOException {
    HashMap<String, String> metadata = new HashMap<>();
    metadata.put("operation", "update");

    HashMap<String, String> payload = new HashMap<>();
    metadata.put(field, change);
    Adb.postREST(url, metadata, payload);
  }

  public void enterMedicine(Medicine med) throws IOException {
//    HashMap<String, String> map = med.getStringFields();
//    map.put("operation", "add");
//    Adb.postREST(url, map);
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
    Medicine medicine = new Medicine(medicineID,
            genericName,
            brandName,
            medicineClass,
            uses,
            warnings,
            sideEffects,
            form,
            dosageAmount);
    enterMedicine(medicine);
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
    return (List<Float>) medicine.getFields().get("dosage_amounts");
  }


  /**
   * Get a list of MedicineDosage entites to export to csv
   * @return
   */
  public List<MedicineDosage> getAllDosages() throws IOException {
//    try{
//      Statement get = Adb.connection.createStatement();
//      String str = String.format("SELECT * FROM MedicineDosage");
//      ResultSet rset = get.executeQuery(str);
//
//      List<MedicineDosage> dosList = new ArrayList<>();
//      while(rset.next()){
//        MedicineDosage thisDos = new MedicineDosage(
//                rset.getString("medicine_id"),
//                rset.getFloat("dosage_amount")
//        );
//
//        dosList.add(thisDos);
//      }
//
//
//      return dosList;
//    } catch(SQLException e){
//      System.out.println("Error caught");
//      System.out.println("Error Code: " + e.getErrorCode());
//      System.out.println("SQL State: " + e.getSQLState());
//      System.out.println(e.getMessage());
//      return null;
//    }

//    List<MedicineDosage> arrayList = new ArrayList<>();
//    List<HashMap<String, String>> mapArrayList = Adb.getAllREST(url);
//    for (HashMap<String, String> map: mapArrayList) {
//      MedicineDosage l = new MedicineDosage();
//      for (String key: map.keySet()) {
//        l.setFieldByString(key, map.get(key));
//        if (key.equals("dosage_amounts"))
//        {
//          arrayList.add(map.get(key));
//        }
//      }
//      arrayList.add(l);
//    }
//
//    return arrayList;


//    List<List<Float>> arrayList = new ArrayList<>();
//    List<HashMap<String, String>> mapArrayList = Adb.getREST(url);
//    for (HashMap<String, String> map: mapArrayList) {
//      Medicine m = new Medicine();
//      for (String key: map.keySet()) {
//        if (key.equals("dosage_amounts"))
//        {
//          List<Float> l = (List<Float>) List.of(map.get(key));
//          arrayList.add(List.of(map.get(key)));
//        }
//        m.setFieldByString(key, map.get(key));
//      }
//      arrayList.add(m);
//    }
//
//    return arrayList;
//
    return new ArrayList<>();
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

  public static List<Medicine> readMedicineCSV(String medicineCSVFilePath)
      throws IOException, ParseException {
    //System.out.println("Starting readMedicineCSV");
    List<Medicine> medicineList = new ArrayList<>();

    // Go through medicine CSV file


      ClassLoader classLoader = MedicineRESTImpl.class.getClassLoader();
      InputStream is = classLoader.getResourceAsStream(medicineCSVFilePath);
      Scanner lineScanner = new Scanner(is);


    Scanner dataScanner;
    int dataIndex = 0;

    lineScanner.nextLine();
    while (lineScanner.hasNext()) { // Scan CSV line by line
      //System.out.println("Starting New Line");
      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      Medicine thisMed = new Medicine();

      while (dataScanner.hasNext()) { // Scan CSV Line data by data
        String data = dataScanner.next();
        data = data.trim();
        if (dataIndex == 0) thisMed.setFieldByString("medicine_id", data);
        else if (dataIndex == 1) thisMed.setFieldByString("generic_name", data);
        else if (dataIndex == 2) thisMed.setFieldByString("brand_name", data);
        else if (dataIndex == 3) thisMed.setFieldByString("medicine_class", data);
        else if (dataIndex == 4) thisMed.setFieldByString("uses", data);
        else if (dataIndex == 5) thisMed.setFieldByString("warnings", data);
        else if (dataIndex == 6) thisMed.setFieldByString("side_effects", data);
        else if (dataIndex == 7) thisMed.setFieldByString("form", data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      thisMed.setFieldByString("dosage_amounts", null);
      medicineList.add(thisMed);
      dataIndex = 0;
    }
    return medicineList;
  }

  /**
   * Import dosages from a csv file
   * NOTE: this function does not check or anything if foreign key stuff
   * @param csvFilePath
   */
  public void importDosagesFromCSV(String csvFilePath) throws IOException {

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
    metadata_map.put("table-name", "dosages");

    Adb.populate_db(url, metadata_map, med_list);
  }

  public static List<MedicineDosage> readDosagesFromCSV(String dosageCSVFilePath)
          throws FileNotFoundException {
    List<MedicineDosage> dosList = new ArrayList<>();
    ClassLoader classLoader = MedicineRESTImpl.class.getClassLoader();
    InputStream is = classLoader.getResourceAsStream(dosageCSVFilePath);
    Scanner lineScanner = new Scanner(is);
    int dataIndex = 0;

    lineScanner.nextLine();
    while (lineScanner.hasNext()) {
      Scanner dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      MedicineDosage thisDos = new MedicineDosage();

      while (dataScanner.hasNext()) {
        String data = dataScanner.next();
        data = data.trim();
        if (dataIndex == 0) thisDos.setMedicine_id(data);
        else if (dataIndex == 1) thisDos.setDosage_amount(Float.parseFloat(data));
        else System.out.println("Invalid data, I broke::" + data);


        dataIndex++;
      }

      //System.out.println(thisDos);
      dosList.add(thisDos);
      dataIndex = 0;
    }
    return dosList;
  }

  /***************** EXPORT FUNCTIONS *************************************************/
  //To export to csv

  public static void exportMedicineToCSV(String medicineCSVFilePath)
          throws IOException, ParseException {
    MedicineDAO medicineDerby = new MedicineRESTImpl();
    List<Medicine> medicineList = ((MedicineRESTImpl) medicineDerby).getMedicineList();
    writeMedicineToCSV(medicineList, medicineCSVFilePath);
  }

  public static void exportDosagesToCSV(String dosageCSVFilePath) throws IOException {
    MedicineRESTImpl derby = new MedicineRESTImpl();
    List<MedicineDosage> dosList = derby.getAllDosages();
    //System.out.println("Printing dosList");
    //System.out.println(dosList);
    writeDosagesToCSV(dosList, dosageCSVFilePath);
  }

  public static void writeMedicineToCSV(
      List<Medicine> medicineList, String medicineCSVFilePath)
      throws IOException {

    // Export all Medicine

    File medFile = new File(medicineCSVFilePath);
    medFile.createNewFile();
    BufferedWriter medWriter = Files.newBufferedWriter(Paths.get(medicineCSVFilePath));

    String medTitleLine =
        "medicine_id,generic_name,brand_name,medicine_class,uses,warnings,side_effect,form";
    medWriter.write(medTitleLine);
    medWriter.newLine();
    for (Medicine med : medicineList) {
      String thisLine =
          String.join(
              ",",
                  med.getStringFields().get("medicine_id"),
                  med.getStringFields().get("generic_name"),
                  med.getStringFields().get("brand_name"),
                  med.getStringFields().get("medicine_class"),
                  med.getStringFields().get("uses"),
                  med.getStringFields().get("warnings"),
                  med.getStringFields().get("side_effects"),
                  med.getStringFields().get("form"));
      medWriter.write(thisLine);
      medWriter.newLine();
    }
    medWriter.close();

  }


  public static void writeDosagesToCSV(
          List<MedicineDosage> dosList, String dosageCSVFilePath) throws IOException {
    // Export Dosage Information
    File dosFile = new File(dosageCSVFilePath);
    dosFile.createNewFile();
    BufferedWriter dosWriter = Files.newBufferedWriter(Paths.get(dosageCSVFilePath));

    String dosTitleString = "medicine_id, dosage_amount";
    dosWriter.write(dosTitleString);
    dosWriter.newLine();

    for (MedicineDosage thisDos : dosList) {
        String thisLine = String.join(",", thisDos.getMedicine_id(), thisDos.getDosage_amount().toString());
        dosWriter.write(thisLine);
        dosWriter.newLine();
    }
    dosWriter.close();
  }



}
