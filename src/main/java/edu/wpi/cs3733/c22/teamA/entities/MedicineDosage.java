package edu.wpi.cs3733.c22.teamA.entities;

/**
 *  This class only exists to make loading dosages from CSV
 *  DO NOT USE THIS CLASS FOR ANYTHING ELSE
 *  well i guess you can if you really want but it probably wont be usefull
 *   - ryan
 */
public class MedicineDosage {
    private String medicine_id;
    private Float dosage_amount;

    public MedicineDosage(){}

    public MedicineDosage(String medicine_id, Float dosage_amount){
        this.medicine_id = medicine_id;
        this.dosage_amount = dosage_amount;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    public Float getDosage_amount() {
        return dosage_amount;
    }

    public void setDosage_amount(Float dosage_amount) {
        this.dosage_amount = dosage_amount;
    }

    public String toString(){
        return "" +
                "medID: " + medicine_id +
                " dosage: " + dosage_amount + "\n";
    }
}
