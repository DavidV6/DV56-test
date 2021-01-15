package com.adthena.testapi.api;

import com.adthena.testapi.db.dao.VenueDao;
import com.adthena.testapi.db.entities.VenueEntity;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lombok.Value;
import org.jdbi.v3.core.Jdbi;

@Value
@Path("/venues")
@Produces(MediaType.APPLICATION_JSON)
public class VenueResource implements Venue {

  Jdbi jdbi;

  @GET
  @Path("/{venueid}")
  @Override
  public VenueResource getVenueById(@PathParam("venueid") final Integer venueid) {
    throw new UnsupportedOperationException("Not implemented yet!");
  }

  @GET
  @Override
  public List<VenueEntity> list() {
    return jdbi.onDemand(VenueDao.class).list();
  }
}
