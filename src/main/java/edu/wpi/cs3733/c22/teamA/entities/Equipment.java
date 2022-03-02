package edu.wpi.cs3733.c22.teamA.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Equipment extends RecursiveTreeObject<Equipment> {

  protected HashMap<String, Object> fields = new HashMap<>();
  protected HashMap<String, String> fields_string = new HashMap<>();

  public Equipment() {}

  public Equipment(
      String equipmentID,
      String equipmentType,
      boolean isClean,
      String currentLocation,
      boolean isAvailable) {
    this.fields.put("equipment_id", equipmentID);
    this.fields.put("equipment_type", equipmentType);
    this.fields.put("is_clean", isClean);
    this.fields.put("current_location", currentLocation);
    this.fields.put("is_available", isAvailable);
  }

  public List<String> getListForm() {
    return List.of(getStringFields().get("equipment_id"),
            getStringFields().get("equipment_type"),
            getStringFields().get("is_clean"),
            getStringFields().get("current_location"),
            getStringFields().get("is_available"));
  }

  public HashMap<String, String> getStringFields() {
    for (String key : this.fields.keySet()) {
       this.fields_string.put(key, String.valueOf(this.fields.get(key)));
    }
    return this.fields_string;
  }

  public HashMap<String, Object> getFields() {
    return fields;
  }

  public void setField(String key, Object value) {
    this.fields.put(key, value);
  }

  public void setFieldByString(String key, String value) {
    if (Objects.equals(key, "is_clean") || Objects.equals(key, "is_available") ) {
      this.fields.put(key, Boolean.valueOf(value));
    }else {
      this.fields.put(key, value);
    }
  }
}
