package edu.wpi.cs3733.c22.teamA.Adb.location;

import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDAO;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.employee.EmployeeRESTImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Location;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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

    public void exportToCSV(String csvFilePath) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {LocationRESTImpl.exportToCSV(csvFilePath);}
        else {LocationDerbyImpl.exportToCSV(csvFilePath);}

    }

}
