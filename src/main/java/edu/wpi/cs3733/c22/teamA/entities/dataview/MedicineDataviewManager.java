package edu.wpi.cs3733.c22.teamA.entities.dataview;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicine.MedicineDerbyImpl;
import edu.wpi.cs3733.c22.teamA.controllers.DataViewCtrl;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.Medicine;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MedicineDataviewManager {
    JFXTreeTableView<RecursiveObj> table;
    DataViewCtrl dataViewCtrl;

    String[] medicineColumnNames = {
            "MedicineID", "GenericName",
            "BrandName", "MedicineClass",
            "Uses", "Warnings",
            "SideEffects", "Form",
            "DosageAmounts"};

    private List<Medicine> medList = new ArrayList<>();

    public MedicineDataviewManager(DataViewCtrl dataViewCtrl){
        this.dataViewCtrl = dataViewCtrl;
    }

    public void delete() {
        MedicineDAO database = new MedicineDerbyImpl();
        database.deleteMedicine(
                table.getSelectionModel().getSelectedItem().getValue().med.getMedicineID()
        );
        dataViewCtrl.titleLabel.setText("Medicine");
        initializeMedicineTable();
    }

    public void initializeMedicineTable() {
        table = dataViewCtrl.getTable();
        dataViewCtrl.getSelectEmployeeBox().setVisible(false);

        List<JFXTreeTableColumn<RecursiveObj, String>> medicineColumns = new ArrayList<>();

        for (String columnName: medicineColumnNames) {
            JFXTreeTableColumn<RecursiveObj, String> column = new JFXTreeTableColumn<>(columnName);
            column.setPrefWidth(100);
            column.setStyle("-fx-alignment: center ;");

            medicineColumns.add(column);
        }

        medicineColumns
                .get(0)
                .setCellValueFactory(
                        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                                new SimpleStringProperty(param.getValue().getValue().med.getMedicineID()));
        medicineColumns
                .get(1)
                .setCellValueFactory(
                        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                                new SimpleStringProperty(param.getValue().getValue().med.getGenericName()));
        medicineColumns
                .get(2)
                .setCellValueFactory(
                        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                                new SimpleStringProperty(param.getValue().getValue().med.getBrandName()));
        medicineColumns
                .get(3)
                .setCellValueFactory(
                        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                                new SimpleStringProperty(param.getValue().getValue().med.getMedicineClass()));
        medicineColumns
                .get(4)
                .setCellValueFactory(
                        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                                new SimpleStringProperty(param.getValue().getValue().med.getUses()));
        medicineColumns
                .get(5)
                .setCellValueFactory(
                        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                                new SimpleStringProperty(param.getValue().getValue().med.getWarnings()));
        medicineColumns
                .get(6)
                .setCellValueFactory(
                        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                                new SimpleStringProperty(param.getValue().getValue().med.getSideEffects()));
        medicineColumns
                .get(7)
                .setCellValueFactory(
                        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                                new SimpleStringProperty(param.getValue().getValue().med.getForm()));
        medicineColumns
                .get(8)
                .setCellValueFactory(
                        (TreeTableColumn.CellDataFeatures<RecursiveObj, String> param) ->
                                new SimpleStringProperty(param.getValue().getValue().med.getDosageAmounts().toString()));

        // Grab medicine from database
        MedicineDAO database = new MedicineDerbyImpl();
        this.medList = database.getMedicineList();
        ObservableList<RecursiveObj> medicine = FXCollections.observableArrayList();
        for (Medicine item : this.medList) {
            RecursiveObj recursiveEquipment = new RecursiveObj();
            recursiveEquipment.med = item;
            medicine.add(recursiveEquipment);
        }

        // Sets up the table and puts the equipment data under the columns
        final TreeItem<RecursiveObj> root =
                new RecursiveTreeItem<>(medicine, RecursiveTreeObject::getChildren);
        table.getColumns().setAll(medicineColumns);
        table.setRoot(root);

        dataViewCtrl.setupViewDetailsAndModify();

    }


    public void modifyPopup(JFXComboBox<String> field, TextArea value, JFXButton updateButton){
        Medicine med = this.medList.get(table.getSelectionModel().getSelectedIndex());
        Method[] methods = med.getClass().getMethods();

        for(Method method: methods) {
            boolean is_the_method_of_med = method.getDeclaringClass().equals(med.getClass());
            boolean starts_with_set = method.getName().split("^set")[0].equals("");

            boolean return_string =
                    Arrays.toString(method.getParameterTypes())
                            .toLowerCase(Locale.ROOT)
                            .contains("string");

            if (is_the_method_of_med && starts_with_set && return_string) {
                field.getItems().addAll(method.getName().substring(3));
            }


        }

        field.setOnAction(
                e -> {
                    if (field.getSelectionModel().getSelectedIndex() > -1) {
                        for (Method method : methods) {
                            boolean starts_with_get = method.getName().split("^get")[0].equals("");
                            boolean contains_name =
                                    method
                                            .getName()
                                            .toLowerCase(Locale.ROOT)
                                            .contains(
                                                    field.getSelectionModel().getSelectedItem().toLowerCase(Locale.ROOT));
                            if (starts_with_get && contains_name) {
                                try {
                                    value.setText((String) method.invoke(med));
                                } catch (IllegalAccessException | InvocationTargetException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                });

        updateButton.setOnAction(
                e -> {
                    if (field.getSelectionModel().getSelectedIndex() > -1
                            && value.getText().length() > 0) {
                        for (Method method : methods) {
                            boolean starts_with_set = method.getName().split("^set")[0].equals("");
                            boolean contains_name =
                                    method
                                            .getName()
                                            .toLowerCase(Locale.ROOT)
                                            .contains(
                                                    field.getSelectionModel().getSelectedItem().toLowerCase(Locale.ROOT));
                            if (starts_with_set && contains_name) {
                                MedicineDAO database = new MedicineDerbyImpl();
                                try {
                                    String aField = "";
                                    System.out.println(field.getValue());
                                    if (field.getValue().equals("GenericName")) {
                                        aField = "generic_name";
                                    } else if (field.getValue().equals("BrandName")) {
                                        aField = "brand_name";
                                    } else if (field.getValue().equals("MedicineClass")) {
                                        aField = "medicine_class";
                                    } else if (field.getValue().equals("Uses")) {
                                        aField = "uses";
                                    } else if (field.getValue().equals("Warnings")) {
                                        aField = "warnings";
                                    } else if (field.getValue().equals("SideEffects")) {
                                        aField = "side_effects";
                                    } else if (field.getValue().equals("Form")) {
                                        aField = "form";
                                    }
                                    if (!(field.getValue().equals("DosageAmounts"))){
                                        database.updateMedicine(
                                                med.getMedicineID(), aField, value.getText());
                                        updateButton.setTextFill(Color.GREEN);
                                        System.out.println("B4 initialize");
                                        this.initializeMedicineTable();
                                    }
                                    else if(field.getValue().equals("DosageAmounts")){
                                        aField = "dosage_amount";
                                        database.enterMedicineDosage(med.getMedicineID(),Float.parseFloat(aField));
                                        updateButton.setTextFill(Color.GREEN);
                                        System.out.println("B4 initialize");
                                        this.initializeMedicineTable();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    updateButton.setTextFill(Color.RED);
                                }
                            }
                        }
                    }
                });
    }
}