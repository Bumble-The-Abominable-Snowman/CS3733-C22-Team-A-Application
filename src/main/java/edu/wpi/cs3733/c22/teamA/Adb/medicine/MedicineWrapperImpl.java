package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestRESTImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.MedicineDosage;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

public class MedicineWrapperImpl implements MedicineDAO {
    public MedicineWrapperImpl()  {}

    @Override
    public Medicine getMedicine(String ID) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new MedicineRESTImpl()).getMedicine(ID);}
        else
        {return (new MedicineDerbyImpl()).getMedicine(ID);}

    }

    @Override
    public void updateMedicine(Medicine m) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).updateMedicine(m);}
        else
        {(new MedicineDerbyImpl()).updateMedicine(m);}

    }

    @Override
    public void enterMedicine(Medicine med) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).enterMedicine(med);}
        else
        {(new MedicineDerbyImpl()).enterMedicine(med);}

    }

    @Override
    public void enterMedicine(String medicineID, String genericName, String brandName, String medicineClass, String uses, String warnings, String sideEffects, String form, List<Float> dosageAmount) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).enterMedicine(medicineID, genericName, brandName, medicineClass, uses, warnings, sideEffects, form, dosageAmount);}
        else
        {(new MedicineDerbyImpl()).enterMedicine(medicineID, genericName, brandName, medicineClass, uses, warnings, sideEffects, form, dosageAmount);}

    }

    @Override
    public void enterMedicineDosage(String ID, Float dosage) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).enterMedicineDosage(ID, dosage);}
        else
        {(new MedicineDerbyImpl()).enterMedicineDosage(ID, dosage);}

    }

    @Override
    public void deleteMedicine(String ID) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).deleteMedicine(ID);}
        else
        {(new MedicineDerbyImpl()).deleteMedicine(ID);}

    }

    @Override
    public void deleteMedicineDosage(String ID, Float dosage) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).deleteMedicineDosage(ID, dosage);}
        else
        {(new MedicineDerbyImpl()).deleteMedicineDosage(ID, dosage);}

    }

    @Override
    public List<Float> getSpecificDosages(String ID) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new MedicineRESTImpl()).getSpecificDosages(ID);}
        else
        {return (new MedicineDerbyImpl()).getSpecificDosages(ID);}

    }

    @Override
    public List<Medicine> getMedicineList() throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new MedicineRESTImpl()).getMedicineList();}
        else
        {return (new MedicineDerbyImpl()).getMedicineList();}
    }

    @Override
    public List<MedicineDosage> getAllDosages() throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new MedicineRESTImpl()).getAllDosages();}
        else
        {return (new MedicineDerbyImpl()).getAllDosages();}
    }

    public void importMedicineFromCSV(String csvFilePath) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).importMedicineFromCSV(csvFilePath);}
        else {MedicineDerbyImpl.importMedicineFromCSV(csvFilePath);}

    }
    public void importDosagesFromCSV(String csvFilePath) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).importDosagesFromCSV(csvFilePath);}
        else {MedicineDerbyImpl.importDosagesFromCSV(csvFilePath);}

    }

    public void exportMedicineToCSV(String csvFilePath) throws Exception {

        // Get list of this type of service Requests
        List<Medicine> list;
        if (App.DB_CHOICE.equals("nosql")) {
            list = (new MedicineRESTImpl()).getMedicineList();
        } else {
            list = (new MedicineDerbyImpl()).getMedicineList();
        }

        if (list.size() > 0) {

            File file = new File(csvFilePath);
            file.createNewFile();
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilePath));

            StringBuilder tleString = new StringBuilder();

            // column line
            Set<String> keys = list.get(0).getStringFields().keySet();
            for (String key : keys) {
                key = key + ", ";
                if (!(key.equals("dosage_amount, "))) {
                    tleString.append(key);
                }
            }
            String firstLine = tleString.toString().substring(0, tleString.toString().length() - 2);

            writer.write(firstLine);
            writer.newLine();

            // rows
            for (Medicine thisMed : list) {
                String str = "";

                boolean first_column = true;
                for (String key : keys) {
                    if (!(key.equals("dosage_amount"))) {
                        if (first_column) {
                            str = thisMed.getStringFields().get(key);
                            first_column = false;
                        } else {
                            str = String.join(",", str, thisMed.getStringFields().get(key));
                        }
                    }
                }

                writer.write(str);
                writer.newLine();
            }
            writer.close(); // close the writer
        } else {
            throw new Exception("Medicine list is empty!");
        }
    }

    public void exportDosagesToCSV(String csvFilePath) throws IOException, ParseException {
        // Get list of this type of service Requests
        List<MedicineDosage> list;
        if (App.DB_CHOICE.equals("nosql")) {
            list = (new MedicineRESTImpl()).getAllDosages();
        } else {
            list = (new MedicineDerbyImpl()).getAllDosages();
        }

        File dosFile = new File(csvFilePath);
        dosFile.createNewFile();
        BufferedWriter dosWriter = Files.newBufferedWriter(Paths.get(csvFilePath));

        String dosTitleString = "medicine_id, dosage_amount";
        dosWriter.write(dosTitleString);
        dosWriter.newLine();

        for (MedicineDosage thisDos : list) {
            if (thisDos.getDosage_amount() != null)
            {
                String thisLine = String.join(",", thisDos.getMedicine_id(), thisDos.getDosage_amount().toString());
                dosWriter.write(thisLine);
                dosWriter.newLine();
            }
        }
        dosWriter.close();

    }
}
