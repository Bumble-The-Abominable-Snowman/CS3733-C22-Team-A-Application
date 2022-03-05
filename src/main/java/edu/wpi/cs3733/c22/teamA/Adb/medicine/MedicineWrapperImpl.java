package edu.wpi.cs3733.c22.teamA.Adb.medicine;

import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentRESTImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.MedicineDosage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MedicineWrapperImpl implements MedicineDAO {
    public MedicineWrapperImpl()  {}

    @Override
    public Medicine getMedicine(String ID) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new MedicineRESTImpl()).getMedicine(ID);}
        else
        {return (new MedicineDerbyImpl()).getMedicine(ID);}

    }

    @Override
    public void updateMedicine(String ID, String field, String change) {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).updateMedicine(ID, field, change);}
        else
        {(new MedicineDerbyImpl()).updateMedicine(ID, field, change);}

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
    public void enterMedicineDosage(String ID, Float dosage) {
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
    public void deleteMedicineDosage(String ID, Float dosage) {
        if (App.DB_CHOICE.equals("nosql"))
        {(new MedicineRESTImpl()).deleteMedicineDosage(ID, dosage);}
        else
        {(new MedicineDerbyImpl()).deleteMedicineDosage(ID, dosage);}

    }

    @Override
    public List<Float> getSpecificDosages(String ID) {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new MedicineRESTImpl()).getSpecificDosages(ID);}
        else
        {return (new MedicineDerbyImpl()).getSpecificDosages(ID);}

    }

    @Override
    public List<Medicine> getMedicineList() throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new MedicineRESTImpl()).getMedicineList();}
        else
        {return (new MedicineDerbyImpl()).getMedicineList();}
    }

    @Override
    public List<MedicineDosage> getAllDosages() {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new MedicineRESTImpl()).getAllDosages();}
        else
        {return (new MedicineDerbyImpl()).getAllDosages();}
    }
}
