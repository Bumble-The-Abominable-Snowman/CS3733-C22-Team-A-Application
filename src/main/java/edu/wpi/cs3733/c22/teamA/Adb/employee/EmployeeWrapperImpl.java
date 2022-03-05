package edu.wpi.cs3733.c22.teamA.Adb.employee;

import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.controllers.servicerequest.SelectServiceRequestCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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

    public void exportToCSV(String csvFilePath) throws IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {EmployeeRESTImpl.exportToCSV(csvFilePath);}
        else {EmployeeDerbyImpl.exportToCSV(csvFilePath);}

    }

}
