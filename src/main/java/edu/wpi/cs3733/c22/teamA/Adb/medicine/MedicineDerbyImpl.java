package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.Adb.Adb;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDerbyImpl {
  public MedicineDerbyImpl() {}

  public Medicine getMedicine(String ID) {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement get = connection.createStatement();

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
      return null;
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
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement insert = connection.createStatement();
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
                "INSERT INTO MedicineDosage(medicineID, dosageAmount)" + "VALUES('%s','%s')",
                medicineID, f);
        insert.executeUpdate(strDos);
      }

    } catch (SQLException e) {
      System.out.println("Error caught");
    }
  }

  public void enterMedicineDosage(String ID, Float dosage) {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement insert = connection.createStatement();
      String str =
          String.format(
              "INSERT INTO MedicineDosage(medicineID, dosageAmount) VALUES('%s', %f)", ID, dosage);
      insert.execute(str);

    } catch (SQLException e) {
      System.out.println("Error caught");
    }
  }

  public void deleteMedicineDosage(String ID, Float dosage) {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement delete = connection.createStatement();
      delete.execute(
          String.format(
              "DELETE FROM MedicineDosage WHERE (medicineID = '%s' AND dosageAmount = '%f')",
              ID, dosage));

    } catch (SQLException e) {
      System.out.println("Error caught");
    }
  }

  public void deleteMedicine(String ID) {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement delete = connection.createStatement();
      delete.execute(String.format("DELETE FROM MedicineDosage WHERE medicineID = '%s'", ID));

      delete.execute(String.format("DELETE FROM Medicine WHERE medicineID = '%s'", ID));

    } catch (SQLException e) {
      System.out.println("Error caught");
    }
  }

  public List<Float> getDosages(String ID) {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement get = connection.createStatement();

      ResultSet rset =
          get.executeQuery(String.format("SELECT FROM Medicine WHERE medicineID = '%s'", ID));

      List<Float> returnList = new ArrayList<>();
      while (rset.next()) {
        returnList.add(rset.getFloat("dosageAmount"));
      }
      return returnList;
    } catch (SQLException e) {
      System.out.println("Error caught");
      return null;
    }
  }

  public List<Medicine> getMedicineList() {
    try {
      Connection connection =
          DriverManager.getConnection(
              String.format("jdbc:derby:%s;user=Admin;password=admin", Adb.pathToDBA));
      Statement get = connection.createStatement();
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
      }
      return returnList;

    } catch (SQLException e) {
      System.out.println("Error caught");
      return null;
    }
  }

}
