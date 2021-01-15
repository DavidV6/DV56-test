package com.adthena.testapi.db.dao;

import com.adthena.testapi.db.entities.VenueEntity;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface VenueDao {

  @RegisterConstructorMapper(VenueEntity.class)
  @SqlQuery("SELECT * FROM venue WHERE venueid=:venueid")
  VenueEntity getVenueById(@Bind("venueid") Integer venueid);
  
  @SqlQuery("SELECT * FROM venue ORDER BY venueid ASC")
  @RegisterConstructorMapper(VenueEntity.class)
  List<VenueEntity> list();
}
