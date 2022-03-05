package edu.wpi.cs3733.c22.teamA.Adb.medicalequipment;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationRESTImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class EquipmentWrapperImpl implements EquipmentDAO {
    public EquipmentWrapperImpl()  {}

    @Override
    public Equipment getMedicalEquipment(String ID) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new EquipmentDerbyImpl()).getMedicalEquipment(ID);}
        else
        {return (new EquipmentRESTImpl()).getMedicalEquipment(ID);}
    }

    @Override
    public void updateMedicalEquipment(Equipment e) throws SQLException, IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EquipmentRESTImpl()).updateMedicalEquipment(e);}
        else
        {(new EquipmentRESTImpl()).updateMedicalEquipment(e);}

    }

    @Override
    public void enterMedicalEquipment(Equipment e) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EquipmentRESTImpl()).enterMedicalEquipment(e);}
        else
        {(new EquipmentRESTImpl()).enterMedicalEquipment(e);}

    }

    @Override
    public void enterMedicalEquipment(String equipmentID, String equipmentType, boolean isClean, String currentLocation, boolean isAvailable) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EquipmentRESTImpl()).enterMedicalEquipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable);}
        else
        {(new EquipmentRESTImpl()).enterMedicalEquipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable);}

    }

    @Override
    public void deleteMedicalEquipment(String ID) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EquipmentRESTImpl()).deleteMedicalEquipment(ID);}
        else
        {(new EquipmentRESTImpl()).deleteMedicalEquipment(ID);}

    }

    @Override
    public List<Equipment> getMedicalEquipmentList() throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new EquipmentRESTImpl()).getMedicalEquipmentList();}
        else
        {return (new EquipmentDerbyImpl()).getMedicalEquipmentList();}

    }
}
