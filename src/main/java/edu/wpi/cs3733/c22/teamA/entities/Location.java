package edu.wpi.cs3733.c22.teamA.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Location extends RecursiveTreeObject<Location> {

  protected HashMap<String, Object> fields = new HashMap<>();
  protected HashMap<String, String> fields_string = new HashMap<>();

  public Location() {}

  public Location(
      String nodeID,
      int xCoord,
      int yCoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {

    this.fields.put("node_id", nodeID);
    this.fields.put("xcoord", xCoord);
    this.fields.put("ycoord", yCoord);
    this.fields.put("floor", floor);
    this.fields.put("building", building);
    this.fields.put("node_type", nodeType);
    this.fields.put("long_name", longName);
    this.fields.put("short_name", shortName);
  }

  public HashMap<String, String> getStringFields() {
    for (String key : this.fields.keySet()) {
      this.fields_string.put(key, String.valueOf(this.fields.get(key)));
    }
    return this.fields_string;
  }

  public void setField(String key, Object value) {
    this.fields.put(key, value);
  }

  public void setFieldByString(String key, String value) throws ParseException {
    if (Objects.equals(key, "xcoord") || Objects.equals(key, "ycoord")) {
      int i = Integer.parseInt(value);
      this.fields.put(key, i);
    } else {
      this.fields.put(key, value);
    }
  }
}
