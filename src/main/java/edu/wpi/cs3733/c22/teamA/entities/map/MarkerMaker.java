package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.entities.Equipment;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

public class MarkerMaker {

  private static Polygon locationMarkerShape = new Polygon(1.0, 4.0, 0.0, 2.0, 1.0, 0.0, 2.0, 2.0);
  private static Polygon equipmentMarkerShape = new Polygon(0.0, 0.0, 0.0, 1.0, 4.0, 1.0, 4.0, 0.0);

  public static LocationMarker makeLocationMarker(Location location, int offSetX, int offSetY) {
    double buttonX = location.getXCoord() + offSetX - 8;
    double buttonY = location.getYCoord() + offSetY - 24;
    Button button = newDraggableButton(buttonX, buttonY, 0);

    button.setStyle("-fx-background-color: #78aaf0");
    button.setShape(locationMarkerShape);

    double labelX = location.getXCoord() + offSetX - 8 + 7.5;
    double labelY = location.getYCoord() + offSetY - 24 - 15;
    Label label = newDraggableLabel(labelX, labelY, location.getShortName());

    LocationMarker locationMarker = new LocationMarker(button, label, location);
    // buttonLocationMarker.put(button, locationMarker);
    return locationMarker;
  }

  public static EquipmentMarker makeEquipmentMarker(
      Equipment equipment, LocationMarker locationMarker, int offSetX, int offSetY) {
    double buttonX = locationMarker.getLocation().getXCoord() + offSetX - 8 + 10;
    double buttonY = locationMarker.getLocation().getYCoord() + offSetY - 24 + 10;
    Button button = newDraggableButton(buttonX, buttonY, 1);

    double labelX = locationMarker.getLocation().getXCoord() + offSetX - 8 + 7.5 + 10;
    double labelY = locationMarker.getLocation().getYCoord() + offSetY - 24 - 15 + 10;
    Label label =
        newDraggableLabel(
            labelX, labelY, equipment.getEquipmentID() + equipment.getEquipmentType());

    button.setStyle("-fx-background-color: RED");
    button.setShape(equipmentMarkerShape);

    EquipmentMarker equipmentMarker = new EquipmentMarker(equipment, button, label, locationMarker);
    locationMarker.setEquipmentMarker(equipmentMarker);
    // buttonEquipmentMarker.put(button, equipmentMarker);
    return equipmentMarker;
  }

  public static SRMarker makeSRMarker(
      SR serviceRequest, LocationMarker locationMarker, int offSetX, int offSetY) {
    double buttonX = locationMarker.getLocation().getXCoord() + offSetX - 8;
    double buttonY = locationMarker.getLocation().getYCoord() + offSetY - 24;
    Button button = newDraggableButton(buttonX, buttonY, 2);

    double labelX = locationMarker.getLocation().getXCoord() + offSetX - 8 + 7.5;
    double labelY = locationMarker.getLocation().getYCoord() + offSetY - 24 - 15;
    Label label = newDraggableLabel(labelX, labelY, "");

    button.setStyle(colorSR(serviceRequest));
    button.setShape(locationMarkerShape);

    SRMarker serviceRequestMarker = new SRMarker(serviceRequest, button, label, locationMarker);
    locationMarker.setServiceRequestMarker(serviceRequestMarker);
    // buttonServiceRequestMarker.put(button, serviceRequestMarker);
    return serviceRequestMarker;
  }

  public static String colorSR(SR serviceRequest) {
    switch (serviceRequest.getSrType()) {
      case EQUIPMENT:
        return "-fx-background-color: YELLOW";
      case FLORAL_DELIVERY:
        return "-fx-background-color: GREEN";
      case FOOD_DELIVERY:
        return "-fx-background-color: ORANGE";
      case GIFT_DELIVERY:
        return "-fx-background-color: ORCHID";
      case LANGUAGE:
        return "-fx-background-color: WHEAT";
      case LAUNDRY:
        return "-fx-background-color: MEDIUMBLUE";
      case MAINTENANCE:
        return "-fx-background-color: MINTCREAM";
      case MEDICINE_DELIVERY:
        return "-fx-background-color: SADDLEBROWN";
      case RELIGIOUS:
        return "-fx-background-color: TOMATO";
      case SANITATION:
        return "-fx-background-color: DIMGREY";
      case SECURITY:
        return "-fx-background-color: MAROON";
    }
    return "-fx-background-color: YELLOW";
  }

  public static Label newDraggableLabel(double posX, double posY, String text) {
    Label label = new Label();
    label.setText(text);
    label.setFont(new Font(15));

    label.setLayoutX(posX);
    label.setLayoutY(posY);

    return label;
  }

  // Makes a new Draggable Button
  public static Button newDraggableButton(double posX, double posY, int markerType) {
    Button button = newButton(posX, posY, 2.0, 4.0);
    // setDragFunctions(button, markerType);
    return button;
  }

  // Makes a new Button
  public static Button newButton(double posX, double posY, double minW, double minH) {
    Button button = new Button();
    button.setMinWidth(minW);
    button.setMinHeight(minH);
    button.setLayoutX(posX);
    button.setLayoutY(posY);
    button.setPickOnBounds(false);
    return button;
  }

  /*
    // Sets drag functions
    public void setDragFunctions(Button button, int markerType) {
    	final Delta dragDelta = new Delta();
    	button.setOnAction(
    			event -> {
    				// highlight(button, selectedButton);
    				// selectedButton = button;
    				try {
    					existingLocationSelected(buttonLocationMarker.get(button).getLocation());
    					currentID = buttonLocationMarker.get(button).getLocation().getNodeID();
    				} catch (Exception e) {
    					System.out.println("This isn't a location :)");
    				}
    			});
    	button.setOnMousePressed(
    			mouseEvent -> {
    				// record a delta distance for the drag and drop operation.
    				dragDelta.buttonX = button.getLayoutX();
    				dragDelta.buttonY = button.getLayoutY();
    				dragDelta.mouseX = mouseEvent.getSceneX();
    				dragDelta.mouseY = mouseEvent.getSceneY();
    				button.setCursor(Cursor.MOVE);
    				if (markerType == 0) {
    					existingLocationSelected(buttonLocationMarker.get(button).getLocation());
    				} else if (markerType == 1) {
    					existingEquipmentSelected(buttonEquipmentMarker.get(button).getEquipment());
    				} else {
    					existingServiceRequestSelected(
    							buttonServiceRequestMarker.get(button).getServiceRequest());
    				}
    				editButton.setDisable(false);
    				deleteButton.setDisable(false);
    				saveButton.setDisable(true);
    				clearButton.setDisable(true);
    			});
    	button.setOnMouseDragged(
    			mouseEvent -> {
    				if (dragCheckBox.isSelected()) {
    					button.setLayoutX(
    							(mouseEvent.getSceneX() - dragDelta.mouseX)
    									/ (transformed.getHeight() / miniAnchorPane.getHeight())
    									+ dragDelta.buttonX);
    					button.setLayoutY(
    							(mouseEvent.getSceneY() - dragDelta.mouseY)
    									/ (transformed.getHeight() / miniAnchorPane.getHeight())
    									+ dragDelta.buttonY);

    					// TODO make sure math on this is right
    					if (markerType == 1 || markerType == 2) {
    						// standard circle radius around medical equipment markers, 30 is placeholder
    						double radius = Math.sqrt(2 * Math.pow(10, 2));
    						for (Location l : locations) {
    							// check hypotenuse between this equipment and every location on floor
    							double radiusCheck =
    									Math.sqrt(
    											Math.pow(l.getXCoord() - button.getLayoutX(), 2)
    													+ (Math.pow(l.getYCoord() - button.getLayoutY(), 2)));
    							if (l.getFloor().equals(floor) && (radius > radiusCheck)) {
    								button.setLayoutX(l.getXCoord());
    								button.setLayoutY(l.getYCoord());
    							}
    						}
    					}

    					xPosText.setText(String.valueOf(button.getLayoutX() - mapImageView.getLayoutX() + 8));
    					yPosText.setText(String.valueOf(button.getLayoutY() - mapImageView.getLayoutY() + 24));
    					Label correspondingLabel;
    					if (markerType == 0) {
    						correspondingLabel = buttonLocationMarker.get(button).getLabel();
    					} else if (markerType == 1) {
    						correspondingLabel = buttonEquipmentMarker.get(button).getLabel();
    					} else {
    						correspondingLabel = buttonServiceRequestMarker.get(button).getLabel();
    					}
    					correspondingLabel.setLayoutX(
    							(mouseEvent.getSceneX() - dragDelta.mouseX)
    									/ (transformed.getHeight() / miniAnchorPane.getHeight())
    									+ dragDelta.buttonX
    									+ 8);
    					correspondingLabel.setLayoutY(
    							(mouseEvent.getSceneY() - dragDelta.mouseY)
    									/ (transformed.getHeight() / miniAnchorPane.getHeight())
    									+ dragDelta.buttonY
    									- 24);
    				}
    			});
    	button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));
    	button.setOnMouseReleased(
    			mouseEvent -> {
    				button.setCursor(Cursor.HAND);
    				if (markerType == 1 || markerType == 2) {
    					boolean isSnapped = false;
    					Location nearestLocation = locations.get(0);
    					double radiusOfNearest = Integer.MAX_VALUE;
    					for (Location l : locations) {
    						if (l.getFloor().equals(floor)) {
    							double radiusCheck =
    									Math.sqrt(
    											Math.pow(l.getXCoord() - button.getLayoutX(), 2)
    													+ (Math.pow(l.getYCoord() - button.getLayoutY(), 2)));
    							// update nearest location
    							if (radiusCheck < radiusOfNearest) {
    								radiusOfNearest = radiusCheck;
    								nearestLocation = l;
    							}
    							// when it finds the location already snapped to, do this
    							if (button.getLayoutX() == l.getXCoord() && button.getLayoutY() == l.getYCoord()) {
    								nearestLocation = l;
    								isSnapped = true;
    								break;
    							}
    						}
    					}
    					if (!isSnapped) {
    						button.setLayoutX(nearestLocation.getXCoord());
    						button.setLayoutY(nearestLocation.getYCoord());
    					}
    					// update label to new location
    					xPosText.setText(String.valueOf(button.getLayoutX() - mapImageView.getLayoutX() + 8));
    					yPosText.setText(String.valueOf(button.getLayoutY() - mapImageView.getLayoutY() + 24));
    					Label correspondingLabel;
    					if (markerType == 1) {
    						correspondingLabel = buttonEquipmentMarker.get(button).getLabel();
    					} else {
    						correspondingLabel = buttonServiceRequestMarker.get(button).getLabel();
    					}
    					correspondingLabel.setLayoutX(button.getLayoutX());
    					correspondingLabel.setLayoutY(button.getLayoutY() - 20);

    					// TODO this function should update database but getting errors
    					try {
    						updateOnRelease(button);
    					} catch (SQLException e) {
    						e.printStackTrace();
    					}
    				}
    			});
    }

  */
}
