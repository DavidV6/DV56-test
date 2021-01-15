package com.adthena.testapi.api;

import com.adthena.testapi.db.entities.DateEntity;

public interface Date {

  DateEntity getDateById(Integer dateid);

  DateEntity upsert(DateEntity date);
}
