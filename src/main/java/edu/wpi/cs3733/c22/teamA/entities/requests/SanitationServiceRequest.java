package edu.wpi.cs3733.c22.teamA.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SanitationServiceRequest {
  private String sanitationType;
  private String location;
  private String notes;

  public SanitationServiceRequest() {}
}
