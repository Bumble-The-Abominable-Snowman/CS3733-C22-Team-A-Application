package edu.wpi.cs3733.c22.teamA.entities.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.App;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PathFinder {
  private List<Location> locations;
  private Map<Location, HashSet<Edge>> neighborMap;
  private List<Line> pfLine;
  private String path;
  private MarkerManager markerManager;

  private JFXComboBox fromBox;
  private JFXComboBox toBox;

  private String destinationFloor = "";

  public PathFinder(String path, JFXComboBox fromBox, JFXComboBox toBox, MarkerManager markerManager) {
    locations = new LocationDerbyImpl().getNodeList();
    pfLine = new ArrayList<>();
    this.path = path;
    this.neighborMap = getEdges();
    this.fromBox = fromBox;
    this.toBox = toBox;
    this.markerManager = markerManager;
  }

  public List<Location> findPath(String floor) {
    Location start = null;
    Location end = null;
    for (Location l : locations) {
      if (l.getShortName().equals(fromBox.getSelectionModel().getSelectedItem())) {
        start = l;
      }
      if (l.getShortName().equals(toBox.getSelectionModel().getSelectedItem())) {
        end = l;
      }
      if (start != null && end != null) {
        break;
      }
    }

    Map<Location, Integer> minDistances = dijkstra(locations, start, neighborMap);
    List<Location> path = getPath(end, neighborMap, minDistances, floor);

    return path;
  }

  private Map<Location, HashSet<Edge>> getEdges() {
    HashMap<String, Location> map = new HashMap<>();
    HashMap<Location, HashSet<Edge>> resultMap = new HashMap<>();

    for (Location l : locations) {
      map.put(l.getStringFields().get("node_id"), l);
      resultMap.put(l, new HashSet<>());
    }

    InputStream pathStream = App.class.getResourceAsStream(this.path);
    Scanner lineScanner = new Scanner(pathStream);
    Scanner dataScanner;
    int dataIndex = 0;

    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      String edge1 = "";
      String edge2 = "";
      boolean isTaxiCab = false;
      boolean isTop = false;
      boolean isFloorCross = false;

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) edge1 = data;
        else if (dataIndex == 1) edge2 = data;
        else if (dataIndex == 2) isTaxiCab = data.equals("TRUE") ? true : false;
        else if (dataIndex == 3) isTop = (isTaxiCab && data.equals("TRUE")) ? true : false;
        else if (dataIndex == 4) isFloorCross = data.equals("TRUE") ? true : false;
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      if (map.get(edge1) == null || map.get(edge2) == null) {
        continue;
      }

      resultMap.get(map.get(edge1)).add(new Edge(map.get(edge1), map.get(edge2), isTaxiCab, isTop, isFloorCross));
      resultMap.get(map.get(edge2)).add(new Edge(map.get(edge2), map.get(edge1), isTaxiCab, isTop, isFloorCross));
    }

    lineScanner.close();
    return resultMap;
  }

  private HashMap<Location, Integer> dijkstra(
      List<Location> allLocations, Location here, Map<Location, HashSet<Edge>> neighborMap) {

    HashMap<Location, Integer> minDistances = new HashMap<>();
    for (Location l : allLocations) {
      minDistances.put(l, Integer.MAX_VALUE);
    }

    Comparator<Location> c =
        (Location a, Location b) -> (int) (minDistances.get(a) - minDistances.get(b));
    PriorityQueue<Location> priorityQueue = new PriorityQueue<>(c);
    priorityQueue.add(here);
    minDistances.put(here, 0);

    while (!priorityQueue.isEmpty()) {
      Location current = priorityQueue.poll();
      for (Edge e : neighborMap.get(current)) {
        if (minDistances.get(current) + e.getWeight() < minDistances.get(e.getEnd())) {
          minDistances.put(e.getEnd(), (int) (minDistances.get(current) + e.getWeight()));
          priorityQueue.remove(e.getEnd());
          priorityQueue.add(e.getEnd());
        }
      }
    }
    return minDistances;
  }

  private List<Location> getPath(
      Location end, Map<Location, HashSet<Edge>> neighborMap, Map<Location, Integer> minDistances, String floor) {
    List<Location> path = new ArrayList<>();
    List<Edge> edgePath = new ArrayList<>();
    List<Location> resultPath = new ArrayList<>();
    int lowestDist = Integer.MAX_VALUE;
    Location current = end;
    Edge chosen = new Edge();
    while (lowestDist > 0) {
      path.add(current);
      edgePath.add(chosen);

      for (Edge e : neighborMap.get(current)) {
        if (lowestDist > minDistances.get(e.getEnd())) {
          current = e.getEnd();
          chosen = e;
          lowestDist = minDistances.get(e.getEnd());
        }
      }
    }
   path.add(current);
    destinationFloor = path.get(0).getStringFields().get("floor");
  for(Location l:path){
    if(l.getStringFields().get("floor").equals(floor)) resultPath.add(l);
  }
    return resultPath;
  }

  public void drawPath(List<Location> path, AnchorPane miniAnchorPane) {
    if(path.size() == 0){
      return;
    }
    Location prev = path.get(0);
    int offsetX = 4;
    int offsetY = 6;
    for (int i = 1; i < path.size(); i++) {
      Line line =
          new Line(
              Integer.parseInt(prev.getStringFields().get("xcoord")) + offsetX,
                  Integer.parseInt(prev.getStringFields().get("ycoord")) + offsetY,
                  Integer.parseInt(path.get(i).getStringFields().get("xcoord")) + offsetX,
                  Integer.parseInt(path.get(i).getStringFields().get("ycoord")) + offsetY);
      line.setStroke(Color.RED);
      line.setVisible(true);
      line.setStrokeWidth(4);
      miniAnchorPane.getChildren().add(line);
      prev = path.get(i);
      pfLine.add(line);
    }
  }

  public void clearPath(AnchorPane miniAnchorPane, boolean clearList) {
    for (Line line : pfLine) {
      miniAnchorPane.getChildren().remove(line);
    }
    if (clearList){
      pfLine.clear();
      destinationFloor = "";
    }
  }

  public void updateComboBoxes() {
    fromBox.getItems().clear();
    toBox.getItems().clear();
    List<Location> floorLocations = markerManager.getAllLocations();
    for (Location l : floorLocations) {
      fromBox.getItems().add(l.getShortName());
      toBox.getItems().add(l.getShortName());
    }
  }

  public String getDestinationFloor() {
    return destinationFloor;
  }
}
