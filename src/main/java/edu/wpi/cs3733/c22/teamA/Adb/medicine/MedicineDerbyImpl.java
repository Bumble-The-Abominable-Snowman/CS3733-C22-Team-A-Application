package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
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
        med.setMedicineID(rset.getString("medicine_id"));
        med.setGenericName(rset.getString("generic_name"));
        med.setBrandName(rset.getString("brand_name"));
        med.setMedicineClass(rset.getString("medicine_class"));
        med.setUses(rset.getString("uses"));
        med.setWarnings(rset.getString("warnings"));
        med.setSideEffects(rset.getString("side_effects"));
        med.setForm(rset.getString("form"));
      }

      med.setDosageAmounts(getDosages(ID));
      return med;

    } catch (SQLException e) {
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
      System.out.println("Error caught");

      return null;
    }
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
        med.getMedicineID(),
        med.getGenericName(),
        med.getBrandName(),
        med.getMedicineClass(),
        med.getUses(),
        med.getWarnings(),
        med.getSideEffects(),
        med.getForm(),
        med.getDosageAmounts());
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
      for (Float dos : dosageAmount) {
        String f = dos.toString();
        String strDos =
            String.format(
                "INSERT INTO MedicineDosage(medicine_id, dosage_amount)" + " VALUES('%s',%f)",
                medicineID, dos);
        insert.executeUpdate(strDos);
      }

    } catch (SQLException e) {
      System.out.println("Error caught");
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

  /**
   * Get all the dosages for a specific dosage
   * @param ID
   * @return
   */
  public List<Float> getDosages(String ID) {
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


  // get all medicine
  public List<Medicine> getMedicineList() {
    try {
      Statement get = Adb.connection.createStatement();
      String str = String.format("SELECT * FROM Medicine");
      ResultSet rset = get.executeQuery(str);
      List<Medicine> returnList = new ArrayList<>();
      while (rset.next()) {
        Medicine med = new Medicine();
        med.setMedicineID(rset.getString("medicine_id"));
        med.setGenericName(rset.getString("generic_name"));
        med.setBrandName(rset.getString("brand_name"));
        med.setMedicineClass(rset.getString("medicine_class"));
        med.setUses(rset.getString("uses"));
        med.setWarnings(rset.getString("warnings"));
        med.setSideEffects(rset.getString("side_effects"));
        med.setForm(rset.getString("form"));

        med.setDosageAmounts(getDosages(med.getMedicineID()));
        returnList.add(med);
      }
      //System.out.println(returnList);
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
   * Import dosages from a csv file
   * NOTE: this function does not check or anything if foreign key stuff
   * @param dosageCSVFilePath
   */
  public static void importDosagesFromCSV(String dosageCSVFilePath){
    try{
      Statement dropTable = Adb.connection.createStatement();
      dropTable.execute("DELETE FROM MedicineDosage");
      List<MedicineDosage> dosList = readDosagesFromCSV(dosageCSVFilePath);
      MedicineDerbyImpl derby = new MedicineDerbyImpl();

      for(MedicineDosage thisDos: dosList){
        derby.enterMedicineDosage(thisDos.getMedicine_id(), thisDos.getDosage_amount());
      }

    } catch(Exception e){
      System.out.println("Error Caught! Uh Oh!");
      System.out.println("Error Code: " + e.getMessage());
    }
  }

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
    try {
      List<Medicine> medicineList = readMedicineCSV(medicineCSVFilePath);
      MedicineDerbyImpl derby = new MedicineDerbyImpl();
      for(Medicine med : medicineList){
        derby.enterMedicine(med);
      }

    } catch (Exception e) {
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getMessage());
    }
  }

  public static List<Medicine> readMedicineCSV(String medicineCSVFilePath)
      throws IOException, ParseException {
    List<Medicine> medicineList = new ArrayList<>();

    // Go through medicine CSV file
    File medFile = new File(medicineCSVFilePath);
    Scanner lineScanner = new Scanner(medFile);
    Scanner dataScanner;
    int dataIndex = 0;

    lineScanner.nextLine();
    while (lineScanner.hasNext()) { // Scan CSV line by line
      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      Medicine thisMed = new Medicine();

      while (dataScanner.hasNext()) { // Scan CSV Line data by data
        String data = dataScanner.next();
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
      medicineList.add(thisMed);
      dataIndex = 0;
    }
    return medicineList;
  }

  public static List<MedicineDosage> readDosagesFromCSV(String dosageCSVFilePath)
          throws FileNotFoundException {
    List<MedicineDosage> dosList = new ArrayList<>();
    File dosFile = new File(dosageCSVFilePath);
    Scanner lineScanner = new Scanner(dosFile);
    int dataIndex = 0;

    lineScanner.nextLine();
    while (lineScanner.hasNext()) {
      Scanner dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      String thisID = "";
      String thisDosage = "";
      while (dataScanner.hasNext()) {
        String data = dataScanner.next();
        if (dataIndex == 0) thisID = data;
        if (dataIndex == 1) thisDosage = data;

        dataIndex++;
      }
      MedicineDosage thisDos = new MedicineDosage(thisID, Float.parseFloat(thisDosage));
      dosList.add(thisDos);
      dataIndex = 0;
    }
    return dosList;
  }

  /** EXPORT FUNCTIONS *************************************************/

  public static void exportToCSV(String medicineCSVFilePath, String dosageCSVFilePath)
      throws IOException {
    MedicineDAO medicineDerby = new MedicineDerbyImpl();
    List<Medicine> medicineList = ((MedicineDerbyImpl) medicineDerby).getMedicineList();
    writeToCSV(medicineList, medicineCSVFilePath, dosageCSVFilePath);
  }

  public static void writeToCSV(
      List<Medicine> medicineList, String medicineCSVFilePath, String dosageCSVFilePath)
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

    // Export Dosage Information
    File dosFile = new File(dosageCSVFilePath);
    dosFile.createNewFile();
    BufferedWriter dosWriter = Files.newBufferedWriter(Paths.get(dosageCSVFilePath));

    String dosTitleString = "medicine_id, dosage_amount";
    dosWriter.write(dosTitleString);
    dosWriter.newLine();

    for (Medicine med : medicineList) {
      for (Float dosageAmount : med.getDosageAmounts()) {
        String thisLine = String.join(",", med.getMedicineID(), dosageAmount.toString());
        dosWriter.write(thisLine);
        dosWriter.newLine();
      }
    }
    dosWriter.close();
  }
}
