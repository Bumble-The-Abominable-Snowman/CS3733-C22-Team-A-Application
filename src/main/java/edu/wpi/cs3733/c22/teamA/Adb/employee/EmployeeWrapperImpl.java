package edu.wpi.cs3733.c22.teamA.Adb.employee;

import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationRESTImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.controllers.servicerequest.SelectServiceRequestCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
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

public class EmployeeWrapperImpl implements EmployeeDAO{
    public EmployeeWrapperImpl()  {}

    @Override
    public Employee getEmployee(String ID) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new EmployeeRESTImpl()).getEmployee(ID);}
        else
        {return (new EmployeeDerbyImpl()).getEmployee(ID);}
    }

    @Override
    public void updateEmployee(Employee e) throws SQLException, IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EmployeeRESTImpl()).updateEmployee(e);}
        else
        {(new EmployeeDerbyImpl()).updateEmployee(e);}
    }

    @Override
    public void enterEmployee(Employee e) throws ParseException, IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EmployeeRESTImpl()).enterEmployee(e);}
        else
        {(new EmployeeDerbyImpl()).enterEmployee(e);}

    }

    @Override
    public void enterEmployee(String employeeID, String employeeType, String firstName, String lastName, String email, String phoneNum, String address, Date startDate) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EmployeeRESTImpl()).enterEmployee(employeeID, employeeType, firstName, lastName, email, phoneNum, address, startDate);}
        else
        {(new EmployeeDerbyImpl()).enterEmployee(employeeID, employeeType, firstName, lastName, email, phoneNum, address, startDate);}

    }

    @Override
    public void deleteEmployee(Employee e) throws IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EmployeeRESTImpl()).deleteEmployee(e);}
        else
        {(new EmployeeDerbyImpl()).deleteEmployee(e);}

    }

    @Override
    public List<Employee> getEmployeeList() throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new EmployeeRESTImpl()).getEmployeeList();}
        else
        {return (new EmployeeDerbyImpl()).getEmployeeList();}

    }

    public void inputFromCSVfile(String csvFilePath) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new EmployeeRESTImpl()).inputFromCSVfile(csvFilePath);}
        else {EmployeeDerbyImpl.inputFromCSVfile(csvFilePath);}

    }

    public void exportToCSV(String csvFilePath) throws Exception {

        // Get list of this type of service Requests
        List<Employee> list;
        if (App.DB_CHOICE.equals("nosql")) {
            list = (new EmployeeRESTImpl()).getEmployeeList();
        } else {
            list = (new EmployeeDerbyImpl()).getEmployeeList();
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
            for (Employee thisEq : list) {
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
