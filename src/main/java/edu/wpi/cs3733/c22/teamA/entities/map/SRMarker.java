package edu.wpi.cs3733.c22.teamA.entities.map;

import edu.wpi.cs3733.c22.teamA.entities.servicerequests.SR;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SRMarker extends Marker {
  private SR serviceRequest;

  public SRMarker(SR serviceRequest, Button button, Label label, LocationMarker locationMarker) {
    super(button, label, locationMarker);

    this.serviceRequest = serviceRequest;
  }

  public SR getServiceRequest() {
    return serviceRequest;
  }

  public void setServiceRequest(SR serviceRequest) {
    this.serviceRequest = serviceRequest;
  }
}
