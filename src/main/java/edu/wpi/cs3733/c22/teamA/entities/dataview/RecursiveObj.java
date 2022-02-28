package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.controllers.DataViewCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Employee;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;


public class RecursiveObj extends RecursiveTreeObject<RecursiveObj> {
	public SR sr;
	public Location locStart;
	public Location locEnd;
	public Employee employeeReq;
	public Employee employeeAss;
	public Location loc;
	public Equipment equip;
	public Employee employee;
	public Medicine med;
}

