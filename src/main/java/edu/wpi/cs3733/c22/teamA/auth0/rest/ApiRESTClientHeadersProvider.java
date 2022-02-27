package edu.wpi.cs3733.c22.teamA.auth0.rest;

import java.util.Map;

public interface ApiRESTClientHeadersProvider {
    Map<String, String> getExtraHeaders();
}
