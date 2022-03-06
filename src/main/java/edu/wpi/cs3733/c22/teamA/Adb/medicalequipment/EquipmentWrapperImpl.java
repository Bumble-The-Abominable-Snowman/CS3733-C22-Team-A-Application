package edu.wpi.cs3733.c22.teamA.Adb.medicalequipment;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestRESTImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
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

public class EquipmentWrapperImpl implements EquipmentDAO {
    public EquipmentWrapperImpl()  {}

    @Override
    public Equipment getMedicalEquipment(String ID) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new EquipmentRESTImpl()).getMedicalEquipment(ID);}
        else
        {return (new EquipmentDerbyImpl()).getMedicalEquipment(ID);}
    }

    @Override
    public void updateMedicalEquipment(Equipment e) throws SQLException, IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EquipmentRESTImpl()).updateMedicalEquipment(e);}
        else
        {(new EquipmentDerbyImpl()).updateMedicalEquipment(e);}

    }

    @Override
    public void enterMedicalEquipment(Equipment e) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EquipmentRESTImpl()).enterMedicalEquipment(e);}
        else
        {(new EquipmentDerbyImpl()).enterMedicalEquipment(e);}

    }

    @Override
    public void enterMedicalEquipment(String equipmentID, String equipmentType, boolean isClean, String currentLocation, boolean isAvailable) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EquipmentRESTImpl()).enterMedicalEquipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable);}
        else
        {(new EquipmentDerbyImpl()).enterMedicalEquipment(equipmentID, equipmentType, isClean, currentLocation, isAvailable);}

    }

    @Override
    public void deleteMedicalEquipment(String ID) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EquipmentRESTImpl()).deleteMedicalEquipment(ID);}
        else
        {(new EquipmentDerbyImpl()).deleteMedicalEquipment(ID);}

    }

    @Override
    public List<Equipment> getMedicalEquipmentList() throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new EquipmentRESTImpl()).getMedicalEquipmentList();}
        else
        {return (new EquipmentDerbyImpl()).getMedicalEquipmentList();}

    }

    public void inputFromCSVfile(String csvFilePath) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EquipmentRESTImpl()).inputFromCSVfile(csvFilePath);}
        else {EquipmentDerbyImpl.inputFromCSVfile(csvFilePath);}

    }

    public void exportToCSV(String csvFilePath) throws Exception {

        // Get list of this type of service Requests
        List<Equipment> list;
        if (App.DB_CHOICE.equals("nosql")) {
            list = (new EquipmentRESTImpl()).getMedicalEquipmentList();
        } else {
            list = (new EquipmentDerbyImpl()).getMedicalEquipmentList();
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
                tleString.append(key);
            }
            String firstLine = tleString.toString().substring(0, tleString.toString().length() - 2);

            writer.write(firstLine);
            writer.newLine();

            // rows
            for (Equipment thisEq : list) {
                String str = "";

                boolean first_column = true;
                for (String key : keys) {
                    if (first_column) {
                        str = thisEq.getStringFields().get(key);
                        first_column = false;
                    } else {
                        str = String.join(",", str, thisEq.getStringFields().get(key));
                    }
                }

                writer.write(str);
                writer.newLine();
            }
            writer.close(); // close the writer
        } else {
            throw new Exception("Eq list is empty!");
        }
    }

}
