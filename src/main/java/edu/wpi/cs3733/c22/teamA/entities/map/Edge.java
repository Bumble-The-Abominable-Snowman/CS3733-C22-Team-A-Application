package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.entities.Location;

public class Edge {
  private Location start, end;
  private double weight;
  private boolean taxiCabDistance;
  private boolean top;
  private boolean floorCross;

  public Edge(Location start, Location end, boolean taxiCabDistance, boolean top, boolean floorCross) {
    this.start = start;
    this.end = end;
    this.taxiCabDistance = taxiCabDistance;
    this.top = top;
    this.floorCross = floorCross;
    weight =
        taxiCabDistance
            ? Math.abs(start.getXCoord() - end.getXCoord())
                + Math.abs(start.getYCoord() - end.getYCoord())
            : Math.sqrt(
                Math.pow((end.getXCoord() - start.getXCoord()), 2)
                    + Math.pow((end.getYCoord() - start.getYCoord()), 2));
  }

  public Edge() {

  }

  public Location getStart() {
    return start;
  }

  public void setStart(Location start) {
    this.start = start;
  }

  public Location getEnd() {
    return end;
  }

  public void setEnd(Location end) {
    this.end = end;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public boolean isTaxiCabDistance() {
    return taxiCabDistance;
  }

  public void setTaxiCabDistance(boolean taxiCabDistance) {
    this.taxiCabDistance = taxiCabDistance;
  }

  public boolean isTop() {
    return top;
  }

  public void setTop(boolean top) {
    this.top = top;
  }

  public boolean isFloorCross() {
    return floorCross;
  }
}
