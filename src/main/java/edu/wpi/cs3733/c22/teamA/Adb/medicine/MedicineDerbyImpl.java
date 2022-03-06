package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.MedicineDosage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MedicineDerbyImpl implements MedicineDAO {
  public MedicineDerbyImpl() {}

  public Medicine getMedicine(String ID) {
    try {
      Statement get = Adb.connection.createStatement();

      String strMed = String.format("SELECT * FROM Medicine WHERE medicine_id = '%s'", ID);

      ResultSet rset = get.executeQuery(strMed);
      Medicine med = new Medicine();
      if (rset.next()) {
        med.setFieldByString("medicine_id", rset.getString("medicine_id"));
        med.setFieldByString("generic_name", rset.getString("generic_name"));
        med.setFieldByString("brand_name", rset.getString("brand_name"));
        med.setFieldByString("medicine_class", rset.getString("medicine_class"));
        med.setFieldByString("uses", rset.getString("uses"));
        med.setFieldByString("warnings", rset.getString("warnings"));
        med.setFieldByString("side_effects", rset.getString("side_effects"));
        med.setFieldByString("form", rset.getString("form"));
      }

      med.setField("dosage_amounts", (List<Float>) getSpecificDosages(med.getStringFields().get("medicine_id")));
      return med;

    } catch (SQLException e) {
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
      System.out.println("Error caught");

    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void updateMedicine(String ID, String field, String change) {
    try {
      Statement update = Adb.connection.createStatement();

      String str =
          String.format("UPDATE Medicine SET %s = '%s' WHERE medicine_id = '%s'", field, change, ID);

      //System.out.println(str);
      update.execute(str);

    } catch (SQLException e) {
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
      System.out.println("Error caught");
    }
  }

  public void enterMedicine(Medicine med) {
    enterMedicine(
        med.getStringFields().get("medicine_id"),
        med.getStringFields().get("generic_name"),
        med.getStringFields().get("brand_name"),
        med.getStringFields().get("medicine_class"),
        med.getStringFields().get("uses"),
        med.getStringFields().get("warnings"),
        med.getStringFields().get("side_effects"),
        med.getStringFields().get("form"),
            (List<Float>) med.getFields().get("dosage_amounts"));
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
      List<Float> dosageAmount) {
    try {
      Statement insert = Adb.connection.createStatement();
      String strMed =
              String.format(
                      "INSERT INTO Medicine(medicine_id, generic_name, brand_name, medicine_class, uses, warnings, side_effects, form)"
                              + " VALUES('%s', '%s','%s','%s','%s','%s','%s','%s')",
                      medicineID, genericName, brandName, medicineClass, uses, warnings, sideEffects, form);

      insert.executeUpdate(strMed);

    } catch (SQLException e) {
      System.out.println("Error caught during enter");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
    }
  }

  public void enterMedicineDosage(String ID, Float dosage) {
    try {
      Statement insert = Adb.connection.createStatement();
      String str =
          String.format(
              "INSERT INTO MedicineDosage(medicine_id, dosage_amount) VALUES('%s', %f)", ID, dosage);
      insert.execute(str);

    } catch (SQLException e) {
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
    }
  }

  /**
   * Delete a specific Dosage
   * @param ID The specifc ID
   * @param dosage The specifc Dosage
   */
  public void deleteMedicineDosage(String ID, Float dosage) {
    try {
      Statement delete = Adb.connection.createStatement();
      delete.execute(
          String.format(
              "DELETE FROM MedicineDosage WHERE (medicine_id = '%s' AND dosage_amount = '%f')",
              ID, dosage));

    } catch (SQLException e) {
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
    }
  }

  /**
   * Delete a specific medicine
   * @param ID The ID of the medicine you want to delete
   */
  public void deleteMedicine(String ID) {
    try {
      Statement delete = Adb.connection.createStatement();
      delete.execute(String.format("DELETE FROM MedicineDosage WHERE medicine_id = '%s'", ID));

      delete.execute(String.format("DELETE FROM Medicine WHERE medicine_id = '%s'", ID));

    } catch (SQLException e) {
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
    }
  }


  /****************** GET LIST FUNCTIONS *************************/
  /**
   * Get all the dosages for a specific dosage
   * @param ID
   * @return
   */
  public List<Float> getSpecificDosages(String ID) {
    try {
      Statement get = Adb.connection.createStatement();

      ResultSet rset =
          get.executeQuery(
              String.format("SELECT * FROM MedicineDosage WHERE medicine_id = '%s'", ID));

      List<Float> returnList = new ArrayList<>();
      while (rset.next()) {
        returnList.add(rset.getFloat("dosage_amount"));
      }
      return returnList;
    } catch (SQLException e) {
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());

      return null;
    }
  }


  /**
   * Get a list of MedicineDosage entites to export to csv
   * @return
   */
  public List<MedicineDosage> getAllDosages(){
    try{
      Statement get = Adb.connection.createStatement();
      String str = String.format("SELECT * FROM MedicineDosage");
      ResultSet rset = get.executeQuery(str);

      List<MedicineDosage> dosList = new ArrayList<>();
      while(rset.next()){
        MedicineDosage thisDos = new MedicineDosage(
                rset.getString("medicine_id"),
                rset.getFloat("dosage_amount")
        );

        dosList.add(thisDos);
      }


      return dosList;
    } catch(SQLException e){
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
      return null;
    }
  }


  // get all medicine
  public List<Medicine> getMedicineList() {
    try {
      Statement get = Adb.connection.createStatement();
      String str = String.format("SELECT * FROM Medicine");
      ResultSet rset = get.executeQuery(str);
      List<Medicine> returnList = new ArrayList<>();
      while (rset.next()) {
        Medicine med = new Medicine();
        med.setFieldByString("medicine_id", rset.getString("medicine_id"));
        med.setFieldByString("generic_name", rset.getString("generic_name"));
        med.setFieldByString("brand_name", rset.getString("brand_name"));
        med.setFieldByString("medicine_class", rset.getString("medicine_class"));
        med.setFieldByString("uses", rset.getString("uses"));
        med.setFieldByString("warnings", rset.getString("warnings"));
        med.setFieldByString("side_effects", rset.getString("side_effects"));
        med.setFieldByString("form", rset.getString("form"));

        med.setField("dosage_amounts", (List<Float>) getSpecificDosages(med.getStringFields().get("medicine_id")));
        returnList.add(med);
      }
      //System.out.println(returnList);
      return returnList;

    } catch (SQLException e) {
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());

    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
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

    MedicineDerbyImpl derby = new MedicineDerbyImpl();
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


      ClassLoader classLoader = MedicineDerbyImpl.class.getClassLoader();
      InputStream is = classLoader.getResourceAsStream(medicineCSVFilePath);
      Scanner lineScanner = new Scanner(is);


    Scanner dataScanner;
    int dataIndex = 0;

    lineScanner.nextLine();
    while (lineScanner.hasNext()) { // Scan CSV line by line
      //System.out.println("Starting New Line");
      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      Medicine med = new Medicine();

      while (dataScanner.hasNext()) { // Scan CSV Line data by data
        String data = dataScanner.next();
        data = data.trim();
        if (dataIndex == 0) med.setFieldByString("medicine_id", data);
        else if (dataIndex == 1) med.setFieldByString("generic_name", data);
        else if (dataIndex == 2) med.setFieldByString("brand_name", data);
        else if (dataIndex == 3) med.setFieldByString("medicine_class", data);
        else if (dataIndex == 4) med.setFieldByString("uses", data);
        else if (dataIndex == 5) med.setFieldByString("warnings", data);
        else if (dataIndex == 6) med.setFieldByString("side_effects", data);
        else if (dataIndex == 7) med.setFieldByString("form", data);
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      med.setField("dosage_amounts", null);
      //System.out.println(thisMed);
      medicineList.add(med);
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
    try{
      Statement dropTable = Adb.connection.createStatement();
      dropTable.execute("DELETE FROM MedicineDosage");
      List<MedicineDosage> dosList = readDosagesFromCSV(dosageCSVFilePath);
      //System.out.println("Printing dosList:");
      //System.out.println(dosList);
      MedicineDerbyImpl derby = new MedicineDerbyImpl();

      for(MedicineDosage thisDos: dosList){
        derby.enterMedicineDosage(thisDos.getMedicine_id(), thisDos.getDosage_amount());
      }

    } catch(Exception e){
      System.out.println("Error Caught! Uh Oh!");
      System.out.println("Error Code: " + e.getMessage());
    }
  }

  public static List<MedicineDosage> readDosagesFromCSV(String dosageCSVFilePath)
          throws FileNotFoundException {
    List<MedicineDosage> dosList = new ArrayList<>();
    ClassLoader classLoader = MedicineDerbyImpl.class.getClassLoader();
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
    MedicineDAO medicineDerby = new MedicineDerbyImpl();
    List<Medicine> medicineList = ((MedicineDerbyImpl) medicineDerby).getMedicineList();
    writeMedicineToCSV(medicineList, medicineCSVFilePath);
  }

  public static void exportDosagesToCSV(String dosageCSVFilePath) throws IOException {
    MedicineDerbyImpl derby = new MedicineDerbyImpl();
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
