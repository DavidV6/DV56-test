package com.adthena.testapi.api;

import com.adthena.testapi.db.dao.DateDao;
import com.adthena.testapi.db.entities.DateEntity;
import com.adthena.testapi.services.FilterDataService;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lombok.Value;
import org.jdbi.v3.core.Jdbi;

@Value
@Path("/dates")
@Produces(MediaType.APPLICATION_JSON)
public class DateResource implements Date {

  Jdbi jdbi;
  FilterDataService service;

  @GET
  @Path("/{dateid}")
  @Override
  public DateEntity getDateById(@PathParam("dateid") final Integer dateid) {
    return jdbi.onDemand(DateDao.class).getDateById(dateid);
  }

  @PUT
  @Path("/upsert")
  @Override
  public DateEntity upsert(final DateEntity date) {
    return service.save(service.cleanup(date));
  }
}
