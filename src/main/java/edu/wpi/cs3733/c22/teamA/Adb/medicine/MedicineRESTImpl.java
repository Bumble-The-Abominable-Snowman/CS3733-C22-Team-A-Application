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

  public Medicine getMedicine(String ID) throws IOException {
    HashMap<String, String> map = new HashMap<>();
    map.put("operation", "get");
    map.put("medicine_id", ID);
    HashMap<String, String> resp = Adb.getREST(url, map);
    Medicine medicine = new Medicine();
    for (String key: resp.keySet()) {
//      medicine.setFieldByString(key, resp.get(key));
    }
    return medicine;
  }

  public void updateMedicine(String ID, String field, String change) {
//    HashMap<String, String> map = e.getStringFields();
//    map.put("operation", "update");
//    Adb.postREST(url, map);

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

  public void enterMedicineDosage(String ID, Float dosage) {
//    try {
//      Statement insert = Adb.connection.createStatement();
//      String str =
//          String.format(
//              "INSERT INTO MedicineDosage(medicine_id, dosage_amount) VALUES('%s', %f)", ID, dosage);
//      insert.execute(str);
//
//    } catch (SQLException e) {
//      System.out.println("Error caught");
//      System.out.println("Error Code: " + e.getErrorCode());
//      System.out.println("SQL State: " + e.getSQLState());
//      System.out.println(e.getMessage());
//    }
  }

  /**
   * Delete a specific Dosage
   * @param ID The specifc ID
   * @param dosage The specifc Dosage
   */
  public void deleteMedicineDosage(String ID, Float dosage) {
//    try {
//      Statement delete = Adb.connection.createStatement();
//      delete.execute(
//          String.format(
//              "DELETE FROM MedicineDosage WHERE (medicine_id = '%s' AND dosage_amount = '%f')",
//              ID, dosage));
//
//    } catch (SQLException e) {
//      System.out.println("Error caught");
//      System.out.println("Error Code: " + e.getErrorCode());
//      System.out.println("SQL State: " + e.getSQLState());
//      System.out.println(e.getMessage());
//    }
  }

  /**
   * Delete a specific medicine
   * @param ID The ID of the medicine you want to delete
   */
  public void deleteMedicine(String ID) throws IOException {
    HashMap<String, String> map = new HashMap<>();
    map.put("medicine_id", ID);
    map.put("operation", "delete");
    Adb.postREST(url, map);
  }


  /****************** GET LIST FUNCTIONS *************************/
  /**
   * Get all the dosages for a specific dosage
   * @param ID
   * @return
   */
  public List<Float> getSpecificDosages(String ID) {
//    try {
//      Statement get = Adb.connection.createStatement();
//
//      ResultSet rset =
//          get.executeQuery(
//              String.format("SELECT * FROM MedicineDosage WHERE medicine_id = '%s'", ID));
//
//      List<Float> returnList = new ArrayList<>();
//      while (rset.next()) {
//        returnList.add(rset.getFloat("dosage_amount"));
//      }
//      return returnList;
//    } catch (SQLException e) {
//      System.out.println("Error caught");
//      System.out.println("Error Code: " + e.getErrorCode());
//      System.out.println("SQL State: " + e.getSQLState());
//      System.out.println(e.getMessage());
//
//      return null;
//    }
    return new ArrayList<>();
  }


  /**
   * Get a list of MedicineDosage entites to export to csv
   * @return
   */
  public List<MedicineDosage> getAllDosages(){
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
    return new ArrayList<>();
  }


  // get all medicine
  public List<Medicine> getMedicineList() throws IOException {
    List<Medicine> arrayList = new ArrayList<>();
    List<HashMap<String, String>> mapArrayList = Adb.getAllREST(url);
    for (HashMap<String, String> map: mapArrayList) {
      Medicine l = new Medicine();
      for (String key: map.keySet()) {
//        l.setFieldByString(key, map.get(key));
      }
      arrayList.add(l);
    }

    return arrayList;
  }


  /********** IMPORT FUNCTIONS  *********************/




  /**
   * Import medicine from a csv file
   * Note: This function does not do dosages
   * @param medicineCSVFilePath
   */
  public static void importMedicineFromCSV(String medicineCSVFilePath) {
    // Delete all contents of both table
    try {
      Statement dropTable = Adb.connection.createStatement();
      dropTable.execute("DELETE FROM MedicineDosage");
      dropTable.execute("DELETE FROM Medicine");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }

    //Read all values from the medicine CSV
    List<Medicine> medicineList = new ArrayList<>();
    try {
      medicineList = readMedicineCSV(medicineCSVFilePath);
      //System.out.println("Printing out medicineList: ");
      //System.out.println(medicineList);
    } catch (IOException | ParseException e){
      System.out.println("Error caught while trying to read CSV");
      System.out.println(e.getMessage());

    }

    MedicineRESTImpl derby = new MedicineRESTImpl();
    try{
      for(Medicine med : medicineList){
        derby.enterMedicine(med);
      }
    } catch (Exception e) {
      System.out.println("Error caught when trying to enter");
      System.out.println("Error Code: " + e.getMessage());
    }
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
        if (dataIndex == 0) thisMed.setMedicineID(data);
        else if (dataIndex == 1) thisMed.setGenericName(data);
        else if (dataIndex == 2) thisMed.setBrandName(data);
        else if (dataIndex == 3) thisMed.setMedicineClass(data);
        else if (dataIndex == 4) thisMed.setUses(data);
        else if (dataIndex == 5) thisMed.setWarnings(data);
        else if (dataIndex == 6) thisMed.setSideEffects(data);
        else if (dataIndex == 7) thisMed.setForm(data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      thisMed.setDosageAmounts(null);
      //System.out.println(thisMed);
      medicineList.add(thisMed);
      dataIndex = 0;
    }
    return medicineList;
  }

  /**
   * Import dosages from a csv file
   * NOTE: this function does not check or anything if foreign key stuff
   * @param dosageCSVFilePath
   */
  public static void importDosagesFromCSV(String dosageCSVFilePath){
//    try{
//      Statement dropTable = Adb.connection.createStatement();
//      dropTable.execute("DELETE FROM MedicineDosage");
//      List<MedicineDosage> dosList = readDosagesFromCSV(dosageCSVFilePath);
//      //System.out.println("Printing dosList:");
//      //System.out.println(dosList);
//      MedicineRESTImpl derby = new MedicineRESTImpl();
//
//      for(MedicineDosage thisDos: dosList){
//        derby.enterMedicineDosage(thisDos.getMedicine_id(), thisDos.getDosage_amount());
//      }
//
//    } catch(Exception e){
//      System.out.println("Error Caught! Uh Oh!");
//      System.out.println("Error Code: " + e.getMessage());
//    }
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
      throws IOException {
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
              med.getMedicineID(),
              med.getGenericName(),
              med.getBrandName(),
              med.getMedicineClass(),
              med.getUses(),
              med.getWarnings(),
              med.getSideEffects(),
              med.getForm());
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
