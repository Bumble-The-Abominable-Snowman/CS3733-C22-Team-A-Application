package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class Searcher {
  List<LocationMarker> locations;
  List<SRMarker> SRs;
  List<EquipmentMarker> equipments;
  String floor;
  JFXComboBox searchComboBox;

  public Searcher(JFXComboBox comboBox) {
    searchComboBox = comboBox;
  }

  public void setLocations(List<LocationMarker> locations) {
    this.locations = locations;
  }

  public void setEquipments(List<EquipmentMarker> equipments) {
    this.equipments = equipments;
  }

  public void setSRs(List<SRMarker> SRs) {
    this.SRs = SRs;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public void setAll(
      List<LocationMarker> locations,
      List<EquipmentMarker> equipments,
      List<SRMarker> SRs,
      String floor) {
    this.locations = locations;
    this.equipments = equipments;
    this.SRs = SRs;
    this.floor = floor;
  }

  public void initSearchListener() {
    searchComboBox.setEditable(true);
    // set up list of locations to be wrapped
    ObservableList<Location> searchLocationList = FXCollections.observableArrayList();
    for (LocationMarker l : locations) {
      searchLocationList.add(l.getLocation());
    }
    ObservableList<Equipment> searchEquipmentList = FXCollections.observableArrayList();
    for (EquipmentMarker e : equipments) {
      searchEquipmentList.add(e.getEquipment());
    }
    /*
    ObservableList<SR> searchSRList = FXCollections.observableArrayList();
    for(SR s : SRs){
        searchSRList.add(s.get)
    }
     */

    // create filtered list, can be filtered (duh)
    FilteredList<Location> filteredLocations = new FilteredList<>(searchLocationList, p -> true);
    FilteredList<Equipment> filteredEquipment = new FilteredList<>(searchEquipmentList, p -> true);
    // FilteredList<SR> filteredSR = new FilteredList<>(searchSRList, p->true);
    // add listener that checks whenever changes are made to JFXText searchText
    searchComboBox
        .getEditor()
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              filteredLocations.setPredicate(
                  location -> {
                    // if field is empty display all locations
                    if ((newValue == null
                        || searchComboBox.getSelectionModel().toString().isEmpty())) {
                      return true;
                    }
                    // make sure case is factored out
                    String lowerCaseFilter = newValue.toLowerCase();
                    // if search matches either name or ID, display it
                    // if not, this returns false and doesn't display
                    return (location.getLongName().toLowerCase().contains(lowerCaseFilter)
                        || location.getShortName().toLowerCase().contains(lowerCaseFilter)
                        || location.getNodeID().toLowerCase().contains(lowerCaseFilter));
                  });
              filteredEquipment.setPredicate(
                  equipment -> {
                    // if field is empty display all locations
                    if ((newValue == null
                        || searchComboBox.getSelectionModel().toString().isEmpty())) {
                      return true;
                    }
                    // make sure case is factored out
                    String lowerCaseFilter = newValue.toLowerCase();
                    // if search matches either name or ID, display it
                    // if not, this returns false and doesn't display
                    return (equipment.getEquipmentID().toLowerCase().contains(lowerCaseFilter)
                        || equipment.getEquipmentType().toLowerCase().contains(lowerCaseFilter)
                        || equipment.getCurrentLocation().toLowerCase().contains(lowerCaseFilter));
                  });
              // add items to comboBox dropdown
              ArrayList<String> locationNames = new ArrayList<>();
              for (Location l : filteredLocations) {
                locationNames.add(l.getLongName());
                // System.out.println(l.getLongName() + " <- filtered check 1");
              }
              ArrayList<String> equipmentNames = new ArrayList<>();
              for (Equipment e : filteredEquipment) {
                equipmentNames.add(e.getEquipmentID());
              }

              searchComboBox.getItems().clear();
              searchComboBox.getItems().addAll(locationNames);
              if (searchComboBox.getItems().size() < 5) {
                searchComboBox.setVisibleRowCount(searchComboBox.getItems().size());
              } else {
                searchComboBox.setVisibleRowCount(5);
              }
              /*
              // select location if search complete
              if (searchComboBox.getItems().size() == 1) {
                  existingLocationSelected(filteredLocations.get(0));
                  editButton.setDisable(false);
              }
              */

              // HashMap<String, LocationMarker> locationIDs = new HashMap<>();
              // Loops through every location filtered & draws them if present on the floor
              for (LocationMarker l : locations) {
                if (locationNames.contains(l.getLocation().getLongName())) {
                  l.setButtonVisibility(true);
                  l.setLabelVisibility(true);
                  //   locationIDs.put(l.getLocation().getNodeID(), l);
                } else {
                  l.setButtonVisibility(false);
                  l.setLabelVisibility(false);
                }
              }
              // Loops through every medical equipment & draws them
              for (EquipmentMarker equipment : equipments) {
                if (equipmentNames.contains(equipment.getEquipment().getEquipmentID())) {
                  equipment.setButtonVisibility(true);
                  equipment.setLabelVisibility(true);
                } else {
                  equipment.setButtonVisibility(false);
                  equipment.setLabelVisibility(false);
                }
              }
            });
  }
}
