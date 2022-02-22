package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
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

      String strMed = String.format("SELECT * FROM Medicine WHERE medicineID = '%s'", ID);

      ResultSet rset = get.executeQuery(strMed);
      Medicine med = new Medicine();
      if (rset.next()) {
        med.setMedicineID(rset.getString("medicineID"));
        med.setGenericName(rset.getString("genericName"));
        med.setBrandName(rset.getString("brandName"));
        med.setMedicineClass(rset.getString("medicineClass"));
        med.setUses(rset.getString("uses"));
        med.setWarnings(rset.getString("warnings"));
        med.setSideEffects(rset.getString("sideEffects"));
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
          String.format("UPDATE Medicine SET %s = '%s' WHERE medicineID = '%s'", field, change, ID);

      System.out.println(str);
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
              "INSERT INTO Medicine(medicineID, genericName, brandName, medicineClass, uses, warnings, sideEffects, form)"
                  + " VALUES('%s', '%s','%s','%s','%s','%s','%s','%s')",
              medicineID, genericName, brandName, medicineClass, uses, warnings, sideEffects, form);

      insert.executeUpdate(strMed);
      for (Float dos : dosageAmount) {
        String f = dos.toString();
        String strDos =
            String.format(
                "INSERT INTO MedicineDosage(medicineID, dosageAmount)" + " VALUES('%s',%f)",
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
              "INSERT INTO MedicineDosage(medicineID, dosageAmount) VALUES('%s', %f)", ID, dosage);
      insert.execute(str);

    } catch (SQLException e) {
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
    }
  }

  public void deleteMedicineDosage(String ID, Float dosage) {
    try {
      Statement delete = Adb.connection.createStatement();
      delete.execute(
          String.format(
              "DELETE FROM MedicineDosage WHERE (medicineID = '%s' AND dosageAmount = '%f')",
              ID, dosage));

    } catch (SQLException e) {
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
    }
  }

  public void deleteMedicine(String ID) {
    try {
      Statement delete = Adb.connection.createStatement();
      delete.execute(String.format("DELETE FROM MedicineDosage WHERE medicineID = '%s'", ID));

      delete.execute(String.format("DELETE FROM Medicine WHERE medicineID = '%s'", ID));

    } catch (SQLException e) {
      System.out.println("Error caught");
      System.out.println("Error Code: " + e.getErrorCode());
      System.out.println("SQL State: " + e.getSQLState());
      System.out.println(e.getMessage());
    }
  }

  public List<Float> getDosages(String ID) {
    try {
      Statement get = Adb.connection.createStatement();

      ResultSet rset =
          get.executeQuery(
              String.format("SELECT * FROM MedicineDosage WHERE medicineID = '%s'", ID));

      List<Float> returnList = new ArrayList<>();
      while (rset.next()) {
        returnList.add(rset.getFloat("dosageAmount"));
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

  public List<Medicine> getMedicineList() {
    try {
      Statement get = Adb.connection.createStatement();
      String str = String.format("SELECT * FROM Medicine");
      ResultSet rset = get.executeQuery(str);
      List<Medicine> returnList = new ArrayList<>();
      while (rset.next()) {
        Medicine med = new Medicine();
        med.setMedicineID(rset.getString("medicineID"));
        med.setGenericName(rset.getString("genericName"));
        med.setBrandName(rset.getString("brandName"));
        med.setMedicineClass(rset.getString("medicineClass"));
        med.setUses(rset.getString("uses"));
        med.setWarnings(rset.getString("warnings"));
        med.setSideEffects(rset.getString("sideEffects"));
        med.setForm(rset.getString("form"));

        med.setDosageAmounts(getDosages(med.getMedicineID()));
        returnList.add(med);
      }
      System.out.println(returnList);
      return returnList;

    } catch (SQLException e) {
      System.out.println("Error caught");
      return null;
    }
  }

  public static void inputFromCSV(String medicineCSVFilePath, String dosageCSVFilePath) {
    // Delete all contents of both table
    try {
      Statement dropTable = Adb.connection.createStatement();
      dropTable.execute("DELETE FROM MedicineDosage");
      dropTable.execute("DELETE FROM Medicine");
    } catch (SQLException e) {
      System.out.println("delete failed");
    }
    try {
      List<Medicine> medicineList = readMedicineCSV(medicineCSVFilePath, dosageCSVFilePath);

    } catch (Exception e) {
      System.out.println("Insertion failed!");
    }
  }

  public static List<Medicine> readMedicineCSV(String medicineCSVFilePath, String dosageCSVFilePath)
      throws IOException, ParseException {
    List<Medicine> medicineList = new ArrayList<>();

    // Go through medicine CSV file
    File medFile = new File(medicineCSVFilePath);
    Scanner lineScanner = new Scanner(medFile);
    Scanner dataScanner;
    int dataIndex = 0;
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

      thisMed.setDosageAmounts(new ArrayList<Float>());
      dataIndex = 0;
      medicineList.add(thisMed);
    }

    // Go through Dosage CSV File
    File dosFile = new File(dosageCSVFilePath);
    lineScanner = new Scanner(dosFile);
    dataIndex = 0;
    while (lineScanner.hasNext()) {
      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      String thisID = "";
      String thisDosage = "";
      while (dataScanner.hasNext()) {
        String data = dataScanner.next();
        if (dataIndex == 0) thisID = data;
        if (dataIndex == 1) thisDosage = data;

        dataIndex++;
      }

      for (Medicine med : medicineList) {
        if (med.getMedicineID().equals(thisID))
          med.getDosageAmounts().add(Float.parseFloat(thisDosage));
      }
      dataIndex = 0;
    }

    return medicineList;
  }

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
        "medicineID,genericName,brandName,medicineClass,uses,warnings,sideEffect,form";
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

    String dosTitleString = "medicineID, dosageAmount";
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
