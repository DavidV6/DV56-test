package com.adthena.testapi.db.dao;

import com.adthena.testapi.db.entities.EventEntity;

import java.time.LocalDateTime;
import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface EventDao {

  @SqlQuery("SELECT * FROM event where eventid = :eventid")
  @RegisterConstructorMapper(EventEntity.class)
  EventEntity getEventById(@Bind("eventid") Integer eventid);

  /**
   * Create or update depending on the presence of an id and whether it's higher than 0 or not.
   * @param event entity to create or update
   * @return new entity
   */
  default EventEntity upsert(EventEntity event) {
    if (event.getEventid() != null && event.getEventid() > 0) {
      update(event.getEventid(), event.getVenueid(), event.getCatid(), event.getDateid(),
          event.getEventname(), event.getStarttime());
      return event;
    }
    final EventEntity entity = event.withEventid(nextId());
    create(entity.getEventid(), event.getVenueid(), event.getCatid(), event.getDateid(),
        event.getEventname(), event.getStarttime());
    return entity;
  }

  @SqlQuery("SELECT MAX(eventid)+1 FROM event")
  Integer nextId();

  @SqlUpdate("INSERT INTO event (eventid, venueid, catid, dateid, eventname, starttime) VALUES (:eventid, :venueid, :catid, :dateid, :eventname, :starttime)")
  Integer create(
      @Bind("eventid") Integer eventid,
      @Bind("venueid") Integer venueid,
      @Bind("catid") Integer catid,
      @Bind("dateid") Integer dateid,
      @Bind("eventname") String eventname,
      @Bind("starttime") LocalDateTime starttime
  );

  @SqlUpdate("UPDATE event SET venueid=:venueid, catid=:catid, dateid=:dateid, eventname=:eventname, starttime=:starttime WHERE eventid=:eventid")
  Integer update(
      @Bind("eventid") Integer eventid,
      @Bind("venueid") Integer venueid,
      @Bind("catid") Integer catid,
      @Bind("dateid") Integer dateid,
      @Bind("eventname") String eventname,
      @Bind("starttime") LocalDateTime starttime
  );
  
  @SqlQuery("SELECT * FROM event ORDER BY catid ASC")
  @RegisterConstructorMapper(EventEntity.class)
  List<EventEntity> list();
  
}
