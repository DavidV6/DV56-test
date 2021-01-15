package com.adthena.testapi.api;

import java.util.List;

import com.adthena.testapi.db.entities.EventEntity;

public interface Event {

  EventEntity getEventById(Integer eventid);

  EventEntity upsert(EventEntity event);
  
  public List<EventObject> list();
}
