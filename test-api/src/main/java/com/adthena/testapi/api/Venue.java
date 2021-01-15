package com.adthena.testapi.api;

import com.adthena.testapi.db.entities.VenueEntity;
import java.util.List;

public interface Venue {

  VenueResource getVenueById(Integer venueid);

  List<VenueEntity> list();
}
