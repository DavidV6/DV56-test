package com.adthena.testapi.db.dao;

import com.adthena.testapi.db.entities.DateEntity;
import java.time.LocalDate;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface DateDao {

  @RegisterConstructorMapper(DateEntity.class)
  @SqlQuery("SELECT * FROM date WHERE dateid=:dateid")
  DateEntity getDateById(@Bind("dateid") Integer dateid);

  /**
   * Create or update depending on the presence of an id and whether it's higher than 0 or not.
   * @param date entity to create or update
   * @return new entity
   */
  default DateEntity upsert(DateEntity date) {
    if (date.getDateid() != null && date.getDateid() > 0) {
      update(
          date.getDateid(),
          date.getCaldate(),
          date.getDay(),
          date.getWeek(),
          date.getMonth(),
          date.getQtr(),
          date.getYear(),
          date.getHoliday()
      );
      return date;
    }
    final DateEntity entity = date.withDateid(nextId());
    create(
        entity.getDateid(),
        entity.getCaldate(),
        entity.getDay(),
        entity.getWeek(),
        entity.getMonth(),
        entity.getQtr(),
        entity.getYear(),
        entity.getHoliday()
    );
    return entity;
  }


  @SqlQuery("SELECT MAX(dateid)+1 FROM date")
  Integer nextId();

  @SqlUpdate("INSERT INTO date (dateid, caldate, day, week, month, qtr, year, holiday) VALUES (:dateid, :caldate, :day, :week, :month, :qtr, :year, :holiday)")
  Integer create(
      @Bind("dateid") final Integer dateid,
      @Bind("caldate") final LocalDate caldate,
      @Bind("day") final String day,
      @Bind("week") final Integer week,
      @Bind("month") final String month,
      @Bind("qtr") final String qtr,
      @Bind("year") final Integer year,
      @Bind("holiday") final Boolean holiday
  );

  @SqlUpdate("UPDATE date SET caldate=:caldate, day=:day, week=:week, month=:month, qtr=:qtr, year=:year, holiday=:holiday WHERE dateid=:dateid")
  Integer update(
      @Bind("dateid") final Integer dateid,
      @Bind("caldate") final LocalDate caldate,
      @Bind("day") final String day,
      @Bind("week") final Integer week,
      @Bind("month") final String month,
      @Bind("qtr") final String qtr,
      @Bind("year") final Integer year,
      @Bind("holiday") final Boolean holiday
  );
}
