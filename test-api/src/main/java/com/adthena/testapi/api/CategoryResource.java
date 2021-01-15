package com.adthena.testapi.api;

import com.adthena.testapi.db.dao.CategoryDao;
import com.adthena.testapi.db.entities.CategoryEntity;
import com.adthena.testapi.services.FilterDataService;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lombok.Value;
import org.jdbi.v3.core.Jdbi;

@Value
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource implements Category {

  Jdbi jdbi;
  FilterDataService service;

  @GET
  @Path("/{catid}")
  @Override
  public CategoryEntity getCategoryById(@PathParam("catid") final Integer catid) {
    return jdbi.onDemand(CategoryDao.class).getCategoryById(catid);
  }

  @PUT
  @Path("/upsert")
  @Override
  public CategoryEntity upsert(final CategoryEntity category) {
    return service.save(service.cleanup(category));
  }

  @GET
  @Override
  public List<CategoryEntity> list() {
    return jdbi.onDemand(CategoryDao.class).list();
  }
}
