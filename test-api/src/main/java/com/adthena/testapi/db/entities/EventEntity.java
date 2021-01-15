package com.adthena.testapi.db.entities;

import java.time.LocalDateTime;
import lombok.Value;
import lombok.With;

@Value
public class EventEntity {

  @With
  Integer eventid;
  Integer venueid;
  Integer catid;
  Integer dateid;
  String eventname;
  LocalDateTime starttime;
}
