package edu.wpi.teama.entities;

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
