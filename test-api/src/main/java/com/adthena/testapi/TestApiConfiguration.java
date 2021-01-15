package com.adthena.testapi;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TestApiConfiguration extends Configuration {

  private DataSourceFactory database;
}
