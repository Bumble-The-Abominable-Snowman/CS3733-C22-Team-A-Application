package edu.wpi.cs3733.c22.teamA.controllers.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.c22.teamA.Adb.location.LocationDerbyImpl;
import edu.wpi.cs3733.c22.teamA.entities.Location;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import javafx.fxml.FXML;

public class PathFindingController {
  @FXML private JFXButton submitButton;
  @FXML private JFXComboBox toComboBox;
  @FXML private JFXComboBox fromComboBox;

  private List<Location> locations;
  private Set<Location> presentLocations;
  private HashMap<Location, HashSet<Edge>> neighborMap;

  @FXML
  public void initialize() throws IOException, ParseException {
    locations = new LocationDerbyImpl().getNodeList();
    presentLocations = new HashSet<>();
    neighborMap = getEdges();

    toComboBox
        .getItems()
        .addAll(presentLocations.stream().map(Location::getShortName).collect(Collectors.toList()));
    fromComboBox
        .getItems()
        .addAll(presentLocations.stream().map(Location::getShortName).collect(Collectors.toList()));
  }

  @FXML
  public void submit() throws IOException, ParseException {
    String shortNameFrom = fromComboBox.getSelectionModel().getSelectedItem().toString();
    String shortNameTo = toComboBox.getSelectionModel().getSelectedItem().toString();

    Location start = null;
    Location end = null;
    for (Location l : locations) {
      if (l.getShortName().equals(shortNameFrom)) {
        start = l;
      }
      if (l.getShortName().equals(shortNameTo)) {
        end = l;
      }
      if (start != null && end != null) {
        break;
      }
    }

    Map<Location, Integer> minDistances = dijkstra(locations, start, neighborMap);
    List<Location> path = getPath(end, neighborMap, minDistances);
    for (Location l : path) {
      System.out.println(l.getNodeID());
    }
    System.out.println(start.getNodeID());
  }

  public HashMap<Location, Integer> dijkstra(
      List<Location> allLocations, Location here, HashMap<Location, HashSet<Edge>> neighborMap) {
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
        if (minDistances.get(current) + e.weight < minDistances.get(e.end)) {
          minDistances.put(e.end, (int) (minDistances.get(current) + e.weight));
          priorityQueue.remove(e.end);
          priorityQueue.add(e.end);
        }
      }
    }
    return minDistances;
  }

  public List<Location> getPath(
      Location end,
      HashMap<Location, HashSet<Edge>> neighborMap,
      Map<Location, Integer> minDistances) {
    List<Location> path = new ArrayList<>();
    int lowestDist = Integer.MAX_VALUE;
    Location current = end;
    while (lowestDist > 0) {
      path.add(current);
      //      if (neighborMap.get(current) == null) {
      //        continue;
      //      }
      for (Edge e : neighborMap.get(current)) {
        if (lowestDist > minDistances.get(e.end)) {
          current = e.end;
          lowestDist = minDistances.get(e.end);
        }
      }
    }
    return path;
  }

  public HashMap<Location, HashSet<Edge>> getEdges() throws IOException, ParseException {
    HashMap<String, Location> map = new HashMap<>();
    HashMap<Location, HashSet<Edge>> resultMap = new HashMap<>();

    for (Location l : locations) {
      map.put(l.getNodeID(), l);
      resultMap.put(l, new HashSet<>());
    }

    File file = new File("src/main/resources/edu/wpi/cs3733/c22/teamA/db/CSVs/AllEdgesHand.csv");
    Scanner lineScanner = new Scanner(file);
    Scanner dataScanner;
    int dataIndex = 0;
    int lineIndex = 0;
    int intData = 0;

    lineScanner.nextLine();

    while (lineScanner.hasNextLine()) { // Scan CSV line by line

      dataScanner = new Scanner(lineScanner.nextLine());
      dataScanner.useDelimiter(",");
      String edge1 = "";
      String edge2 = "";
      boolean isTaxiCab = false;
      boolean isTop = false;

      while (dataScanner.hasNext()) {

        String data = dataScanner.next();
        if (dataIndex == 0) edge1 = data;
        else if (dataIndex == 1) edge2 = data;
        else if (dataIndex == 2) isTaxiCab = data.equals("TRUE") ? true : false;
        else if (dataIndex == 3) isTop = (isTaxiCab && data.equals("TRUE")) ? true : false;
        else System.out.println("Invalid data, I broke::" + data);
        dataIndex++;
      }

      dataIndex = 0;
      if (map.get(edge1) == null || map.get(edge2) == null) {
        continue;
      }

      presentLocations.add(map.get(edge1));
      presentLocations.add(map.get(edge2));
      resultMap.get(map.get(edge1)).add(new Edge(map.get(edge1), map.get(edge2), isTaxiCab, isTop));
      resultMap.get(map.get(edge2)).add(new Edge(map.get(edge2), map.get(edge1), isTaxiCab, isTop));
    }

    lineIndex++;
    lineScanner.close();
    return resultMap;
  }

  private static class Edge {
    Location start, end;
    double weight;
    boolean taxiCabDistance;
    boolean top;

    Edge(Location start, Location end, boolean taxiCabDistance, boolean top) {
      this.start = start;
      this.end = end;
      this.taxiCabDistance = taxiCabDistance;
      this.top = top;
      weight =
          taxiCabDistance
              ? Math.abs(start.getXCoord() - end.getXCoord())
                  + Math.abs(start.getYCoord() - end.getYCoord())
              : Math.sqrt(
                  Math.pow((end.getXCoord() - start.getXCoord()), 2)
                      + Math.pow((end.getYCoord() - start.getYCoord()), 2));
    }
  }
}
