package com.adthena.testapi.db.entities;

import lombok.Value;

@Value
public class VenueEntity {

  Integer venueid;
  String venuename;
  String venuecity;
  String venuestate;
  Integer venueseats;
}
