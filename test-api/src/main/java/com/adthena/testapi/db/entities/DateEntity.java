package com.adthena.testapi.db.entities;

import java.time.LocalDate;
import lombok.ToString;
import lombok.Value;
import lombok.With;

@Value
@ToString
public class DateEntity {

  @With
  Integer dateid;
  LocalDate caldate;
  String day;
  Integer week;
  String month;
  String qtr;
  Integer year;
  Boolean holiday;
}
