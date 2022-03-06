package edu.wpi.cs3733.c22.teamA.Adb.location;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentRESTImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class LocationWrapperImpl implements LocationDAO {
    public LocationWrapperImpl()  {}

    @Override
    public List<Location> getNodeList() throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new LocationRESTImpl()).getNodeList();}
        else
        {return (new LocationDerbyImpl()).getNodeList();}
    }

    @Override
    public void deleteLocationNode(String ID) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new LocationRESTImpl()).deleteLocationNode(ID);}
        else
        {(new LocationDerbyImpl()).deleteLocationNode(ID);}

    }

    @Override
    public void enterLocationNode(Location location) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new LocationRESTImpl()).enterLocationNode(location);}
        else
        {(new LocationDerbyImpl()).enterLocationNode(location);}

    }

    @Override
    public void enterLocationNode(String ID, int xcoord, int ycoord, String floor, String building, String nodeType, String longName, String shortName) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new LocationRESTImpl()).enterLocationNode(ID, xcoord, ycoord, floor, building, nodeType, longName, shortName);}
        else
        {(new LocationDerbyImpl()).enterLocationNode(ID, xcoord, ycoord, floor, building, nodeType, longName, shortName);}

    }

    @Override
    public void updateLocation(Location location) throws SQLException, IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new LocationRESTImpl()).updateLocation(location);}
        else
        {(new LocationDerbyImpl()).updateLocation(location);}

    }

    @Override
    public Location getLocationNode(String ID) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new LocationRESTImpl()).getLocationNode(ID);}
        else
        {return (new LocationDerbyImpl()).getLocationNode(ID);}

    }

    public void inputFromCSVfile(String csvFilePath) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new LocationRESTImpl()).inputFromCSVfile(csvFilePath);}
        else {LocationDerbyImpl.inputFromCSVfile(csvFilePath);}

    }

    public void exportToCSV(String csvFilePath) throws Exception {

        // Get list of this type of service Requests
        List<Location> list;
        if (App.DB_CHOICE.equals("nosql")) {
            list = (new LocationRESTImpl()).getNodeList();
        } else {
            list = (new LocationDerbyImpl()).getNodeList();
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
            for (Location thisEq : list) {
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
            throw new Exception("Location list is empty!");
        }
    }



}
