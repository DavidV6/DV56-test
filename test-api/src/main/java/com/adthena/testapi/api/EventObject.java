package com.adthena.testapi.api;

import java.time.format.DateTimeFormatter;

import com.adthena.testapi.db.entities.CategoryEntity;
import com.adthena.testapi.db.entities.EventEntity;
import com.adthena.testapi.db.entities.VenueEntity;

import lombok.Value;

@Value
public class EventObject  {
	
	Integer eventid;
	  String event;
	  String category;
	  String venue;
	  String date;
	  String buyers;
	  
	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	  
	  public EventObject(EventEntity eventEntity, VenueEntity venueEntity, CategoryEntity categoryEntity, int buyers) {
		  this.eventid = eventEntity.getEventid();
		  this.event = eventEntity.getEventname();
		  this.category = this.parseCategory(categoryEntity);
		  this.venue = this.parseVenue(venueEntity);
		  this.date = formatter.format(eventEntity.getStarttime());
		  this.buyers = String.valueOf(buyers);
	  }
	  
	  private String parseCategory(CategoryEntity categoryEntity) {
		  return categoryEntity.getCatname().concat("(").concat(categoryEntity.getCatgroup()).concat(")");
	  }
	  
	  private String parseVenue(VenueEntity venueEntity) {
		  return venueEntity.getVenuename().concat(" - ").concat(venueEntity.getVenuecity()).concat(", ").concat(venueEntity.getVenuestate());
	  }
}
