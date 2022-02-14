package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.entities.requests.ServiceRequest;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SRMarker extends Marker {
  private ServiceRequest serviceRequest;

  public SRMarker(
      ServiceRequest serviceRequest, Button button, Label label, LocationMarker locationMarker) {
    super(button, label, locationMarker);

    this.serviceRequest = serviceRequest;
  }

  public ServiceRequest getServiceRequest() {
    return serviceRequest;
  }

  public void setServiceRequest(ServiceRequest equipment) {
    this.serviceRequest = serviceRequest;
  }
}
