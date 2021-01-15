package com.adthena.testapi.api;

import com.adthena.testapi.db.dao.CategoryDao;
import com.adthena.testapi.db.dao.EventDao;
import com.adthena.testapi.db.dao.VenueDao;
import com.adthena.testapi.db.entities.CategoryEntity;
import com.adthena.testapi.db.entities.EventEntity;
import com.adthena.testapi.db.entities.VenueEntity;
import com.adthena.testapi.services.FilterDataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource implements Event {

  Jdbi jdbi;
  FilterDataService service;

  @GET
  @Path("/{eventid}")
  @Override
  public EventEntity getEventById(@PathParam("eventid") final Integer eventid) {
    return jdbi.onDemand(EventDao.class).getEventById(eventid);
  }

  @PUT
  @Path("/upsert")
  @Override
  public EventEntity upsert(final EventEntity event) {
    return service.save(service.cleanup(event));
  }
  
  @GET
  @Override
  public List<EventObject> list() {
    List<EventEntity> listEvents = jdbi.onDemand(EventDao.class).list();
    
    HashMap<String, List<EventEntity>> allEvents = new HashMap<String, List<EventEntity>>();
    HashMap<String, Integer> numberEvents = new HashMap<String, Integer>();
    for (EventEntity eventEntity : listEvents) {
    	if(allEvents.containsKey(eventEntity.getEventname())) {
    		allEvents.get(eventEntity.getEventname()).add(eventEntity);
    		numberEvents.put(eventEntity.getEventname(), numberEvents.get(eventEntity.getEventname()) + 1);
    	} else {
    		List<EventEntity> list = new ArrayList<EventEntity>();
    		allEvents.put(eventEntity.getEventname(), list);
    		numberEvents.put(eventEntity.getEventname(), 1);
    	}
	}
    
    List<EventEntity> resultEvents = new ArrayList<>();
    for (String eventName : numberEvents.keySet()) {
		if(resultEvents.size() < allEvents.get(eventName).size()) {
			resultEvents = allEvents.get(eventName);
		}
	}
    
    Collections.sort(resultEvents, new Comparator<EventEntity>() {
		@Override
		public int compare(EventEntity o1, EventEntity o2) {
			return o2.getStarttime().compareTo(o1.getStarttime());
		}
	});
    
    EventEntity latestEvent = resultEvents.get(0);
    CategoryEntity categoryEntity = jdbi.onDemand(CategoryDao.class).getCategoryById(latestEvent.getCatid());
    VenueEntity venueEntity = jdbi.onDemand(VenueDao.class).getVenueById(latestEvent.getVenueid());
    EventObject eventObject = new EventObject(latestEvent, venueEntity, categoryEntity, resultEvents.size());
    
    List<EventObject> resultList = new ArrayList<>();
    resultList.add(eventObject);
    
    return resultList;
  }
  
}
