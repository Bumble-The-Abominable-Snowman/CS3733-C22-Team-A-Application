package edu.wpi.cs3733.c22.teamA.Adb.servicerequest;

import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationRESTImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentRESTImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public void populateFromCSV(String csvFilePath) throws IOException, ParseException, SQLException, InvocationTargetException, IllegalAccessException {
        if (App.DB_CHOICE.equals("nosql"))
        {(new ServiceRequestRESTImpl(this.srType)).populateFromCSV(csvFilePath);}
        else {(new ServiceRequestDerbyImpl(this.srType)).populateFromCSVfile(csvFilePath);}

    }

    public static List<SR> getAllServiceRequestList()
            throws SQLException, IllegalAccessException, InvocationTargetException, IOException, ParseException {
        if (App.DB_CHOICE.equals("nosql"))
        {return ServiceRequestRESTImpl.getAllServiceRequestList();}
        else
        {return ServiceRequestDerbyImpl.getAllServiceRequestList();}
    }

    public void exportToCSV(String csvFilePath) throws Exception {

        // Get list of this type of service Requests
        List<SR> list;
        if (App.DB_CHOICE.equals("nosql")) {
            list = (new ServiceRequestRESTImpl(this.srType)).getServiceRequestList();
        } else {
            list = (new ServiceRequestDerbyImpl(this.srType)).getServiceRequestList();
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
                if (!(key.equals("sr_type, "))) {
                    tleString.append(key);
                }
            }
            String firstLine = tleString.toString().substring(0, tleString.toString().length() - 2);

            writer.write(firstLine);
            writer.newLine();

            // rows
            for (SR thisSR : list) {
                String str = "";

                boolean first_column = true;
                for (String key : keys) {
                    if (!(key.equals("sr_type"))) {
                        if (first_column) {
                            str = thisSR.getStringFields().get(key);
                            first_column = false;
                        } else {
                            str = String.join(",", str, thisSR.getStringFields().get(key));
                        }
                    }
                }

                writer.write(str);
                writer.newLine();
            }
            writer.close(); // close the writer
        } else {
            throw new Exception("SR list is empty!");
        }
    }

}
