package com.adthena.testapi.services;

import com.adthena.testapi.db.dao.CategoryDao;
import com.adthena.testapi.db.dao.DateDao;
import com.adthena.testapi.db.dao.EventDao;
import com.adthena.testapi.db.entities.CategoryEntity;
import com.adthena.testapi.db.entities.DateEntity;
import com.adthena.testapi.db.entities.EventEntity;
import lombok.Data;
import org.jdbi.v3.core.Jdbi;

@Data
public class FilterDataService {

	private final Jdbi jdbi;

	/**
	 * Prepare the entity for create or update.
	 * @param entity entity to be created or updated
	 * @return the cleaned up entity
	 */
	public CategoryEntity cleanup(CategoryEntity entity) {
		return new CategoryEntity(
				entity.getCatid(),
				maxLength(entity.getCatgroup(), 10),
				maxLength(entity.getCatname(), 10),
				maxLength(entity.getCatdesc(), 50)
				);
	}

	/**
	 * Prepare the entity for create or update.
	 * @param entity entity to be created or updated
	 * @return the cleaned up entity
	 */
	public DateEntity cleanup(DateEntity entity) {
		return new DateEntity(
				entity.getDateid(),
				entity.getCaldate(),
				entity.getDay(),
				entity.getWeek(),
				entity.getMonth(),
				entity.getQtr(),
				entity.getYear(),
				entity.getHoliday()
				);
	}

	/**
	 * Prepare the entity for create or update.
	 * @param entity entity to be created or updated
	 * @return the cleaned up entity
	 */
	public EventEntity cleanup(EventEntity entity) {
		return new EventEntity(
				entity.getEventid(),
				entity.getVenueid(),
				entity.getCatid(),
				entity.getDateid(),
				maxLength(entity.getEventname(), 200),
				entity.getStarttime()
				);
	}

	public CategoryEntity save(CategoryEntity entity) {
		return jdbi.onDemand(CategoryDao.class).upsert(entity);
	}

	public DateEntity save(DateEntity entity) {
		return jdbi.onDemand(DateDao.class).upsert(entity);
	}

	public EventEntity save(EventEntity entity) {
		return jdbi.onDemand(EventDao.class).upsert(entity);
	}

	private String maxLength(final String string, final int length) {
		if(string.length() < length) 
			return string;
		return string.substring(0, length);
	}
}
