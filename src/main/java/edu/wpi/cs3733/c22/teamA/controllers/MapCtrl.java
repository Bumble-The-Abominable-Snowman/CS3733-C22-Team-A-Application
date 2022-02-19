package edu.wpi.cs3733.c22.teamA.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDAO;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDAO;
import edu.wpi.cs3733.c22.teamA.Adb.medicalequipment.EquipmentDerbyImpl;
import edu.wpi.cs3733.c22.teamA.Adb.servicerequest.ServiceRequestDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.SceneSwitcher;
import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.map.*;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.animation.Interpolator;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import net.kurobako.gesturefx.AffineEvent;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

// TODO Change all instances of looping through locations to find related short names & node ids
// with method in backend once implemented
public class MapCtrl extends MasterCtrl {

	@FXML private JFXComboBox pfFromComboBox, pfToComboBox, floorSelectionComboBox, searchComboBox;
	@FXML private JFXCheckBox locationCheckBox, dragCheckBox, equipmentCheckBox, serviceRequestCheckBox, showTextCheckBox;
	@FXML private JFXButton deleteButton;
	@FXML private JFXTextArea nodeIDText;
	@FXML private JFXTextArea xPosText;
	@FXML private JFXTextArea yPosText;
	@FXML private JFXTextArea floorText;
	@FXML private JFXTextArea buildingText;
	@FXML private JFXTextArea typeText;
	@FXML private JFXTextArea longnameText;
	@FXML private JFXTextArea shortnameText;
	@FXML private JFXButton saveButton;
	@FXML private JFXButton editButton;
	@FXML private JFXButton clearButton;
	@FXML private VBox inputVBox;
	@FXML private ImageView mapImageView = new ImageView();
	@FXML private GesturePane gesturePane;

	private LocationMarker newLocationMarker = null;
	private Dimension2D transformed = new Dimension2D(967, 1050);
	private AnchorPane miniAnchorPane = new AnchorPane();
	private List<Line> pfLine = new ArrayList<>();

	private List<Location> locations;
	private List<Equipment> equipments;
	private List<SR> serviceRequests;
	private Polygon locationMarkerShape;
	private Polygon equipmentMarkerShape;

	private String floor;
	private String floorName;
	private String currentID;
	private Location selectedLocation;

	private LocationDAO locationDAO = new LocationDerbyImpl();
	private EquipmentDAO equipmentDAO = new EquipmentDerbyImpl();

	private HashMap<Button, LocationMarker> buttonLocationMarker;
	private HashMap<Button, EquipmentMarker> buttonEquipmentMarker;
	private HashMap<Button, SRMarker> buttonServiceRequestMarker;

	private SelectionManager selectionManager;
	private CheckBoxManager checkBoxManager;
	private MarkerManager markerManager;

	public MapCtrl() {
		locationMarkerShape = new Polygon();
		equipmentMarkerShape = new Polygon();
		locationMarkerShape.getPoints().addAll(new Double[]{1.0, 4.0, 0.0, 2.0, 1.0, 0.0, 2.0, 2.0});
		equipmentMarkerShape.getPoints().addAll(new Double[]{0.0, 0.0, 0.0, 1.0, 4.0, 1.0, 4.0, 0.0});

		buttonLocationMarker = new HashMap<>();
		buttonEquipmentMarker = new HashMap<>();
		buttonServiceRequestMarker = new HashMap<>();

		locations = new ArrayList<>();
		equipments = new ArrayList<>();
		serviceRequests = new ArrayList<>();

		currentID = "";
		floor = "";

		selectionManager = new SelectionManager(editButton, saveButton, clearButton, deleteButton, inputVBox, nodeIDText, xPosText, yPosText, floorText, buildingText, typeText, longnameText, shortnameText);
		checkBoxManager = new CheckBoxManager(equipmentCheckBox, locationCheckBox, serviceRequestCheckBox, showTextCheckBox, dragCheckBox);
		//markerManager = new MarkerManager();
	}

	@FXML
	public void initialize() {

		configure();

		// Pathfinding Setup
		// Setup Functions
		fillFromDB();
		checkBoxManager.setupCheckboxListeners();
		selectionManager.setInitialUIStates();
		checkBoxManager.setIntitialUIState();
		//setupContextMenu();
		setupSearchListener();
		setupFloor("Choose Floor:");

		floorSelectionComboBox.getItems().removeAll(floorSelectionComboBox.getItems());
		floorSelectionComboBox
				.getItems()
				.addAll("Choose Floor:", "Floor 1", "Floor 2", "Floor 3", "L1", "L2");

		floorSelectionComboBox.getSelectionModel().select("Choose Floor:");
		floorSelectionComboBox
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(obs, oldValue, newValue) -> {
							fillFromDB();
							// Sets up floor on Map
							setupFloor(newValue.toString());
							clearAll();

							// Maps Location IDs to their markers
							HashMap<String, LocationMarker> locationIDs = new HashMap<>();

							List<Location> thisFloorLocations = new ArrayList<>();

							// Loops through every location & draws them if present on the floor
							for (Location location : locations) {
								if (location.getFloor().equals(floor)) {
									LocationMarker locationMarker = newDraggableLocation(location);
									locationMarker.draw(miniAnchorPane);
									locationIDs.put(location.getNodeID(), locationMarker);
									thisFloorLocations.add(location);
								}
							}

							try {
								neighborMap = getEdges();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (ParseException e) {
								e.printStackTrace();
							}
							pfToComboBox
									.getItems()
									.addAll(
											thisFloorLocations.stream()
													.map(Location::getShortName)
													.collect(Collectors.toList()));
							pfFromComboBox
									.getItems()
									.addAll(
											thisFloorLocations.stream()
													.map(Location::getShortName)
													.collect(Collectors.toList()));

							// Loops through every medical equipment & draws them if they're on this floor
							for (Equipment equipment : equipments) {
								if (locationIDs.containsKey(equipment.getCurrentLocation())) {
									EquipmentMarker equipmentMarker =
											newDraggableEquipment(
													equipment, locationIDs.get(equipment.getCurrentLocation()));
									equipmentMarker.draw(miniAnchorPane);
								}
							}

							// Loops through every service request & draws them if they're on this floor
							for (SR serviceRequest : serviceRequests) {
								if (locationIDs.containsKey(serviceRequest.getEndLocation())) {
									SRMarker serviceRequestMarker =
											newDraggableServiceRequest(
													serviceRequest, locationIDs.get(serviceRequest.getEndLocation()));
									serviceRequestMarker.draw(miniAnchorPane);
								}
							}
						});
	}

	// Fills info from DB
	public void fillFromDB() {
		locations.clear();
		equipments.clear();
		serviceRequests.clear();
		locations.addAll(new ArrayList<>(new LocationDerbyImpl().getNodeList()));
		for (Location l : locations) {
			if (l.getNodeID().equals("N/A")) {
				locations.remove(l);
				break;
			}
		}
		equipments.addAll(new ArrayList<>(new EquipmentDerbyImpl().getMedicalEquipmentList()));
		// TODO when implementation done
		try {
			List<?> requestList = ServiceRequestDerbyImpl.getAllServiceRequestList();
			for (Object sr : requestList) {
				serviceRequests.add((SR) sr);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Set up searchbar for LOCATIONS ONLY
	// TODO IMPLEMENT CHOICE OF LOCATION, EQUIPMENT, SR SEARCH (another button?)
	public void setupSearchListener() {
		searchComboBox.setEditable(true);
		// set up list of locations to be wrapped
		ObservableList<Location> searchLocationList = FXCollections.observableArrayList();
		searchLocationList.addAll(locations);

		// create filtered list, can be filtered (duh)
		FilteredList<Location> filteredLocations = new FilteredList<>(searchLocationList, p -> true);
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
												|| searchComboBox.getSelectionModel().toString().isEmpty())
												&& location.getFloor().equals(floor)) {
											return true;
										}
										// make sure case is factored out
										String lowerCaseFilter = newValue.toLowerCase();
										// if search matches either name or ID, display it
										// if not, this returns false and doesn't display
										return (location.getLongName().toLowerCase().contains(lowerCaseFilter)
												|| location.getShortName().toLowerCase().contains(lowerCaseFilter)
												|| location.getNodeID().toLowerCase().contains(lowerCaseFilter))
												&& location.getFloor().equals(floor);
									});
							// add items to comboBox dropdown
							ArrayList<String> locationNames = new ArrayList<>();
							for (Location l : filteredLocations) {
								locationNames.add(l.getLongName());
							}
							searchComboBox.getItems().clear();
							searchComboBox.getItems().addAll(locationNames);
							if (searchComboBox.getItems().size() < 5) {
								searchComboBox.setVisibleRowCount(searchComboBox.getItems().size());
							} else {
								searchComboBox.setVisibleRowCount(5);
							}
							// select location if search complete
							if (searchComboBox.getItems().size() == 1) {
								existingLocationSelected(filteredLocations.get(0));
								editButton.setDisable(false);
							}

							clearAll();
							HashMap<String, LocationMarker> locationIDs = new HashMap<>();
							// Loops through every location filtered & draws them if present on the floor
							// TODO clean this up somehow, need a new .contains type method
							for (Location l : locations) {
								System.out.println(l.getLongName());
								for (Location ls : filteredLocations) {
									if (ls.getNodeID().equals(l.getNodeID())) {
										LocationMarker locationMarker = newDraggableLocation(l);
										locationMarker.draw(miniAnchorPane);
										locationMarker.setButtonVisibility(true);
										locationIDs.put(l.getNodeID(), locationMarker);
										break;
									}
								}
							}
							// Loops through every medical equipment & draws them if they're on this floor
							for (Equipment equipment : equipments) {
								if (locationIDs.containsKey(equipment.getCurrentLocation())) {
									EquipmentMarker equipmentMarker =
											newDraggableEquipment(
													equipment, locationIDs.get(equipment.getCurrentLocation()));
									equipmentMarker.draw(miniAnchorPane);
								}
							}
						});
	}

	// Sets up the floor
	public void setupFloor(String newValue) {
		URL url;
		switch (newValue) {
			case "Choose Floor: ":
				floor = "";
				floorName = "";
				url = App.class.getResource("images/Side View.png");
				break;
			case "Floor 1":
				floor = "1";
				floorName = "Floor 1";
				url = App.class.getResource("images/1st Floor.png");
				break;
			case "Floor 2":
				floor = "2";
				floorName = "Floor 2";
				url = App.class.getResource("images/2nd Floor.png");
				break;
			case "Floor 3":
				floor = "3";
				floorName = "Floor 3";
				url = App.class.getResource("images/3rd Floor.png");
				break;
			case "L1":
				floor = "L1";
				floorName = "L1";
				url = App.class.getResource("images/LL1.png");
				break;
			case "L2":
				floor = "L2";
				floorName = "L2";
				url = App.class.getResource("images/LL2.png");
				break;
			default:
				url = App.class.getResource("images/Side View.png");
				break;
		}

		Image image = new Image(String.valueOf(url));
		mapImageView.setImage(image);
		setupGesture();
		hideSideView();
	}

	public void setupGesture() {
		miniAnchorPane.getChildren().remove(mapImageView);
		miniAnchorPane.getChildren().add(mapImageView);
		miniAnchorPane.setLayoutX(0);
		mapImageView.setLayoutX(0);
		gesturePane.reset();
		gesturePane.setContent(miniAnchorPane);
		gesturePane.addEventFilter(
				AffineEvent.CHANGED,
				event -> {
					// System.out.println(event.getTransformedDimension());
					transformed = event.getTransformedDimension();
				});
		gesturePane.setOnMouseClicked(
				e -> {
					if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
						Point2D pivotOnTarget =
								gesturePane
										.targetPointAt(new Point2D(e.getX(), e.getY()))
										.orElse(gesturePane.targetPointAtViewportCentre());
						// increment of scale makes more sense exponentially instead of linearly
						gesturePane
								.animate(Duration.millis(200))
								.interpolateWith(Interpolator.EASE_BOTH)
								.zoomBy(gesturePane.getCurrentScale(), pivotOnTarget);
					}
				});
	}

	// Go to Location Table
	@FXML
	public void goToLocationTable() throws IOException {
		HomeCtrl.sceneFlag = 2;
		sceneSwitcher.switchScene(SceneSwitcher.SCENES.DATA_VIEW);
	}
}
