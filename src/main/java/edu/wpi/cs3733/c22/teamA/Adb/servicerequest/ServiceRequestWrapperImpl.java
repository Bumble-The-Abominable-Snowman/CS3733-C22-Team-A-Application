package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentRESTImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestWrapperImpl implements ServiceRequestDAO {
    public ServiceRequestWrapperImpl()  {}

    SR.SRType srType;

    public ServiceRequestWrapperImpl(SR.SRType srType)
    {
        this.srType = srType;
    }

    @Override
    public SR getRequest(String ID) throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new ServiceRequestRESTImpl(this.srType)).getRequest(ID);}
        else
        {return (new ServiceRequestDerbyImpl(this.srType)).getRequest(ID);}
    }

    @Override
    public void updateServiceRequest(SR sr) throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new ServiceRequestRESTImpl(this.srType)).updateServiceRequest(sr);}
        else
        {(new ServiceRequestDerbyImpl(this.srType)).updateServiceRequest(sr);}

    }

    @Override
    public void enterServiceRequest(SR sr) throws SQLException, InvocationTargetException, IllegalAccessException, IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new ServiceRequestRESTImpl(this.srType)).enterServiceRequest(sr);}
        else
        {(new ServiceRequestDerbyImpl(this.srType)).enterServiceRequest(sr);}

    }

    @Override
    public void deleteServiceRequest(SR sr) throws SQLException, IOException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new ServiceRequestRESTImpl(this.srType)).deleteServiceRequest(sr);}
        else
        {(new ServiceRequestDerbyImpl(this.srType)).deleteServiceRequest(sr);}

    }

    @Override
    public List<SR> getServiceRequestList() throws SQLException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return (new ServiceRequestRESTImpl(this.srType)).getServiceRequestList();}
        else
        {return (new ServiceRequestDerbyImpl(this.srType)).getServiceRequestList();}
    }

    public void populateFromCSVfile(String csvFilePath) throws IOException, ParseException, SQLException, InvocationTargetException, IllegalAccessException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new ServiceRequestRESTImpl(this.srType)).populateFromCSVfile(csvFilePath);}
        else {(new ServiceRequestDerbyImpl(this.srType)).populateFromCSVfile(csvFilePath);}

    }

    public static List<SR> getAllServiceRequestList()
            throws SQLException, IllegalAccessException, InvocationTargetException, IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return ServiceRequestRESTImpl.getAllServiceRequestList();}
        else
        {return ServiceRequestDerbyImpl.getAllServiceRequestList();}
    }


}
