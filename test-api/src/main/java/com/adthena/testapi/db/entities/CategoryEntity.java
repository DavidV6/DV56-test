package com.adthena.testapi.db.entities;

import lombok.Value;
import lombok.With;

@Value
public class CategoryEntity {

  @With
  Integer catid;
  String catgroup;
  String catname;
  String catdesc;
}
